# CohortHub Learners and Study Groups

## Purpose
Console cohort manager demonstrating the Java Collection Framework:
Map for constant-time learner lookup, Set for unique group membership,
Queue for FIFO mentor support, List for sorted reports, and Comparator
for consistent sorting.

## How to run
cd 04-coorthub-learners-and-groups
mvn test
mvn exec:java

## Project structure
- model/Learner       : immutable learnerId, mutable name/email, equals/hashCode on ID
- model/StudyGroup    : LinkedHashSet of members (automatic dedup)
- model/CohortReport  : immutable value object for the report
- service/LearnerService       : Map<String, Learner> + Set<String> for email uniqueness
- service/StudyGroupService    : Map<String, StudyGroup> + cascade-remove helper
- service/MentorSupportService : ArrayDeque<Learner> used as a FIFO Queue
- service/CohortCoordinator    : the ONLY class that touches all three services
- report/CohortReportBuilder   : read-only aggregation for the report
- comparator/LearnerNameThenIdComparator : name (case-insensitive) then ID
- console/InputReader          : Scanner wrapper, handles empty and non-numeric input
- console/CohortHubMenu        : menu loop, only class that touches System.out

## Why each collection
- LearnerService uses `Map<String, Learner>` for O(1) lookup by ID and to
  enforce one-learner-per-ID. A `Set<String>` of lowercased emails is kept
  separately so the email-uniqueness check is O(1) and survives email case
  changes during an update.
- StudyGroup uses `LinkedHashSet<Learner>` because Set rejects duplicate
  additions automatically (the "no duplicate membership" rule becomes free)
  and LinkedHashSet preserves insertion order for display. `Learner`'s
  equals/hashCode are based on the immutable ID, so two Learner objects
  representing the same person are treated as the same member.
- MentorSupportService uses `ArrayDeque<Learner>` as a `Queue<Learner>`:
  `add()` enqueues at the tail, `poll()` dequeues from the head, giving
  strict first-in-first-out order per the acceptance check.
- `getLearnersSortedBy()` builds a new `ArrayList` from `Map.values()` and
  sorts that list. The `Map` is never touched, so sorting cannot corrupt
  ID-based lookup.

## The cascading-remove design
`LearnerService.removeLearner` only removes the learner from the Map and
the email Set. Study groups and the support queue are handled by
`CohortCoordinator.removeLearner`, which:
1. finds the learner (so we have the object to pass downstream),
2. removes it from the LearnerService map and email Set,
3. calls `StudyGroupService.removeLearnerFromAllGroups(learner)`,
4. calls `MentorSupportService.removeLearnerFromQueue(learner)`.

All three services remain ignorant of each other; coordination lives in
one small class. `CohortCoordinatorTest.removeLearner_learnerInGroupsAndQueue_leavesNoStaleReferences`
is the test that proves this.

## Known limitations
- Everything is in-memory; there is no persistence (allowed by the challenge).
- The queue accepts the same learner more than once (a learner can ask for
  help on two different topics); `removeIf` clears every occurrence when the
  learner is removed.
- Learner name and email are mutable in place via `updateProfile`. The ID is
  final and immutable, so `Set` and `Map` behaviour is stable even after an
  update.
- The comparator sorts names case-insensitively; "alice" and "Alice" tie on
  name and fall through to ID order.

## Stretch goals completed
- `getLearnersInBothGroups` returns the intersection of two groups.
- `getLearnersInEitherButNotBothGroups` returns the symmetric difference.