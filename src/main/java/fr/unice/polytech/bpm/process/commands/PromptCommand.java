package fr.unice.polytech.bpm.process.commands;

import java.util.Scanner;

public abstract class PromptCommand implements Command {


    private Scanner scanner;

    public PromptCommand() {
        this.scanner = new Scanner(System.in);
    }

    /**
     *
     * @return user input
     */
    public String nextLine(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }
}
