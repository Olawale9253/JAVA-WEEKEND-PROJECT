# TextLab Text Analysis and Cleanup

## Purpose
Console application that inspects and cleans written passages: counts
characters, words and sentences; cleans whitespace; searches and replaces
whole words; checks palindromes; shows longest words; and compares the
current text with the original.

## How to run
cd 02-textlab-text-analysis
mvn test
mvn exec:java

## Project structure
- model/Passage             : holds originalText (immutable) and currentText (mutable)
- analysis/TextStatistics   : immutable value object for the four counts
- analysis/TextStatisticsAnalyzer : characters / words / sentences
- analysis/WhitespaceCleaner : trim + collapse, uses StringBuilder
- analysis/WordFrequencyCounter : whole-word, case-insensitive counting
- analysis/WordReplacer     : whole-word, case-insensitive replacement
- analysis/PalindromeChecker : ignores case, spaces, punctuation; uses StringBuilder
- analysis/LongestWordFinder : longest words, deduplicated case-insensitively
- analysis/PassageComparator : stretch goal, first differing position
- console/InputReader       : Scanner wrapper, handles non-numeric and multi-line
- console/TextLabMenu       : menu loop, only class that touches System.out

## Design decisions
- Analysis classes are stateless pure functions, so each one is unit-tested
  independently and each test asserts a returned value.
- "Word" is defined as `[A-Za-z0-9]+` — ASCII letters and digits only, per
  the challenge specification. Non-ASCII letters (accented, non-Latin) would
  require switching to `[\p{L}\p{N}]+`.
- Whole-word matching uses explicit lookarounds
  `(?<![A-Za-z0-9])` / `(?![A-Za-z0-9])` rather than `\b`, because `\b`
  treats underscore as a word character and would give inconsistent results.
- WordReplacer uses Matcher.quoteReplacement so a replacement containing
  `$` or `\` is literal, not a regex backreference.
- The original passage is never mutated. Passage keeps it in a final field
  so the compare-with-original feature always works, even after any number
  of edits.
- The empty string is treated as a palindrome (a mathematically correct
  answer), and punctuation-only input collapses to the empty string before
  the palindrome check runs.

## Known limitations
- Word definition is ASCII-only. Accented characters count as separators.
- Whole-word matching treats non-letter characters as boundaries, which
  means "e-mail" splits into two words by design.
- The compare-with-original feature reports the first difference only.

## Stretch goals completed
- Compare two passages and report the first position where they differ.