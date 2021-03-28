# Typing Tracker

## How to build & run

### Option 1

```shell
./gradlew run --args="2 33"
```

or

```shell
./gradlew run -Pargs="2 33"
```

### Option 2

```shell
./gradlew jar # to make jar
java -jar app/build/libs/app.jar 2 33
```

### Option 3

```shell
./gradlew installDist # to make distribution
./app/build/install/app/bin/app 2 33
```
