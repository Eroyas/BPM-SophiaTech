package fr.unice.polytech.bpm.process;

import fr.unice.polytech.bpm.Role;
import fr.unice.polytech.bpm.engine.BPMNFactory;
import fr.unice.polytech.bpm.process.commands.CommandRegistry;
import fr.unice.polytech.bpm.process.tasks.OrganizerTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
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
    public static ProcessEngine engine;

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

        this.integrateSubProcess();
    }

    /**
     * Integrate our different process
     */
    private void integrateSubProcess() {
        ProcessIntegration integration = new ProcessIntegration(engine);
        // Define integrations
        integration.addSimpleTrigger("form-team", "schedule", "chose-companies");
        integration.addAggregateTrigger(new String[]{"schedule", "chose-companies"}, "notify-attendees");
        integration.addSimpleTrigger("notify-attendees", "communicate-to-students", "prepare-event");
        integration.addAggregateTrigger(new String[]{"communicate-to-students", "prepare-event"}, "achieve");
        integration.addSimpleTrigger("achieve", "feedback");
        // Add code trigger for feedback
        integration.addSimpleTrigger("feedback", () -> System.out.println("Fin du SophiaTech Forum"));
        integration.addSimpleTrigger("feedback", SophiaTechForumProcess.this::displayMetrics);
    }

    /**
     * Display the metrics
     */
    private void displayMetrics() {
        HistoryService historyService = engine.getHistoryService();
        List<ProcessInstance> instances = engine.getRuntimeService()
                .createProcessInstanceQuery()
                .list();

        for(ProcessInstance processInstance: instances) {
            System.out.println("Metrics for process instance" + processInstance.getId());
            List<HistoricActivityInstance> activities =
                    historyService.createHistoricActivityInstanceQuery()
                            .processInstanceId(processInstance.getId())
                            .orderByHistoricActivityInstanceEndTime().asc()
                            .list();
            for (HistoricActivityInstance activity : activities) {
                System.out.println(activity.getActivityId() + " took "
                        + activity.getDurationInMillis() + " milliseconds");
            }
            System.out.println("");
        }
    }
    /**
     * Register our different tasks
     */
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
            displayCurrentTasks();
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
     * Display the current asks
     */
    private void displayCurrentTasks() {
        TaskService taskService = engine.getTaskService();
        System.out.println("========================================");
        System.out.println("Voici les tâches qu'il y a à faire:");
        for (Role role : Role.values()) {
            String candidateGroup = role.name().toLowerCase();
            // List the different tasks and display them
            List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(candidateGroup).list();
            // If some some tasks for the candidate group
            if (!tasks.isEmpty()) {
                // Else, sad face we have some work to do. Display the tasks
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("#" + tasks.get(i).getId() + " " + tasks.get(i).getName() + " [" + role.getName() + "]");
                }
            }
        }
        System.out.println("========================================");
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
        System.exit(0);
    }


    public static void main(String[] args) {
        Process process = new SophiaTechForumProcess();
        process.start();
        process.stop();
    }
}
