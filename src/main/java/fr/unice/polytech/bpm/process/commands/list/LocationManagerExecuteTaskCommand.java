package fr.unice.polytech.bpm.process.commands.list;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.PromptCommand;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

public class LocationManagerExecuteTaskCommand extends PromptCommand {

    private static final String SELECT_LOCATIONS = "Sélectionner lieux";

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
            case SELECT_LOCATIONS:
                fillLocations(variables);
                break;
            /*default:
                System.err.println("TODO: Tâche avec aucune entrée!!");*/
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

    @Override
    public Role getRole() {
        return Role.LOCATION_MANAGER;
    }

    @Override
    public String name() {
        return "executer";
    }
}
