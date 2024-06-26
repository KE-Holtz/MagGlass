 

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageBuffer {
  private final BufferedImage image;
  private final Graphics2D    graphics;

  public ImageBuffer(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    graphics = image.createGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  }

  public BufferedImage getImage() {
    return image;
  }

  public Graphics2D getGraphics() {
    return graphics;
  }
}
