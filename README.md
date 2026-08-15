Academic Degree Planner & Scheduler

A Java application that models university course prerequisites, validates course eligibility, and determines prerequisite-aware course ordering using graph algorithms and object-oriented design.

Overview

The Academic Degree Planner & Scheduler helps model the logic involved in planning a university degree.

University courses often have complex prerequisite relationships. A course may require multiple earlier courses, which themselves may depend on additional prerequisites.

This project represents those relationships as a directed graph and applies graph algorithms to:

validate prerequisite structures
detect circular dependencies
determine valid course ordering
evaluate student course eligibility
validate semester course selections

The application is organized into separate domain, graph, and service layers to keep the data model, algorithms, and business logic separated.

Example

Consider a student who wants to take:

COEN 346 — Operating Systems

COEN 346 requires both:

COEN 311 — Computer Organization
COEN 352 — Data Structures

Those courses also have their own prerequisites:

                         COEN 346
                        /        \
                       /          \
                 COEN 311       COEN 352
                    |           /       \
                    |          /         \
                COEN 243   COEN 231    COEN 244
                    |          |           |
                    |          |           |
                 MATH 204   MATH 204    COEN 243
                                           |
                                        MATH 204

This creates a multi-level prerequisite dependency graph.

For example, before taking COEN 346, a student may need to satisfy a sequence such as:

MATH 204
   ↓
COEN 243
   ↓
COEN 244
   ↓
COEN 352
   ↓
COEN 346

while another prerequisite path is:

MATH 204
   ↓
COEN 243
   ↓
COEN 311
   ↓
COEN 346

The planner uses these relationships to determine whether courses can be taken and in what order prerequisite requirements can be satisfied.

Features
Model university courses and prerequisite relationships
Maintain a centralized course catalog
Represent prerequisites using a directed graph
Determine whether a student is eligible to take a course
Check whether required prerequisites have been completed
Prevent already-completed courses from being selected again
Validate whether courses belong to a student's degree requirements
Detect circular prerequisite dependencies using Depth-First Search
Determine prerequisite-aware course ordering using Topological Sorting
Validate semester plans against maximum credit limits
Model student records, degree programs, courses, and semester plans
Separate domain models, graph algorithms, and business logic through a layered architecture
Architecture

The project separates responsibilities into three main layers:

┌──────────────────────────────┐
│        Service Layer         │
│                              │
│ CourseCatalogService         │
│ EligibilityService           │
│ DegreePlannerService         │
└──────────────┬───────────────┘
               │
               ↓
┌──────────────────────────────┐
│         Graph Layer          │
│                              │
│ PrerequisiteGraph            │
│ GraphValidator               │
│ TopologicalPlanner           │
└──────────────┬───────────────┘
               │
               ↓
┌──────────────────────────────┐
│         Domain Layer         │
│                              │
│ Course                       │
│ DegreeProgram                │
│ StudentRecord                │
│ SemesterPlan                 │
└──────────────────────────────┘
Domain Layer

Contains the application's core entities:

Course
DegreeProgram
StudentRecord
SemesterPlan
Graph Layer

Handles prerequisite relationships and graph algorithms:

prerequisite graph construction
cycle detection
graph traversal
topological ordering
Service Layer

Contains the application's business logic:

course catalog management
course eligibility checking
semester validation
degree-planning operations
Graph Representation

Course prerequisites are represented using an adjacency list.

Conceptually:

Map<String, List<String>>

Each course is associated with the courses that must be completed before it.

For example:

COEN 346
├── COEN 311
└── COEN 352

COEN 352
├── COEN 231
└── COEN 244

COEN 244
└── COEN 243

COEN 311
└── COEN 243

COEN 243
└── MATH 204

Using an adjacency-list representation makes it possible to efficiently traverse prerequisite relationships and apply graph algorithms to the course catalog.

Algorithms
Depth-First Search — Cycle Detection

A valid prerequisite graph should not contain circular dependencies.

For example, this would be impossible:

COMP A
   ↓
COMP B
   ↓
COMP C
   ↓
COMP A

A student could never satisfy these prerequisites because each course ultimately depends on itself.

The application uses Depth-First Search (DFS) with visited nodes and a recursion stack to detect these cycles.

If a course is encountered again while it is already part of the current DFS traversal path, a cycle exists.

Topological Sorting

The application uses topological sorting to determine a valid ordering of courses based on prerequisite relationships.

The implementation tracks the number of unresolved prerequisites associated with courses and processes eligible nodes using a queue.

For a simplified prerequisite chain:

MATH 204
   ↓
COEN 243
   ↓
COEN 244
   ↓
COEN 352
   ↓
COEN 346

a valid topological ordering ensures that prerequisite courses appear before courses that depend on them.

This allows the planner to reason about prerequisite-aware course sequencing.

Course Eligibility

The application evaluates whether a student is currently eligible to take a course.

Eligibility checks include whether:

the course exists in the course catalog
the student has already completed the course
the course belongs to the student's degree requirements
all required prerequisites have been completed

For example:

Student completed:
✓ MATH 204
✓ COEN 243
✓ COEN 231
✓ COEN 244

Requested course:
COEN 352

Prerequisites:
✓ COEN 231
✓ COEN 244

Result:
ELIGIBLE

But:

Student completed:
✓ MATH 204
✓ COEN 243

Requested course:
COEN 352

Prerequisites:
✗ COEN 231
✗ COEN 244

Result:
NOT ELIGIBLE
Semester Validation

Semester plans are validated before they are accepted.

The planner checks that:

every selected course exists
courses have not already been completed
selected courses satisfy degree requirements
prerequisite requirements are satisfied
the semester does not exceed its maximum credit limit

This separates individual course eligibility from validation of an entire semester plan.

Design Pattern
Singleton — Course Catalog

The project uses a Singleton-style course catalog service to provide centralized access to course information throughout the application.

CourseCatalogService.getInstance()

The planning and eligibility services access the shared catalog through this service instead of independently maintaining separate course collections.

This provides a single access point for course-catalog data across the application.

Project Structure
degreePlanner/
│
└── src/
    └── degreePlanner/
        │
        ├── domain/
        │   ├── Course.java
        │   ├── DegreeProgram.java
        │   ├── SemesterPlan.java
        │   └── StudentRecord.java
        │
        ├── graph/
        │   ├── GraphValidator.java
        │   ├── PrerequisiteGraph.java
        │   └── TopologicalPlanner.java
        │
        ├── service/
        │   ├── CourseCatalogService.java
        │   ├── DegreePlannerService.java
        │   └── EligibilityService.java
        │
        └── Main.java
Technologies & Concepts
Language
Java
Software Design
Object-Oriented Programming
Layered Architecture
Singleton Design Pattern
Separation of Concerns
Data Structures
Directed Graphs
HashMaps
Lists
Sets
Queues
Adjacency Lists
Algorithms
Depth-First Search
Cycle Detection
Topological Sorting
Graph Traversal
Running the Project
IntelliJ IDEA
Clone the repository:
git clone https://github.com/tarekbarake/degreePlanner.git
Open the project in IntelliJ IDEA.
Configure a Java JDK if one is not already configured.
Open:
src/degreePlanner/Main.java
Run the Main class.
Command Line

From the project root on macOS or Linux:

mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out degreePlanner.Main
Future Improvements

Possible future extensions include:

automatic multi-semester graduation-plan generation
persistent storage for course and student data
a graphical or web-based user interface
importing real university course catalogs
support for elective groups and alternative prerequisite paths
course scheduling based on semester availability
automated unit and integration testing
Author

Tarek Barake

Computer Science
Concordia University
