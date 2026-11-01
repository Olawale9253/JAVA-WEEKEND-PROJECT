package com.semicolon.coorthub.report;

import com.semicolon.coorthub.model.CohortReport;
import com.semicolon.coorthub.model.StudyGroup;
import com.semicolon.coorthub.service.LearnerService;
import com.semicolon.coorthub.service.MentorSupportService;
import com.semicolon.coorthub.service.StudyGroupService;

import java.util.LinkedHashMap;
import java.util.Map;

public class CohortReportBuilder {

    private final LearnerService learnerService;
    private final StudyGroupService studyGroupService;
    private final MentorSupportService mentorSupportService;

    public CohortReportBuilder(LearnerService learnerService,
                               StudyGroupService studyGroupService,
                               MentorSupportService mentorSupportService) {
        this.learnerService = learnerService;
        this.studyGroupService = studyGroupService;
        this.mentorSupportService = mentorSupportService;
    }

    public CohortReport buildReport() {
        Map<String, Integer> membershipsPerGroup = new LinkedHashMap<>();
        for (StudyGroup group : studyGroupService.getAllGroups()) {
            membershipsPerGroup.put(group.getGroupName(), group.getMemberCount());
        }
        return new CohortReport(
                learnerService.getTotalLearners(),
                studyGroupService.getGroupCount(),
                membershipsPerGroup,
                mentorSupportService.getWaitingCount());
    }
}