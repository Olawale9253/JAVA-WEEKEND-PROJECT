# TextLab Acceptance Checklist

| Check (from challenge PDF)                                    | Expected                                                     | Actual                                                              | Pass |
|---------------------------------------------------------------|--------------------------------------------------------------|---------------------------------------------------------------------|------|
| Repeated spaces, tabs, line breaks                            | Collapse to single space                                     | Test: clean_tabsAndNewlines_collapseToOneSpace                      | [x]  |
| Mixed-case words: Java, JAVA, java                            | All matched case-insensitively                               | Test: countWholeWord_caseInsensitiveVariants_allMatch               | [x]  |
| Passage containing Java and JavaScript                        | "Java" search does not match the "Java" inside "JavaScript"  | Test: countWholeWord_javaInJavaScript_notCounted                    | [x]  |
| "A man, a plan, a canal: Panama!"                             | Palindrome = true                                            | Test: isPalindrome_aManAPlanACanalPanama_returnsTrue                | [x]  |
| Passage containing punctuation but no words                   | Statistics zero, no crash                                    | Test: countWords_punctuationOnlyText_returnsZero                    | [x]  |
| Empty or whitespace-only input                                | Zero statistics, no crash                                    | Tests: countWords_emptyText, countWords_whitespaceOnlyText          | [x]  |
| Whole-word search for "Java" does not match "JavaScript"      | Correct rejection                                            | Test: countWholeWord_javaInJavaScript_notCounted                    | [x]  |
| Original passage preserved for comparison                     | Original unchanged after edit                                | Test: getOriginalText_remainsUnchangedWhenCurrentTextEdited         | [x]  |
| Stretch: first differing position between two passages        | Correct index or -1                                          | Tests in PassageComparatorTest                                      | [x]  |