package com.semicolon.studyvault;

import com.semicolon.studyvault.console.InputReader;
import com.semicolon.studyvault.console.StudyVaultMenu;
import com.semicolon.studyvault.service.NoteService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class StudyVaultApplication {

    public static void main(String[] args) {
        Path projectDirectory = Paths.get("").toAbsolutePath();
        Path notesDirectory = projectDirectory.resolve("data").resolve("notes");
        Path backupsDirectory = projectDirectory.resolve("data").resolve("backups");

        NoteService noteService = new NoteService(notesDirectory, backupsDirectory);
        InputReader inputReader = new InputReader(new Scanner(System.in));
        StudyVaultMenu menu = new StudyVaultMenu(noteService, inputReader);
        menu.start();
    }
}