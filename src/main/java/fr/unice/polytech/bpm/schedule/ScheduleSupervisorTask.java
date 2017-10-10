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
    private static final String EXECUTE = "executer";
    private static final String GET_COMPANY_AVAILABILITY = "Récupérer disponnibilités des entreprises";
    private static final String SELECT_LOCATION = "Choisir un lieu";
    private static final String SELECT_DATE = "Choisir une date";

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
        String action = nextLine("Que voulez vous faire (nouveau,liste,executer) ? ");
        switch (action) {
            case NEW:
                createNewProcess();
                break;
            case LIST:
                listTasks();
                break;
            case EXECUTE:
                executeTask();
                break;
            default:
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

        if (task == null) {
            // TODO: check that role can validate task
            System.out.println("Tâche invalide!");
        } else {
            Map<String, Object> variables = new HashMap<String, Object>();
            switch(task.getName()) {
                case GET_COMPANY_AVAILABILITY:
                    fillAvailabilityDetails(variables);
                    break;
                case SELECT_LOCATION:
                    fillLocation(variables);
                    break;
                case SELECT_DATE:
                    fillDate(variables);
                    break;
                default:
                    System.err.println("Tâche avec aucune entrée!!");
            }
            taskService.complete(task.getId(), variables);
        }
    }

    /**
     * Fill in date
     * @param variables
     */
    private void fillDate(Map<String, Object> variables) {
        String location = nextLine("Date du Sophia Tech Forum: ");
        variables.put("date", location);
    }

    /**
     * Fill in the location
     * @param variables
     */
    private void fillLocation(Map<String, Object> variables) {
        String location = nextLine("Lieu du Sophia Tech Forum: ");
        variables.put("location", location);
    }

    /**
     * Fill in the company availability details
     * @param variables
     */
    private void fillAvailabilityDetails(Map<String, Object> variables) {
        String details = nextLine("Entrez les disponnibilités: ");
        variables.put("availability", details);
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
                System.out.println((i+1) + ") " + tasks.get(i).getName() + " #" + tasks.get(i).getId());
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
