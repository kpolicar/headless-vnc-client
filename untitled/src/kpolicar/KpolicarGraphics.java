package kpolicar;

import ghostawt.image.GGraphics2D;
import ghostawt.image.GGraphicsConfiguration;
import ghostawt.image.GGraphicsDevice;
import sun.awt.image.ToolkitImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class KpolicarGraphics extends GGraphics2D {
    String path;
    public KpolicarGraphics(String s) {
        super(new GGraphicsConfiguration(new GGraphicsDevice()));
        path = s;
    }

    long previous = 0;
    long rantimes = 0;

    static BufferedImage fullBufferedImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        if (System.currentTimeMillis() - previous > 1000) {
            System.out.println("1 second");
            previous = System.currentTimeMillis();
        } else {
            return false;
        }

        ToolkitImage tk = (ToolkitImage) img;
        BufferedImage buffered = tk.getBufferedImage();
        if (buffered == null) {
            return false;
        }

        WritableRaster wr = fullBufferedImage
                .getSubimage(x,y,width, height)
                .getRaster();
        buffered.copyData(wr);

        System.out.println("PRINTING TO FILE "+path);
        File f = new File(path);
        try {
            ImageIO.write(fullBufferedImage, "png", f);
        } catch (IOException e) {
            System.out.println("no? ");
        }

        rantimes++;
        return false;

    }

    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return drawImage(img, x, y, img.getWidth(observer), img.getHeight(observer), observer);
    }
}
