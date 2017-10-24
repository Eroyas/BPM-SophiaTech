package fr.unice.polytech.bpm.process.commands;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.process.commands.list.OrganizerListTasksCommand;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import java.util.*;

/**
 * A command registry
 */
public class CommandRegistry {

    private Map<Role, Map<String, Command>> commands;

    public CommandRegistry() {
        this.commands = new HashMap<>();
        Reflections reflections = new Reflections(
                ClasspathHelper.forClass(OrganizerListTasksCommand.class),
                new SubTypesScanner(false));
        Set<Class<? extends Command>> allClasses =
                reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> clazz : allClasses) {
            Command command = null;
            try {
                command = clazz.newInstance();
                registerCommand(command);
            } catch (Exception e) {
                // TODO: add logger
            }
        }
    }

    /**
     * Registers a command to the registry
     * @param command
     */
    public void registerCommand(Command command) {
        Map<String, Command> commandsForRole = commands.get(command.getRole());
        if (commandsForRole == null) {
            commandsForRole = new HashMap<>();
            commands.put(command.getRole(), commandsForRole);
        }
        commandsForRole.put(command.name(), command);
    }

    /**
     *
     * @param role
     * @param name
     * @return command for a given role and name
     */
    public Optional<Command> getCommand(Role role, String name) {
        return Optional.ofNullable(commands.getOrDefault(role, new HashMap<>()).get(name));
    }

    /**
     *
     * @param role
     * @return commands for a given role
     */
    public Collection<Command> getCommands(Role role) {
        return commands.getOrDefault(role, new HashMap<>()).values();
    }
}
