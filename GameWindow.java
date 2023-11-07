import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * (c) 2023 nocheatoriginal
 * 
 * Eine implementierung der graphischen Oberfläche
 * für das Spiel Minesweeper!
 */


public class GameWindow extends JFrame
{
    private GraphPanel graphPanel;
    private Map map;
    private final Controller controller;
    private final String title = "Minesweeper";
    private static int size;

    public GameWindow(Controller controller, int spriteSize)
    {
        size = spriteSize;
        setPreferredSize(new Dimension(16 * spriteSize, 20 * spriteSize));

        if (System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            this.setIconImage(
                new ImageIcon(getClass().getClassLoader().getResource("sprites/windows.png")).getImage()
            );
        }
        else
        {
            this.setIconImage(
                new ImageIcon(getClass().getClassLoader().getResource("sprites/minimize.png")).getImage()
            );
        }

        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        map = new Map(16, 16, size);
        panel.add(map);
        add(panel);
        this.controller = controller;
        pack();
        setVisible(true);
        initializeUI(spriteSize);
        this.requestFocus();
    }

    private void initializeUI(int spriteSize) {
        setTitle(title);
        setSize(16 * spriteSize, 16 * spriteSize + 5 + (spriteSize / 2));
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            setSize((17 * spriteSize) - 29, (17 * spriteSize) - 8);
        }


        graphPanel = new GraphPanel();

        graphPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                /*
                int x = e.getX();
                int y = e.getY();
                // System.out.println("Left mouse button clicked at coordinates: (" + x + ", " + y + ")");

                int cellSize = 96 / 2;
                int row = y / cellSize;
                int column = x / cellSize;

                if (row >= 0 && row < 8 && column >= 0 && column < 8)
                {
                    controller.select(row, column);
                }


                graphPanel.setCoordinates(x, y);
                graphPanel.repaint();
                */
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int x = e.getX();
                    int y = e.getY();

                    int cellSize = spriteSize;
                    int row = y / cellSize;
                    int column = x / cellSize;

                    if (row >= 0 && row < 16 && column >= 0 && column < 16)
                    {
                        controller.setFlag(row, column);
                    }
                    graphPanel.setCoordinates(x, y);
                    graphPanel.repaint();
                }
                else
                {
                    int x = e.getX();
                    int y = e.getY();

                    int cellSize = 94 / 2;
                    int row = y / cellSize;
                    int column = x / cellSize;

                    if (row >= 0 && row < 16 && column >= 0 && column < 16)
                    {
                        controller.select(row, column);
                    }
                    graphPanel.setCoordinates(x, y);
                    graphPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Not needed for this example
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Not needed for this example
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Not needed for this example
            }
        });

        add(graphPanel);
        setLocationRelativeTo(null);
    }


    public Map getMap()
    {
        return this.map;
    }

    private static class GraphPanel extends JPanel
    {

        public GraphPanel()
        {
            setOpaque(false);
        }
        private int xCoordinate;
        private int yCoordinate;

        public void setCoordinates(int x, int y) {
            xCoordinate = x;
            yCoordinate = y;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int width = getWidth();
            int height = getHeight();

            // Draw the coordinates as a point
            g.setColor(Color.RED);
            g.fillOval(xCoordinate - 5, yCoordinate - 5, 10, 10);

            // Draw the x and y values
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("X: " + xCoordinate, 20 * size, height - 30);
            g.drawString("Y: " + yCoordinate, 20 * size, height - 15);
        }
    }
}
