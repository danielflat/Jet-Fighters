package jetfighters.tests.windows;

import jetfighters.windows.OptionsWindow;
import jetfighters.windows.Window;
import jetfighters.windows.exceptions.ImageNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class AbstractWindowTest {
    private static final Window window = new Window(true);

    private OptionsWindow optionsWindow;
    private ImageIcon dummyImage;

    @BeforeEach
    void setup() {
        optionsWindow = window.getOptionsWindow();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            dummyImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "backgrounds/Background_dummy.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        optionsWindow.setBackgroundState(1);
    }

    @Test
    void testConstructor() {
        assertEquals(1068, optionsWindow.getWidth());
        assertEquals(685, optionsWindow.getHeight());
        assertSame(optionsWindow.getBackgroundState(), 1);
        try {
            assertNotNull(optionsWindow.getImage("Morning", false));
            assertNotNull(optionsWindow.getImage("Morning", true));
            assertNotNull(optionsWindow.getImage("Daytime", false));
            assertNotNull(optionsWindow.getImage("Daytime", true));
            assertNotNull(optionsWindow.getImage("Night", false));
            assertNotNull(optionsWindow.getImage("Night", true));
            assertNotNull(optionsWindow.getImage("Import", false));
            assertNotNull(optionsWindow.getImage("Import", true));
            assertNotNull(optionsWindow.getImage("Apply", false));
            assertNotNull(optionsWindow.getImage("Apply", true));
            assertNotNull(optionsWindow.getImage("Cancel", false));
            assertNotNull(optionsWindow.getImage("Cancel", true));
            Assertions.assertThrows(ImageNotFoundException.class,
                    () -> optionsWindow.getImage("morning", true));
            Assertions.assertThrows(ImageNotFoundException.class,
                    () -> optionsWindow.getImage("dAtIMe", true));
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testBackgroundState() {
        assertSame(1, optionsWindow.getBackgroundState());
        assertNotSame(2, optionsWindow.getBackgroundState());
        optionsWindow.setBackgroundState(2);
        assertSame(2, optionsWindow.getBackgroundState());
        assertNotSame(1, optionsWindow.getBackgroundState());
        optionsWindow.setBackgroundImage(dummyImage, 4);
        assertSame(4, optionsWindow.getBackgroundState());
        assertNotSame(1, optionsWindow.getBackgroundState());
        assertNotSame(2, optionsWindow.getBackgroundState());
    }
}
