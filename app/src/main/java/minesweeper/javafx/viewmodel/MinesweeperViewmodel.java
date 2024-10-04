package minesweeper.javafx.viewmodel;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import minesweeper.config.MinesweeperConfig;
import minesweeper.config.MinesweeperListener;
import minesweeper.provider.MinesweeperBoard;
import minesweeper.provider.MinesweeperService;

/**
 * Viewmodel for the Minesweeper game.
 */
public class MinesweeperViewmodel implements MinesweeperListener {
  private final MinesweeperService service;
  private final ObjectProperty<MinesweeperBoard> board;
  private final ObjectProperty<String> status;

  /**
   * Creates a new Minesweeper viewmodel.
   *
   * @param service The Minesweeper service.
   */
  public MinesweeperViewmodel(final MinesweeperService service) {
    super();
    this.service = service;
    this.service.addListener(this);
    this.board = new SimpleObjectProperty<>(service.getBoard());
    this.status = new SimpleObjectProperty<>(service.getStatus());
  }

  /**
   * Returns the board property.
   *
   * @return The board property.
   */
  public ObjectProperty<MinesweeperBoard> boardProperty() {
    return board;
  }

  /**
   * Returns the status property.
   *
   * @return The status property.
   */
  public ObjectProperty<String> statusProperty() {
    return status;
  }

  /**
   * Opens a cell.
   *
   * @param row The row of the cell.
   * @param column The column of the cell.
   */
  public void open(int row, int column) {
    service.open(row, column);
  }

  /**
   * Flags a cell.
   *
   * @param row The row of the cell.
   * @param column The column of the cell.
   */
  public void flag(int row, int column) {
    service.flag(row, column);
  }

  /**
   * Resets the game.
   */
  @Override
  public void updateBoard(final MinesweeperBoard board) {
    Platform.runLater(() -> {
      this.board.set(this.getNewBoard(board));
    });
  }

  /**
   * Updates the status.
   *
   * @param status The new status.
   */
  @Override
  public void updateStatus(final String status) {
    Platform.runLater(() -> {
      this.status.set(status);
    });
  }

  /**
   * Returns a new board.
   *
   * @param oldBoard The old board.
   * @return The new board.
   */
  private MinesweeperBoard getNewBoard(MinesweeperBoard oldBoard) {
    MinesweeperBoard newBoard = new MinesweeperBoard();
    for (int i = 0; i < MinesweeperConfig.MAP_SIZE; i++) {
      for (int j = 0; j < MinesweeperConfig.MAP_SIZE; j++) {
        newBoard.setCell(i, j, oldBoard.getCell(i, j));
      }
    }
    return newBoard;
  }
}