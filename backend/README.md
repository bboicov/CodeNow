# Air Quality API

* Simple REST service providing cached data from https://openaq.org
* Implementation based on Spring Boot v2.3.4
* Use of Hibernate and second level cache
* Asynchronous implementation based on Project Reactor (https://projectreactor.io) 
* Docker image build

### Application workflow

* Application will try to connect to openAQ API and starting fetching data upon start.
* First all available countries would be retrieved and measurements data.
* Periodically cached data would be updated (every 8h).
* Processing is completely asynchronous. This allows retrieving data during update process. 

### Database

Service is working with memory based H2 embedded DB. Configuration parameters are located in `resources/application.yml`.

### Building

```shell script
mvn package
```

Running the package target will compile, run unit tests, and create a Fat JAR of the application.

### Tests

```shell script
mvn test
```

Running the test target will run tests.

## Running

#### Kubernetes

Provided Dockerfile could be used to build docker image and deploy to  kubernetes.

This sample application exposes port 5005 for remote debugging.

#### Locally

To run the application locally, it's just a matter of executing the fat jar. From the top-level directory of this project, simply do:

```shell script
java -jar target/codenow-0.0.1-SNAPSHOT.jar
```

## Execute test calls against server running on localhost

### Fetch specific air result by ID

Example call:
 
```shell script
curl -v -k  -H "Content-Type: application/json"  -X GET "http://localhost:8080/api/results/3"
```

### Fetch ALL air results data

Example call:
 
```shell script
curl -v -k  -H "Content-Type: application/json"  -X GET "http://localhost:8080/api/results"
```

### Fetch air results data with pagination and sorting

Example call:

```shell script
curl -v -k  -H "Content-Type: application/json"  -X GET "http://localhost:8080/api/results/pageable?page=0&size=20&sort=country.name"
```
