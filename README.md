### Build and Run

#### Use the gradle wrapper to make sure the application works properly!

```sh
./gradlew clean build run
```

### Generating and executing Jar-File

```sh
./gradlew clean build shadowJar
java --module-path $PATH_TO_FX --add-modules javafx.controls -jar app/build/libs/minesweeper.jar
```

#### Code-Author: nocheatoriginal
