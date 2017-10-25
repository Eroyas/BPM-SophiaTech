package fr.unice.polytech.bpm.process.tasks;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.ProcessTask;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import org.flowable.engine.ProcessEngine;

/**
 * SupervisorTask task
 */
public class LocationManagerTask extends ProcessTask {

    /**
     * Instantiates a new supervisor task
     * @param engine
     */
    public LocationManagerTask(CommandRegistry commands, ProcessEngine engine) {
        super(Role.LOCATION_MANAGER, commands, engine);
    }

}
