package xyz.nayskutzu.mythicalclient;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class WindowState {
    public static void updateTitle(String title) {
        Display.setTitle(title);
    }

    public static void UpdateIcon() {
        Display.setIcon(new ByteBuffer[] { loadImage("/icon16.png"), loadImage("/icon32.png") });
    }

    private static ByteBuffer loadImage(String string) {
        try {
            BufferedImage image = ImageIO.read(WindowState.class.getResourceAsStream(string));
            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
            ByteBuffer buffer = ByteBuffer.allocate(4 * image.getWidth() * image.getHeight());
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            buffer.flip();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
