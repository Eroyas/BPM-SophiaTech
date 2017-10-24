package fr.unice.polytech.bpm.process.commands.list;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.Command;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;

import java.util.HashMap;
import java.util.List;

public class SupervisorNewForumCommand implements Command {

    private static final String PROCESS_DEFINITION_KEY = "schedule";

    @Override
    public void execute(ProcessEngine context) {
        RuntimeService runtimeService = context.getRuntimeService();
        // Create a new instance of the schedule process and display its id
        ProcessInstance inst = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, new HashMap<>());
        System.out.println("Sophia-Tech forum (planification), #" + inst.getId() + " créé");
    }

    @Override
    public Role getRole() {
        return Role.SUPERVISOR;
    }

    @Override
    public String name() {
        return "creer-forum";
    }
}
