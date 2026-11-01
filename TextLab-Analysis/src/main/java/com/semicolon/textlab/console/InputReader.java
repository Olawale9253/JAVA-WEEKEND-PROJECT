package com.semicolon.textlab.console;

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

    /** Re-prompts until the user types a non-empty line. */
    public String readNonEmptyLine(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input must not be empty. Please try again.");
        }
    }

    /** Reads lines until the user enters an empty line. */
    public String readMultiLineText(String prompt) {
        System.out.println(prompt);
        System.out.println("(Press Enter on an empty line to finish)");
        StringBuilder builder = new StringBuilder();
        boolean isFirstLine = true;
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            if (!isFirstLine) {
                builder.append('\n');
            }
            builder.append(line);
            isFirstLine = false;
        }
        return builder.toString();
    }

    public int readMenuChoice(String prompt, int minimumChoice, int maximumChoice) {
        while (true) {
            String rawInput = readLine(prompt);
            try {
                int choice = Integer.parseInt(rawInput);
                if (choice < minimumChoice || choice > maximumChoice) {
                    System.out.println(
                            "Choice must be between " + minimumChoice
                                    + " and " + maximumChoice + ".");
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