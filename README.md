`# CODE CHALLENGE JOBSITY
Project wich solves the code challenge of Jobsity. Developed in Java and Gradle.

## Project structure

```
├── build.gradle
├── gradle    
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java  
    │   │   └── demo
    │   │       └── App.java
    │   └── resources
    └── test      
        ├── java
        │   └── demo
        │       └── AppTest.java
        └── resources
```

## Requirements

* Java >= 8
* Gradle
* Mockito
* JUnit

## Run the project

### Gradle

Run the project agaisnt sample test inputs:
`./gradlew run --args="--gen-score ./samples/sample6.txt"`

Test the project:
`./gradlew test`

Build the project:

`./gradlew build`

### Execute jar

You have to prevously build the jar.

`java -jar app.jar --gen-score ./samples/sample6.txt`

# Developed by Bastidas Fabian 2020