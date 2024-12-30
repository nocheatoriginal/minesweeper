# Minesweeper

![Alt-Text](app/src/main/resources/sprites/minimize.png)

### Build and Run

#### Use the gradle wrapper to make sure the application works properly!

```sh
# building the application ...
./gradlew clean build
./gradlew run
```

### Run the application in debug mode

```sh
./gradlew debug
```

### Generating and executing Jar-File

```sh
./gradlew clean build shadowJar
java --module-path $PATH_TO_FX --add-modules javafx.controls -jar app/build/libs/minesweeper.jar
# for debug mode, add the following argument at the end: "DEBUG"
```

#### Code-Author: nocheatoriginal
