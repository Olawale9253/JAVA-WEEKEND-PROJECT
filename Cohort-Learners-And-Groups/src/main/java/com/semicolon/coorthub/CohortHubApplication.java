package com.semicolon.coorthub;

import com.semicolon.coorthub.console.CohortHubMenu;
import com.semicolon.coorthub.console.InputReader;
import com.semicolon.coorthub.report.CohortReportBuilder;
import com.semicolon.coorthub.service.CohortCoordinator;
import com.semicolon.coorthub.service.LearnerService;
import com.semicolon.coorthub.service.MentorSupportService;
import com.semicolon.coorthub.service.StudyGroupService;

import java.util.Scanner;

public class CohortHubApplication {

    public static void main(String[] args) {
        LearnerService learnerService = new LearnerService();
        StudyGroupService studyGroupService = new StudyGroupService();
        MentorSupportService mentorSupportService = new MentorSupportService();

        CohortCoordinator cohortCoordinator = new CohortCoordinator(
                learnerService, studyGroupService, mentorSupportService);
        CohortReportBuilder reportBuilder = new CohortReportBuilder(
                learnerService, studyGroupService, mentorSupportService);

        InputReader inputReader = new InputReader(new Scanner(System.in));
        CohortHubMenu menu = new CohortHubMenu(
                learnerService, studyGroupService, mentorSupportService,
                cohortCoordinator, reportBuilder, inputReader);

        menu.start();
    }
}