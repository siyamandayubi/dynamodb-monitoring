package com.siyamand.aws.dynamodb.core.workflow

import java.lang.Exception

class WorkflowManagerImpl() : WorkflowManager {

    override suspend fun execute(input: WorkflowInstance, workflowPersister: WorkflowPersister?): WorkflowResult {

        if (!input.steps.any()) {
            throw  Exception("no step has been defined")
        }

        if (input.currentStep >= input.steps.size ||
                input.currentStep < 0) {
            throw Exception("Current step is out of order")
        }

        var lastResult = WorkflowResultType.SUCCESS
        var currentInstance = input
        while (currentInstance.currentStep < currentInstance.steps.size && lastResult == WorkflowResultType.SUCCESS) {
            val pair = executeStep(currentInstance, workflowPersister)
            val currentStepInstance = pair.first
            var stepResult = pair.second

            currentInstance = updateInstance(currentInstance, stepResult, currentStepInstance)
            workflowPersister?.save(currentInstance)

            if (stepResult.resultType == WorkflowResultType.WAITING) {
                Thread.sleep(5000)
            }
            lastResult = stepResult.resultType
        }

        return WorkflowResult(lastResult, mapOf<String, String>(), "")
    }

    private suspend fun executeStep(currentInstance: WorkflowInstance, workflowPersister: WorkflowPersister?): Pair<WorkflowStepInstance, WorkflowResult> {
        val currentStepInstance = currentInstance.steps[currentInstance.currentStep]
        val currentStep = currentInstance.steps[currentInstance.currentStep]
        try {

            val params = mergeParams(currentInstance, currentStepInstance)

            var stepResult = when (currentStepInstance.status) {
                WorkflowStepStatus.INITIAL, WorkflowStepStatus.STARTING -> {
                    currentStepInstance.status = WorkflowStepStatus.STARTING
                    workflowPersister?.save(currentInstance)
                    currentStep.step.execute(currentInstance, currentInstance.context, params)
                }
                WorkflowStepStatus.WAITING -> {
                    currentStep.step.isWaiting(currentInstance, currentInstance.context, params)
                }

                WorkflowStepStatus.FINISHED -> {
                    WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
                }
            }
            return Pair(currentStepInstance, stepResult)
        } catch (ex: Exception) {
            return Pair(currentStepInstance, WorkflowResult(WorkflowResultType.ERROR, mapOf(), "${ex.message} - ${ex.stackTrace.joinToString("")}"))
        }
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
        return WorkflowInstance(instance.id, instance.context, instance.steps, newStepIndex, result, instance.template)
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