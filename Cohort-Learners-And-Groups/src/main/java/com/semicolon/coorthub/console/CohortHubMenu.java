package com.semicolon.coorthub.console;

import com.semicolon.coorthub.exception.CohortException;
import com.semicolon.coorthub.model.CohortReport;
import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.model.StudyGroup;
import com.semicolon.coorthub.report.CohortReportBuilder;
import com.semicolon.coorthub.service.CohortCoordinator;
import com.semicolon.coorthub.service.LearnerService;
import com.semicolon.coorthub.service.MentorSupportService;
import com.semicolon.coorthub.service.StudyGroupService;

import java.util.List;
import java.util.Map;

public class CohortHubMenu {

    private static final int MENU_OPTION_REGISTER = 1;
    private static final int MENU_OPTION_LIST_LEARNERS = 2;
    private static final int MENU_OPTION_UPDATE_LEARNER = 3;
    private static final int MENU_OPTION_REMOVE_LEARNER = 4;
    private static final int MENU_OPTION_CREATE_GROUP = 5;
    private static final int MENU_OPTION_ADD_TO_GROUP = 6;
    private static final int MENU_OPTION_SHOW_LEARNER_GROUPS = 7;
    private static final int MENU_OPTION_REQUEST_SUPPORT = 8;
    private static final int MENU_OPTION_ATTEND_NEXT = 9;
    private static final int MENU_OPTION_REPORT = 10;
    private static final int MENU_OPTION_EXIT = 11;

    private final LearnerService learnerService;
    private final StudyGroupService studyGroupService;
    private final MentorSupportService mentorSupportService;
    private final CohortCoordinator cohortCoordinator;
    private final CohortReportBuilder reportBuilder;
    private final InputReader inputReader;

    public CohortHubMenu(LearnerService learnerService,
                         StudyGroupService studyGroupService,
                         MentorSupportService mentorSupportService,
                         CohortCoordinator cohortCoordinator,
                         CohortReportBuilder reportBuilder,
                         InputReader inputReader) {
        this.learnerService = learnerService;
        this.studyGroupService = studyGroupService;
        this.mentorSupportService = mentorSupportService;
        this.cohortCoordinator = cohortCoordinator;
        this.reportBuilder = reportBuilder;
        this.inputReader = inputReader;
    }

