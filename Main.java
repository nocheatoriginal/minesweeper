import javax.swing.JFrame;

public class Main
{

    private static final int spriteSize = 47; // (94 / 2);

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        Controller controller = new Controller(spriteSize);

        int delay = 10; // 16; // Millisekunden 1000 / 16 = (ca. 60 FPS)

        while (true)
        {
            long startTime = System.currentTimeMillis();

            controller.update();
            controller.render();

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            long sleepTime = delay - elapsedTime;
            if (sleepTime > 0)
            {
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}