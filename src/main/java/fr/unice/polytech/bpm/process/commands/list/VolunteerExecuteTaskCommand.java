package fr.unice.polytech.bpm.process.commands.list;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.PromptCommand;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

public class VolunteerExecuteTaskCommand extends PromptCommand {


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
        // Mark the task as completed with the user variables
        taskService.complete(task.getId(), variables);
    }

   
    @Override
    public Role getRole() {
        return Role.VOLUNTEER;
    }

    @Override
    public String name() {
        return "executer";
    }
}
