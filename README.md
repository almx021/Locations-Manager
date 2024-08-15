# Locations-Manager
REST API that allows the user to perform CRUD operations to Locations. Implemented using Java Spring Boot and in-memory database H2, tested using MockMVC and Mockito, Endpoints documented with OpenApi.

## How to Run

* Clone this repository
* Make sure you are using JDK 17 or superior and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* You can run again the tests if you want by running ```nvm test```
* Once successfully built, you can run the service by one of these two methods:
```
        mvn spring-boot:run
or
        java -jar target/locations-manager-0.0.1-SNAPSHOT.jar
```
* Once the application runs you should see something like this
```
2024-08-15T09:22:35.426-03:00  INFO 2332 --- [locations-manager] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2024-08-15T09:22:35.447-03:00  INFO 2332 --- [locations-manager] [           main] c.m.l.LocationsManagerApplication        : Started LocationsManagerApplication in 6.186 seconds (process running for 6.725)
```

## About the Endpoints

After running the server, you can open [this link](http://localhost:8080/swagger-ui.html) to access a visual documentation of the API's endpoints and interact with the API’s resources. You can also follow [this other link](http://localhost:8080/v3/api-docs) to explore a JSON documentation of the API's endpoints.

## Questions and Comments: alm021@hotmail.com

