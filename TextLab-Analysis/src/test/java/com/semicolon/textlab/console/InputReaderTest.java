package com.semicolon.textlab.console;

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
    void readLine_returnsTrimmedLine() {
        InputReader inputReader = readerFor("   hello   \n");
        assertEquals("hello", inputReader.readLine("Enter: "));
    }

    @Test
    void readMultiLineText_threeLines_joinedWithNewlines() {
        InputReader inputReader = readerFor("First line\nSecond line\nThird line\n\n");
        assertEquals("First line\nSecond line\nThird line",
                inputReader.readMultiLineText("Passage:"));
    }

    @Test
    void readMultiLineText_emptyOnFirstLine_returnsEmptyString() {
        InputReader inputReader = readerFor("\n");
        assertEquals("", inputReader.readMultiLineText("Passage:"));
    }

    @Test
    void readNonEmptyLine_emptyThenValue_repromptsUntilNonEmpty() {
        InputReader inputReader = readerFor("\nhello\n");
        assertEquals("hello", inputReader.readNonEmptyLine("Enter: "));
    }

    @Test
    void readMenuChoice_nonNumericInput_rePromptsUntilValidChoice() {
        InputReader inputReader = readerFor("abc\n3\n");
        assertEquals(3, inputReader.readMenuChoice("Choice: ", 1, 11));
    }
}