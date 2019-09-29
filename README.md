# behandler-elektronisk-kommunikasjon
Application for handling rules used for infotrygd and later on persisting them in infotrygd or create a manuall task
to persisting them in infotrygd

## Technologies used
* Kotlin
* Ktor
* Gradle
* Spek
* Vault
* Oracle DB

#### Requirements

* JDK 11

## Getting started
#### Running locally
`./gradlew run`

## Add new clients to consume api
https://github.com/navikt/IaC/tree/master/Azure/registerApplication
And and then add clients in the list of authorizedUsers in the code

#### Build and run tests
To build locally and run the integration tests you can simply run `./gradlew shadowJar` or on windows 
`gradlew.bat shadowJar`

#### Creating a docker image
Creating a docker image should be as simple as `docker build -t behandler-elektronisk-kommunikasjon .`

#### Running a docker image
`docker run --rm -it -p 8080:8080 behandler-elektronisk-kommunikasjon`

## Contact us
### Code/project related questions can be sent to
* Joakim Kartveit, `joakim.kartveit@nav.no`

### For NAV employees
We are available at the Slack channel #team-sykmelding