package com.semicolon.coorthub.console;

import java.util.Scanner;

public class InputReader {

    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readNonEmptyLine(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input must not be empty. Please try again.");
        }
    }

    public int readMenuChoice(String prompt, int minimumChoice, int maximumChoice) {
        while (true) {
            String rawInput = readLine(prompt);
            try {
                int choice = Integer.parseInt(rawInput);
                if (choice < minimumChoice || choice > maximumChoice) {
                    System.out.println("Choice must be between "
                            + minimumChoice + " and " + maximumChoice + ".");
                    continue;
                }
                return choice;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("'" + rawInput
                        + "' is not a valid menu number. Please enter a number from "
                        + minimumChoice + " to " + maximumChoice + ".");
            }
        }
    }
}