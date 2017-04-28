# githubapi

## Objective

This app downloads all projects from the user Bearch Inc and list the languages used and all projects by language and issue count

## Requirements for development

* Android Studio
* Gradle 3.3
* Gradle Tools 2.3.1

## Requirements for usage

* Android minSDK 17

## Decisions during development

### ORM

I was using DBFlow for a long time, but since DBFlow follows the ActiveRecord pattern, it makes it very difficult for mocking the database during unit tests.
Because of that I decided to develop an ORM following the repository pattern that was possible for mocking during unit tests.
The ORM is not full developed but since it was what I am used to use in my projects, I decided to follow the same standard in this project.

### SyncService

The sync service is a over engineered solution for a simple request, but since the idea of this project is to avaliate my android knowledge, I thought that it was a good idea to use a service.
