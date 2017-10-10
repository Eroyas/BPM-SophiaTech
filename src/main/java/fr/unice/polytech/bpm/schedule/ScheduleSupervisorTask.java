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
    private static final String NEW = "creer";
    private static final String LIST = "lister";
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
        String action = nextLine(String.format("Que voulez vous faire (%s,%s,%s) ? ", NEW, LIST, EXECUTE));
        // Handle the differenr actions
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
        // Mark the task as completed with the user variables
        taskService.complete(task.getId(), variables);
    }

    /**
     * Fill in date
     * @param variables
     */
    private void fillDate(Map<String, Object> variables) {
        // Get the dates from the previous tasks and ask for a final date
        String sophiaTechId = nextLine("#id de la planification: ");
        System.out.println("Date possible pour les étudiants: " + getStudentDates(sophiaTechId));
        System.out.println("Date possible pour les entreprises: " + getCompanyDates(sophiaTechId));

        String location = nextLine("Date du Sophia Tech Forum: ");
        variables.put("date", location);
    }

    /**
     * Get company dates
     * @param sophiaTechId
     * @return
     */
    private String getCompanyDates(String sophiaTechId) {
       return "Dates des étudiants (TODO)";
    }

    /**
     * Get student dates
     * @param sophiaTechId
     * @return
     */
    private String getStudentDates(String sophiaTechId) {
        return "Dates des entreprises (TODO)";
    }

    /**
     * Fill in the location
     * @param variables
     */
    private void fillLocation(Map<String, Object> variables) {
        // Get the potential locations and ask for a final location
        String sophiaTechId = nextLine("#id de la planification: ");
        System.out.println("Date possible pour les étudiants: " + getPossibleLocations(sophiaTechId));

        String location = nextLine("Lieu du Sophia Tech Forum: ");
        variables.put("location", location);
    }

    /**
     * get possible locations
     * @param sophiaTechId
     * @return
     */
    private String getPossibleLocations(String sophiaTechId) {
        return "Forums (TODO)";
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

    /**
     * Create a new process
     */
    private void createNewProcess() {
        RuntimeService runtimeService = engine.getRuntimeService();
        // Create a new instance of the schedule process and display its id
        ProcessInstance inst = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, new HashMap<>());
        System.out.println("Sophia-Tech forum (planification), #" + inst.getId() + " créé");
    }
}
