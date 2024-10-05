package minesweeper.config;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Configuration class for Minesweeper.
 */
public class MinesweeperConfig {
  private static final double multiplier = 1.0;
  public static final int SPRITE_SIZE = (int) (44 / multiplier);
  public static final int MAP_SIZE = (int) (16 * multiplier);
  public static final int BOMB_COUNT = (int) (40 * multiplier);
  public static final boolean NO_GUESSING_MODE = true;
  public static final Color BACKGROUND_COLOR = Color.web("#c0c0c0");
  public static final Color STATUS_COLOR = Color.web("#808080");
  public static final Font FONT = Font.loadFont(MinesweeperConfig.class
      .getResourceAsStream("/fonts/mine-sweeper.ttf"), 20);
  public static OperatingMode MODE = OperatingMode.PRODUCTION;
}