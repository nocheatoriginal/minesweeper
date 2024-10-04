package minesweeper.config;

import minesweeper.provider.MinesweeperBoard;

public interface MinesweeperListener {
  void updateBoard(MinesweeperBoard board);
  void updateStatus(String status);
}
