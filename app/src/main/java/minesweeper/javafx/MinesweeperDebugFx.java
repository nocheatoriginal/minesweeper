package minesweeper.javafx;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import minesweeper.config.MinesweeperConfig;
import minesweeper.config.MinesweeperListener;
import minesweeper.provider.MinesweeperBoard;
import minesweeper.provider.MinesweeperService;
import minesweeper.provider.MinesweeperTile;

/**
 * The Minesweeper debug window.
 */
public class MinesweeperDebugFx extends Pane implements MinesweeperListener {
  private MinesweeperCellFx[][] cells;
  private MinesweeperService service;
  private HBox topbar;

  /**
   * Creates a new Minesweeper debug window.
   *
   * @param service The Minesweeper service.
   */
  public MinesweeperDebugFx(final MinesweeperService service) {
    super();
    service.addListener(this);
    this.service = service;

    GridPane grid = new GridPane();
    this.cells = new MinesweeperCellFx[MinesweeperConfig.MAP_SIZE][MinesweeperConfig.MAP_SIZE];
    MinesweeperBoard internalBoard = service.getMap();

    for (int i = 0; i < MinesweeperConfig.MAP_SIZE; i++) {
      for (int j = 0; j < MinesweeperConfig.MAP_SIZE; j++) {
        cells[i][j] = new MinesweeperCellFx(internalBoard.getCell(i, j), i, j);
        grid.add(cells[i][j], i, j);
      }
    }

    Platform.runLater(() -> {
      updateMap(internalBoard);
    });

    initTopbar();
    VBox screen = new VBox();
    screen.getChildren().addAll(topbar, grid);
    this.getChildren().addAll(screen);
    this.setBackground(Background.fill(MinesweeperConfig.BACKGROUND_COLOR));
  }

  private void initTopbar() {
    topbar = new HBox();
    Label status = new Label("DEBUG");
    status.setFont(MinesweeperConfig.FONT);
    status.setTextFill(MinesweeperConfig.STATUS_COLOR);
    topbar.getChildren().addAll(status);
    topbar.setAlignment(javafx.geometry.Pos.CENTER);
  }

  /**
   * Update the cells.
   *
   * @param newBoard ConnectFourBoard
   */
  public void updateMap(MinesweeperBoard newBoard) {
    newBoard = service.getMap();
    for (int column = 0; column < newBoard.getSize(); column++) {
      for (int row = 0; row < newBoard.getSize(); row++) {
        MinesweeperTile tile = newBoard.getCell(row, column);
        cells[row][column].setCell(tile);
      }
    }
  }

  public void updateBoard(MinesweeperBoard board) {
    // do nothing ...
  }

  public void updateStatus(String status) {
    // do nothing ...
  }
}
