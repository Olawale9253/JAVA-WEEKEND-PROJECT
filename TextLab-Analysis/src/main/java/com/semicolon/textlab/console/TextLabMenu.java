package com.semicolon.textlab.console;

import com.semicolon.textlab.analysis.LongestWordFinder;
import com.semicolon.textlab.analysis.PalindromeChecker;
import com.semicolon.textlab.analysis.PassageComparator;
import com.semicolon.textlab.analysis.TextStatistics;
import com.semicolon.textlab.analysis.TextStatisticsAnalyzer;
import com.semicolon.textlab.analysis.WhitespaceCleaner;
import com.semicolon.textlab.analysis.WordFrequencyCounter;
import com.semicolon.textlab.analysis.WordReplacer;
import com.semicolon.textlab.model.Passage;

import java.util.List;

public class TextLabMenu {

    private static final int MENU_OPTION_STATISTICS = 1;
    private static final int MENU_OPTION_CLEAN = 2;
    private static final int MENU_OPTION_SEARCH = 3;
    private static final int MENU_OPTION_REPLACE = 4;
    private static final int MENU_OPTION_PALINDROME = 5;
    private static final int MENU_OPTION_LONGEST_WORDS = 6;
    private static final int MENU_OPTION_SHOW_ORIGINAL = 7;
    private static final int MENU_OPTION_SHOW_CURRENT = 8;
    private static final int MENU_OPTION_COMPARE = 9;
    private static final int MENU_OPTION_RESET = 10;
    private static final int MENU_OPTION_EXIT = 11;

    private final InputReader inputReader;
    private final TextStatisticsAnalyzer statisticsAnalyzer = new TextStatisticsAnalyzer();
    private final WhitespaceCleaner whitespaceCleaner = new WhitespaceCleaner();
    private final WordFrequencyCounter frequencyCounter = new WordFrequencyCounter();
    private final WordReplacer wordReplacer = new WordReplacer();
    private final PalindromeChecker palindromeChecker = new PalindromeChecker();
    private final LongestWordFinder longestWordFinder = new LongestWordFinder();
    private final PassageComparator passageComparator = new PassageComparator();

    private Passage passage;

    public TextLabMenu(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    public void start() {
        System.out.println("=== Welcome to TextLab ===");
        loadPassage();
        runMenuLoop();
    }

    private void loadPassage() {
        passage = new Passage(inputReader.readMultiLineText("Enter your passage:"));
        if (passage.getCurrentText().trim().isEmpty()) {
            System.out.println("Note: passage is empty. You can still try the menu options.");
        } else {
            System.out.println("Passage loaded ("
                    + passage.getCurrentText().length() + " characters).");
        }
    }

    private void runMenuLoop() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = inputReader.readMenuChoice(
                    "Enter your choice: ", 1, MENU_OPTION_EXIT);
            running = handleChoice(choice);
        }
        System.out.println("Thank you for using TextLab. Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- TextLab Menu ---");
        System.out.println(MENU_OPTION_STATISTICS + ". Show statistics");
        System.out.println(MENU_OPTION_CLEAN + ". Clean whitespace");
        System.out.println(MENU_OPTION_SEARCH + ". Search for a word");
        System.out.println(MENU_OPTION_REPLACE + ". Replace a word");
        System.out.println(MENU_OPTION_PALINDROME + ". Check palindrome");
        System.out.println(MENU_OPTION_LONGEST_WORDS + ". Show longest words");
        System.out.println(MENU_OPTION_SHOW_ORIGINAL + ". Show original passage");
        System.out.println(MENU_OPTION_SHOW_CURRENT + ". Show current passage");
        System.out.println(MENU_OPTION_COMPARE + ". Compare current with original");
        System.out.println(MENU_OPTION_RESET + ". Reset to original");
        System.out.println(MENU_OPTION_EXIT + ". Exit");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case MENU_OPTION_STATISTICS: showStatistics(); return true;
            case MENU_OPTION_CLEAN: cleanWhitespace(); return true;
            case MENU_OPTION_SEARCH: searchForWord(); return true;
            case MENU_OPTION_REPLACE: replaceWord(); return true;
            case MENU_OPTION_PALINDROME: checkPalindrome(); return true;
            case MENU_OPTION_LONGEST_WORDS: showLongestWords(); return true;
            case MENU_OPTION_SHOW_ORIGINAL: showOriginal(); return true;
            case MENU_OPTION_SHOW_CURRENT: showCurrent(); return true;
            case MENU_OPTION_COMPARE: compareWithOriginal(); return true;
            case MENU_OPTION_RESET: resetPassage(); return true;
            case MENU_OPTION_EXIT: return false;
            default: return true;
        }
    }

    private void showStatistics() {
        TextStatistics statistics = statisticsAnalyzer.analyze(passage.getCurrentText());
        System.out.println("Characters (with whitespace):    "
                + statistics.getCharacterCountWithWhitespace());
        System.out.println("Characters (without whitespace): "
                + statistics.getCharacterCountWithoutWhitespace());
        System.out.println("Words:                           "
                + statistics.getWordCount());
        System.out.println("Sentences:                       "
                + statistics.getSentenceCount());
    }

    private void cleanWhitespace() {
        passage.setCurrentText(whitespaceCleaner.clean(passage.getCurrentText()));
        System.out.println("Whitespace cleaned. Run \"Show current passage\" to see the result.");
    }

    private void searchForWord() {
        String word = inputReader.readNonEmptyLine("Enter word to search for: ");
        int frequency = frequencyCounter.countWholeWord(passage.getCurrentText(), word);
        System.out.println("'" + word + "' occurs " + frequency + " time(s) in the current passage.");
    }

    private void replaceWord() {
        String targetWord = inputReader.readNonEmptyLine("Enter word to replace: ");
        String replacement = inputReader.readLine("Enter replacement: ");
        String replaced = wordReplacer.replaceWholeWord(
                passage.getCurrentText(), targetWord, replacement);
        passage.setCurrentText(replaced);
        System.out.println("Replaced all whole-word occurrences of '"
                + targetWord + "' (case-insensitive).");
    }

    private void checkPalindrome() {
        boolean isPalindrome = palindromeChecker.isPalindrome(passage.getCurrentText());
        System.out.println(isPalindrome
                ? "Yes, the current passage is a palindrome "
                + "(ignoring case, spaces, and punctuation)."
                : "No, the current passage is not a palindrome.");
    }

    private void showLongestWords() {
        List<String> longestWords = longestWordFinder.findLongestWords(
                passage.getCurrentText());
        if (longestWords.isEmpty()) {
            System.out.println("No words to inspect.");
            return;
        }
        System.out.println("Longest word(s): " + String.join(", ", longestWords));
    }

    private void showOriginal() {
        System.out.println("--- Original passage ---");
        System.out.println(passage.getOriginalText());
    }

    private void showCurrent() {
        System.out.println("--- Current passage ---");
        System.out.println(passage.getCurrentText());
    }

    private void compareWithOriginal() {
        int position = passageComparator.findFirstDifferencePosition(
                passage.getOriginalText(), passage.getCurrentText());
        if (position == -1) {
            System.out.println("Current passage is identical to the original.");
        } else {
            System.out.println("First difference at position " + position
                    + " (0-based).");
        }
    }

    private void resetPassage() {
        passage.resetToOriginal();
        System.out.println("Reset to original passage.");
    }
}