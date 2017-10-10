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
public class ScheduleLocationManagerTask extends ProcessTask {

    private static final String PROCESS_DEFINITION_KEY = "schedule";
    private static final String SUPERVISOR_ID = "location_manager";
    private static final String LIST = "lister";
    private static final String EXECUTE = "executer";
    private static final String SELECT_LOCATIONS = "Sélectionner lieux";

    /**
     * Instantiates a new supervisor task
     * @param engine
     */
    public ScheduleLocationManagerTask(ProcessEngine engine) {
        super(engine);
    }

    /**
     * Execute supervisor role
     */
    @Override
    public void run() {
        String action = nextLine(String.format("Que voulez vous faire (%s,%s) ? ", LIST, EXECUTE));
        // Handle the different actions
        switch (action) {
            case LIST:
                listTasks();
                break;
            case EXECUTE:
                executeTask();
                break;
            default:
                // Default fallback, no valid action given
                System.err.println("Action invalide!");
                break;
        }
    }

    /**
     * Execute a task
     */
    private void executeTask() {
        String taskId = nextLine("Quelle tache #? ");
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // Ff the task is found, complete it
        if (task == null) {
            // TODO: check that role can validate task
            System.out.println("Tâche invalide!");
        } else {
            completeTask(task);
        }
    }

    /**
     * Complete a task according to the user input
     * @param task
     */
    private void completeTask(Task task) {
        TaskService taskService = engine.getTaskService();
        Map<String, Object> variables = new HashMap<>();
        // Get the variables of the task
        switch(task.getName()) {
            case SELECT_LOCATIONS:
                fillLocations(variables);
                break;
            default:
                System.err.println("Tâche avec aucune entrée!!");
        }
        // Mark the task as completed with the user variables
        taskService.complete(task.getId(), variables);
    }

    /**
     * Fill in locations
     * @param variables
     */
    private void fillLocations(Map<String, Object> variables) {
        String location = nextLine("Lieux possibles: ");
        variables.put("locations", location);
    }

    /**
     * List all the current asks
     * TODO: remove code duplication
     */
    private void listTasks() {
        TaskService taskService = engine.getTaskService();
        // List the different tasks and display them
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(SUPERVISOR_ID).list();
        // If no task, happy face
        if (tasks.isEmpty()) {
            System.out.println("Vous avez rien à faire :D!");
        } else {
            // Else, sad face we have some work to do. Display the tasks
            System.out.println("Voici les tâches que vous devez faire:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i+1) + ") " + tasks.get(i).getName() + " #" + tasks.get(i).getId());
            }
        }
    }
}
