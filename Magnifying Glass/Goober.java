import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.management.Query;

public class Goober {
    public ArrayList<BufferedImage> frames = new ArrayList<>();
    public Queue<int[]> movementQueue = new LinkedList<>();// 0 - DeltaX 1 - DeltaY 2 - Time milis
    public int x;
    public int y;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public Goober() {
        File[] files = new File("Goobers").listFiles();
        File file = files[(int) (Math.random() * files.length)];
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                try {
                    frames.add(ImageIO.read(f));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            try {
                frames.add(ImageIO.read(file));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        x = (int) (Math.random() * (screenSize.getWidth() - frames.get(0).getWidth(null) - 100) + 100);
        y = (int) (Math.random() * (screenSize.getHeight() - frames.get(0).getHeight(null) - 100) + 100);
    }

    public void queueMove(int[] movement) {
        movementQueue.add(movement);
    }

    public void move() {

    }
}