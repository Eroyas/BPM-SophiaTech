package fr.unice.polytech.bpm.process.tasks;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.ProcessTask;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import org.flowable.engine.ProcessEngine;

/**
 * SupervisorTask task
 */
public class OrganizerTask extends ProcessTask {

    /**
     * Instantiates a new organizer task
     * @param engine
     */
    public OrganizerTask(CommandRegistry commands, ProcessEngine engine) {
        super(Role.ORGANIZER, commands, engine);
    }

}
