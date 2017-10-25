package fr.unice.polytech.bpm.process.commands.list;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.Command;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Task;

import java.util.List;

public class OrganizerListTasksCommand implements Command {

    private static final String SUPERVISOR_ID = "organizer";

    @Override
    public void execute(ProcessEngine context) {
        TaskService taskService = context.getTaskService();
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

    @Override
    public Role getRole() {
        return Role.ORGANIZER;
    }

    @Override
    public String name() {
        return "lister";
    }
}
