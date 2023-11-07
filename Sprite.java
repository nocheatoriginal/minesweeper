import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Sprite
{
    private BufferedImage image;
    private String path;

    public Sprite(String imagePath)
    {
        try
        {
            path = imagePath;
            InputStream img = Sprite.class.getResourceAsStream(path);
            //image = ImageIO.read(new File(imagePath));
            image = ImageIO.read(img);
        }
        catch (IOException e)
        {
            System.out.println(this.getClass().getSimpleName() + ".java: Fehler beim laden eines Sprites (" + imagePath + ")");
            System.out.println("Exception: " + e.toString());
        }
    }

    public String getPath()
    {
        return this.path;
    }

    public void draw(Graphics g, int x, int y, int size)
    {
        g.drawImage(image, x, y, size, size, null);
    }
}
