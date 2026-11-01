package com.semicolon.coorthub.model;

import java.util.Collections;
import java.util.Map;

public class CohortReport {

    private final int totalLearners;
    private final int totalGroups;
    private final Map<String, Integer> membershipsPerGroup;
    private final int learnersWaitingForSupport;

    public CohortReport(int totalLearners,
                        int totalGroups,
                        Map<String, Integer> membershipsPerGroup,
                        int learnersWaitingForSupport) {
        this.totalLearners = totalLearners;
        this.totalGroups = totalGroups;
        this.membershipsPerGroup = Collections.unmodifiableMap(membershipsPerGroup);
        this.learnersWaitingForSupport = learnersWaitingForSupport;
    }

    public int getTotalLearners() { return totalLearners; }
    public int getTotalGroups() { return totalGroups; }
    public Map<String, Integer> getMembershipsPerGroup() { return membershipsPerGroup; }
    public int getLearnersWaitingForSupport() { return learnersWaitingForSupport; }
}