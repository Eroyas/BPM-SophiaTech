package fr.unice.polytech.bpm.schedule;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * Supervisor task
 */
public class ScheduleSupervisorTask extends ProcessTask {

    private static final String PROCESS_DEFINITION_KEY = "schedule";
    private static final String SUPERVISOR_ID = "supervisor";
    private static final String NEW = "nouveau";
    private static final String LIST = "liste";

    /**
     * Instantiates a new supervisor task
     * @param engine
     */
    public ScheduleSupervisorTask(ProcessEngine engine) {
        super(engine);
    }

    /**
     * Execute supervisor role
     */
    @Override
    public void run() {
        String action = nextLine("Que voulez vous faire (nouveau,liste) ? ");
        switch (action) {
            case NEW:
                createNewProcess();
                break;
            case LIST:
                System.out.println("TODO");
                break;
            default:
                System.err.println("Action invalide!");
                break;

        }
    }

    /**
     * Create a new process
     */
    private void createNewProcess() {
        RuntimeService runtimeService = engine.getRuntimeService();
        Map<String, Object> variables = new HashMap<String, Object>();
        ProcessInstance inst = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
        System.out.println("Sophia-Tech forum (planification), #" + inst.getId() + " créé");
    }
}
