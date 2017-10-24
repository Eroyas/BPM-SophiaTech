package fr.unice.polytech.bpm.process.commands;

import fr.unice.polytech.bpm.Role;
import org.flowable.engine.ProcessEngine;

/**
 * A command
 */
public interface Command {

    /**
     * Execute a command with a given context
     * @param context
     */
    void execute(ProcessEngine context);

    /**
     * Get the role associated with the command
     * @return
     */
    Role getRole();

    /**
     *
     * @return name of the command
     */
    String name();
}
