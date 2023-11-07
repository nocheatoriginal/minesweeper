import java.util.Random;

public class RandomLevel
{
    private Tiles[][] board;
    private int x;
    private int numBombs;
    public RandomLevel(int x)
    {
        this.board = generate(x);
    }

    private Tiles[][] generate(int x)
    {
        this.x = x;
        Tiles[][] board = new Tiles[x][x];
        double bombs = x * x / 6.4;
        this.numBombs = (int)bombs;
        Random random = new Random();
        for (int i = 0; i < numBombs; i++)
        {
            int row, col;
            do
            {
                row = random.nextInt(x);
                col = random.nextInt(x);
            } while (board[row][col] == Tiles.BOMB);
            board[row][col] = Tiles.BOMB;
        }
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < x; j++)
            {
                if (board[i][j] != Tiles.BOMB)
                {
                    int count = 0;
                    for (int dx = -1; dx <= 1; dx++)
                    {
                        for (int dy = -1; dy <= 1; dy++)
                        {
                            int newRow = i + dx;
                            int newCol = j + dy;
                            if (newRow >= 0 && newRow < x && newCol >= 0 && newCol < x && board[newRow][newCol] == Tiles.BOMB)
                            {
                                count++;
                            }
                        }
                    }
                    Tiles t;
                    switch (count)
                    {
                        case 0 -> t = Tiles.OPEN;
                        case 1 -> t = Tiles.ONE;
                        case 2 -> t = Tiles.TWO;
                        case 3 -> t = Tiles.THREE;
                        case 4 -> t = Tiles.FOUR;
                        case 5 -> t = Tiles.FIVE;
                        case 6 -> t = Tiles.SIX;
                        case 7 -> t = Tiles.SEVEN;
                        case 8 -> t = Tiles.EIGHT;
                        default -> t = Tiles.OPEN;
                    }
                    board[i][j] = t;
                }
            }
        }

        int i = x-1;

        if ((board[0][0] == Tiles.BOMB && board[0][1] == Tiles.BOMB && board[1][0] == Tiles.BOMB && board[1][0] == Tiles.BOMB && board[1][1] == Tiles.BOMB) ||
        (board[i][0] == Tiles.BOMB && board[i-1][0] == Tiles.BOMB && board[i][1] == Tiles.BOMB && board[i-1][1] == Tiles.BOMB) ||
        (board[0][i] == Tiles.BOMB && board[0][i-1] == Tiles.BOMB && board[1][i] == Tiles.BOMB && board[1][i-1] == Tiles.BOMB) ||
        (board[i][i] == Tiles.BOMB && board[i-1][i] == Tiles.BOMB && board[i][i-1] == Tiles.BOMB && board[i-1][i-1] == Tiles.BOMB))
        {
            board = generate(x);
        }

        return board;
    }

    public Tiles[][] getBoard()
    {
        return this.board;
    }

    public Tiles[][] getNewLevel()
    {
        Tiles[][] level = new Tiles[x][x];
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < x; j++)
            {
                level[i][j] = Tiles.CLOSED;
            }
        }

        Random random = new Random();
        boolean foundStart = false;
        while (!foundStart)
        {
            int row = random.nextInt(x);
            int col = random.nextInt(x);

            if (board[row][col] == Tiles.OPEN)
            {
                level[row][col] = Tiles.START;
                foundStart = true;
            }
        }
        return level;
    }

    public int getNumberOfBombs()
    {
        return this.numBombs;
    }
}