package minesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import minesweeper.config.MinesweeperConfig;
import minesweeper.config.OperatingMode;
import minesweeper.javafx.MinesweeperDebugFx;
import minesweeper.javafx.MinesweeperWindowFx;
import minesweeper.provider.MinesweeperService;

import java.util.logging.Logger;

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

    debugMode(window.getService());
  }

  private void debugMode(MinesweeperService service) {
    if (MinesweeperConfig.MODE != OperatingMode.DEBUG) {
      return;
    }

    MinesweeperDebugFx debugWindow = new MinesweeperDebugFx(service);
    Scene debugScene = new Scene(debugWindow);
    Stage debugStage = new Stage();
    debugStage.setTitle("DEBUG");
    debugStage.setScene(debugScene);
    debugStage.show();

    Logger logger = Logger.getLogger(MinesweeperLauncher.class.getName());
    logger.warning("The application runs in debug mode!");
  }

  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("DEBUG")) {
      MinesweeperConfig.MODE = OperatingMode.DEBUG;
    }

    launch(args);
  }
}
