package jetfighters.audio;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public enum Music {

    Tetris01("Tetris_01.wav"),
    Tetris03("Tetris_03.wav"),
    Tetris99("Tetris_99.wav");

    private final String musicPath;

    Music(String musicPath) {
        this.musicPath = musicPath;
    }

    /**
     * a getter for the music path
     *
     * @return the music path
     */
    public String getMusicPath() {
        return musicPath;
    }
}
