package fr.unice.polytech.bpm.process.commands.list;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.PromptCommand;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

public class OrganizerExecuteTaskCommand extends PromptCommand {

    private static final String GET_COMPANY_AVAILABILITY = "Récupérer disponnibilités des entreprises";
    private static final String SELECT_LOCATION = "Choisir un lieu";
    private static final String SELECT_DATE = "Choisir une date";

    @Override
    public void execute(ProcessEngine context) {
        String taskId = nextLine("Quelle tache #? ");
        TaskService taskService = context.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // Ff the task is found, complete it
        if (task == null) {
            // TODO: check that role can validate task
            System.out.println("Tâche invalide!");
        } else {
            completeTask(context, task);
        }
    }

    /**
     * Complete a task according to the user input
     * @param task
     */
    private void completeTask(ProcessEngine context, Task task) {
        TaskService taskService = context.getTaskService();
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
                System.err.println("TODO: Tâche avec aucune entrée!!");
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
        String sophiaTechId = "TODO";//nextLine("#id de la planification: ");
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
        String sophiaTechId = "TODO";//nextLine("#id de la planification: ");
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

    @Override
    public Role getRole() {
        return Role.ORGANIZER;
    }

    @Override
    public String name() {
        return "executer";
    }
}
