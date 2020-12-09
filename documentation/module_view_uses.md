# Quizzes-Tutor - Module View: Uses View

## Primary Presentation

<img src="pictures/Uses View.png" width="500" >

Fig 1. Uses View graphic representation. All arrows represent *uses* relations, which are specialized cases of *depends-on* relations between modules. Pairs of red arrows highlight cyclic dependencies.

## Element Catalog

### <span style="color:#0080ff">quizzes-tutor</span>
 This module equates to the whole of quizzes-tutor. The functionalities it provides include:
- Users (students and teachers) may submit multiple choice questions based on topics and create quizzes based on the existing questions in the system;
- Multiple courses are supported, a course can have distinct executions over the years, with reuse of questions;
- Teachers can approve or vet question submissions;
- Teachers can create topics;
- Students may answer quizzes;
- Discussions between users can be created and carried for each quizz question;
- Tournaments between students can be organized. These tournaments may be password restricted;
- Student are allowed to view their current stats. (Gamification)

### <span style="color:#0080ff">answer</span>
This module contains the business logic related to the various answers of a question. *There's a cyclic uses-dependency between this module and the **quesiton** module. Please refer to the **Rationale** section below for reasoning on possible future considerations/solutions in the optics of Domain Driven Design.*

### <span style="color:#0080ff">auth</span>
This module provides the means to authenticate a user into the system using token-based authentication. Authentication works for both IST students and external students. This module has a *uses* relation to a module from an external bounded context, the FenixEdu REST API, whose ubiquitous language is a publicly published language to which quizzes-tutor assumes a comformist posture. For more information consult [FenixEdu REST API's SLA](https://fenix.tecnico.ulisboa.pt/personal/external-applications/api-service-agreement).

### <span style="color:#0080ff">course</span>
This module contains the business logic related to a course. A distinction is done between a Cource and a CourceExecution, since a course can be executed multiple times over the years, and with different students, but reuse of questions.

### <span style="color:#0080ff">discussion</span>
This module contains the business logic related to the discussions functionality and management of replies.

### <span style="color:#0080ff">question</span>
This module contains the business logic related to questions and topics. A Topic has a set of Questions and a Question has a Topic. *There's a cyclic uses-dependency between this module and the **answers** module. Furthermore, this module has many incoming uses-dependencies as it is a part of the Core Domain of quizzes-tutor. Please refer to the **Rationale** section below for reasoning on possible future considerations/solutions in the optics of Domain Driven Design.*

### <span style="color:#0080ff">questionsubmission</span>
This module contains the business logic related to question submissions and posterior review/vetting of such submissions.

### <span style="color:#0080ff">quiz</span>
This module contains the business logic related to quizzes, including the ordering of the questions, among others.

### <span style="color:#0080ff">statement</span>
This module contains services to handle the data collected in the various stages of a student answering the quiz, including the start of the quiz, when there's a click in a multiple choice answer, or the quiz is completed.
*This module is deprecated. In the future it is expected to disappear and all data to be handled directly by other elements of the domain.*

### <span style="color:#0080ff">statistics</span>
This module doesn't have persistent content and is simply used to present data. It is however relevant for the business logic as it materializes the gamification aspect of the system, in the form of a stats board.

### <span style="color:#0080ff">tournament</span>
This module contains the business logic related with the tournaments functionality.

### <span style="color:#0080ff">user</span>
This module contains the business logic related with a user. In particular, it contains the god class User.
*In the future, to migrate quizzes-tutor into a microservices architecture, this module may be turned into an event publisher, publishing events to all the other modules. In turn the other modules would have a specific partition of the original user module, relevant in that context, that would adapt to the changed brought by the events.*

### <span style="color:#0080ff">permission</span>
This module handles permissions of access to all the logical business entities - Relevant questions like: Can a user view this course? Can a user view this tournament? ...

## Context Diagram

`NOTE: To document the interfaces of each module, the best place to look is at the Service classes in each module.`

## Rationale

Rationale on Model Integrity improvements and considerations:

*Since the **answer** uses the **question** module, which in turn also uses the **answer** module, there's a prominent cyclic dependency. To eliminate this cyclic dependency and improve the continuous integration of these and the dependent modules, a possible future solution is to merge **answer** and **question** in the same module and make them a part of the same Bounded Context. Furthermore, given the fact this conjoined Bounded Context has a lot of incoming uses-dependencies, this should be considered a Shared Kernel, among all other Bounded Contexts that use it. This guarantees uncoordinated teams have a common point of knowledge. It's important to keep in mind that the combination of these two modules is a fundamental piece of quizzes-tutor and that any change will impact the remaining peripheral Bounded Contexts, therefore this shouldn't be changed without consultation with all participating teams.*

## Related Views

## References
For a detailed style guide, refer to Chapter 2.2 of Documenting Software Architectures: Views and Beyond (2nd Edition): Paul Clements, Felix Bachmann, Len Bass, David Garlan, James Ivers, Reed Little, Paulo Merson, Robert Nord, Judith Stafford 2010 Addison-Wesley.


-----------------------------------------------------
TOURNAMENT

Correct implementation depends on:
- quiz
- question
- user
- course

Other dependencies:
- exceptions
- statement
- config
- answer
-----------------------------------------------------
DISCUSSION

Correct implementation depends on:
- question
- course
- user
- answer

Other dependencies:
- exceptions
- impexp
- config
- quiz
-----------------------------------------------------
ANSWER

Correct implementation depends on:
- question

Other dependencies:
- impexp
- statement
- config
- discussion
- quiz
- course
- user
- exceptions
-----------------------------------------------------
QUIZ

Correct implementation depends on:
- question
- course
- user

Other dependencies:
- impexp
- tournament
- statement
- config
- answer
- exceptions
-----------------------------------------------------
QUESTIONSUBMISSION

Correct implementation depends on:
- course
- user
- question

Other dependencies:
- tournament
- config
- exceptions
-----------------------------------------------------
QUESTION

Correct implementation depends on:
- answer
- course

Other dependencies:
- impexp
- tournament
- statement
- config
- discussion
- quiz
- exceptions
-----------------------------------------------------
COURSE

Correct implementation depends on:
- auth

Other dependencies:
- impexp
- statement
- config
- discussion
- answer
- quiz
- questionsubmission
- question
- user
- exceptions
-----------------------------------------------------
USER

Correct implementation depends on:
- auth

Other dependencies:
- impexp
- tournament
- config
- discussion
- answer
- quiz
- questionsubmission
- question
- course
- exceptions
- mailer
- utils
-----------------------------------------------------
CROSS-CUTTING CONCERN MODULES (MORE RELEVANT TO ASPECTS STYLE):

- impexp 
- mailer
- utils
- exceptions
- config
- auth?

-----------------------------------------------------

-----------------------------------------------------

OTHER NOTES:
- impexp can be a ONE HOST SERVICE ?