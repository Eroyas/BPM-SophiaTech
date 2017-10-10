package fr.unice.polytech.bpm.schedule;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.engine.BPMNFactory;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;

import java.util.Optional;
import java.util.Scanner;

/**
 * Our schedule sub process
 */
public class Schedule {


    private static final String STOP = "stop";

    private ProcessEngine engine;

    /**
     * Create our schedule business process from a given bpmn file
     * @param file
     */
    public Schedule(String file) {
        this.engine = BPMNFactory.create("Planifier une date et un lieu")
                                 .withDiagram(file)
                                 .build();
    }

    /**
     * Starts the current process
     */
    public Schedule start() {
        String message = "";
        Scanner scanner = new Scanner(System.in);
        // While the role isn't empty (empty=user stop)
        while (true) {
            Optional<Role> role = askRole(scanner);
            // If we have a user, do the different user actions
            if (role.isPresent()) {
                switch (role.get()) {
                    // Handle supervisor actions
                    case SUPERVISOR:
                        new ScheduleSupervisorTask(this.engine).run();
                        break;
                    default:
                        // Default fallback -> We do not have this role implemented yet
                        System.err.println("Role " + role.get() + " non implémenté");
                        break;
                }
            } else {
                // Else, exit the loop
                break;
            }
        }
        scanner.close();
        return this;
    }

    /**
     * Ask for a role
     * @param scanner
     */
    private Optional<Role> askRole(Scanner scanner) {
        System.out.print("\nQui êtes vous (superviseur,stop) ? ");
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
        new Schedule("schedule.bpmn").start().stop();
    }
}
