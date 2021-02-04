package com.siyamand.aws.dynamodb.core.workflow

import java.lang.Exception

class WorkflowManagerImpl(private val workflowPersistance: WorkflowPersister) : WorkflowManager {
    override suspend fun execute(instance: WorkflowInstance): WorkflowResult {

        if (instance.template.steps.any()) {
            throw  Exception("no step has been defined")
        }

        if (instance.currentStep >= instance.template.steps.size ||
                instance.currentStep < 0) {
            throw Exception("Current step is out of order")
        }

        val currentStep = instance.steps[instance.currentStep]

        val params = createParams(instance, currentStep)

        when (currentStep.status) {
            WorkflowStepStatus.INITIAL -> {
                currentStep.status = WorkflowStepStatus.STARTING
                workflowPersistance.save(instance)
                val result = currentStep.workflowStep.execute(instance.context, params)

                val newInstance = createNewInstance(instance, result, currentStep)
                workflowPersistance.save(newInstance)
                return result

            }
            WorkflowStepStatus.WAITING -> {
                val result = currentStep.workflowStep.isWaiting(instance.context, params)

                val newInstance = createNewInstance(instance, result, currentStep)
                workflowPersistance.save(newInstance)
                return result

            }
        }

        return WorkflowResult(WorkflowResultType.FINISH, mapOf<String, String>(), "")
    }

    private fun createNewInstance(instance: WorkflowInstance, result: WorkflowResult, currentStep: WorkflowStepInstance): WorkflowInstance {
        var newStepIndex = instance.currentStep
        if (result.resultType == WorkflowResultType.SUCCESS) {
            currentStep.status = WorkflowStepStatus.FINISHED
            if (instance.currentStep < instance.steps.size) {
                newStepIndex++
            }
        } else if (result.resultType == WorkflowResultType.WAITING) {
            currentStep.status = WorkflowStepStatus.WAITING
        }
        val newInstance = WorkflowInstance(instance.id, instance.context, instance.template, instance.steps, newStepIndex, result)
        return newInstance
    }

    private fun createParams(instance: WorkflowInstance, currentStep: WorkflowStepInstance): MutableMap<String, String> {
        val params = mutableMapOf<String, String>()
        if (instance.lastResult!!.params!!.any()) {
            params.putAll(instance.lastResult!!.params)
        }
        if (currentStep.params.any()) {
            params.putAll(currentStep.params)
        }
        return params
    }
}