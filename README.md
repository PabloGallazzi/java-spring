[![Build Status](https://travis-ci.com/niko118/tp-tacs.svg?token=yWHUWReDvcqkbLeRzp1p&branch=master)](https://magnum.travis-ci.com/niko118/tp-tacs)

## This is the repository for the TACS assignment.

For detailed information abuot the API see the [wiki] (https://github.com/niko118/tp-tacs/wiki)

## Frameworks:

JAVA: v1.7

JUNIT: v4.11

Spring: v1.3.3.RELEASE

MORPHIA: v1.1.1

mongo-java-server: v1.6.0

commons-codec: v1.10

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

```
./populate_data.sh
```