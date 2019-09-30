# Purpose
This service is built to fulfill two different requirements regarding the handling of Users of the system. UAA means User Authentication
and Administration.

First the service is capable to
work as [OpenID Connect](http://openid.net/connect) authentication endpoint and can authenticate user credentials against a persistent
storage.

The second part deals with administration of Users like creating new Users, updating properties of existing ones or deleting them. Most
application permissions are not directly assigned to Users explicitly, but to Roles. Users are assigned to Roles and application permissions
are granted to particular Roles. Administration of Roles and permissions is especially required for an UI application.

![classes][1]

An User has embedded UserDetails, and Emails assigned. Furthermore the password history of the User is managed and compared against new
passwords. An User can be assigned to multiple Roles and a Role can consist of several Users. A Role is a SecurityObject in general and has
multiple Grants assigned. A Grant is a permission that can be referenced from a client application.

# Resources

[![Build status](https://travis-ci.com/openwms/org.openwms.core.uaa.svg?branch=master)](https://travis-ci.com/openwms/org.openwms.core.uaa)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Quality](https://sonarcloud.io/api/project_badges/measure?project=org.openwms:org.openwms.core.uaa&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.openwms:org.openwms.core.uaa)
[![Join the chat at https://gitter.im/openwms/org.openwms](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/openwms/org.openwms?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# Deployment

The UAA is an essential component for all kind of applications and requires the highest
degree of availability level (Highest). Therefore it is deployed in an redundant setup
in different locations, on different cloud platforms with different ISP.

| endpoints | billed | SLA |
| --------- | ------ | --- |
| https://openwms-core-uaa.herokuapp.com 
  https://openwms.org/uaa | no | Heroku SLA for Europe region depends on AWS Europe region |

# Build

Build a runnable fat jar with execution of all unit and in-memory database integrations, but without a [RabbitMQ](https://www.rabbitmq.com)
server required to run: 

```
$ mvn package
```

To also build and run with RabbitMQ support call:

```
$ mvn package -DsurefireArgs=-Dspring.profiles.active=ASYNCHRONOUS,TEST
```

But notice that this requires a RabbitMQ server running locally with default settings.

Run the Sonar analysis:

```
$ mvn package -Psonar
```

## Run

After the binary is built it can be started from command line. By default no other infrastructure services are required to run this service.

```
$ java -jar target/openwms-core-uaa-exec.jar
```

In a distributed Cloud environment the service configuration is fetched from a centralized configuration service. This behavior can be 
enabled by activating the Spring Profile `CLOUD`. Additionally it makes sense to enable asynchronous communication that requires [RabbitMQ](https://www.rabbitmq.com)
as an AMQP message broker - just add another profile `ASYNCHRONOUS`. If the latter is not applies all asynchronous AMQP endpoints are 
disabled and the service does not send any events nor does it receive application events from remote services.

```
$ java -jar target/openwms-core-uaa-exec.jar --spring.profiles.active=CLOUD,ASYNCHRONOUS
```

Now the configuration service is tried to be discovered at service startup. The service fails to start if no instance of the configuration
service is available after retrying a configured amount of times.

## Release

```
$ mvn deploy -Prelease,gpg
```

### Release Documentation

```
$ mvn package -DsurefireArgs=-Dspring.profiles.active=ASYNCHRONOUS,TEST -Psonar
$ mvn site scm-publish:publish-scm
```

[1]: images/ClassDiagram.svg
