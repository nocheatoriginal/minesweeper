import javax.swing.*;
import javax.swing.text.StyledDocument;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (c) 2023 nocheatoriginal
 * Diese Klasse implementiert die
 * Logik des Spiels Minesweeper!
 */

public class Controller
{
    private GameWindow window;
    private RandomLevel level;
    private boolean firstMove;
    private boolean gameOver;
    private boolean won;
    private int size;
    private int count;

    public Controller(int spriteSize)
    {
        window = new GameWindow(this, spriteSize);
        restart();
    }

    public void restart()
    {
        window.setTitle("Minesweeper - Select the green X to start!");
        this.firstMove = true;
        this.gameOver = false;
        this.won = false;
        this.size = 16;
        this.level = new RandomLevel(size);
        this.count = 0;
        window.getMap().clear();
        window.getMap().setAllTiles(level.getNewLevel());

        /*
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                System.out.print(level.getBoard()[i][j] + ", ");
            }
            System.out.print("\n");
        }
        */
    }

    public void select(int x, int y)
    {  
        if (firstMove && window.getMap().getOverlay(x, y) != Tiles.START)
        {
            return;
        }
        firstMove = false;


        if (gameOver)
        {
            if (window.getMap().getOverlay(x, y) == Tiles.BOMBSELECTED)
            {
                this.restart();
            }
            return;
        }

        if (won)
        {
            if (window.getMap().getOverlay(x, y) == Tiles.FLAG)
            {
                this.restart();
            }
            return;
        }

        if (window.getMap().getOverlay(x, y) == Tiles.FLAG)
            return;

        if (window.getMap().getOverlay(x, y) == Tiles.START)
            window.getMap().setOverlay(x, y, Tiles.CLOSED);
        uncover(x, y);
        checkWin();
    }


    public void uncover(int x, int y)
    {
        // Prüfen, ob die übergebene Position (x, y) innerhalb des Spielfelds liegt
        if (x < 0 || y < 0 || x >= level.getBoard().length || y >= level.getBoard()[0].length)
        {
            return;
        }
    
        // Prüfen, ob das Feld bereits geöffnet ist oder mit einer Flagge markiert wurde
        if (window.getMap().getOverlay(x, y) != Tiles.CLOSED && window.getMap().getOverlay(x, y) != Tiles.START)
        {
            return;
        }
    
        Tiles[][] board = level.getBoard();
        Tiles tile = board[x][y];
    
        // Das Feld öffnen und das Overlay setzen
        window.getMap().setOverlay(x, y, tile);
    
        // Wenn das geöffnete Feld eine Bombe ist, ist das Spiel verloren
        if (tile == Tiles.BOMB)
        {
            // Hier kannst du die Verlier-Logik hinzufügen
            // Zum Beispiel das Spiel beenden oder eine Verliermeldung anzeigen
            Tiles[][] flags = window.getMap().getAllTiles();
            
            window.getMap().setAllTiles(level.getBoard());

            for (int i = 0; i < size; i++)
            {
                for (int j = 0; j < size; j++)
                {
                    if (flags[i][j] == Tiles.CLOSED && level.getBoard()[i][j] != Tiles.BOMB)
                    {
                        window.getMap().setOverlay(i, j, Tiles.CLOSED);
                    }
                    
                    if (flags[i][j] == Tiles.FLAG && level.getBoard()[i][j] == Tiles.BOMB)
                    {
                        window.getMap().setOverlay(i, j, Tiles.FLAG);
                    }

                    if (flags[i][j] == Tiles.FLAG && level.getBoard()[i][j] != Tiles.BOMB)
                    {
                        window.getMap().setOverlay(i, j, Tiles.WRONGFLAG);
                    }
                }
            }

            window.getMap().setOverlay(x, y, Tiles.BOMBSELECTED);
            gameOver = true;
            return;
        }
    
        // Wenn das geöffnete Feld keine Bombe ist und keine Nachbarbombe hat, öffnen wir Nachbarfelder
        if (tile == Tiles.OPEN)
        {
            // Prüfen und öffnen der Nachbarfelder oben, unten, links, rechts und diagonal
            uncover(x - 1, y);
            uncover(x + 1, y);
            uncover(x, y - 1);
            uncover(x, y + 1);
            uncover(x - 1, y - 1);
            uncover(x + 1, y - 1);
            uncover(x - 1, y + 1);
            uncover(x + 1, y + 1);
        }
    }

    public void setFlag(int x, int y)
    {
        if (gameOver || firstMove)
            return;


        if (window.getMap().getOverlay(x, y) == Tiles.CLOSED)
        {
            window.getMap().setOverlay(x, y, Tiles.FLAG);
            checkWin();
            return;
        }
        
        if (window.getMap().getOverlay(x, y) == Tiles.FLAG && !won)
            window.getMap().setOverlay(x, y, Tiles.CLOSED);
            checkWin();
    }

    public void checkWin()
    {
        int countFlags = 0;
        int countOpen = 0;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (window.getMap().getOverlay(i, j) == Tiles.FLAG || window.getMap().getOverlay(i, j) == Tiles.CLOSED)
                {
                    countFlags++;
                }
                else
                {
                    countOpen++;
                }
            }
        }

        if (!gameOver && countFlags == level.getNumberOfBombs() && countOpen == (size * size) - countFlags)
        {
            won = true;
        }
    }

    public void update()
    {
        if (!won && !gameOver && !firstMove)
            count++;
    }

    public void render()
    {
        String time = toTime(count);
        if (gameOver && !won)
        {
            window.setTitle("Minesweeper - Game over! Select the red Bomb to restart! Time: " + time);
            return;
        }
        if (won)
        {
            window.setTitle("Minesweeper - You win! Select a Flag to restart! Time: " + time);
            return;
        }
        if (!firstMove)
            window.setTitle("Minesweeper - Time: " + time);
    }

    private String toTime(int c)
    {
        String time = "";
        int t = c;
        int seconds = t / 100; // Hundertstel-Sekunde
        t = t - seconds * 100;
        int minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        if (seconds < 10)
        {
            time = minutes + ":0" + seconds;
        }
        else
        {
            time = minutes + ":" + seconds;
        }

        return time;
    }

}