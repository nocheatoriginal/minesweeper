package minesweeper.javafx;

import javafx.scene.image.Image;
import minesweeper.config.MinesweeperConfig;

import java.io.InputStream;

public class MinesweeperSpriteFx extends Image {

  public MinesweeperSpriteFx(InputStream spriteName) {
    super(spriteName, MinesweeperConfig.SPRITE_SIZE, MinesweeperConfig.SPRITE_SIZE, false, false);
  }
}