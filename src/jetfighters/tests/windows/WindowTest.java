package jetfighters.tests.windows;

import jetfighters.windows.Window;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class WindowTest {

    private static final Window window = new Window(true);

    @Test
    void testConstructor() {
        assertSame(window.getTitle(), "Jet Fighters");
    }
}
