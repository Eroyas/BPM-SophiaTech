package fr.unice.polytech.bpm.process;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Integrate different processes
 */
public class ProcessIntegration {

    private ProcessEngine engine;
    private RuntimeService runtimeService;

    public ProcessIntegration(ProcessEngine engine) {
        this.engine = engine;
        this.runtimeService = engine.getRuntimeService();
    }

    /**
     * Add a trigger.
     * the execution of the process from will trigger the proccess in the to array
     * @param from
     * @param toArray
     */
    public void addSimpleTrigger(String from, String ... toArray) {
        addSimpleTrigger(from, () -> {
            for (String toLaunch : toArray) {
                triggerProcess(toLaunch);
            }
        });
    }

    /**
     * Add a trigger.
     * the execution of the process from will trigger the proccess in the to array
     * @param from
     * @param task execute when completed
     */
    public void addSimpleTrigger(String from, Runnable task) {
        runtimeService.addEventListener(new FlowableEventListener() {
            @Override
            public void onEvent(FlowableEvent flowableEvent) {
                FlowableEntityEventImpl event = (FlowableEntityEventImpl) flowableEvent;
                ExecutionEntityImpl e = (ExecutionEntityImpl) event.getEntity();

                if (isSameProcess(from, e)) {
                    task.run();
                }
            }

            @Override
            public boolean isFailOnException() {
                return false;
            }
        }, FlowableEngineEventType.PROCESS_COMPLETED);
    }

    /**
     * Triggers a process
     * @param toLaunch
     */
    private void triggerProcess(String toLaunch) {
        ProcessInstance inst = runtimeService.startProcessInstanceByKey(toLaunch, new HashMap<>());
        System.out.println("[LOG] Sous-process " + toLaunch + " créé par intégration");
    }

    /**
     * Add a trigger.
     * the execution of the process from will trigger the proccess in the to array if all the process from the from array are executed
     * @param fromArray
     * @param toArray
     */
    public void addAggregateTrigger(String[] fromArray, String ... toArray) {
        Queue<Integer> queue = new PriorityQueue<>();
        for (String from : fromArray) {
            addAggregateTriggerFor(queue, fromArray.length, from, toArray);
        }
    }

    /**
     * Add a trigger from to
     * @param from
     * @param to
     */
    private void addAggregateTriggerFor(Queue<Integer> queue, int size, String from, String... to) {
        runtimeService.addEventListener(new FlowableEventListener() {
            @Override
            public void onEvent(FlowableEvent flowableEvent) {
                FlowableEntityEventImpl event = (FlowableEntityEventImpl) flowableEvent;
                ExecutionEntityImpl e = (ExecutionEntityImpl) event.getEntity();

                if (isSameProcess(from, e)) {
                    synchronized (ProcessIntegration.this) {
                        queue.add(1);
                        // Launch the different process if we all the process have been completed
                        if (queue.size() == size) {
                            System.out.println("[LOG] We waited for all the sub-processes. Continuing the flow");
                            for (String toLaunch : to) {
                                triggerProcess(toLaunch);
                            }
                            queue.clear();
                        }
                    }
                }
            }

            @Override
            public boolean isFailOnException() {
                return false;
            }
        }, FlowableEngineEventType.PROCESS_COMPLETED);
    }

    /**
     *
     * @param from
     * @param e
     * @return true if the process id is from
     */
    private boolean isSameProcess(String from, ExecutionEntityImpl e) {
        //second check : flowable bug..
        return from.equals(e.getProcessDefinitionKey()) || from.equals(e.getProcessDefinitionId().split(":")[0]);
    }
}
