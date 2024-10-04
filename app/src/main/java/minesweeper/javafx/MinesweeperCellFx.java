package minesweeper.javafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import minesweeper.provider.MinesweeperTile;

public class MinesweeperCellFx extends Pane {
  private ImageView imageView;
  private final MinesweeperTile tile;
  private final int x;
  private final int y;
  public MinesweeperCellFx(MinesweeperTile tile, int x, int y) {
    super();
    this.tile = tile;
    this.x = x;
    this.y = y;
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
    return x;
  }

  public int getY() {
    return y;
  }
}
