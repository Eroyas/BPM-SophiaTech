package fr.unice.polytech.bpm.process.commands.list;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.Command;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.HashMap;

public class SupervisorNewForumCommand implements Command {

    private static final String PROCESS_DEFINITION_KEY = "form-team";

    @Override
    public void execute(ProcessEngine context) {
        RuntimeService runtimeService = context.getRuntimeService();
        // Create a new instance of the schedule process and display its id
        System.out.println("Création du sous process " + PROCESS_DEFINITION_KEY);
        ProcessInstance inst = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, new HashMap<>());
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
