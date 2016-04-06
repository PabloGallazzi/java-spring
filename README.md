## This is the repository for the TACS assignment.

For detailed information abuot the API see the [wiki] (https://github.com/niko118/tp-tacs/wiki)

## Frameworks:

JAVA: v1.7

JUNIT: v4.11

Spring: v1.3.3.RELEASE

MORPHIA: v1.1.1

mongo-java-server: v1.6.0

commons-codec: v1.10

## How do i run the tests?

```
mvn test
```

## How do I run the app in XXXX environment?

```
mvn spring-boot:run -Dspring.profiles.active=XXXX
```
or you can set some profile as default
```
export spring_profiles_default=XXXX
mvn spring-boot:run
```
