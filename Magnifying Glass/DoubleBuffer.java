 

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.swing.JComponent;

public class DoubleBuffer extends JComponent {
  private transient ImageBuffer visibleBuffer;
  private transient ImageBuffer drawingBuffer;

  public DoubleBuffer(int width, int height) {
    visibleBuffer = new ImageBuffer(width, height);
    drawingBuffer = new ImageBuffer(width, height);
    setPreferredSize(new Dimension(width, height));
  }

  public void frameSync(int framesPerSecond, Consumer<Graphics2D> renderer) {
    long nanosPerFrame = TimeUnit.SECONDS.toNanos(1) / framesPerSecond;
    long targetTime = System.nanoTime();

    while (true) {
      long currentTime;
      do {
        currentTime = System.nanoTime();
        long sleepTime = TimeUnit.NANOSECONDS.toMillis(targetTime - currentTime);
        if (sleepTime <= 0) continue;

        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } while (targetTime - currentTime > 0);

      renderer.accept(getGraphics());
      swapBuffers();
      targetTime += nanosPerFrame;
    }
  }

  public void swapBuffers() {
    ImageBuffer tmp = visibleBuffer;
    visibleBuffer = drawingBuffer;
    drawingBuffer = tmp;

    repaint();
  }

  @Override
  public Graphics2D getGraphics() {
    return drawingBuffer.getGraphics();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(visibleBuffer.getImage(), 0, 0, this);
  }
}
