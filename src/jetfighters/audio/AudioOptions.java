package jetfighters.audio;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class AudioOptions {
    private float musicValue;
    private float soundValue;
    private float tempMusicValue;
    private float tempSoundValue;

    public AudioOptions() {
        musicValue = 1f;
        soundValue = 1f;
        tempMusicValue = musicValue;
        tempSoundValue = soundValue;
    }

    /**
     * a method to check if the music or sound valie has changed
     *
     * @return true if one of these values has changed, no otherwise
     */
    public boolean hasChanged() {
        return musicValue != tempMusicValue || soundValue != tempSoundValue;
    }

    /**
     * a getter for the current music value
     *
     * @return the music value
     */
    public float getMusicValue() {
        return musicValue;
    }

    /**
     * a getter for the current sound value
     *
     * @return the sound value
     */
    public float getSoundValue() {
        return soundValue;
    }

    /**
     * a setter for the current music value
     *
     * @param musicValue the new value for the current music value
     */
    public void setMusicValue(float musicValue) {
        this.musicValue = musicValue;
    }

    /**
     * a setter for the current sound value
     *
     * @param soundValue the new value for the current sound value
     */
    public void setSoundValue(float soundValue) {
        this.soundValue = soundValue;
    }

    /**
     * a getter for the temporary music value
     *
     * @return the temporary music value
     */
    public float getTempMusicValue() {
        return tempMusicValue;
    }

    /**
     * a getter for the temporary sound value
     *
     * @return the temporary sound value
     */
    public float getTempSoundValue() {
        return tempSoundValue;
    }

    /**
     * a setter for the temporary sound value
     *
     * @param musicValue the new value for the temporary music value
     */
    public void setTempMusicValue(float musicValue) {
        this.tempMusicValue = musicValue;
    }

    /**
     * a setter for the temporary sound value
     *
     * @param soundValue the new value for the temporary sound value
     */
    public void setTempSoundValue(float soundValue) {
        this.tempSoundValue = soundValue;
    }

}
