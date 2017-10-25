package fr.unice.polytech.bpm.process.tasks;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.ProcessTask;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import org.flowable.engine.ProcessEngine;

/**
 * SupervisorTask task
 */
public class SupervisorTask extends ProcessTask {

    /**
     * Instantiates a new supervisor task
     * @param engine
     */
    public SupervisorTask(CommandRegistry commands, ProcessEngine engine) {
        super(Role.SUPERVISOR, commands, engine);
    }

}
