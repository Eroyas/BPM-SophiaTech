package fr.unice.polytech.bpm.process.tasks;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.ProcessTask;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import org.flowable.engine.ProcessEngine;

/**
 * VolunteerTask task
 */
public class VolunteerTask extends ProcessTask {

    /**
     * Instantiates a new supervisor task
     * @param engine
     */
    public VolunteerTask(CommandRegistry commands, ProcessEngine engine) {
        super(Role.VOLUNTEER, commands, engine);
    }

}
