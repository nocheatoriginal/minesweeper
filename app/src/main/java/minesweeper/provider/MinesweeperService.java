package minesweeper.provider;

import minesweeper.config.MinesweeperConfig;
import minesweeper.config.MinesweeperListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class MinesweeperService {
  private List<MinesweeperListener> listeners;
  private MinesweeperBoard map = new MinesweeperBoard();
  private MinesweeperBoard board;
  private MinesweeperBoard backup;
  private boolean gameOver;
  private boolean gameWon;
  boolean firstMove;
  private int countFlags;
  private String status;

  public MinesweeperService() {
    listeners = new ArrayList<MinesweeperListener>();
    this.reset();
  }

  public void reset() {
    this.map = MinesweeperBoard.generateRandom();
    board = new MinesweeperBoard();
    backup = new MinesweeperBoard();
    gameOver = false;
    gameWon = false;
    countFlags = 0;

    firstMove = MinesweeperConfig.NO_GUESSING_MODE;
    boolean foundStart = !firstMove;
    Random random = new Random();
    while (!foundStart)
    {
      int row = random.nextInt(board.getSize());
      int col = random.nextInt(board.getSize());

      if (map.getCell(row, col) == MinesweeperTile.OPEN)
      {
        board.setCell(row, col, MinesweeperTile.START);
        foundStart = true;
      }
    }
    status = firstMove ? "Select the green X to start!" : countFlags + "/" + MinesweeperConfig.BOMB_COUNT;
    notifyListeners(listener -> listener.updateStatus(status));
    notifyListeners(listener -> listener.updateBoard(board));
  }

  public void addListener(MinesweeperListener listener) {
    if (listeners.contains(listener)) {
      return;
    }
    listeners.add(listener);
  }

  private void notifyListeners(Consumer<MinesweeperListener> consumer) {
    try {
      for (MinesweeperListener listener : listeners) {
        consumer.accept(listener);
      }
    } catch (ConcurrentModificationException e) {
      String err = String.format("Error while notifying listeners: %s", e.getMessage());
      System.err.println(err);
    }
  }

  public MinesweeperBoard getBoard() {
    return board;
  }

  public String getStatus() {
    return status;
  }

  public void open(int row, int column) {
    if (gameOver) {
      if (board.getCell(row, column) == MinesweeperTile.BOMBSELECTED) {
        reset();
      }
      return;
    }

    if (firstMove) {
      if (board.getCell(row, column) == MinesweeperTile.START) {
        firstMove = false;
      } else {
        return;
      }
    }

    if (gameWon) {
      if (board.getCell(row, column) == MinesweeperTile.FLAG) {
        reset();
      }
      return;
    }

    if (board.getCell(row, column) != MinesweeperTile.CLOSED &&
        board.getCell(row, column) != MinesweeperTile.START) {
         return;
    }

    status = countFlags + "/" + MinesweeperConfig.BOMB_COUNT;
    MinesweeperTile openTile = map.getCell(row, column);
    switch (openTile) {
      case BOMB:
        gameOver();
        openTile = MinesweeperTile.BOMBSELECTED;
        board.setCell(row, column, openTile);
        break;
      case OPEN:
        uncover(row, column);
        break;
      default:
        board.setCell(row, column, openTile);
        break;
    }
    checkForWin();
    notifyListeners(listener -> listener.updateStatus(status));
    notifyListeners(listener -> listener.updateBoard(board));
  }


  private boolean outOfBounds(int row, int column) {
    return row < 0 || row >= board.getSize() || column < 0 || column >= board.getSize();
  }

  private void uncover(int row, int column) {
    if (outOfBounds(row, column)) {
      return;
    }

    if (board.getCell(row, column) != MinesweeperTile.CLOSED ||
        board.getCell(row, column) == MinesweeperTile.FLAG ||
        map.getCell(row, column) == MinesweeperTile.BOMB) {
      if (board.getCell(row, column) != MinesweeperTile.START) {
        return;
      }
    }

    MinesweeperTile tile = map.getCell(row, column);
    board.setCell(row, column, tile);
    if (tile == MinesweeperTile.OPEN) {
      uncover(row - 1, column);
      uncover(row + 1, column);
      uncover(row, column - 1);
      uncover(row, column + 1);
      uncover(row - 1, column - 1);
      uncover(row + 1, column - 1);
      uncover(row - 1, column + 1);
      uncover(row + 1, column + 1);
    }
  }

  private void checkForWin() {
    int countOpen = 0;

    for (int i = 0; i < board.getSize(); i++) {
      for (int j = 0; j < board.getSize(); j++) {
        if (board.getCell(i, j) != MinesweeperTile.CLOSED) {
          countOpen++;
        }
      }
    }

    int fieldSize = MinesweeperConfig.MAP_SIZE * MinesweeperConfig.MAP_SIZE;
    if (countFlags == MinesweeperConfig.BOMB_COUNT && countOpen == fieldSize) {
      gameWon = true;
    } else if (countOpen == fieldSize) {
      gameWon = true;
    }

    if (gameWon) {
      for (int i = 0; i < board.getSize(); i++) {
        for (int j = 0; j < board.getSize(); j++) {
          if (map.getCell(i, j) == MinesweeperTile.BOMB) {
            board.setCell(i, j, MinesweeperTile.FLAG);
          }
        }
      }
      status = "Select a flag to play again!";
      notifyListeners(listener -> listener.updateStatus(status));
    }
  }

  private void gameOver() {
    for (int i = 0; i < board.getSize(); i++) {
      for (int j = 0; j < board.getSize(); j++) {
        if (map.getCell(i, j) == MinesweeperTile.BOMB) {
          if (board.getCell(i, j) == MinesweeperTile.CLOSED) {
            board.setCell(i, j, MinesweeperTile.BOMB);
          }
        } else {
          if (board.getCell(i, j) == MinesweeperTile.FLAG) {
            board.setCell(i, j, MinesweeperTile.WRONGFLAG);
          }
        }
      }
    }
    gameOver = true;
    status = "Select the red bomb to play again!";
    notifyListeners(listener -> listener.updateStatus(status));
  }

  public void flag(int row, int column) {
    if (gameOver || gameWon || firstMove) {
      return;
    }

    if (board.getCell(row, column) == MinesweeperTile.CLOSED) {
      if (countFlags >= MinesweeperConfig.BOMB_COUNT) {
        return;
      }
      board.setCell(row, column, MinesweeperTile.FLAG);
      countFlags++;
    } else if (board.getCell(row, column) == MinesweeperTile.FLAG) {
      MinesweeperTile b = backup.getCell(row, column);
      board.setCell(row, column, b);
      countFlags--;
    }

    status = countFlags + "/" + MinesweeperConfig.BOMB_COUNT;
    notifyListeners(listener -> listener.updateStatus(status));
    notifyListeners(listener -> listener.updateBoard(board));
  }
}
