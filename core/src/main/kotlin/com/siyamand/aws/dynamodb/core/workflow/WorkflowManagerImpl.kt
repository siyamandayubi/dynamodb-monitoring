package com.siyamand.aws.dynamodb.core.workflow

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WorkflowManagerImpl() : WorkflowManager {

    var logger: Logger = LoggerFactory.getLogger(WorkflowManagerImpl::class.java)

    override suspend fun execute(input: WorkflowInstance, owner: Any, workflowPersister: WorkflowPersister?): WorkflowResult {

        logger.info("executing ${input.template.name}")

        if (!input.steps.any()) {
            throw  Exception("no step has been defined")
        }

        if (input.currentStep >= input.steps.size ||
                input.currentStep < 0) {
            throw Exception("Current step is out of order")
        }

        var lastResult = WorkflowResultType.SUCCESS
        var currentInstance = input
        while (currentInstance.currentStep < currentInstance.steps.size && (
                        lastResult == WorkflowResultType.SUCCESS || lastResult == WorkflowResultType.WAITING)
        ) {
            val pair = executeStep(currentInstance, owner, workflowPersister)
            if (pair.second.resultType == WorkflowResultType.ERROR) {
                logger.error(pair.second.message)
            }

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

    private suspend fun executeStep(currentInstance: WorkflowInstance, owner: Any, workflowPersister: WorkflowPersister?): Pair<WorkflowStepInstance, WorkflowResult> {
        val currentStepInstance = currentInstance.steps[currentInstance.currentStep]
        val currentStep = currentInstance.steps[currentInstance.currentStep]
        try {

            val params = mergeParams(currentInstance, currentStepInstance)

            var stepResult = when (currentStepInstance.status) {
                WorkflowStepStatus.INITIAL, WorkflowStepStatus.STARTING -> {
                    currentStepInstance.status = WorkflowStepStatus.STARTING
                    workflowPersister?.save(currentInstance)
                    logger.info("executing execute of ${currentStep.identifier}")
                    currentStep.step.execute(currentInstance, owner, params)
                }
                WorkflowStepStatus.WAITING -> {
                    logger.info("executing isWaiting step of${currentStep.identifier}")
                    currentStep.step.isWaiting(currentInstance, owner, params)
                }

                WorkflowStepStatus.FINISHED -> {
                    WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
                }
            }
            return Pair(currentStepInstance, stepResult)
        } catch (ex: Exception) {
            logger.error("error in running ${currentStep.identifier} {}", ex)
            return Pair(currentStepInstance, WorkflowResult(WorkflowResultType.ERROR, mapOf(), "${ex.message} - ${ex.stackTrace.joinToString("")}"))
        }
    }

    private fun updateInstance(instance: WorkflowInstance, result: WorkflowResult, currentStep: WorkflowStepInstance): WorkflowInstance {
        var newStepIndex = instance.currentStep
        if (result.resultType == WorkflowResultType.SUCCESS) {
            currentStep.status = WorkflowStepStatus.FINISHED
            if (!result.nextStep.isNullOrEmpty()) {
                val nextStep = instance.steps.indexOfFirst { it.identifier == result.nextStep }
                if (nextStep == -1){
                    throw IllegalArgumentException("couldn't find the step ${result.nextStep}")
                }
                newStepIndex = nextStep
                val steps = instance.steps.filter { instance.steps.indexOf(it) >= nextStep }
                steps.forEach { it.status = WorkflowStepStatus.INITIAL }
            } else if (instance.currentStep < instance.steps.size) {
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