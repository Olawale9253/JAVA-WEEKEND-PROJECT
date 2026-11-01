package com.semicolon.coorthub.comparator.console;

import com.semicolon.coorthub.console.InputReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputReaderTest {

    private PrintStream originalOut;

    @BeforeEach
    void suppressConsoleOutput() {
        originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
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
        InputReader inputReader = readerFor("  Alice  \n");
        assertEquals("Alice", inputReader.readLine("Name: "));
    }

    @Test
    void readNonEmptyLine_emptyThenValue_reprompts() {
        InputReader inputReader = readerFor("\nAlice\n");
        assertEquals("Alice", inputReader.readNonEmptyLine("Name: "));
    }

    @Test
    void readMenuChoice_nonNumericInput_reprompts() {
        InputReader inputReader = readerFor("abc\n3\n");
        assertEquals(3, inputReader.readMenuChoice("Choice: ", 1, 10));
    }

    @Test
    void readMenuChoice_outOfRange_reprompts() {
        InputReader inputReader = readerFor("99\n2\n");
        assertEquals(2, inputReader.readMenuChoice("Choice: ", 1, 10));
    }

    @Test
    void readMenuChoice_validInput_returnsImmediately() {
        InputReader inputReader = readerFor("5\n");
        assertEquals(5, inputReader.readMenuChoice("Choice: ", 1, 10));
    }
}