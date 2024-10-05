package minesweeper;

import minesweeper.config.MinesweeperConfig;
import minesweeper.provider.MinesweeperBoard;
import minesweeper.provider.MinesweeperService;
import minesweeper.provider.MinesweeperTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestMinesweeperService {
  MinesweeperService service;

  @BeforeEach
  void setup() {
    service = new MinesweeperService();
  }

  @Test
  void testBoardIsClosed() {
    MinesweeperBoard board = service.getBoard();

    if (MinesweeperConfig.NO_GUESSING_MODE) {
      Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
      assertNotNull(startTile);

      for (int i = 0; i < board.getSize(); i++) {
        for (int j = 0; j < board.getSize(); j++) {
          if (i == startTile.firstElement() && j == startTile.lastElement()) {
            continue;
          }

          assertEquals(MinesweeperTile.CLOSED, board.getCell(i, j));
        }
      }
    } else {
      for (int i = 0; i < board.getSize(); i++) {
        for (int j = 0; j < board.getSize(); j++) {
          assertEquals(MinesweeperTile.CLOSED, board.getCell(i, j));
        }
      }
    }
  }

  private Vector<Integer> findTile(MinesweeperBoard board, MinesweeperTile tile) {
    for (int i = 0; i < board.getSize(); i++) {
      for (int j = 0; j < board.getSize(); j++) {
        if (board.getCell(i, j) == tile) {
          Vector<Integer> result = new Vector<>();
          result.add(i);
          result.add(j);
          return result;
        }
      }
    }
    return null;
  }

  @Test
  void testBoardIsGenerated() {
    MinesweeperBoard board = service.getMap();
    int bombCount = 0;

    for (int i = 0; i < board.getSize(); i++) {
      for (int j = 0; j < board.getSize(); j++) {
        if (board.getCell(i, j) == MinesweeperTile.BOMB) {
          bombCount++;
        }
      }
    }

    assertEquals(MinesweeperConfig.BOMB_COUNT, bombCount);
  }

  @Test
  void testOpenTile() {
    MinesweeperBoard board = service.getBoard();
    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);

    service.open(startTile.firstElement(), startTile.lastElement());
    assertNotEquals(MinesweeperTile.CLOSED, board.getCell(startTile.firstElement(), startTile.lastElement()));
  }

  @Test
  void testFlagTile() {
    MinesweeperBoard board = service.getBoard();
    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);

    service.open(startTile.firstElement(), startTile.lastElement());
    assertNotEquals(MinesweeperTile.CLOSED, board.getCell(startTile.firstElement(), startTile.lastElement()));

    startTile = findTile(board, MinesweeperTile.CLOSED);
    assertNotNull(startTile);

    service.flag(startTile.firstElement(), startTile.lastElement());
    assertEquals(MinesweeperTile.FLAG, board.getCell(startTile.firstElement(), startTile.lastElement()));
  }

  @Test
  void testUnflagTile() {
    MinesweeperBoard board = service.getBoard();
    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);

    service.open(startTile.firstElement(), startTile.lastElement());
    assertNotEquals(MinesweeperTile.CLOSED, board.getCell(startTile.firstElement(), startTile.lastElement()));

    startTile = findTile(board, MinesweeperTile.CLOSED);
    assertNotNull(startTile);

    service.flag(startTile.firstElement(), startTile.lastElement());
    assertEquals(MinesweeperTile.FLAG, board.getCell(startTile.firstElement(), startTile.lastElement()));

    service.flag(startTile.firstElement(), startTile.lastElement());
    assertEquals(MinesweeperTile.CLOSED, board.getCell(startTile.firstElement(), startTile.lastElement()));
  }

  @Test
  void testOpenBomb() {
    MinesweeperBoard map = service.getMap();
    MinesweeperBoard board = service.getBoard();
    Vector<Integer> bombTile = findTile(map, MinesweeperTile.BOMB);
    assertNotNull(bombTile);

    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);
    service.open(startTile.firstElement(), startTile.lastElement());
    service.open(bombTile.firstElement(), bombTile.lastElement());
    assertEquals(MinesweeperTile.BOMBSELECTED, board.getCell(bombTile.firstElement(), bombTile.lastElement()));
  }

  @Test
  void testOpenEmptyTile() {
    MinesweeperBoard board = service.getBoard();
    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);

    service.open(startTile.firstElement(), startTile.lastElement());
    Vector<Integer> emptyTile = findTile(board, MinesweeperTile.OPEN);
    assertNotNull(emptyTile);

    service.open(emptyTile.firstElement(), emptyTile.lastElement());
    assertNotEquals(MinesweeperTile.CLOSED, board.getCell(emptyTile.firstElement(), emptyTile.lastElement()));
  }

  @Test
  void testOpenAfterBombHit() {
    MinesweeperBoard board = service.getBoard();
    MinesweeperBoard map = service.getMap();
    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);
    Vector<Integer> bombTile = findTile(map, MinesweeperTile.BOMB);
    assertNotNull(bombTile);

    service.open(startTile.firstElement(), startTile.lastElement());
    service.open(bombTile.firstElement(), bombTile.lastElement());
    Vector<Integer> emptyTile = findTile(map, MinesweeperTile.OPEN);
    assertNotNull(emptyTile);

    while (board.getCell(emptyTile.firstElement(), emptyTile.lastElement()) != MinesweeperTile.CLOSED) {
      emptyTile = findTile(map, MinesweeperTile.OPEN);
    }
    service.open(emptyTile.firstElement(), emptyTile.lastElement());
    assertEquals(MinesweeperTile.BOMBSELECTED, board.getCell(bombTile.firstElement(), bombTile.lastElement()));
    assertEquals(MinesweeperTile.CLOSED, board.getCell(emptyTile.firstElement(), emptyTile.lastElement()));
  }

  @Test
  void testFlagAfterBombHit() {
    MinesweeperBoard board = service.getBoard();
    MinesweeperBoard map = service.getMap();
    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);
    Vector<Integer> bombTile = findTile(map, MinesweeperTile.BOMB);
    assertNotNull(bombTile);

    service.open(startTile.firstElement(), startTile.lastElement());
    service.open(bombTile.firstElement(), bombTile.lastElement());
    Vector<Integer> closedTile = findTile(board, MinesweeperTile.CLOSED);
    assertNotNull(closedTile);

    while (board.getCell(closedTile.firstElement(), closedTile.lastElement()) != MinesweeperTile.CLOSED) {
      closedTile = findTile(map, MinesweeperTile.CLOSED);
    }
    service.flag(closedTile.firstElement(), closedTile.lastElement());
    assertEquals(MinesweeperTile.BOMBSELECTED, board.getCell(bombTile.firstElement(), bombTile.lastElement()));
    assertEquals(MinesweeperTile.CLOSED, board.getCell(closedTile.firstElement(), closedTile.lastElement()));
  }

  @Test
  void testWinConditionAndReset() {
    MinesweeperBoard board = service.getBoard();
    MinesweeperBoard map = service.getMap();
    Vector<Integer> startTile = findTile(board, MinesweeperTile.START);
    assertNotNull(startTile);
    service.open(startTile.firstElement(), startTile.lastElement());

    for (int i = 0; i < map.getSize(); i++) {
      for (int j = 0; j < map.getSize(); j++) {
        if (map.getCell(i, j) == MinesweeperTile.BOMB) {
          service.flag(i, j);
        } else {
          service.open(i, j);
        }
      }
    }

    assertEquals("Select a flag to play again!", service.getStatus());
    service.reset();
    if (MinesweeperConfig.NO_GUESSING_MODE) {
      assertEquals("Select the green X to start!", service.getStatus());
    } else {
      assertEquals(MinesweeperConfig.BOMB_COUNT + "/" + MinesweeperConfig.BOMB_COUNT,
          service.getStatus());
    }
  }
}
