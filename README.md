# TEN PIN BOWLING SCORE PARSER

Project wich parses a file of bowling plays and return the score table.. Developed in Java and Gradle.

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
    │   └── resources
    └── test      
        ├── java
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

You have to previously build the jar.

`java -jar build/libs/ten_pin_bowling-all.jar --process-plays ./samples/sample6.txt`

### Developed by Bastidas Fabian 2020