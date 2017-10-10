package fr.unice.polytech.bpm.schedule;

import org.flowable.engine.ProcessEngine;

import java.util.Scanner;

/**
 * A process task
 */
public abstract class ProcessTask {

    protected ProcessEngine engine;
    private Scanner scanner;

    /**
     * Instantiates a new process task
     *
     * @param engine
     */
    public  ProcessTask(ProcessEngine engine) {
        this.engine = engine;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Run the proccess task
     */
    public abstract void run();

    /**
     *
     * @return user input
     */
    public String nextLine(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }
}
