package minesweeper.provider;

import minesweeper.javafx.MinesweeperSpriteFx;

/**
 * Minesweeper tile enumeration.
 */
public enum MinesweeperTile {
  OPEN("open.png"),
  CLOSED("closed.png"),
  FLAG("flag.png"),
  WRONGFLAG("wrongflag.png"),
  BOMB("bomb.png"),
  BOMBSELECTED("bombSelected.png"),
  WRONGBOMB("wrongbomb.png"),
  ONE("1.png"),
  TWO("2.png"),
  THREE("3.png"),
  FOUR("4.png"),
  FIVE("5.png"),
  SIX("6.png"),
  SEVEN("7.png"),
  EIGHT("8.png"),
  START("start.png");

  private final MinesweeperSpriteFx sprite;

  MinesweeperTile(String path) {
    sprite = new MinesweeperSpriteFx(getClass().getResourceAsStream("/sprites/" + path));
  }

  /**
   * Returns the sprite.
   *
   * @return The sprite.
   */
  public MinesweeperSpriteFx getSprite() {
    return sprite;
  }

  /**
   * Checks if the tile is equal to another tile.
   *
   * @param other The other tile.
   * @return True if the tiles are equal, false otherwise.
   */
  public boolean equals(MinesweeperTile other) {
    if (other == null) {
      return false;
    }
    return sprite.equals(other.sprite);
  }
}
