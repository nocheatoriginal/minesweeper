package minesweeper.javafx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import minesweeper.config.MinesweeperConfig;
import minesweeper.javafx.viewmodel.MinesweeperViewmodel;
import minesweeper.provider.MinesweeperBoard;
import minesweeper.provider.MinesweeperTile;

public class MinesweeperBoardFx extends Pane implements ChangeListener<MinesweeperBoard> {
  private MinesweeperBoard internalBoard;
  private MinesweeperCellFx[][] cells;
  private MinesweeperViewmodel viewModel;
  private HBox topbar;
  private Label status;

  public MinesweeperBoardFx(MinesweeperViewmodel viewModel) {
    super();
    this.viewModel = viewModel;
    viewModel.boardProperty().addListener(this);
    internalBoard = viewModel.boardProperty().get();
    cells = new MinesweeperCellFx[MinesweeperConfig.MAP_SIZE][MinesweeperConfig.MAP_SIZE];
    topbar = new HBox();
    initTopbar();
    GridPane grid = new GridPane();

    for (int i = 0; i < MinesweeperConfig.MAP_SIZE; i++) {
      for (int j = 0; j < MinesweeperConfig.MAP_SIZE; j++) {
        cells[i][j] = new MinesweeperCellFx(internalBoard.getCell(i, j), i, j);
        cells[i][j].setOnMouseClicked(event -> {
          MinesweeperCellFx cell = (MinesweeperCellFx) event.getSource();

          if (event.getButton().toString().equals("SECONDARY")) {
            viewModel.flag(cell.getX(), cell.getY());
          } else {
            viewModel.open(cell.getX(), cell.getY());
          }
        });
        grid.add(cells[i][j], i, j);
      }
    }

    VBox screen = new VBox();
    screen.getChildren().addAll(topbar, grid);
    this.getChildren().addAll(screen);
    this.setBackground(Background.fill(MinesweeperConfig.BACKGROUND_COLOR));
  }

  private void initTopbar() {
    Font minesweeperfont = Font.loadFont(getClass().getResourceAsStream("/fonts/mine-sweeper.ttf"), 20);
    status = new Label();
    status.textProperty().bindBidirectional(viewModel.statusProperty());
    status.setFont(minesweeperfont);
    topbar.getChildren().addAll(status);
    topbar.setAlignment(javafx.geometry.Pos.CENTER);
  }

  /**
   * Called when the value of an {@link ObservableValue} changes.
   * In general, it is considered bad practice to modify the observed value in
   * this method.
   *
   * @param observable The {@code ObservableValue} which value changed
   * @param oldValue   The old value
   * @param newValue   The new value
   */
  @Override
  public void changed(final ObservableValue<? extends MinesweeperBoard> observable,
                      final MinesweeperBoard oldValue, final MinesweeperBoard newValue) {
    if (Platform.isFxApplicationThread()) {
      updateBoard(newValue);
    } else {
      Platform.runLater(() -> updateBoard(newValue));
    }
  }

  /**
   * Update the cells.
   *
   * @param newBoard ConnectFourBoard
   */
  private void updateBoard(final MinesweeperBoard newBoard) {
    for (int column = 0; column < newBoard.getSize(); column++) {
      for (int row = 0; row < newBoard.getSize(); row++) {
        MinesweeperTile tile = newBoard.getCell(row, column);
        updateCell(tile, row, column);
      }
    }
  }

  private void updateCell(MinesweeperTile tile, int row, int column) {
    MinesweeperTile cell = cells[row][column].getCell();
    //if (cell != tile) {
    cells[row][column].setCell(tile);
    //}
  }
}
