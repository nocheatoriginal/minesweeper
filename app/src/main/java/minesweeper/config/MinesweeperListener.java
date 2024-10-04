package minesweeper.config;

import minesweeper.provider.MinesweeperBoard;

/**
 * Listener interface for Minesweeper.
 */
public interface MinesweeperListener {
  void updateBoard(MinesweeperBoard board);

  void updateStatus(String status);
}
