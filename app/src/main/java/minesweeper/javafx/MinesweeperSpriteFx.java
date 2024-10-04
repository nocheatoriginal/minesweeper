package minesweeper.javafx;

import java.io.InputStream;
import javafx.scene.image.Image;
import minesweeper.config.MinesweeperConfig;

/**
 * Minesweeper sprite class for JavaFX.
 */
public class MinesweeperSpriteFx extends Image {
  public MinesweeperSpriteFx(InputStream spriteName) {
    super(spriteName, MinesweeperConfig.SPRITE_SIZE, MinesweeperConfig.SPRITE_SIZE, false, false);
  }
}