package com.semicolon.studyvault.console;

import com.semicolon.studyvault.exception.InvalidNoteNameException;
import com.semicolon.studyvault.exception.NoteAlreadyExistsException;
import com.semicolon.studyvault.exception.NoteNotFoundException;
import com.semicolon.studyvault.exception.StorageException;
import com.semicolon.studyvault.model.Note;
import com.semicolon.studyvault.model.NoteSummary;
import com.semicolon.studyvault.model.SearchResult;
import com.semicolon.studyvault.service.NoteService;

import java.util.List;

public class StudyVaultMenu {

    private static final int MENU_OPTION_CREATE = 1;
    private static final int MENU_OPTION_LIST = 2;
    private static final int MENU_OPTION_OPEN = 3;
    private static final int MENU_OPTION_APPEND = 4;
    private static final int MENU_OPTION_SEARCH = 5;
    private static final int MENU_OPTION_DELETE = 6;
    private static final int MENU_OPTION_EXPORT = 7;
    private static final int MENU_OPTION_RESTORE = 8;
    private static final int MENU_OPTION_EXIT = 9;

    private final NoteService noteService;
    private final InputReader inputReader;

    public StudyVaultMenu(NoteService noteService, InputReader inputReader) {
        this.noteService = noteService;
        this.inputReader = inputReader;
    }

    public void start() {
        System.out.println("=== Welcome to StudyVault ===");
        runMenuLoop();
    }

    private void runMenuLoop() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = inputReader.readMenuChoice(
                    "Enter your choice: ", 1, MENU_OPTION_EXIT);
            try {
                running = handleChoice(choice);
            } catch (StorageException storageException) {
                System.out.println("Operation failed: "
                        + storageException.getMessage());
            }
        }
        System.out.println("Thank you for using StudyVault. Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- StudyVault Menu ---");
        System.out.println(MENU_OPTION_CREATE + ". Create a note");
        System.out.println(MENU_OPTION_LIST + ". List notes");
        System.out.println(MENU_OPTION_OPEN + ". Open a note");
        System.out.println(MENU_OPTION_APPEND + ". Append to a note");
        System.out.println(MENU_OPTION_SEARCH + ". Search notes");
        System.out.println(MENU_OPTION_DELETE + ". Delete a note");
        System.out.println(MENU_OPTION_EXPORT + ". Export a note (backup)");
        System.out.println(MENU_OPTION_RESTORE + ". Restore from backup");
        System.out.println(MENU_OPTION_EXIT + ". Exit");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case MENU_OPTION_CREATE: createNote(); return true;
            case MENU_OPTION_LIST: listNotes(); return true;
            case MENU_OPTION_OPEN: openNote(); return true;
            case MENU_OPTION_APPEND: appendToNote(); return true;
            case MENU_OPTION_SEARCH: searchNotes(); return true;
            case MENU_OPTION_DELETE: deleteNote(); return true;
            case MENU_OPTION_EXPORT: exportNote(); return true;
            case MENU_OPTION_RESTORE: restoreNote(); return true;
            case MENU_OPTION_EXIT: return false;
            default: return true;
        }
    }

    private void createNote() {
        String title = inputReader.readNonEmptyLine("Enter note title: ");
        String content = inputReader.readMultiLineContent("Enter note content:");
        try {
            noteService.createNote(new Note(title, content), false);
            System.out.println("Note '" + title + "' created.");
        } catch (NoteAlreadyExistsException noteAlreadyExistsException) {
            boolean overwrite = inputReader.readYesNo(
                    "Note '" + title + "' already exists. Overwrite? (y/n): ");
            if (overwrite) {
                noteService.createNote(new Note(title, content), true);
                System.out.println("Note '" + title + "' overwritten.");
            } else {
                System.out.println("Creation cancelled.");
            }
        } catch (InvalidNoteNameException invalidNoteNameException) {
            System.out.println("Invalid note title: "
                    + invalidNoteNameException.getMessage());
        }
    }

    private void listNotes() {
        List<NoteSummary> summaries = noteService.listNotes();
        if (summaries.isEmpty()) {
            System.out.println("No notes saved yet.");
            return;
        }
        System.out.println("--- " + summaries.size() + " note(s) ---");
        for (NoteSummary summary : summaries) {
            System.out.println("- " + summary.getTitle()
                    + " | " + summary.getSizeInBytes() + " bytes"
                    + " | " + summary.getLineCount() + " line(s)"
                    + " | modified " + summary.getLastModified());
        }
    }

    private void openNote() {
        String title = inputReader.readNonEmptyLine("Enter note title: ");
        try {
            Note note = noteService.readNote(title);
            System.out.println("--- " + note.getTitle() + " ---");
            System.out.println(note.getContent());
        } catch (NoteNotFoundException noteNotFoundException) {
            System.out.println(noteNotFoundException.getMessage());
        }
    }

    private void appendToNote() {
        String title = inputReader.readNonEmptyLine("Enter note title: ");
        String additionalContent = inputReader.readMultiLineContent(
                "Enter content to append:");
        try {
            noteService.appendToNote(title, "\n" + additionalContent);
            System.out.println("Appended to note '" + title + "'.");
        } catch (NoteNotFoundException noteNotFoundException) {
            System.out.println(noteNotFoundException.getMessage());
        }
    }

    private void searchNotes() {
        String keyword = inputReader.readNonEmptyLine("Enter search keyword: ");
        List<SearchResult> results = noteService.searchNotes(keyword);
        if (results.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }
        System.out.println("--- " + results.size() + " match(es) ---");
        for (SearchResult result : results) {
            System.out.println(result.getTitle()
                    + ":" + result.getLineNumber()
                    + "  " + result.getLineText());
        }
    }

    private void deleteNote() {
        String title = inputReader.readNonEmptyLine("Enter note title to delete: ");
        boolean confirmed = inputReader.readYesNo(
                "Delete note '" + title + "'? (y/n): ");
        if (!confirmed) {
            System.out.println("Deletion cancelled.");
            return;
        }
        try {
            noteService.deleteNote(title);
            System.out.println("Note '" + title + "' deleted.");
        } catch (NoteNotFoundException noteNotFoundException) {
            System.out.println(noteNotFoundException.getMessage());
        }
    }

    private void exportNote() {
        String title = inputReader.readNonEmptyLine("Enter note title to export: ");
        try {
            noteService.exportNote(title);
            System.out.println("Exported '" + title + "' to backups.");
        } catch (NoteNotFoundException noteNotFoundException) {
            System.out.println(noteNotFoundException.getMessage());
        }
    }

    private void restoreNote() {
        String title = inputReader.readNonEmptyLine("Enter note title to restore: ");
        boolean overwrite = false;
        try {
            if (noteService.noteExists(title)) {
                overwrite = inputReader.readYesNo(
                        "A note titled '" + title + "' already exists. Overwrite? (y/n): ");
                if (!overwrite) {
                    System.out.println("Restore cancelled.");
                    return;
                }
            }
            noteService.restoreNote(title, overwrite);
            System.out.println("Restored note '" + title + "' from backups.");
        } catch (StorageException storageException) {
            System.out.println(storageException.getMessage());
        }
    }
}