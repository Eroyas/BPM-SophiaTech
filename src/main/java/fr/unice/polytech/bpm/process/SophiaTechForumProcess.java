package fr.unice.polytech.bpm.process;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.engine.BPMNFactory;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import fr.unice.polytech.bpm.process.tasks.OrganizerTask;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import java.util.*;

/**
 * Sophia tech forum process
 */
public class SophiaTechForumProcess implements Process {

    /**
     * Our process engine
     */
    private ProcessEngine engine;

    private CommandRegistry commands;

    /**
     * Tasks by roles
     */
    private Map<Role, ProcessTask> tasks;

    private static final String STOP = "stop";
    private String question;

    /**
     * Create Sophia tech forum process
     */
    public SophiaTechForumProcess() {
        this.commands = new CommandRegistry();
        this.engine = BPMNFactory.create("Organisation du SophiaTech Forum")
                .withDiagram("achieve.bpmn")
                .withDiagram("chose-companies.bpmn")
                .withDiagram("communicate-to-students.bpmn")
                .withDiagram("feedback.bpmn")
                .withDiagram("form-team.bpmn")
                .withDiagram("notify-attendees.bpmn")
                .withDiagram("prepare-event.bpmn")
                .withDiagram("schedule.bpmn")
                .build();
        this.tasks = new HashMap<>();
        this.registerTaskForRoles();
        this.question = "\nQui êtes vous ? ";
        this.tasks.keySet().forEach(role -> question+=role.getName()+",");
        this.question+="stop > ";
    }

    private void registerTaskForRoles() {
        Reflections reflections = new Reflections(
                ClasspathHelper.forClass(OrganizerTask.class),
                new SubTypesScanner(false));
        Set<Class<? extends ProcessTask>> allClasses =
                reflections.getSubTypesOf(ProcessTask.class);
        for (Class<? extends ProcessTask> clazz : allClasses) {
            try {
                ProcessTask task = clazz.getConstructor(CommandRegistry.class, ProcessEngine.class).newInstance(commands, engine);
                this.tasks.put(task.getRole(), task);
            } catch (Exception e) {
                // TODO: add logger
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        Scanner scanner = new Scanner(System.in);
        // While the role isn't empty (empty=user stop)
        while (true) {
            Optional<Role> role = askRole(scanner);
            // If we have a user, do the different user actions
            if (role.isPresent()) {
                Optional<ProcessTask> taskOpt = Optional.ofNullable(tasks.get(role.get()));

                if (taskOpt.isPresent()) {
                    try {
                        taskOpt.get().execute(engine);
                    } catch (Exception e) {
                        System.err.println("Role " + role.get() + " mal implémenté");
                    }
                } else {
                    System.err.println("Role " + role.get() + " non implémenté");
                }

            } else {
                // Else, exit the loop
                break;
            }
        }
        scanner.close();
    }


    /**
     * Ask for a role
     * @param scanner
     */
    private Optional<Role> askRole(Scanner scanner) {
        System.out.print(question);
        String message = scanner.nextLine();
        // If input is stop, return no role
        if (message.equals(STOP)) {
            System.out.println("Au revoir");
            return Optional.empty();
        } else {
            // Else return a role from its name. If role=Unknown, send an error
            Role role = Role.fromName(message);
            if (role.equals(Role.UNKNOWN)) {
                System.err.println("Role inconnu: " + message);
                return askRole(scanner);
            } else {
                return Optional.of(role);
            }
        }
    }

    /**
     * Stop the current process
     */
    public void stop() {
        ProcessEngines.destroy();
    }


    public static void main(String[] args) {
        Process process = new SophiaTechForumProcess();
        process.start();
        process.stop();
    }
}
