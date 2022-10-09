# Employee Service Project

### Introduction

Employee Service is a simple application to manage employees information. It is written by Java in Spring boot framework
and uses an in-memory database to store the data. Database versioning is handled by Liquibase. There is also a
dockerfile which enables to run it in containerized mode.

---
In this application Spring Data Rest is used to generate CRUD APIs and since Swagger doesn't support it, HAL Explorer
was used for APIs documentation. Using these two libraries reduce the boilerplate codes for creating, documenting and
testing REST APIs. However, in this project there was a problem in returning csv in search response. So the search API
was written by Spring Rest controller and therefor this API is not documented by HAL Explorer.

### Project Features

* Initiate the app with data in a CSV file.
* Load additional data to the app with CSV files during the runtime.
* REST APIs to do CRUD actions on Employee.
* API to Search by multiple operators and export the result in a CSV file.

### Getting Started

* Clone the repository
  from [https://github.com/identrics-dev/JRP-MB2209.git](https://github.com/identrics-dev/JRP-MB2209.git).
* Set configuration parameters according to [Configuration Parameters](#configuration-parameters) section.
* You can put a CSV file in the location specified in the parameters to load in the application startup.
* You can put CSV files in the location specified in the parameters to load during the application runtime.
* Launch the application:
    * CLI: `$ mvn spring-boot:run`
* Launch with Docker
    * `$ mvn clean package`
    * `$ mvn docker:build`
    * `$ docker run -p 8080:8080 -v C:\location-to-watch-folder:/watch --env identrics_employee_importing_watch_location=/watch  identrics/emp
      loyee-service:0.0.1-SNAPSHOT`
* Connect to the API using Postman on port 8080
* You can see the full document of APIs on [http://localhost:8080/api/](http://localhost:8080/api/)

### Configuration Parameters

Your can load configuration parameters from several places with the following hierarchy:

* CLI argument
* Environment variable
* Config file in any of these file formats: `yml | ini | toml | json | properites`.
* The sample config files are available in `files/other-application-format` folder

Configuration parameters and their default values are as below:

* search.default-order-dir=asc
* importing.watch-location=C:\\Users\\asus\\Desktop\\test-work\\employee-service\\src\\main\\resources\\watch
* importing.watch-poll-interval-seconds=10
* importing.watch-quiet-period-seconds=5

### API Endpoints

| HTTP Verbs | Endpoints             | Action                                                |
|------------|-----------------------|-------------------------------------------------------|
| GET        | /api/employees        | To retrieve all employees                             |
| POST       | /api/employees        | To create a new employee                              |
| GET        | /api/employees/:id    | To retrieve details of a single employee              |
| PUT        | /api/employees/:id    | To edit details of a single employee                  |
| DELETE     | /api/employees/:id    | To delete a single employees                          |
| PATCH      | /api/employees/:id    | To partially update a single employees                |

*You can see the full document of APIs on [http://localhost:8080/api/](http://localhost:8080/api/)

| HTTP Verb  | Endpoint              | Action                                                      |
|------------|-----------------------|-------------------------------------------------------------|
| POST       | /api/employees-search | To retrieve/export all employees matching the given filters |

* To retrieve the response as JSON, set the accept header to `aplication/json` 
* To export the response as a CSV file, set the accept header to `text/csv, aplication/json` 
* The sample PostMan collection for these APIs is available in files folder in root directory.
* Filter structure is explained in next section.

### Search request body structure:

The request body for search API should contain the following data:

```json lines
{
  "filters": [
    <search_filter>
  ],
  "aggregator": <operation_aggregator>,
  "page": <page>,
  "size": <size>,
  "sort": <sort>,
}
```

Search Filter contains following data:

```json lines
{
  "key": <search_field>,
  "operation": <search_operation>,
  "value": <search_value>
}
```

Supported values:

* key: `"name" | "company" | "education" | "salary"`
*

operation: `"EQUAL" | "NOT_EQUAL" | "START_WITH" | "LESS_THAN" | "LESS_THAN_OR_EQUAL" | "GREATER_THAN" | "GREATER_THAN_OR_EQUAL"`

* value: string for `"name" | "company" | "education"` and number for `"salary"`

### Used Technologies

* Java v17
* Spring Boot v2.7.4
* Spring Data Rest
* HAL Explorer
* H2 database
* Lombok
* Liquibase
* Docker

### Author

* Masoud Bahrami