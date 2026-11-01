package com.semicolon.coorthub.service;

import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.model.StudyGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CohortCoordinator {

    private final LearnerService learnerService;
    private final StudyGroupService studyGroupService;
    private final MentorSupportService mentorSupportService;

    public CohortCoordinator(LearnerService learnerService,
                             StudyGroupService studyGroupService,
                             MentorSupportService mentorSupportService) {
        this.learnerService = learnerService;
        this.studyGroupService = studyGroupService;
        this.mentorSupportService = mentorSupportService;
    }


    public void removeLearner(String learnerId) {
        Learner learner = learnerService.findById(learnerId);
        learnerService.removeLearner(learnerId);
        studyGroupService.removeLearnerFromAllGroups(learner);
        mentorSupportService.removeLearnerFromQueue(learner);
    }

    public void addLearnerToGroup(String learnerId, String groupName) {
        Learner learner = learnerService.findById(learnerId);
        studyGroupService.addLearnerToGroup(learner, groupName);
    }

    public List<String> getGroupNamesForLearner(String learnerId) {
        Learner learner = learnerService.findById(learnerId);
        return studyGroupService.getGroupNamesForLearner(learner);
    }

    public void requestSupport(String learnerId) {
        Learner learner = learnerService.findById(learnerId);
        mentorSupportService.requestSupport(learner);
    }

    public List<Learner> getLearnersInBothGroups(String firstGroupName,
                                                 String secondGroupName) {
        StudyGroup firstGroup = studyGroupService.findGroup(firstGroupName);
        StudyGroup secondGroup = studyGroupService.findGroup(secondGroupName);
        List<Learner> intersection = new ArrayList<>();
        for (Learner learner : firstGroup.getMembers()) {
            if (secondGroup.hasMember(learner)) {
                intersection.add(learner);
            }
        }
        return intersection;
    }


    public List<Learner> getLearnersInEitherButNotBothGroups(String firstGroupName,
                                                             String secondGroupName) {
        StudyGroup firstGroup = studyGroupService.findGroup(firstGroupName);
        StudyGroup secondGroup = studyGroupService.findGroup(secondGroupName);
        Set<Learner> eitherOr = new HashSet<>();
        for (Learner learner : firstGroup.getMembers()) {
            if (!secondGroup.hasMember(learner)) {
                eitherOr.add(learner);
            }
        }
        for (Learner learner : secondGroup.getMembers()) {
            if (!firstGroup.hasMember(learner)) {
                eitherOr.add(learner);
            }
        }
        return new ArrayList<>(eitherOr);
    }
}