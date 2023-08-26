package jetfighters.tests.windows;

import jetfighters.windows.BackgroundImage;
import jetfighters.windows.Window;
import jetfighters.windows.exceptions.ImageNotFoundException;
import jetfighters.windows.exceptions.StateNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class BackgroundImageTest {

    private static final Window window = new Window(true);

    private BackgroundImage backgroundImage;

    private ImageIcon dummyImage;

    @BeforeEach
    void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        backgroundImage = new BackgroundImage(window);
        try {
            dummyImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "backgrounds/Background_dummy.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructor() {
        int width = 1068;
        assertEquals(width, backgroundImage.getBackgroundImage().getIconWidth());
        int height = 685;
        assertEquals(height, backgroundImage.getBackgroundImage().getIconHeight());
        try {
            assertNotNull(backgroundImage.getImage(1));
            assertNotNull(backgroundImage.getImage(2));
            assertNotNull(backgroundImage.getImage(3));
            assertNotNull(backgroundImage.getImage(4));
            assertTrue(checkPixelsEqual(backgroundImage.getBackgroundImage().getImage(),
                    backgroundImage.getScaledImage(1).getImage()));
        } catch (ImageNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testScaleImageIcon() {
        assertEquals(dummyImage.getIconHeight() * 2,
                backgroundImage.scaleImageIcon(dummyImage, 2.0, 2.0).getIconHeight());
        assertEquals(dummyImage.getIconWidth() * 2,
                backgroundImage.scaleImageIcon(dummyImage, 2.0, 2.0).getIconWidth());
        try {
            assertTrue(checkPixelsEqual(dummyImage.getImage(),
                    backgroundImage.scaleImageIcon(dummyImage, 1, 1).getImage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRestorePreviousBackground() {
        try {
            backgroundImage.restorePreviousBackground(1);
            assertEquals(1, window.getOptionsWindow().getBackgroundState());
            backgroundImage.restorePreviousBackground(2);
            assertEquals(2, window.getOptionsWindow().getBackgroundState());
            backgroundImage.restorePreviousBackground(3);
            assertEquals(3, window.getOptionsWindow().getBackgroundState());
            backgroundImage.restorePreviousBackground(4);
            assertEquals(4, window.getOptionsWindow().getBackgroundState());
            Assertions.assertThrows(StateNotFoundException.class,
                    () -> backgroundImage.restorePreviousBackground(Integer.MIN_VALUE));
            Assertions.assertThrows(StateNotFoundException.class,
                    () -> backgroundImage.restorePreviousBackground(-1));
            Assertions.assertThrows(StateNotFoundException.class,
                    () -> backgroundImage.restorePreviousBackground(0));
            Assertions.assertThrows(StateNotFoundException.class,
                    () -> backgroundImage.restorePreviousBackground(5));
            Assertions.assertThrows(StateNotFoundException.class,
                    () -> backgroundImage.restorePreviousBackground(Integer.MAX_VALUE));

        } catch (StateNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetImage() {
        try {
            assertNotNull(backgroundImage.getImage(1));
            assertNotNull(backgroundImage.getImage(2));
            assertNotNull(backgroundImage.getImage(3));
            assertNotNull(backgroundImage.getImage(4));
            assertSame(backgroundImage.getImage(1), backgroundImage.getImage(1));
            assertSame(backgroundImage.getImage(2), backgroundImage.getImage(2));
            assertSame(backgroundImage.getImage(3), backgroundImage.getImage(3));
            assertSame(backgroundImage.getImage(4), backgroundImage.getImage(4));
            assertNotSame(backgroundImage.getImage(1), backgroundImage.getImage(2));
            assertNotSame(backgroundImage.getImage(1), backgroundImage.getImage(3));
            assertNotSame(backgroundImage.getImage(1), backgroundImage.getImage(4));
            assertNotSame(backgroundImage.getImage(2), backgroundImage.getImage(3));
            assertNotSame(backgroundImage.getImage(2), backgroundImage.getImage(4));
            assertNotSame(backgroundImage.getImage(3), backgroundImage.getImage(4));
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetScaledImageAndGetBackgroundImageScaled() {
        try {
            assertNotNull(backgroundImage.getScaledImage(1));
            assertNotNull(backgroundImage.getScaledImage(2));
            assertNotNull(backgroundImage.getScaledImage(3));
            assertNotNull(backgroundImage.getScaledImage(4));
            assertEquals(backgroundImage.getScaledImage(1).getIconWidth() * 2,
                    backgroundImage.scaleImageIcon(backgroundImage.getScaledImage(1), 2.0, 2.0).getIconWidth());
            assertEquals(backgroundImage.getScaledImage(1).getIconHeight() * 2,
                    backgroundImage.scaleImageIcon(backgroundImage.getScaledImage(1), 2.0, 2.0).getIconHeight());
            backgroundImage.setBackgroundImage(backgroundImage.scaleImageIcon(dummyImage, 2, 2));
            try {
                assertTrue(checkPixelsEqual(backgroundImage.getBackgroundImageScaled(2, 2).getImage(),
                        backgroundImage.scaleImageIcon(dummyImage, 2, 2).getImage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAndSetBackgroundImage() {
        try {
            assertTrue(checkPixelsEqual(backgroundImage.getBackgroundImage().getImage(),
                    backgroundImage.getScaledImage(1).getImage()));
            backgroundImage.setBackgroundImage(dummyImage);
            assertTrue(checkPixelsEqual(backgroundImage.getBackgroundImage().getImage(),
                    dummyImage.getImage()));
            assertFalse(checkPixelsEqual(backgroundImage.getBackgroundImage().getImage(),
                    backgroundImage.getScaledImage(1).getImage()));
        } catch (IOException | ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkPixelsEqual(Image image1, Image image2) throws IOException {
        return checkPixelDifferenceGrabber(image1, image2) == 0;
    }

    private int checkPixelDifferenceGrabber(Image image1, Image image2) throws IOException {
        int width1 = image1.getWidth(null);
        int height1 = image1.getHeight(null);
        int width2 = image2.getWidth(null);
        int height2 = image2.getHeight(null);
        if (width1 != width2 || height1 != height2) {
            return -1;
        }
        int[] bitmap1 = new int[width1 * height1];
        PixelGrabber pg1 = new PixelGrabber(image1, 0, 0, width1, height1,
                bitmap1, 0, width1);
        int[] bitmap2 = new int[width2 * height2];
        PixelGrabber pg2 = new PixelGrabber(image2, 0, 0, width2, height2,
                bitmap2, 0, width2);
        try {
            pg1.grabPixels();
            pg2.grabPixels();
        } catch (InterruptedException e) {
            throw new IOException("Couldn't grab pixels");
        }
        int differingPixelCount = 0;
        for (int x = 0; x < width1; x++) {
            for (int y = 0; y < height1; y++) {
                if (bitmap1[y * width1 + x] != bitmap2[y * width2 + x]) {
                    differingPixelCount++;
                }
            }
        }
        return differingPixelCount;
    }
}
