import javax.swing.*;
import java.awt.*;

public class Map extends JPanel
{
    private int sizeY;
    private int sizeX;
    private int size;
    private Tiles[][] tiles;
    private Tiles[][] overlay;
    public Map(int x, int y, int spriteSize)
    {
        this.sizeY = y;
        this.sizeX = x;
        tiles = new Tiles[x][y];
        overlay = new Tiles[x][y];
        this.size = spriteSize;

        this.setAllTiles(this.setup(x));
    }

    private Tiles[][] setup(int x)
    {
        Tiles[][] setup = new Tiles[x][x];
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < x; j++)
            {
                setup[i][j] = Tiles.CLOSED;
            }
        }
        return setup;
    }

    public int getSizeX()
    {
        return sizeX;
    }

    public int getSizeY()
    {
        return sizeY;
    }

    public int getMapSize()
    {
        return size;
    }

    public Tiles getTile(int x, int y)
    {
        if(x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length)
        {
            return null;
        }
        return tiles[x][y];
    }

    public void setTile(int x, int y, Tiles t)
    {
        tiles[x][y] = t;
        repaint();
    }

    public void clear()
    {
        for(int i = 0; i < tiles.length; i++)
        {
            for(int j = 0; j < tiles[0].length; j++)
            {
                    tiles[i][j] = null;
                    overlay[i][j] = null;
            }
        }
    }

    public void setAllTiles(Tiles[][] t)
    {
        tiles = new Tiles[t.length][t[0].length];
        overlay = new Tiles[t.length][t[0].length];
        sizeX = t.length;
        sizeY = t[0].length;

        for(int i = 0; i < t.length; i++)
        {
            for(int j = 0; j < t[0].length; j++)
            {
                switch(t[i][j])
                {
                    case OPEN, CLOSED, FLAG, BOMB, BOMBSELECTED, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, START, WRONGBOMB ->
                    {
                        overlay[i][j] = t[i][j];
                        tiles[i][j] = Tiles.CLOSED;
                    }
                    default -> throw new IllegalArgumentException("Unexpected value: " + t[i][j]);
                }
            }
        }
        repaint();
    }

    public void setOverlay(int x, int y, Tiles t)
    {
        overlay[x][y] = t;
        repaint();
    }

    public Tiles getOverlay(int x, int y)
    {
        if(x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length)
        {
            return Tiles.CLOSED;
        }
        return overlay[x][y];
    }

    public Tiles[][] getAllTiles()
    {
        return this.overlay;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for(int row = 0; row < sizeX; row++)
        {
            for(int col = 0; col < sizeY; col++)
            {
                int x = col * size;
                int y = row * size;

                if(tiles[row][col] != null)
                {
                    tiles[row][col].getSprite().draw(g, x, y, size);
                }
                /*else if(tiles[row][col] == null) {
                    tiles[row][col] = Tiles.GROUND;
                    tiles[row][col].getSprite().draw(g, x, y, size);
                }*/
                if(overlay[row][col] != null)
                {
                    overlay[row][col].getSprite().draw(g, x, y, size);
                }

                g.setColor(Color.GRAY);
                g.drawRect(x, y, size, size);

            }
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(sizeX * size, sizeY * size);
    }
}
