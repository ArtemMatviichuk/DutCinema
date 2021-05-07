# Cinema_project

## Project details

- Project name: Cinema
- Front repository: [Cinema front](https://github.com/maxXximus23/Cinema_project_front)

## Overview

The web-application will allow user to view list of current films, booking session and buying tickets. 

Also, the web-application will allow user to registr account, where user can view his history of booking films.


## Project setup

Before starting working on the project make sure that the following tools are installed:

- Git
- Necessary libraries, which descripted at the end of document
- MySQL

And make sure you set up application.properties before start.

## Project structure

```
Cinema_project/
├── src/ - directory for code sources
|  └── main/ - directory for files in which application logic and data are defined
|  |   └── java/ - directory for controllers, DAO, DTO, exceptions, security, services and unit tests
|  |   └── resources/ - directory for project resources and settings
|  └── test/ directory contains package and class which start a necessary test checking
└── application-specific configuration files
```

## General Naming Guidelines

```
#Controller
@RestController
@RequestMapping("sessions")
@AllArgsConstructor
public class SessionController { }

#variable name
private ISessionService sessionService;

#file name
message.properties
```

## Way of work

1. Create the backlog (a list of all the desired features of the application) and board in Trello:

   - create Issue: Task, Bug, Story, Sub-Task (“To Do” status) with the origin estimate, assignee and description;
   - planning, difficulty rating and analysis Sprints (2 weeks);

2. Start work with Issue:

   - change status of issue to “DOING”;
   - create a New branch in project (name: `DUT_#_BranchName`);

3. When the task is finished, create merge request in github:

   - assign mentor and team to code review;
   - manual log work;
   - update Trello with link to request and status of issue “CODE REVIEW”;
   - mentor and team code review;

   ##### Commit and push all recent changes
     Some basic Git commands are:

   ```
   git status
   git add
   git commit -m 'Commit'
   git pull
   git push
   ```

4. After merging, status of issue must be changed to “DONE”.

## Technologies / Libraries

- Spring
- Hibernate
- Thymeleaf template engine
- MySQL
- Java Persistence API
- Passay
- BCrypt
- Flyway
- Authentification via JWT
- Lombok
