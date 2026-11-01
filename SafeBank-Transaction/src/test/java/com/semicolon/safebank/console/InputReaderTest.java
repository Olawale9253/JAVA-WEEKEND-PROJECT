package com.semicolon.safebank.console;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputReaderTest {

    private ByteArrayOutputStream capturedOutput;
    private PrintStream originalOut;

    @BeforeEach
    void suppressConsoleOutput() {
        originalOut = System.out;
        capturedOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOutput));
    }

    @AfterEach
    void restoreConsoleOutput() {
        System.setOut(originalOut);
    }

    private InputReader readerFor(String scriptedInput) {
        return new InputReader(new Scanner(new StringReader(scriptedInput)));
    }

    @Test
    void readLine_withSurroundingSpaces_returnsTrimmedValue() {
        InputReader inputReader = readerFor("   Taofeek Olawale   \n");
        assertEquals("Taofeek Olawale", inputReader.readLine("Name: "));
    }

    @Test
    void readAmount_nonNumericInput_rePromptsUntilValidNumber() {
        InputReader inputReader = readerFor("abc\n250\n");
        double amount = inputReader.readAmount("Amount: ");
        assertEquals(250.00, amount, 0.001);
    }

    @Test
    void readMenuChoice_nonNumericInput_rePromptsUntilValidChoice() {
        InputReader inputReader = readerFor("hello\n3\n");
        int choice = inputReader.readMenuChoice("Choice: ", 1, 5);
        assertEquals(3, choice);
    }

    @Test
    void readMenuChoice_outOfRange_rePromptsUntilValidChoice() {
        InputReader inputReader = readerFor("9\n2\n");
        int choice = inputReader.readMenuChoice("Choice: ", 1, 5);
        assertEquals(2, choice);
    }
}