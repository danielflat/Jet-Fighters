package jetfighters.tests.audio;

import jetfighters.audio.AudioOptions;
import jetfighters.windows.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class AudioOptionsTest {

    private static final Window window = new Window(true);

    private final AudioOptions audioOptions = window.getOptionsWindow().getAudioOptions();

    @BeforeEach
    void setup() {
        audioOptions.setMusicValue(1.0f);
        audioOptions.setSoundValue(1.0f);
        audioOptions.setTempMusicValue(1.0f);
        audioOptions.setTempSoundValue(1.0f);
    }

    @Test
    void testConstructor() {
        assertEquals(audioOptions.getMusicValue(), 1.0f);
        assertEquals(audioOptions.getSoundValue(), 1.0f);
        assertEquals(audioOptions.getTempMusicValue(), audioOptions.getMusicValue());
        assertEquals(audioOptions.getTempSoundValue(), audioOptions.getSoundValue());
    }

    @Test
    void testGetAndSetAndHasChanged() {
        assertFalse(audioOptions.hasChanged());
        audioOptions.setMusicValue(2.0f);
        assertTrue(audioOptions.hasChanged());
        assertEquals(audioOptions.getMusicValue(), 2.0f);
        assertNotEquals(audioOptions.getTempMusicValue(), audioOptions.getMusicValue());
        audioOptions.setSoundValue(0.5f);
        assertTrue(audioOptions.hasChanged());
        assertEquals(audioOptions.getSoundValue(), 0.5f);
        assertNotEquals(audioOptions.getTempSoundValue(), audioOptions.getSoundValue());
        audioOptions.setMusicValue(1.0f);
        assertTrue(audioOptions.hasChanged());
        assertEquals(audioOptions.getMusicValue(), 1.0f);
        assertEquals(audioOptions.getTempMusicValue(), audioOptions.getMusicValue());
        audioOptions.setSoundValue(1.0f);
        assertFalse(audioOptions.hasChanged());
        assertEquals(audioOptions.getSoundValue(), 1.0f);
        assertEquals(audioOptions.getTempSoundValue(), audioOptions.getSoundValue());
        audioOptions.setTempMusicValue(0.7f);
        assertTrue(audioOptions.hasChanged());
        assertEquals(audioOptions.getTempMusicValue(), 0.7f);
        assertNotEquals(audioOptions.getTempMusicValue(), audioOptions.getMusicValue());
        audioOptions.setTempSoundValue(1.1f);
        assertTrue(audioOptions.hasChanged());
        assertEquals(audioOptions.getTempSoundValue(), 1.1f);
        assertNotEquals(audioOptions.getTempSoundValue(), audioOptions.getSoundValue());
    }
}
