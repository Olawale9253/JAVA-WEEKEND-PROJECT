package com.semicolon.coorthub.comparator.report;

import com.semicolon.coorthub.model.CohortReport;
import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.report.CohortReportBuilder;
import com.semicolon.coorthub.service.LearnerService;
import com.semicolon.coorthub.service.MentorSupportService;
import com.semicolon.coorthub.service.StudyGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CohortReportBuilderTest {

    private LearnerService learnerService;
    private StudyGroupService studyGroupService;
    private MentorSupportService mentorSupportService;
    private CohortReportBuilder reportBuilder;

    @BeforeEach
    void setUp() {
        learnerService = new LearnerService();
        studyGroupService = new StudyGroupService();
        mentorSupportService = new MentorSupportService();
        reportBuilder = new CohortReportBuilder(
                learnerService, studyGroupService, mentorSupportService);
    }

    @Test
    void buildReport_emptyCohort_returnsZeros() {
        CohortReport report = reportBuilder.buildReport();
        assertEquals(0, report.getTotalLearners());
        assertEquals(0, report.getTotalGroups());
        assertEquals(0, report.getLearnersWaitingForSupport());
    }

    @Test
    void buildReport_populatedCohort_returnsCorrectCounts() {
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        learnerService.register(alice);
        studyGroupService.createGroup("Java");
        studyGroupService.addLearnerToGroup(alice, "Java");
        mentorSupportService.requestSupport(alice);

        CohortReport report = reportBuilder.buildReport();

        assertEquals(1, report.getTotalLearners());
        assertEquals(1, report.getTotalGroups());
        assertEquals(1, report.getMembershipsPerGroup().get("Java"));
        assertEquals(1, report.getLearnersWaitingForSupport());
    }

    @Test
    void buildReport_membershipsPerGroup_listsEveryGroup() {
        studyGroupService.createGroup("Java");
        studyGroupService.createGroup("Python");
        CohortReport report = reportBuilder.buildReport();
        assertEquals(2, report.getMembershipsPerGroup().size());
    }
}