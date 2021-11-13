# Student Enrollment Service

Student Enrollment Service is a software that manages the enrollment of students into the system.

## Table of Content

- [Student Enrollment API](#student-enrollment-service)
  - [Table of Content](#table-of-content)
  - [Project Architecture](#project-architecture)
  - [Git Workflow](#git-workflow)
  - [Project Tracking](#project-tracking)
  - [Coding Style](#coding-style)

## Project Architecture

```
    The root folder for our code is `src`:
    src
    ┣ main
    ┃ ┣ java
    ┃ ┃ ┣ com.flexisaf.enrollmentservice
    ┃ ┃   ┣ configs
    ┃ ┃   ┣ exceptions
    ┃ ┃   ┣ filters
    ┃ ┃   ┣ models
    ┃ ┃   ┣ repositories
    ┃ ┃   ┣ dto
    ┃ ┃   ┣ security
    ┃ ┃   ┣ services
    ┃ ┃   ┗ utilities
    ┃ ┣ resources
    ┃   ┣ static
    ┃   ┣ templates
    ┃   ┗ application.yaml
    ┃ 
    ┣ build.gradle
    ┣ gradle
    ┣ gradle.bat
    ┣ Help.md
    ┣ README.md
    ┗ settings.gradle
```

The description of each folder in the project architecture is given below:

-   `resources`: Contains static assets like images, audios, and properties files.
-   `configs`: Contains spring framework configurations for different modules involved in the project to function. 
-   `controllers`: Contain classes that expose functions that can be accessed by URL mapped with various HTTP Method, the classes are named based on the models they act on.
-   `exceptions`: Contains exceptions to handle error the system encounters.
-   `filters`: Contains Filters to the intercept requests before reaching the main system
-   `models`: Contain Models to translate to different tables in the database, they are named according to concept they abstract.
-   `repositories`: Contains Classes that link to database tables and perform queries on them which are named based on the tables they reference.
-   `dto`: Contain api-related Data Transfer Object (DTO) to be mapped to json output.
-   `security`: Contains files that set up the global security of the system.
-   `services`: functions encapsulated in classes which are named based on their concerns. For example a class named `AuthenticationService` would contain methods that handles api-related logic for anything related to authentication.
-   `utilities`: Contains reusable handy utility functions
    
## Git Workflow

1. The repository at `origin` contains two main branches that are parallel:
    - `main`: This is the production-ready branch
    - `develop`: This branch contains features that have not been merged to `main`.
2. Each developer branches from `develop` and his/her changes would be merged back to `develop`. Only the product manager/ team has the permission to make changes to `main`.
   _Useful git snippet:_

```sh
git pull origin develop ## pulling changes before working on feature
git checkout -b <branch-feature-name> ## creating and checking out to a feature branch
```

3. Each feature being worked on by a developer is expected to have its own branch on the developer's local computer where he can track his changes. When the feature has been baselined, he can then push and create a pull request. After the code has been reviewed and his changes have been applied, he can then delete that feature branch.
   _Useful git snippet:_

```sh
git push origin <branch-feature-name> ## pushing the feature branch for review
git push origin --delete <branch-feature-name> ## deleting the branch from remote after the changes have been applied.
```

4. Whenever the code on the `develop` branch is ready for release, a new branch named after the release version (e.g `release-1.6`) is created where bugfixes can be carried out. Once everything is set for production, the `release` branch is merged to master and tagged to reflect the release version. The `release` branch is also merged to `develop` to reflect the bugfixes carried out on the `release` branch. Afterwards, the release branch can be deleted
    - [Check this document for more information]
    - [This article is also helpful]

## Project Tracking

    The team currently has no means for tracking projects.

## Coding Style

1. Classes are declared using the camel case with the first Letter capitalised like this `JavaClass`.
2. Regular function names, variables (not constants), and property names for API-related objects are declared using camel case.
3. Constants are declared using the uppercase casing style. For example user route would be `USER_ROUTE`.
4. Boolean variables are prefixed to indicate that truthy value. For example:
5. Database tables are defined with plural names
6. All API Endpoint are defined with plural form of noun and actions represented with verb like `api/v1/users/login`   
7. All API Endpoints use dash `-` instead of underscore `_` to separate two words like `api/v1/users-module`


[this article is also helpful]: https://nvie.com/posts/a-successful-git-branching-model/
[check this document for more information]: https://nvie.com/files/Git-branching-model.pdf
