package minesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import minesweeper.javafx.MinesweeperWindowFx;

/**
 * Minesweeper launcher class for JavaFX.
 */
public class MinesweeperLauncher extends Application {
  @Override
  public void start(Stage primaryStage) {
    MinesweeperWindowFx window = new MinesweeperWindowFx();

    Scene scene = new Scene(window);
    primaryStage.setTitle("Minesweeper");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
