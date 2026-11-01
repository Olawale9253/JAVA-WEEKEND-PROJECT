package com.semicolon.coorthub.service;

import com.semicolon.coorthub.comparator.LearnerNameThenIdComparator;
import com.semicolon.coorthub.exception.DuplicateEmailException;
import com.semicolon.coorthub.exception.DuplicateLearnerIdException;
import com.semicolon.coorthub.exception.LearnerNotFoundException;
import com.semicolon.coorthub.model.Learner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LearnerService {

    private final Map<String, Learner> learnersById;
    private final Set<String> registeredEmailsLowerCase;
    private final LearnerNameThenIdComparator comparator;

    public LearnerService() {
        this.learnersById = new HashMap<>();
        this.registeredEmailsLowerCase = new HashSet<>();
        this.comparator = new LearnerNameThenIdComparator();
    }

    public void register(Learner learner) {
        if (learnersById.containsKey(learner.getLearnerId())) {
            throw new DuplicateLearnerIdException(
                    "Learner ID '" + learner.getLearnerId() + "' is already registered.");
        }
        String lowerEmail = learner.getEmailAddress().toLowerCase();
        if (registeredEmailsLowerCase.contains(lowerEmail)) {
            throw new DuplicateEmailException(
                    "Email '" + learner.getEmailAddress() + "' is already registered.");
        }
        learnersById.put(learner.getLearnerId(), learner);
        registeredEmailsLowerCase.add(lowerEmail);
    }

    public Learner findById(String learnerId) {
        Learner learner = learnersById.get(learnerId);
        if (learner == null) {
            throw new LearnerNotFoundException(
                    "No learner with ID '" + learnerId + "'.");
        }
        return learner;
    }

    public boolean exists(String learnerId) {
        return learnersById.containsKey(learnerId);
    }

    public void updateLearner(String learnerId, String newName, String newEmail) {
        Learner learner = findById(learnerId);
        String oldEmailLower = learner.getEmailAddress().toLowerCase();
        String newEmailLower = newEmail.toLowerCase();
        if (!newEmailLower.equals(oldEmailLower)
                && registeredEmailsLowerCase.contains(newEmailLower)) {
            throw new DuplicateEmailException(
                    "Email '" + newEmail + "' is already registered.");
        }
        learner.updateProfile(newName, newEmail);
        registeredEmailsLowerCase.remove(oldEmailLower);
        registeredEmailsLowerCase.add(newEmailLower);
    }

    public void removeLearner(String learnerId) {
        Learner learner = findById(learnerId);
        learnersById.remove(learnerId);
        registeredEmailsLowerCase.remove(learner.getEmailAddress().toLowerCase());
    }

    public int getTotalLearners() {
        return learnersById.size();
    }

    public List<Learner> getLearnersSortedBy() {
        List<Learner> sortedLearners = new ArrayList<>(learnersById.values());
        sortedLearners.sort(comparator);
        return sortedLearners;
    }
}