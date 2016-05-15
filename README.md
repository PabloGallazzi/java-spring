[![Build Status](https://travis-ci.com/niko118/tp-tacs.svg?token=yWHUWReDvcqkbLeRzp1p&branch=master)](https://magnum.travis-ci.com/niko118/tp-tacs)

## This is the repository for the TACS assignment.

For detailed information abuot the API see the [wiki] (https://github.com/niko118/tp-tacs/wiki)

For usage information see the [API-usage-examples] (https://github.com/niko118/tp-tacs/wiki/API-usage-examples-(CURLs))

## Frameworks:

JAVA: v1.7

JUNIT: v4.11

Spring: v1.3.3.RELEASE

MORPHIA: v1.1.1

mongo-java-server: v1.6.0

commons-codec: v1.10

## Can i see this amazing app deployed ?

Yes you [can](http://tptacsutnfrba-pablogallazzi.rhcloud.com/)!

## How do I run the tests with coverage ?

```
mvn clean
mvn cobertura:cobertura
```

Results at: ${project}/target/site/cobertura/index.html

## How do I run the tests?

```
mvn clean
mvn test
```

## How do I run the app ?

```
mvn clean
mvn spring-boot:run
```

## How do I populate the database ?

With the server running, run the following command from your console.

```
./populate_data.sh
```

You can see the results at: ${project}/output.txt, the report includes:
All users created by the script (with access_tokens), it also includes the admin user with its 
access_token to test all the services that need admin privileges.
All teams, and characters created by the script are in this file with some pointers to
identify which team belongs to each user and which characters belong to each team.
