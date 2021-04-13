# quasar-api
Repository that has the project in charge of returning the source and content of the help message sent from the lost imperial ship.

## Clonning project
You should clone this project in your local machine and the PROJECT_ROOT_PATH will be: **(quasar-api/quasar)**

## Automatic compiling and deploying
There is a file `quasar-starter.sh` that compiles and deploys, the project and the DB, into a docker container.<br/>
For executing this file, you need to go into the PROJECT_ROOT_PATH and double click it or execute the following command:
```
$ ./start-quasar-api.sh
```

## Environments
There are 2 different environments available for compiling and deploying quasar project:

- **docker** :arrow_right: For using the Docker provided images. **http://localhost:8080**
- **prod** :arrow_right: For using quasar application and DB hosted in AWS. **http://quasarapi-env.eba-ucxe9pmf.us-east-1.elasticbeanstalk.com**

The properties of each project are defined on `application-docker.properties` and `application-prod.properties`

## Manual compiling
This is a Maven project and for compiling it you should proceed with the Maven wrapper provided. You should go into PROJECT_ROOT_PATH and run the following command:
```
$ mvn clean install
```

## Deploying
This project is dockerized and it has an external Postgres Database dockerized too. If you want to deploy both of them please run the following commands into PROJECT_ROOT_PATH.

If used, the first command will destroy the images generated previusly and the second one will create them again.

The project will always check that the DB is already initialized for starting to deploy the application. You can check this process executing `docker-compose up` instead of `docker-compose up -d`
```
$ docker-compose down -v
$ docker-compose up -d
```

## Docker manipulation
You can run and stop the containers separately with the following commands:
```
$ docker start/stop quasar-api
$ docker start/stop quasar-postgresql
```
If you want to remove each image and container, is possible with the following commands:
```
$ docker rm quasar-api
$ docker rm quasar-postgresql
$ docker rmi quasar-api:latest
```

## Generalities
The project includes:

- Java documentation into PROJECT_ROOT_PATH/java-doc path.
- Swagger documentation (:round_pushpin:e.g. For docker environment :arrow_right: `http://localhost:8080/swagger-ui.html`)
- i18n for responses in spanish:es: or english:us:.
- Actuator for checking health of application.
- Jacoco Tool to validate Coverage of the tests into PROJECT_ROOT_PATH/target/jacoco-ut path this files are available after deploy.

### API request
#### Headers and parameters
| header  | value | description |
| ------------- | ------------- | ------------- |
| Accept  | application/json  | Default value  |
| locale  | en  | Default value  |
| locale  | es  | If you want a response in spanish  |

#### Healthcheck
There is a GET endpoint for verifying health check of application: `http://quasarapi-env.eba-ucxe9pmf.us-east-1.elasticbeanstalk.com/actuator/health`

#### /topsecret
There is a POST endpoint located in: `http://quasarapi-env.eba-ucxe9pmf.us-east-1.elasticbeanstalk.com/topsecret`

#### /topsecret_split
There is a POST endpoint located in: `http://quasarapi-env.eba-ucxe9pmf.us-east-1.elasticbeanstalk.com/topsecret_split/{satellite_name}`
There is a GET endpoint located in: `http://quasarapi-env.eba-ucxe9pmf.us-east-1.elasticbeanstalk.com/topsecret_split/{satellite_name}`


/topsecret Example:
```
{
    "satellites": [
        {
        "name": "kenobi",
        "distance": 400.0,
        "message": ["este", "", "", "mensaje", ""]
        },
        {
        "name": "skywalker",
        "distance": 400.5,
        "message": ["", "es", "", "", "secreto"]
        },
        {
        "name": "sato",
        "distance": 899.4,
        "message": ["este", "", "un", "", ""]
        }
    ]
}
```

The system will find the intersection between the three cricles determined by the distances defined in the request and the position of each satellite
mentioned in the challenge and calculate the point where the Imperial ship is, for this the solution validates that three satelites are provided in the request 
as is the minimum amount to find only one point.

> :warning: The system does not validate the name of the satellites as for the solution of the excercise it will need minimum three, so it does not matter the order, the intersection point of the three circles is going to be only one, please have this in mind in case a new satellite enters the solution.

### API responses
The responses of the system has the following structure for success and error messages:

