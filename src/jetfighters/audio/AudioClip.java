package jetfighters.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class AudioClip {
    private final Clip clip;

    public AudioClip(Clip clip) {
        this.clip = clip;
    }

    /**
     * a method to start the clip
     */
    public void start() {
        clip.start();
    }

    /**
     * a method to set the volume
     *
     * @param volume the volume value
     */
    public void setVolume(float volume) {
        final FloatControl control = (FloatControl) clip
                .getControl(FloatControl.Type.MASTER_GAIN);
        float range = control.getMaximum() - control.getMinimum();
        float gain = range * volume + control.getMinimum();
        control.setValue(gain);
    }

    /**
     * a method to check if the clip is finished
     *
     * @return true if the clip is finished, no otherwise
     */
    public boolean hasFinishedPlaying() {
        return !clip.isRunning();
    }

    /**
     * is closing the clip
     */
    public void cleanup() {
        clip.close();
    }

}
