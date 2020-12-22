# CryptoSystem

This application is a service, that performs following tasks.
- constantly checks the currency exchange rate from Bitcoin to US-Dollar (1 Bitcoin = x USD).
- received rates are stored in "in-memory" database
- exposes REST webservice API to retrieve stored rates 

Architecture diagram is available in `Architecture.pdf`

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Spring Boot 2.4.1](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-parent/2.4.1)
- [Maven 3](https://maven.apache.org)

## Configurable Properties

Following application specific properties are configured in `src/main/resources/application.properties`.
They can be changed as per need.

**check.period.cron.expression**
- configurable check period
- value must be a valid cron expression
- default value is ```*/30 * * * * *```

**rate.provider.url**
- configurable url to get rate
- default value is ```https://api.blockchain.com/v3/exchange/tickers/BTC-USD```

## Running the application locally

There are several ways to run a Spring Boot application on your local machine.

#### 1. <ins> Run in IDE
Import project in IDE and execute the `main` method in the `com.demo.cryptosystem.Application` class from your IDE.

#### 2. <ins> Run using Maven plugin
Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

#### 3. <ins> Run from command line

This application is packaged as a war which has embedded Tomcat server provided by Spring framework. No external Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository
* Make sure that requirements are fulfilled
* Build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by running following command. Check the stdout to make sure no exceptions are thrown.
```
java -jar target/CryptoSystem-0.0.1-SNAPSHOT.jar
```

Once the application starts successfully, you should see log statements as follows.

```
2020-12-22 01:33:09.565  INFO 33472 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path '/cryptosystem'
2020-12-22 01:33:09.850  INFO 33472 --- [           main] com.demo.cryptosystem.Application        : Started Application in 9.008 seconds (JVM running for 9.948)
```

## REST API

Details about exposed REST API endpoints for the application are documented using Swagger UI. Documentation can be viewed using following webpage. It will be activated when application is running.

http://localhost:8080/cryptosystem/swagger-ui/#/