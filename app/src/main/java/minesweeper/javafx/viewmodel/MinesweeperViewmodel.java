package minesweeper.javafx.viewmodel;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import minesweeper.config.MinesweeperConfig;
import minesweeper.config.MinesweeperListener;
import minesweeper.javafx.MinesweeperBoardFx;
import minesweeper.provider.MinesweeperBoard;
import minesweeper.provider.MinesweeperService;

public class MinesweeperViewmodel implements MinesweeperListener {
  private final MinesweeperService service;
  private final ObjectProperty<MinesweeperBoard> board;
  private final ObjectProperty<String> status;

  public MinesweeperViewmodel(final MinesweeperService service) {
    super();
    this.service = service;
    this.service.addListener(this);
    this.board = new SimpleObjectProperty<>(service.getBoard());
    this.status = new SimpleObjectProperty<>(service.getStatus());
  }

  public ObjectProperty<MinesweeperBoard> boardProperty() {
    return board;
  }

  public ObjectProperty<String> statusProperty() {
    return status;
  }

  public void open(int row, int column) {
    service.open(row, column);
  }

  public void flag(int row, int column) {
    service.flag(row, column);
  }

  public void updateBoard(final MinesweeperBoard board) {
    Platform.runLater(() -> {
      this.board.set(this.getNewBoard(board));
    });
  }

  public void updateStatus(final String status) {
    Platform.runLater(() -> {
      this.status.set(status);
    });
  }

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