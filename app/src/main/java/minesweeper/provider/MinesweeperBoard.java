package minesweeper.provider;

import java.util.Random;
import minesweeper.config.MinesweeperConfig;

/**
 * Minesweeper board class.
 */
public class MinesweeperBoard {
  private final int size;
  private MinesweeperTile[][] tiles;

  /**
   * Creates a new Minesweeper board.
   */
  public MinesweeperBoard() {
    size = MinesweeperConfig.MAP_SIZE;
    tiles = new MinesweeperTile[size][size];
    this.reset();
  }

  public int getSize() {
    return size;
  }

  /**
   * Resets the board.
   */
  public void reset() {
    for (int i = 0; i < MinesweeperConfig.MAP_SIZE; i++) {
      for (int j = 0; j < MinesweeperConfig.MAP_SIZE; j++) {
        tiles[i][j] = MinesweeperTile.CLOSED;
      }
    }
  }

  public MinesweeperTile getCell(int row, int column) {
    return tiles[row][column];
  }

  public void setCell(int row, int column, MinesweeperTile tile) {
    tiles[row][column] = tile;
  }

  /**
   * Generates a random board.
   *
   * @return The random board.
   */
  public static MinesweeperBoard generateRandom() {
    MinesweeperBoard randomBoard = new MinesweeperBoard();

    int size = MinesweeperConfig.MAP_SIZE;
    int bombs = MinesweeperConfig.BOMB_COUNT;
    Random random = new Random();

    for (int i = 0; i < bombs; i++) {
      int row;
      int col;
      do {
        row = random.nextInt(size);
        col = random.nextInt(size);
      } while (randomBoard.getCell(row, col) == MinesweeperTile.BOMB);
      randomBoard.setCell(row, col, MinesweeperTile.BOMB);
    }
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (randomBoard.getCell(i, j) != MinesweeperTile.BOMB) {
          int count = 0;
          for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
              int newRow = i + dx;
              int newCol = j + dy;
              if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size
                  && randomBoard.getCell(newRow, newCol) == MinesweeperTile.BOMB) {
                count++;
              }
            }
          }
          
          MinesweeperTile t;
          switch (count) {
            case 1 -> t = MinesweeperTile.ONE;
            case 2 -> t = MinesweeperTile.TWO;
            case 3 -> t = MinesweeperTile.THREE;
            case 4 -> t = MinesweeperTile.FOUR;
            case 5 -> t = MinesweeperTile.FIVE;
            case 6 -> t = MinesweeperTile.SIX;
            case 7 -> t = MinesweeperTile.SEVEN;
            case 8 -> t = MinesweeperTile.EIGHT;
            default -> t = MinesweeperTile.OPEN;
          }
          randomBoard.setCell(i, j, t);
        }
      }
    }

    int i = size - 1;
    if ((randomBoard.getCell(0, 0) == MinesweeperTile.BOMB
        && randomBoard.getCell(0, 1) == MinesweeperTile.BOMB
        && randomBoard.getCell(1, 0) == MinesweeperTile.BOMB
        && randomBoard.getCell(1, 1) == MinesweeperTile.BOMB)
        || (randomBoard.getCell(i, 0) == MinesweeperTile.BOMB
        && randomBoard.getCell(i - 1, 0) == MinesweeperTile.BOMB
        && randomBoard.getCell(i, 1) == MinesweeperTile.BOMB
        && randomBoard.getCell(i - 1, 1) == MinesweeperTile.BOMB)
        || (randomBoard.getCell(0, i) == MinesweeperTile.BOMB
        && randomBoard.getCell(0, i - 1) == MinesweeperTile.BOMB
        && randomBoard.getCell(1, i) == MinesweeperTile.BOMB
        && randomBoard.getCell(1, i - 1) == MinesweeperTile.BOMB)
        || (randomBoard.getCell(i, i) == MinesweeperTile.BOMB
        && randomBoard.getCell(i - 1, i) == MinesweeperTile.BOMB
        && randomBoard.getCell(i, i - 1) == MinesweeperTile.BOMB
        && randomBoard.getCell(i - 1, i - 1) == MinesweeperTile.BOMB)) {
      randomBoard = MinesweeperBoard.generateRandom();
    }

    return randomBoard;
  }
}
