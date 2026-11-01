package com.semicolon.studyvault.console;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void readLine_returnsTrimmedLine() {
        InputReader inputReader = readerFor("   hello   \n");
        assertEquals("hello", inputReader.readLine("Enter: "));
    }

    @Test
    void readMultiLineContent_threeLines_joinedWithNewlines() {
        InputReader inputReader = readerFor("First\nSecond\nThird\n\n");
        assertEquals("First\nSecond\nThird",
                inputReader.readMultiLineContent("Content:"));
    }

    @Test
    void readMenuChoice_nonNumericInput_rePromptsUntilValidChoice() {
        InputReader inputReader = readerFor("abc\n4\n");
        assertEquals(4, inputReader.readMenuChoice("Choice: ", 1, 9));
    }

    @Test
    void readYesNo_y_returnsTrue() {
        InputReader inputReader = readerFor("y\n");
        assertTrue(inputReader.readYesNo("Confirm? "));
    }

    @Test
    void readYesNo_yes_returnsTrue() {
        InputReader inputReader = readerFor("yes\n");
        assertTrue(inputReader.readYesNo("Confirm? "));
    }

    @Test
    void readYesNo_n_returnsFalse() {
        InputReader inputReader = readerFor("n\n");
        assertFalse(inputReader.readYesNo("Confirm? "));
    }

    @Test
    void readYesNo_invalidThenYes_rePromptsThenReturnsTrue() {
        InputReader inputReader = readerFor("maybe\nyes\n");
        assertTrue(inputReader.readYesNo("Confirm? "));
    }
}