package com.semicolon.textlab;

import com.semicolon.textlab.console.InputReader;
import com.semicolon.textlab.console.TextLabMenu;

import java.util.Scanner;

public class TextLabApplication {

    public static void main(String[] args) {
        InputReader inputReader = new InputReader(new Scanner(System.in));
        TextLabMenu menu = new TextLabMenu(inputReader);
        menu.start();
    }
}