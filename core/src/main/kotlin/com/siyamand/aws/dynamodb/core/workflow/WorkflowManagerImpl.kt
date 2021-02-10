package com.siyamand.aws.dynamodb.core.workflow

import java.lang.Exception

class WorkflowManagerImpl() : WorkflowManager {
    private var workflowPersister: WorkflowPersister? = null

    override fun setWorkflowPersister(workflowPersister: WorkflowPersister) {
        this.workflowPersister = workflowPersister
    }

    override suspend fun execute(instance: WorkflowInstance): WorkflowResult {

        if (!instance.template.steps.any()) {
            throw  Exception("no step has been defined")
        }

        if (instance.currentStep >= instance.template.steps.size ||
                instance.currentStep < 0) {
            throw Exception("Current step is out of order")
        }

        val currentStepInstance = instance.steps[instance.currentStep]
        val currentStep = instance.template.steps[instance.currentStep]

        val params = mergeParams(instance, currentStepInstance)

        when (currentStepInstance.status) {
            WorkflowStepStatus.INITIAL -> {
                currentStepInstance.status = WorkflowStepStatus.STARTING
                workflowPersister?.save(instance)
                val result = currentStep.execute(instance.context, params)

                val newInstance = updateInstance(instance, result, currentStepInstance)
                workflowPersister?.save(newInstance)
                return result

            }
            WorkflowStepStatus.WAITING -> {
                val result = currentStep.isWaiting(instance.context, params)

                val newInstance = updateInstance(instance, result, currentStepInstance)
                workflowPersister?.save(newInstance)
                return result

            }
        }

        return WorkflowResult(WorkflowResultType.FINISH, mapOf<String, String>(), "")
    }

    private fun updateInstance(instance: WorkflowInstance, result: WorkflowResult, currentStep: WorkflowStepInstance): WorkflowInstance {
        var newStepIndex = instance.currentStep
        if (result.resultType == WorkflowResultType.SUCCESS) {
            currentStep.status = WorkflowStepStatus.FINISHED
            if (instance.currentStep < instance.steps.size) {
                newStepIndex++
            }
        } else if (result.resultType == WorkflowResultType.WAITING) {
            currentStep.status = WorkflowStepStatus.WAITING
        }
        val newInstance = WorkflowInstance(instance.id, instance.context, instance.steps, newStepIndex, result, instance.template)
        return newInstance
    }

    private fun mergeParams(instance: WorkflowInstance, currentStep: WorkflowStepInstance): MutableMap<String, String> {
        val params = mutableMapOf<String, String>()
        if (instance.lastResult?.params?.any() == true) {
            params.putAll(instance.lastResult!!.params)
        }
        if (currentStep.params.any()) {
            params.putAll(currentStep.params)
        }
        return params
    }
}