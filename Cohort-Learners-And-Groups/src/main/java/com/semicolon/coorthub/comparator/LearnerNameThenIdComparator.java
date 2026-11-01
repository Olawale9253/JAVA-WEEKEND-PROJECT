package com.semicolon.coorthub.comparator;

import com.semicolon.coorthub.model.Learner;

import java.util.Comparator;


public class LearnerNameThenIdComparator implements Comparator<Learner> {

    @Override
    public int compare(Learner first, Learner second) {
        int nameComparison = first.getFullName()
                .compareToIgnoreCase(second.getFullName());
        if (nameComparison != 0) {
            return nameComparison;
        }
        return first.getLearnerId().compareTo(second.getLearnerId());
    }
}