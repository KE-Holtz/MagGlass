import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    // I'm so sorry Xander
    // Its horrible I know
    // Actually Mr. Wuest im sorry too its all so bad
    public static Image foreground;
    public static Image background;
    public static JFrame redWindow;
    public static JFrame greenWindow;
    public static JFrame blueWindow;
    public static JFrame greyWindow;
    public static JFrame inverseWindow;
    public static BufferedImage redImage;
    public static BufferedImage greenImage;
    public static BufferedImage blueImage;
    public static BufferedImage greyImage;
    public static BufferedImage inverseImage;
    public static ArrayList<Goober> goobers;
    static {
        try {
            foreground = ImageIO.read(new File(
                    "Magnifying Glasss\\MagGlassTrans.png"));
            background = ImageIO.read(
                    new File("Magnifying Glasss\\Background.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            foreground = null;
            background = null;
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        goobers = new ArrayList<>();
        for (int i = 0; i < Math.random() * 9 + 6; i++) {
            goobers.add(new Goober());
        }
        redWindow = new JFrame("Red");
        DoubleBuffer redBuffer = new DoubleBuffer(200, 200);
        redWindow.add(redBuffer);
        redWindow.pack();
        redWindow.setVisible(true);
        redImage = new BufferedImage(redBuffer.getWidth(), redBuffer.getHeight(), BufferedImage.TYPE_INT_ARGB);

        greenWindow = new JFrame("Green");
        DoubleBuffer greenBuffer = new DoubleBuffer(200, 200);
        greenWindow.add(greenBuffer);
        greenWindow.pack();
        greenWindow.setVisible(true);
        greenImage = new BufferedImage(greenBuffer.getWidth(), greenBuffer.getHeight(), BufferedImage.TYPE_INT_ARGB);

        blueWindow = new JFrame("Blue");
        DoubleBuffer blueBuffer = new DoubleBuffer(200, 200);
        blueWindow.add(blueBuffer);
        blueWindow.pack();
        blueWindow.setVisible(true);
        blueImage = new BufferedImage(blueBuffer.getWidth(), blueBuffer.getHeight(), BufferedImage.TYPE_INT_ARGB);

        greyWindow = new JFrame("Grey");
        DoubleBuffer greyBuffer = new DoubleBuffer(200, 200);
        greyWindow.add(greyBuffer);
        greyWindow.pack();
        greyWindow.setVisible(true);
        greyImage = new BufferedImage(greyBuffer.getWidth(), greyBuffer.getHeight(), BufferedImage.TYPE_INT_ARGB);

        inverseWindow = new JFrame("Inverse");
        DoubleBuffer inverseBuffer = new DoubleBuffer(200, 200);
        inverseWindow.add(inverseBuffer);
        inverseWindow.pack();
        inverseWindow.setVisible(true);
        inverseImage = new BufferedImage(inverseBuffer.getWidth(), inverseBuffer.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        // JFrame window = new JFrame("Mag");
        // DoubleBuffer buffer = new DoubleBuffer(200, 200);
        // window.add(buffer);
        // window.setVisible(true);
        // window.pack();

        ArrayList<BiConsumer<Graphics2D, JFrame>> renderers = new ArrayList<>();
        // renderers.add(Main::render);
        renderers.add(Main::redRender);
        renderers.add(Main::greenRender);
        renderers.add(Main::blueRender);
        renderers.add(Main::greyRender);
        renderers.add(Main::inverseRender);
        DoubleBuffer[] doubleBuffers = { /* buffer, */ redBuffer, greenBuffer, blueBuffer, greyBuffer, inverseBuffer};
        frameSync(60, renderers, doubleBuffers);

    }

    public static void frameSync(int framesPerSecond, ArrayList<BiConsumer<Graphics2D, JFrame>> renderers,
            DoubleBuffer[] doubleBuffers) {
        long nanosPerFrame = TimeUnit.SECONDS.toNanos(1) / framesPerSecond;
        long targetTime = System.nanoTime();

        while (true) {
            long currentTime;
            do {
                currentTime = System.nanoTime();
                long sleepTime = TimeUnit.NANOSECONDS.toMillis(targetTime - currentTime);
                if (sleepTime <= 0)
                    continue;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (targetTime - currentTime > 0);
            for (int i = 0; i < doubleBuffers.length; i++) {
                renderers.get(i).accept(doubleBuffers[i].getGraphics(),
                        (JFrame) SwingUtilities.windowForComponent(doubleBuffers[i]));
            }
            for (DoubleBuffer doubleBuffer : doubleBuffers) {
                doubleBuffer.swapBuffers();
            }
            targetTime += nanosPerFrame;
        }
    }

    public static void render(Graphics2D g, JFrame frame) {
        drawBG(g);
        drawGoobers(frame, g, goobers);
        drawMagGlass(g);
    }

    public static void redRender(Graphics2D g, JFrame frame) {
        redDrawBg();
        redAddGoobers(frame);
        overlapRGB(frame, greenWindow, 0x00FF00FF, 0xFF00FF00, redImage, greenImage);
        overlapRGB(frame, blueWindow, 0x00FFFF00, 0xFF0000FF, redImage, blueImage);
        overlapGrey(frame, redImage, false);
        overlapinverse(frame, redImage, false);
        g.drawImage(redImage, 0, 0, null);
    }

    public static void greenRender(Graphics2D g, JFrame frame) {
        greenDrawBg();
        greenAddGoobers(frame);
        overlapRGB(frame, blueWindow, 0x00FFFF00, 0xFF0000FF, greenImage, blueImage);
        overlapRGB(frame, redWindow, 0x0000FFFF, 0xFFFF0000, greenImage, redImage);
        overlapGrey(frame, greenImage, false);
        overlapinverse(frame, greenImage, false);
        g.drawImage(greenImage, 0, 0, null);
    }

    public static void blueRender(Graphics2D g, JFrame frame) {
        blueDrawBg();
        blueAddGoobers(frame);
        overlapRGB(frame, redWindow, 0x0000FFFF, 0xFFFF0000, blueImage, redImage);
        overlapRGB(frame, greenWindow, 0x00FF00FF, 0xFF00FF00, blueImage, greenImage);
        overlapGrey(frame, blueImage, false);
        overlapinverse(frame, blueImage, false);
        g.drawImage(blueImage, 0, 0, null);
    }

    public static void greyRender(Graphics2D g, JFrame frame) {
        greyDrawBg();
        greyAddGoobers(frame);
        overlapGrey(redWindow, redImage, true);
        overlapGrey(greenWindow, greenImage, true);
        overlapGrey(blueWindow, blueImage, true);
        overlapinverse(frame, greyImage, false);
        g.drawImage(greyImage, 0, 0, frame);
    }

    public static void inverseRender(Graphics2D g, JFrame frame){
        inverseDrawBg();
        inverseAddGoobers(frame);
        overlapinverse(redWindow, redImage, true);
        overlapinverse(greenWindow, greenImage, true);
        overlapinverse(blueWindow, blueImage, true);
        overlapinverse(greyWindow, greyImage, true);
        g.drawImage(inverseImage, 0, 0, null);
    }

    public static void overlapRGB(JFrame recieverWindow, JFrame donorWindow, int recieverMask, int donorMask,
            BufferedImage receiverImage, BufferedImage donorImage) {
        for (int i = 0; i < donorImage.getWidth(); i++) {
            for (int j = 0; j < donorImage.getHeight(); j++) {
                int px = donorWindow.getX() + i - recieverWindow.getX();
                int py = donorWindow.getY() + j - recieverWindow.getY();
                if ((px >= 0) && (px < redImage.getWidth()) && (py >= 0) && (py < redImage.getHeight())) {
                    int pColor = (receiverImage.getRGB(px, py) & recieverMask)
                            | (donorImage.getRGB(i, j) & donorMask);
                    receiverImage.setRGB(px, py, pColor);
                }
            }
        }
    }

    public static void overlapGrey(JFrame donorWindow, BufferedImage donorImage, boolean amGrey) {
        for (int i = 0; i < donorImage.getWidth(); i++) {
            for (int j = 0; j < donorImage.getHeight(); j++) {
                int px = donorWindow.getX() + i - greyWindow.getX();
                int py = donorWindow.getY() + j - greyWindow.getY();
                Color pColor = new Color(donorImage.getRGB(i, j));
                if ((px >= 0) && (px < redImage.getWidth()) && (py >= 0) && (py < redImage.getHeight())) {
                    float[] pHSV = new float[3];
                    Color.RGBtoHSB(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), pHSV);
                    if (amGrey) {
                        greyImage.setRGB(px, py, Color.HSBtoRGB(pHSV[0], 0, pHSV[2]));
                    } else {
                        donorImage.setRGB(i, j, Color.HSBtoRGB(pHSV[0], 0, pHSV[2]));
                    }
                }
            }
        }
    }

    public static void overlapinverse(JFrame donorWindow, BufferedImage donorImage, boolean amInverse) {
        for (int i = 0; i < donorImage.getWidth(); i++) {
            for (int j = 0; j < donorImage.getHeight(); j++) {
                int px = donorWindow.getX() + i - inverseWindow.getX();
                int py = donorWindow.getY() + j - inverseWindow.getY();
                Color pColor = new Color(donorImage.getRGB(i, j));
                if ((px >= 0) && (px < redImage.getWidth()) && (py >= 0) && (py < redImage.getHeight())) {
                    if (amInverse) {
                        inverseImage.setRGB(px, py, new Color(pColor.getRed(), pColor.getGreen(), pColor.getBlue()).getRGB());
                    } else {
                        donorImage.setRGB(i, j, new Color(255-pColor.getRed(), 255 - pColor.getGreen(), 255 - pColor.getBlue()).getRGB());
                    }
                }
            }
        }
    }

    public static void redDrawBg() {
        for (int i = 0; i < redImage.getWidth(); i++) {
            for (int j = 0; j < redImage.getHeight(); j++) {
                redImage.setRGB(i, j, new Color(255, 0, 0, 255).getRGB());
            }
        }
    }

    public static void greenDrawBg() {
        for (int i = 0; i < greenImage.getWidth(); i++) {
            for (int j = 0; j < greenImage.getHeight(); j++) {
                greenImage.setRGB(i, j, new Color(0, 255, 0, 255).getRGB());
            }
        }
    }

    public static void blueDrawBg() {
        for (int i = 0; i < blueImage.getWidth(); i++) {
            for (int j = 0; j < blueImage.getHeight(); j++) {
                blueImage.setRGB(i, j, new Color(0, 0, 255, 255).getRGB());
            }
        }
    }

    public static void greyDrawBg() {
        for (int i = 0; i < greyImage.getWidth(); i++) {
            for (int j = 0; j < greyImage.getHeight(); j++) {
                greyImage.setRGB(i, j, Color.HSBtoRGB((float) 0, (float) 0, (float) .8));
            }
        }
    }

    public static void inverseDrawBg(){
        for (int i = 0; i < inverseImage.getWidth(); i++) {
            for (int j = 0; j < inverseImage.getHeight(); j++) {
                inverseImage.setRGB(i, j, Color.HSBtoRGB((float) 0, (float) 0, (float) 0));
            }
        }
    }

    public static void redAddGoobers(JFrame frame) {
        BufferedImage img;
        for (Goober goober : goobers) {
            img = goober.frames.get((int) (System.currentTimeMillis() / 250 %
                    goober.frames.size()));
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    int px = goober.x + i - frame.getX();
                    int py = goober.y + j - frame.getY();
                    Color pColor = new Color(img.getRGB(i, j), true);
                    if ((px >= 0) && (px < redImage.getWidth()) && (py >= 0) && (py < redImage.getHeight())
                            && (pColor.getAlpha() > 0)) {
                        Color startColor = new Color(redImage.getRGB(px, py), true);
                        redImage.setRGB(px, py, new Color(pColor.getRed(), startColor.getGreen(),
                                startColor.getBlue(), pColor.getAlpha()).getRGB());
                    }
                }
            }
        }
    }

    public static void greenAddGoobers(JFrame frame) {
        BufferedImage img;
        for (Goober goober : goobers) {
            img = goober.frames.get((int) (System.currentTimeMillis() / 250 %
                    goober.frames.size()));
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    int px = goober.x + i - frame.getX();
                    int py = goober.y + j - frame.getY();
                    Color pColor = new Color(img.getRGB(i, j), true);
                    if ((px >= 0) && (px < greenImage.getWidth()) && (py >= 0) && (py < greenImage.getHeight())
                            && (pColor.getAlpha() > 0)) {
                        Color startColor = new Color(greenImage.getRGB(px, py), true);
                        greenImage.setRGB(px, py, new Color(startColor.getRed(), pColor.getGreen(),
                                startColor.getBlue(), pColor.getAlpha()).getRGB());
                    }
                }
            }
        }
    }

    public static void blueAddGoobers(JFrame frame) {
        BufferedImage img;
        for (Goober goober : goobers) {
            img = goober.frames.get((int) (System.currentTimeMillis() / 250 %
                    goober.frames.size()));
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    int px = goober.x + i - frame.getX();
                    int py = goober.y + j - frame.getY();
                    Color pColor = new Color(img.getRGB(i, j), true);
                    if ((px >= 0) && (px < blueImage.getWidth()) && (py >= 0) && (py < blueImage.getHeight())
                            && (pColor.getAlpha() > 0)) {
                        Color startColor = new Color(blueImage.getRGB(px, py), true);
                        blueImage.setRGB(px, py, new Color(startColor.getRed(), startColor.getGreen(),
                                pColor.getBlue(), pColor.getAlpha()).getRGB());
                    }
                }
            }
        }
    }

    public static void inverseAddGoobers(JFrame frame) {
        BufferedImage img;
        for (Goober goober : goobers) {
            img = goober.frames.get((int) (System.currentTimeMillis() / 250 %
                    goober.frames.size()));
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    int px = goober.x + i - frame.getX();
                    int py = goober.y + j - frame.getY();
                    Color pColor = new Color(img.getRGB(i, j), true);
                    if ((px >= 0) && (px < inverseImage.getWidth()) && (py >= 0) && (py < inverseImage.getHeight())
                            && (pColor.getAlpha() > 0)) {
                        inverseImage.setRGB(px, py, new Color(255-pColor.getRed(), 255 - pColor.getGreen(), 255 - pColor.getBlue()).getRGB());
                    }
                }
            }
        }
    }

    public static void greyAddGoobers(JFrame frame) {
        BufferedImage img;
        for (Goober goober : goobers) {
            img = goober.frames.get((int) (System.currentTimeMillis() / 250 %
                    goober.frames.size()));
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    int px = goober.x + i - frame.getX();
                    int py = goober.y + j - frame.getY();
                    Color pColor = new Color(img.getRGB(i, j), true);
                    if ((px >= 0) && (px < greyImage.getWidth()) && (py >= 0) && (py < greyImage.getHeight())
                            && (pColor.getAlpha() > 0)) {
                        float[] pHSV = new float[3];
                        Color.RGBtoHSB(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), pHSV);
                        greyImage.setRGB(px, py, Color.HSBtoRGB(pHSV[0], 0, pHSV[2]));
                    }
                }
            }
        }
    }

    public static void drawBG(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
    }

    public static void drawGoobers(JFrame frame, Graphics2D g, ArrayList<Goober> goobers) {
        BufferedImage img;
        for (Goober goober : goobers) {
            img = goober.frames.get((int) (System.currentTimeMillis() / 250 % goober.frames.size()));
            g.drawImage(img, goober.x - frame.getX(), goober.y - frame.getY(), null);
        }
    }

    public static void drawMagGlass(Graphics2D g) {
        g.drawImage(foreground, 0, 0, null);
    }
}