[![Build status](https://github.com/navikt/syfopartnerinfo/workflows/Deploy%20to%20dev%20and%20prod/badge.svg)](https://github.com/navikt/syfopartnerinfo/workflows/Deploy%20to%20dev%20and%20prod/badge.svg)
# syfopartnerinfo
Application for getting out partner-id that has electronic communications

## Technologies used
* Kotlin
* Ktor
* Gradle
* Spek
* Vault
* Oracle DB

#### Requirements

* JDK 17

## Getting started
#### Running locally
`./gradlew run`

## Add new clients to consume api
https://github.com/navikt/IaC/tree/master/Azure/registerApplication
And and then add clients in the list of authorizedUsers in the code

#### Build and run tests
To build locally and run the integration tests you can simply run `./gradlew shadowJar` or on windows 
`gradlew.bat shadowJar`

### Lint (Ktlint)

##### Command line

Run checking: `./gradlew --continue ktlintCheck`

Run formatting: `./gradlew ktlintFormat`

##### Git Hooks

Apply checking: `./gradlew addKtlintCheckGitPreCommitHook`

Apply formatting: `./gradlew addKtlintFormatGitPreCommitHook`

#### Creating a docker image
Creating a docker image should be as simple as `docker build -t syfopartnerinfo .`

#### Running a docker image
`docker run --rm -it -p 8080:8080 syfopartnerinfo`

## Contact us

### For NAV employees
We are available at the Slack channel #veden
