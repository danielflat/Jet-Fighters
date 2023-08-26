package jetfighters.audio;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public enum Sound {

    Button01("Button_01.wav"),
    Button02("Button_02.wav"),
    Button03("Button_03.wav"),
    Hit("jet/8bit_hit_1.wav"),
    Collision("jet/8bit_hit_6.wav"),
    Death("jet/8bit_death_3.wav"),
    Heal("jet/8bit_grow_level_up_2.wav"),
    PowerUp("jet/8bit_powerup.wav");

    private final String soundPath;

    Sound(String soundPath) {
        this.soundPath = soundPath;
    }

    /**
     * a getter for the sound path
     *
     * @return the sound path
     */
    public String getSoundPath() {
        return soundPath;
    }

}
