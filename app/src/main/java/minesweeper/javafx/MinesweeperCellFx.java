package minesweeper.javafx;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import minesweeper.provider.MinesweeperTile;

/**
 * Minesweeper cell class for JavaFX.
 */
public class MinesweeperCellFx extends Pane {
  private final ImageView imageView;
  private final MinesweeperTile tile;
  private final int xpos;
  private final int ypos;

  /**
   * Creates a new Minesweeper cell.
   *
   * @param tile The tile.
   * @param xpos The x position.
   * @param ypos The y position.
   */
  public MinesweeperCellFx(MinesweeperTile tile, int xpos, int ypos) {
    super();
    this.tile = tile;
    this.xpos = xpos;
    this.ypos = ypos;
    imageView = new ImageView(tile.getSprite());
    this.getChildren().add(imageView);
  }

  public MinesweeperTile getCell() {
    return tile;
  }

  public void setCell(MinesweeperTile tile) {
    imageView.setImage(tile.getSprite());
  }

  public int getX() {
    return xpos;
  }

  public int getY() {
    return ypos;
  }
}
