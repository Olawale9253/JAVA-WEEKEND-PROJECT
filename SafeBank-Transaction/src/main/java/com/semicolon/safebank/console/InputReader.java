package com.semicolon.safebank.console;

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

    /** Keeps asking until the user enters a valid number. Never throws. */
    public double readAmount(String prompt) {
        while (true) {
            String rawInput = readLine(prompt);
            try {
                return Double.parseDouble(rawInput);
            } catch (NumberFormatException numberFormatException) {
                System.out.println(
                        "'" + rawInput + "' is not a valid amount. "
                                + "Please enter a number, for example 500.");
            }
        }
    }

    /** Keeps asking until the user enters a number inside the menu range. */
    public int readMenuChoice(String prompt, int minimumChoice, int maximumChoice) {
        while (true) {
            String rawInput = readLine(prompt);
            try {
                int choice = Integer.parseInt(rawInput);
                if (choice < minimumChoice || choice > maximumChoice) {
                    System.out.println(
                            "Choice must be between "
                                    + minimumChoice + " and " + maximumChoice + ".");
                    continue;
                }
                return choice;
            } catch (NumberFormatException numberFormatException) {
                System.out.println(
                        "'" + rawInput + "' is not a valid menu number. "
                                + "Please enter a number from "
                                + minimumChoice + " to " + maximumChoice + ".");
            }
        }
    }
}