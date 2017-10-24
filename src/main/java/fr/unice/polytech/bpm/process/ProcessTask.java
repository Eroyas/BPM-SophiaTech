package fr.unice.polytech.bpm.process;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.Command;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import fr.unice.polytech.bpm.process.commands.PromptCommand;
import org.flowable.engine.ProcessEngine;

import java.util.Optional;
import java.util.Scanner;

/**
 * A process task
 */
public abstract class ProcessTask extends PromptCommand {

    protected ProcessEngine engine;
    protected CommandRegistry commands;
    protected Role role;
    private String question;

    /**
     * Instantiates a new process task
     *
     * @param engine
     */
    public  ProcessTask(Role role, CommandRegistry commands, ProcessEngine engine) {
        this.role = role;
        this.commands = commands;
        this.engine = engine;
        // Build question string
        this.question = "Que voulez vous faire ? ";
        this.commands.getCommands(role).forEach(cmd -> question+=cmd.name()+", ");
        this.question = question.substring(0, question.length()-2);
        this.question += " > ";
    }

    @Override
    public void execute(ProcessEngine context) {
        String action = nextLine(question);
        Optional<Command> command = commands.getCommand(role, action);
        if (command.isPresent()) {
            command.get().execute(context);
        } else {
            System.err.println("Command introuvable!");
        }
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public String name() {
        return role.getName();
    }
}
