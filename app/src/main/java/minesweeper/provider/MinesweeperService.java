package minesweeper.provider;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.function.Consumer;

import minesweeper.config.MinesweeperConfig;
import minesweeper.config.MinesweeperListener;

/**
 * Minesweeper service class.
 */
public class MinesweeperService {
  private final List<MinesweeperListener> listeners;
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

  /**
   * Resets the game.
   */
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
    while (!foundStart) {
      int row = random.nextInt(board.getSize());
      int col = random.nextInt(board.getSize());

      if (map.getCell(row, col) == MinesweeperTile.OPEN) {
        board.setCell(row, col, MinesweeperTile.START);
        foundStart = true;
      }
    }
    status = firstMove ? "Select the green X to start!" :
        countFlags + "/" + MinesweeperConfig.BOMB_COUNT;
    notifyListeners(listener -> listener.updateStatus(status));
    notifyListeners(listener -> listener.updateBoard(board));
  }

  /**
   * Adds a listener.
   *
   * @param listener The listener to add.
   */
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

  public MinesweeperBoard getMap() {
    return map;
  }

  public String getStatus() {
    return status;
  }

  /**
   * Opens a cell.
   *
   * @param row    The row of the cell.
   * @param column The column of the cell.
   */
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

    if (board.getCell(row, column) != MinesweeperTile.CLOSED
        && board.getCell(row, column) != MinesweeperTile.START) {
      uncoverIfFlagged(row, column);
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

  private void uncoverIfFlagged(int row, int column) {
    if (outOfBounds(row, column)) {
      return;
    }

    ArrayList<Vector<Integer>> illegal = new ArrayList<Vector<Integer>>();
    int countFlags = 0;
    int countBombs = 0;

    if (!outOfBounds(row - 1, column)) {
      if (map.getCell(row - 1, column) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row - 1, column) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row - 1);
          v.add(column);
          illegal.add(v);
        }
      } else if (board.getCell(row - 1, column) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (!outOfBounds(row + 1, column) ) {
      if (map.getCell(row + 1, column) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row + 1, column) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row + 1);
          v.add(column);
          illegal.add(v);
        }
      } else if (board.getCell(row + 1, column) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (!outOfBounds(row, column - 1) ){
      if (map.getCell(row, column - 1) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row, column - 1) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row);
          v.add(column - 1);
          illegal.add(v);
        }
      } else if (board.getCell(row, column - 1) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (!outOfBounds(row, column + 1) ){
      if (map.getCell(row, column + 1) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row, column + 1) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row);
          v.add(column + 1);
          illegal.add(v);
        }
      } else if (board.getCell(row, column + 1) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (!outOfBounds(row - 1, column - 1) ){
      if (map.getCell(row - 1, column - 1) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row - 1, column - 1) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row - 1);
          v.add(column - 1);
          illegal.add(v);
        }
      } else if (board.getCell(row - 1, column - 1) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (!outOfBounds(row + 1, column - 1) ){
      if (map.getCell(row + 1, column - 1) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row + 1, column - 1) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row + 1);
          v.add(column - 1);
          illegal.add(v);
        }
      } else if (board.getCell(row + 1, column - 1) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (!outOfBounds(row - 1, column + 1) ){
      if (map.getCell(row - 1, column + 1) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row - 1, column + 1) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row - 1);
          v.add(column + 1);
          illegal.add(v);
        }
      } else if (board.getCell(row - 1, column + 1) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (!outOfBounds(row + 1, column + 1) ){
      if (map.getCell(row + 1, column + 1) == MinesweeperTile.BOMB) {
        countBombs++;
        if (board.getCell(row + 1, column + 1) == MinesweeperTile.FLAG) {
          countFlags++;
        } else {
          Vector<Integer> v = new Vector<Integer>();
          v.add(row + 1);
          v.add(column + 1);
          illegal.add(v);
        }
      } else if (board.getCell(row + 1, column + 1) == MinesweeperTile.FLAG) {
        countFlags++;
      }
    }

    if (countFlags < countBombs || countFlags > countBombs) {
      return;
    }

    if (illegal.isEmpty()) {
      uncoverAll(row, column);
    } else {
      for (Vector<Integer> v : illegal) {
        board.setCell(v.firstElement(), v.lastElement(), MinesweeperTile.BOMBSELECTED);
      }
      gameOver = true;
      gameOver();
    }
    notifyListeners(listener -> listener.updateBoard(board));
  }

  private void uncover(int row, int column) {
    if (outOfBounds(row, column)) {
      return;
    }

    if (board.getCell(row, column) != MinesweeperTile.CLOSED
        || board.getCell(row, column) == MinesweeperTile.FLAG
        || map.getCell(row, column) == MinesweeperTile.BOMB) {
      if (board.getCell(row, column) != MinesweeperTile.START) {
        return;
      }
    }

    MinesweeperTile tile = map.getCell(row, column);
    board.setCell(row, column, tile);
    if (tile == MinesweeperTile.OPEN) {
      uncoverAll(row, column);
    }
  }

  private void uncoverAll(int row, int column) {
    uncover(row - 1, column);
    uncover(row + 1, column);
    uncover(row, column - 1);
    uncover(row, column + 1);
    uncover(row - 1, column - 1);
    uncover(row + 1, column - 1);
    uncover(row - 1, column + 1);
    uncover(row + 1, column + 1);
  }

  private void checkForWin() {
    int countOpen = 0;

    for (int i = 0; i < board.getSize(); i++) {
      for (int j = 0; j < board.getSize(); j++) {
        if (board.getCell(i, j) != MinesweeperTile.CLOSED
        && board.getCell(i, j) != MinesweeperTile.FLAG) {
          countOpen++;
        }
      }
    }

    int fieldSize = MinesweeperConfig.MAP_SIZE * MinesweeperConfig.MAP_SIZE;
    if (countOpen == fieldSize - MinesweeperConfig.BOMB_COUNT) {
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

  /**
   * Flags a cell.
   *
   * @param row    The row of the cell.
   * @param column The column of the cell.
   */
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

    checkForWin();
    status = countFlags + "/" + MinesweeperConfig.BOMB_COUNT;
    notifyListeners(listener -> listener.updateStatus(status));
    notifyListeners(listener -> listener.updateBoard(board));
  }
}
