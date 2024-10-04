package minesweeper.javafx;

import javafx.scene.layout.Pane;
import minesweeper.javafx.viewmodel.MinesweeperViewmodel;
import minesweeper.provider.MinesweeperService;

public class MinesweeperWindowFx extends Pane {

  public MinesweeperWindowFx() {
    super();

    MinesweeperService service = new MinesweeperService();
    MinesweeperViewmodel viewModel = new MinesweeperViewmodel(service);
    MinesweeperBoardFx board = new MinesweeperBoardFx(viewModel);
    this.getChildren().add(board);
  }
}