    public void start() {
        System.out.println("=== Welcome to CohortHub ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = inputReader.readMenuChoice(
                    "Enter your choice: ", 1, MENU_OPTION_EXIT);
            try {
                running = handleChoice(choice);
            } catch (CohortException cohortException) {
                System.out.println("Operation failed: " + cohortException.getMessage());
            } catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Invalid input: " + illegalArgumentException.getMessage());
            }
        }
        System.out.println("Thank you for using CohortHub. Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- CohortHub Menu ---");
        System.out.println(MENU_OPTION_REGISTER + ". Register a learner");
        System.out.println(MENU_OPTION_LIST_LEARNERS + ". List learners (sorted)");
        System.out.println(MENU_OPTION_UPDATE_LEARNER + ". Update a learner");
        System.out.println(MENU_OPTION_REMOVE_LEARNER + ". Remove a learner");
        System.out.println(MENU_OPTION_CREATE_GROUP + ". Create a study group");
        System.out.println(MENU_OPTION_ADD_TO_GROUP + ". Add learner to group");
        System.out.println(MENU_OPTION_SHOW_LEARNER_GROUPS + ". Show learner's groups");
        System.out.println(MENU_OPTION_REQUEST_SUPPORT + ". Request mentor support");
        System.out.println(MENU_OPTION_ATTEND_NEXT + ". Attend next support request");
        System.out.println(MENU_OPTION_REPORT + ". Show cohort report");
        System.out.println(MENU_OPTION_EXIT + ". Exit");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case MENU_OPTION_REGISTER: registerLearner(); return true;
            case MENU_OPTION_LIST_LEARNERS: listLearners(); return true;
            case MENU_OPTION_UPDATE_LEARNER: updateLearner(); return true;
            case MENU_OPTION_REMOVE_LEARNER: removeLearner(); return true;
            case MENU_OPTION_CREATE_GROUP: createGroup(); return true;
            case MENU_OPTION_ADD_TO_GROUP: addLearnerToGroup(); return true;
            case MENU_OPTION_SHOW_LEARNER_GROUPS: showLearnerGroups(); return true;
            case MENU_OPTION_REQUEST_SUPPORT: requestSupport(); return true;
            case MENU_OPTION_ATTEND_NEXT: attendNext(); return true;
            case MENU_OPTION_REPORT: showReport(); return true;
            case MENU_OPTION_EXIT: return false;
            default: return true;
        }
    }

    private void registerLearner() {
        String learnerId = inputReader.readNonEmptyLine("Learner ID: ");
        String fullName = inputReader.readNonEmptyLine("Full name: ");
        String emailAddress = inputReader.readNonEmptyLine("Email: ");
        learnerService.register(new Learner(learnerId, fullName, emailAddress));
        System.out.println("Learner '" + learnerId + "' registered.");
    }

    private void listLearners() {
        List<Learner> learners = learnerService.getLearnersSortedBy();
        if (learners.isEmpty()) {
            System.out.println("No learners registered.");
            return;
        }
        for (Learner learner : learners) {
            System.out.println(learner);
        }
    }

    private void updateLearner() {
        String learnerId = inputReader.readNonEmptyLine("Learner ID to update: ");
        String newName = inputReader.readNonEmptyLine("New name: ");
        String newEmail = inputReader.readNonEmptyLine("New email: ");
        learnerService.updateLearner(learnerId, newName, newEmail);
        System.out.println("Learner '" + learnerId + "' updated.");
    }

    private void removeLearner() {
        String learnerId = inputReader.readNonEmptyLine("Learner ID to remove: ");
        cohortCoordinator.removeLearner(learnerId);
        System.out.println("Learner '" + learnerId + "' removed from the cohort.");
    }

    private void createGroup() {
        String groupName = inputReader.readNonEmptyLine("Group name: ");
        studyGroupService.createGroup(groupName);
        System.out.println("Group '" + groupName + "' created.");
    }

    private void addLearnerToGroup() {
        String learnerId = inputReader.readNonEmptyLine("Learner ID: ");
        String groupName = inputReader.readNonEmptyLine("Group name: ");
        cohortCoordinator.addLearnerToGroup(learnerId, groupName);
        System.out.println("Learner '" + learnerId + "' added to '" + groupName + "'.");
    }

    private void showLearnerGroups() {
        String learnerId = inputReader.readNonEmptyLine("Learner ID: ");
        List<String> groups = cohortCoordinator.getGroupNamesForLearner(learnerId);
        if (groups.isEmpty()) {
            System.out.println("Learner '" + learnerId + "' is not in any group.");
        } else {
            System.out.println("Learner '" + learnerId + "' is in: "
                    + String.join(", ", groups));
        }
    }

    private void requestSupport() {
        String learnerId = inputReader.readNonEmptyLine("Learner ID: ");
        cohortCoordinator.requestSupport(learnerId);
        System.out.println("Learner '" + learnerId + "' queued for mentor support.");
    }

    private void attendNext() {
        Learner nextLearner = mentorSupportService.attendNextLearner();
        if (nextLearner == null) {
            System.out.println("No learners waiting for support.");
        } else {
            System.out.println("Now attending to: " + nextLearner);
        }
    }

    private void showReport() {
        CohortReport report = reportBuilder.buildReport();
        System.out.println("Total learners:            " + report.getTotalLearners());
        System.out.println("Total groups:              " + report.getTotalGroups());
        System.out.println("Waiting for support:       " + report.getLearnersWaitingForSupport());
        if (report.getMembershipsPerGroup().isEmpty()) {
            System.out.println("(no groups created yet)");
        } else {
            System.out.println("Memberships per group:");
            for (Map.Entry<String, Integer> entry
                    : report.getMembershipsPerGroup().entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}