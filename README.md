# REST CRUD API Example

This project demonstrates a REST Server and REST Clients. 

It demonstrates CRUD operations on an `employee` table, tracking `firstName`, `lastName`, and `email` address

Spring boot is used for the REST Server

There is a JAVA Rest Client and a Python REST Client. They both do the same thing, basically calling each CRUD operation and printing the results.

## Setup

My have MySQL running and create a `resttest` database, `resttest` user with `resttest` password
Or optionally you can create what ever database and users you like, just update `server/resources/application.properties` with the correct information.

A MySQL script is provided in `server/sql/mysqlsetup.sql` to setup the `resttest` sand `resttest` user.

Spring Boot JPA will automatically create the `employee` table in what ever database is specified in `server/resources/application.properties`. You can turn this feature off by commenting out the following line in the `server/resources/application.properties` file

```
spring.jpa.hibernate.ddl-auto=create
```

