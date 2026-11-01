# StudyVault Notes Manager

## Purpose
Console notes manager that persists study notes to disk across runs. Demonstrates
BufferedReader/BufferedWriter, UTF-8 text I/O, FileInputStream/FileOutputStream
byte-stream copying, and try-with-resources.

## How to run
cd 03-studyvault-notes-manager
mvn test
mvn exec:java

## Data directories
- data/notes/   : saved notes, one .txt file per note
- data/backups/ : exported notes for restore

Both are created automatically on first use. The data/ folder is git-ignored;
.gitkeep files preserve the empty directory structure in the repo.

## Project structure
- storage/NoteNameValidator : pure validation, rejects blank / path separators / dot-dot
- storage/FileNoteStorage   : text I/O with BufferedReader and BufferedWriter (UTF-8)
- storage/BackupStorage     : byte-stream copy with FileInputStream/FileOutputStream + 8 KB buffer
- service/NoteService       : composes the two storages; no console output
- console/InputReader       : Scanner wrapper, handles non-numeric, y/n, multi-line
- console/StudyVaultMenu    : menu loop, only class that touches System.out

## Design decisions
- Every file operation uses try-with-resources. No stream leaks.
- The backup copy reads into a new byte[8192] and loops until read() returns -1.
  Files.copy() is deliberately not used, per the challenge rule.
- FileNoteStorage and BackupStorage both create their directories if absent,
  so first run works on a fresh checkout with no setup.
- The restore path throws BackupConflictException when the target exists.
  The menu catches it and asks the user before calling restore with overwrite=true.
  The storage layer never silently overwrites a note.
- FileNoteStorage.readNote uses read(char[], int, int) instead of readLine() so
  that trailing newlines are preserved exactly on round-trip.
- Search is case-insensitive and returns (title, line number, line text) so
  the menu can display "filename:line  match".
- Note titles are stored as "<title>.txt". If a title already ends in .txt,
  the file becomes "<title>.txt.txt". This is predictable but documented.

## Known limitations
- The compare-with-original features are project 02's; StudyVault has no
  versioning. Backup overwrites the previous backup on every export.
- Search uses simple substring matching per line, so "not" matches "notebook".
  A whole-word version would use a Pattern with lookarounds (as TextLab does).
- Large content files are fine, but there is no streaming menu for very large
  notes; readNote loads the whole file into memory.

## Stretch goals completed
- note list reports size in bytes, line count, and last-modified time via NoteSummary.