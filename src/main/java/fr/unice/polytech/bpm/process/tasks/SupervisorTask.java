package fr.unice.polytech.bpm.process.tasks;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.ProcessTask;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import org.flowable.engine.ProcessEngine;

/**
 * SupervisorTask task
 */
public class SupervisorTask extends ProcessTask {

    private static final String PROCESS_DEFINITION_KEY = "schedule";
    private static final String ORGANIZER_ID = "organizer";
    private static final String LIST = "lister";
    private static final String EXECUTE = "executer";
    private static final String SELECT_LOCATIONS = "Sélectionner lieux";

    /**
     * Instantiates a new supervisor task
     * @param engine
     */
    public SupervisorTask(CommandRegistry commands, ProcessEngine engine) {
        super(Role.SUPERVISOR, commands, engine);
    }

}
