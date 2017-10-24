package fr.unice.polytech.bpm.process.tasks;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.ProcessTask;
import fr.unice.polytech.bpm.process.commands.Command;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SupervisorTask task
 */
public class OrganizerTask extends ProcessTask {

    private static final String PROCESS_DEFINITION_KEY = "schedule";
    private static final String SUPERVISOR_ID = "supervisor";
    private static final String NEW = "creer";
    private static final String LIST = "lister";
    private static final String EXECUTE = "executer";
    private static final String GET_COMPANY_AVAILABILITY = "Récupérer disponnibilités des entreprises";
    private static final String SELECT_LOCATION = "Choisir un lieu";
    private static final String SELECT_DATE = "Choisir une date";

    /**
     * Instantiates a new supervisor task
     * @param engine
     */
    public OrganizerTask(CommandRegistry commands, ProcessEngine engine) {
        super(Role.ORGANIZER, commands, engine);
    }

}
