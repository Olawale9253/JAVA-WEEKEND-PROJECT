package com.semicolon.coorthub.model;

import java.util.Objects;

public class Learner {

    private final String learnerId;
    private String fullName;
    private String emailAddress;

    public Learner(String learnerId, String fullName, String emailAddress) {
        if (learnerId == null || learnerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Learner ID must not be blank.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Learner name must not be blank.");
        }
        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Learner email must not be blank.");
        }
        this.learnerId = learnerId.trim();
        this.fullName = fullName.trim();
        this.emailAddress = emailAddress.trim();
    }

    public String getLearnerId() {
        return learnerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void updateProfile(String newFullName, String newEmailAddress) {
        if (newFullName == null || newFullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Learner name must not be blank.");
        }
        if (newEmailAddress == null || newEmailAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Learner email must not be blank.");
        }
        this.fullName = newFullName.trim();
        this.emailAddress = newEmailAddress.trim();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Learner)) {
            return false;
        }
        Learner otherLearner = (Learner) other;
        return learnerId.equals(otherLearner.learnerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(learnerId);
    }

    @Override
    public String toString() {
        return learnerId + " - " + fullName + " (" + emailAddress + ")";
    }
}