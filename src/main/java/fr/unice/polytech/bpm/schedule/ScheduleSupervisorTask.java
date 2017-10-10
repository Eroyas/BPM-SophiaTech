package fr.unice.polytech.bpm.schedule;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;

import java.util.HashMap;
import java.util.List;
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
                listTasks();
                break;
            default:
                System.err.println("Action invalide!");
                break;

        }
    }

    /**
     * List all the current asks
     */
    private void listTasks() {
        TaskService taskService = engine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(SUPERVISOR_ID).list();
        if (tasks.isEmpty()) {
            System.out.println("Vous avez rien à faire :D!");
        } else {
            System.out.println("Voici les tâches que vous devez faire:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i+1) + ") " + tasks.get(i).getName() + " #" + tasks.get(i).getProcessInstanceId());
            }
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