#### OK
200 OK with body:
```
{
    "position": {
        "x": -243.09986877441406,
        "y": -406.7061462402344
    },
    "message": "este es un mensaje secreto"
}
```

#### If not possible to determinate the location
404 NOT FOUND with body:
```
{
    "errorCode": "quasar.location.exception",
    "errorMessage": "It was not possible to determinate location of ship in distress.",
    "errorDetail": "Not Found",
    "httpStatus": 404,
    "errorOriginPath": "/topsecret",
    "errorOriginApp": "quasar-api"
}
```

#### If not possible to determinate the message
404 NOT FOUND with body:
```
{
    "errorCode": "quasar.message.exception",
    "errorMessage": "It was not posible to determinate the message.",
    "errorDetail": "Internal Server Error",
    "httpStatus": 404,
    "errorOriginPath": "/topsecret",
    "errorOriginApp": "quasar-api"
}
```

#### If there is an exception in the API
500 INTERNAL SERVER ERROR with body:
```
{
    "errorCode": "general.exception",
    "errorMessage": "Something Went Wrong.",
    "errorDetail": null,
    "httpStatus": null,
    "errorOriginPath": null,
    "errorOriginApp": null
}
```

> :warning: If the message is malformed it will return a blank response with code 400 BAD REQUEST.

POST /topsecret_split/{satellite_name} Example:
```
{
        "distance": 899.4,
        "message": ["este", "", "un", "", ""]
}
```

This endpoint saves a register in the postgresDB with the information of the satellite name, distance and message received, if there is already one register in the DB with the satellite name 
specified in the URL, the record is deleted and a new register is created with the new data.

> :warning: The system does not validate the name of the satellites as for the solution of the excercise it will need minimum three, so it does not matter the order, the intersection point of the three circles is going to be only one, please have this in mind in case a new satellite enters the solution.

### API responses
The responses of the system has the following structure for success and error messages:

#### OK
200 OK without body:

> :warning: If the message is malformed it will return a blank response with code 400 BAD REQUEST.

GET /topsecret_split/{satellite_name} Example:
```
{
        "distance": 899.4,
        "message": ["este", "", "un", "", ""]
}
```

This endpoint uses the same logic as the /topsecret, the only difference is that it uses the data stored in the database, so it will necessary to first
insert the data corresponding to the three satelites using the POST endpoint and then use this one to calculate the message and location of the source.

> :warning: The system does not validate the name of the satellites as for the solution of the excercise it will need minimum three, so it does not matter the order, the intersection point of the three circles is going to be only one, please have this in mind in case a new satellite enters the solution.

### API responses
The responses of the system has the following structure for success and error messages:

#### OK
200 OK with body:
```
{
    "position": {
        "x": -243.09986877441406,
        "y": -406.7061462402344
    },
    "message": "este es un mensaje secreto"
}
```

#### If not possible to determinate the location
404 NOT FOUND with body:
```
{
    "errorCode": "quasar.location.exception",
    "errorMessage": "It was not possible to determinate location of ship in distress.",
    "errorDetail": "Not Found",
    "httpStatus": 404,
    "errorOriginPath": "/topsecret",
    "errorOriginApp": "quasar-api"
}
```

#### If there is not enough information to calculate the location or message
404 NOT FOUND with body:
```
{
    "errorCode": "quasar.amountinfo.exception",
    "errorMessage": "Not enough Information.",
    "errorDetail": "Internal Server Error",
    "httpStatus": 404,
    "errorOriginPath": "/topsecret_split/sato",
    "errorOriginApp": "quasar-api"
}
```

#### If not possible to determinate the message
404 NOT FOUND with body:
```
{
    "errorCode": "quasar.message.exception",
    "errorMessage": "It was not posible to determinate the message.",
    "errorDetail": "Internal Server Error",
    "httpStatus": 404,
    "errorOriginPath": "/topsecret",
    "errorOriginApp": "quasar-api"
}
```

#### If there is an exception in the API
500 INTERNAL SERVER ERROR with body:
```
{
    "errorCode": "general.exception",
    "errorMessage": "Something Went Wrong.",
    "errorDetail": null,
    "httpStatus": null,
    "errorOriginPath": null,
    "errorOriginApp": null
}
```
> :warning: If the message is malformed it will return a blank response with code 400 BAD REQUEST.





