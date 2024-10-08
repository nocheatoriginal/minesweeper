package minesweeper.javafx;

import javafx.scene.layout.Pane;
import minesweeper.javafx.viewmodel.MinesweeperViewmodel;
import minesweeper.provider.MinesweeperService;

/**
 * Minesweeper window class for JavaFX.
 */
public class MinesweeperWindowFx extends Pane {
  private MinesweeperService service;

  /**
   * Creates a new Minesweeper window.
   */
  public MinesweeperWindowFx() {
    super();
    service = new MinesweeperService();
    MinesweeperViewmodel viewModel = new MinesweeperViewmodel(service);
    MinesweeperBoardFx board = new MinesweeperBoardFx(viewModel);
    this.getChildren().add(board);
  }

  public MinesweeperService getService() {
    return service;
  }
}
