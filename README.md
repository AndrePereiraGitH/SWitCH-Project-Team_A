
# MITE LOVERS - TEAM A

## Issue Template

### Main issue: US[NUMBER] - [US Text]

- **sub-issue:** US[NUMBER] - Analysis & Design

  **Description:**
     - Acceptance Criteria:
          - SSD covers the main success scenario.
          - SD is consistent with the SSD and reflects the chosen design.
          - CD includes all classes involved and their relationships.
          - Diagrams are reviewed and approved by the team (at least one team member).


- (if needed) **sub-issue:** US[NUMBER] - Implement domain class and unit tests.

  **Description:**
     - Acceptance Criteria:
          - Class follows domain rules and OOD best practices.
          - All edge cases are covered by unit tests.
          - Test coverage ≥ 95%.
          - Code is documented with JavaDoc.
          - Code reviewed and approved by the team (at least one team member).



- **sub-issue:** US[NUMBER] - Implement repository class and unit tests.

  **Description:**
     - Acceptance Criteria:
          - Repository correctly retrieves domain objects.
          - All edge cases are covered by unit tests.
          - Test coverage ≥ 95%.
          - Code is documented with JavaDoc.
          - Code reviewed and approved by the team (at least one team member).



- **sub-issue:** US[NUMBER] - Implement controller class and unit tests.

  **Description:**
     - Acceptance Criteria:
          - Controller correctly orchestrates domain and repository interactions.
          - All edge cases are covered by unit tests.
          - Test coverage ≥ 95%.
          - Code is documented with JavaDoc.
          - Code reviewed and approved by the team (at least one team member).


__All acceptance criteria must be reviewed and adjusted to fit each US. 
Additional criteria may be added as needed to ensure completeness.__

<br>

## Refinement Issue if needed


### Domain class Refinement

**Main issue (if needed):** Refinement - [Domain Class Name] - [Brief Description of Change]

**Description:** [What needs to be clarified, revised or reworked — e.g. domain class restructure, new edge cases discovered, design corrections.]

**Acceptance Criteria:**
- [Domain Class Name] correctly reflects the updated domain rules and OOD best practices.
- Any impacted classes or tests are updated accordingly.
- All edge cases are covered by unit tests.
- Test coverage ≥ 95%.
- Code is documented with JavaDoc.
- Changes are reviewed and approved by the team (at least one team member).

<br>

### US Refinement

**Main issue (if needed):** US[NUMBER] - Refinement - [Topic]

**Description:** [What needs to be clarified, revised or reworked — e.g. domain changes, design corrections, new edge cases discovered mid-sprint.]

**Acceptance Criteria:**
- All open questions related to [Topic] are documented and resolved.
- Any impacted diagrams (SSD/SD/CD) are updated to reflect the changes.
- Any impacted classes or tests are updated and still meet ≥ 95% coverage.
- Changes are reviewed and approved by the team (at least one team member).


<br>

### Commit template

Design implementation/creation:

````
Working on #Issue, created SSD, SD & CD related to US_NUMBER

Working on #Issue, implemented tests & class for X domain class 

Working on #Issue, implemented tests & class for X repository class 

Working on #Issue, implemented tests & class for X controller class related to US_NUMBER

````

Additional work (new methods, corrections, updates):

````
Working on #Issue, implemented additional tests & method(s) in X domain class

Working on #Issue, implemented additional tests & method(s) in X repository class

Working on #Issue, corrected mistakes in X class (write details here)

Working on #Issue, updated X class to reflect new design (write details here)

Working on #Issue, updated (SSD/SD/CD) to reflect new design
````

- When the item meets the definition of done, the commit message should end with (@close #ISSUENUMBER)


- When working with someone else, you should reference that person in the commit message (@Github_Username)


<br>

## Definition of Done

A user story or task in the MiteLovers project is considered Done when:

✔️  The functionality is fully implemented according to the requirements and acceptance criteria.

✔️  All edge cases are handled and no known functional defects remain.

✔️  The solution follows Object-Oriented Design (OOD) principles and best practices.

✔️  UML diagrams are updated whenever structural changes occur.

✔️  Development follows Test-Driven Development (TDD).

✔️  All tests pass and overall test coverage is at least 95%.

✔️  All public classes and methods are documented using JavaDoc.

✔️  Work is tracked through GitHub Issues and every commit references at least one issue.

✔️  The code compiles without errors and complies with all non-functional requirements.

<br>

**Only when all these conditions are satisfied can a user story be considered complete.**

