package com.semicolon.textlab.analysis;


public class PassageComparator {

    public int findFirstDifferencePosition(String firstPassage,
                                           String secondPassage) {
        String safeFirst = firstPassage == null ? "" : firstPassage;
        String safeSecond = secondPassage == null ? "" : secondPassage;
        int shortestLength = Math.min(safeFirst.length(), safeSecond.length());
        for (int position = 0; position < shortestLength; position++) {
            if (safeFirst.charAt(position) != safeSecond.charAt(position)) {
                return position;
            }
        }
        if (safeFirst.length() != safeSecond.length()) {
            return shortestLength;
        }
        return -1;
    }
}