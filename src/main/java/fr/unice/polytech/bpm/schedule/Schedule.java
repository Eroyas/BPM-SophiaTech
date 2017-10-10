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
    public void start() {
        String message = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Optional<Role> role = askRole(scanner);
            if (role.isPresent()) {
                switch (role.get()) {
                    case SUPERVISOR:
                        new ScheduleSupervisorTask(this.engine).run();
                        break;
                    default:
                        System.err.println("Role " + role.get() + " non implémenté");
                        break;
                }
            } else {
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
        System.out.print("\nQui êtes vous (superviseur,stop) ? ");
        String message = scanner.nextLine();
        if (message.equals(STOP)) {
            System.out.println("Bye");
            return Optional.empty();
        } else {
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
        new Schedule("schedule.bpmn").start();
    }
}
