# Java Weekend Project Challenge

## Student
Name: Taofeek Olawale

Cohort: 30

## Projects
1. SafeBank Transaction Simulator (Exception Handling)
2. TextLab Text Analysis (Strings)
3. StudyVault Notes Manager (File Handling and I/O Streams)
4. CohortHub Learners and Groups (Java Collection Framework)

## How to run any project
cd <project-folder>
mvn test
mvn exec:java

## Reflection

1. Which project was most challenging, and why?
   StudyVault. File I/O has more failure modes than any other topic: encoding,
   missing files, silent overwrites, path traversal, and buffer sizing. JUnit's
   @TempDir kept the tests fast and isolated, but designing the layered
   split between FileNoteStorage, BackupStorage, and NoteService took more
   thought than the other three projects combined. The byte-buffer copy
   requirement also forced me to read the FileInputStream/FileOutputStream
   API instead of reaching for Files.copy.

2. Where did you use concepts from another topic?
- Exception handling: StudyVault uses the same custom-exception pattern
  from SafeBank (base StorageException, specific subclasses).
- Strings: StudyVault's NoteNameValidator and search feature use
  String.trim, toLowerCase, and contains, mirroring TextLab's whole-word
  techniques.
- Collections: StudyVault's FileNoteStorage.listNotes returns a sorted
  List<NoteSummary>, using Comparator just like CohortHub's learner sort.
- Input handling: the same InputReader pattern (injected Scanner,
  re-prompt on invalid input) is reused across all four projects, which
  meant I only had to write and test it once carefully.

3. What would I improve with more time?
- Replace double with BigDecimal in SafeBank (money math precision).
- Add whole-word search to StudyVault (currently substring per line).
- Add versioning to StudyVault backups (currently a single backup file
  per note, overwritten on each export).
- Wrap CohortHub's services behind a single facade so the menu has fewer
  dependencies.