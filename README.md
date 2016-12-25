[![Build Status](https://travis-ci.com/niko118/tp-tacs.svg?token=yWHUWReDvcqkbLeRzp1p&branch=master)](https://magnum.travis-ci.com/niko118/tp-tacs)

## This is the repository for the TACS assignment.

To be able to use Marvel's API read [this](https://github.com/PabloGallazzi/java-spring/wiki/Usage-of-Marvel's-API)

For detailed information about the API see the [wiki](https://github.com/PabloGallazzi/java-spring/wiki)

For usage information see the [API-usage-examples](https://github.com/PabloGallazzi/java-spring/wiki/API-usage-examples)

## Frameworks:

JAVA: v1.7

JUNIT: v4.11

Spring: v1.3.3.RELEASE

MORPHIA: v1.1.1

mongo-java-server: v1.6.0

commons-codec: v1.10

log4j (Spring boot embedded)

[jedis](https://github.com/xetorthio/jedis): v2.8.0

## Can i see this amazing app deployed ?

Yes you [can](https://tptacsutnfrba-pablogallazzi.rhcloud.com/)!

## Can i see the deploy history and status ?

Yes you [can](http://190.192.153.205:8080/deploy/5737931a7628e17321000043)!

## Does this app deploy itself ?

Yes it does, it auto deploys every time a push is made to master branch.
If the tests run to completion and the push was not issued by a pull request, travis sends a request to deploy the app.
You can avoid a deploy by adding [SKIP DEPLOY] anywhere in your commit message.

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

This runs the application in develop environment (the default) which uses an in-memory db and has real connectivity with Marvel's API.

## Which are the available environments ?

* develop : This is the default environment it provides an in-memory db, an in-memory cache, and has real connectivity with Marvel's API.
* test : This is the environment in which tests run, it provides an in-memory db, an in-memory cache and a mock for Marvel's API. This environment should not be used for any other purpose than running the tests.
* mongo : This environment provides the settings to connect to a real mongo db, an in-memory cache and has real connectivity with Marvel's API, if you are to run the application in this environment, make sure you are running a real mongo db in port 27017, the dbname is bdtptacs_dev.
* openshift : This environment has all the needed settings to connect to openshift's real mongo db, connects to openshift's real redis as a cache and has real connectivity with Marvel's API. This environment should only be used for deployment purposes.

## Where did you take the Redis cartridge from ?

You can find the Redis cartridge [here](https://github.com/smarterclayton/openshift-redis-cart)!

## How do I run the app in a specific environment ?

```
mvn clean
mvn spring-boot:run -Dspring.profiles.active=<environment>
Example:
mvn spring-boot:run -Dspring.profiles.active=mongo
```

## How do I populate the database ?

With the server running, run the following command from your console.

```
./populate_data.sh
```

You can see the results at: ${basedir}/output.txt, the report includes:
All users created by the script (with access_tokens), it also includes the admin user with its 
access_token to test all the services that need admin privileges.
All teams, and characters created by the script are in this file with some pointers to
identify which team belongs to each user and which characters belong to each team.

## Where can I see the logs of this application ?

This app uses log4j as a logging framework, you can see the logs at ${basedir}/tp_tacs_application.log

```
tail -200f tp_tacs_application.log
```

The above command will show you the last 200 lines of that log file, and will keep on updating to show you the latest logs!
