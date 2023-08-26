package jetfighters.audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class AudioPlayer {

    private static final HashMap<String, byte[]> cachedClips = new HashMap<>();
    private final List<AudioClip> musicClips;
    private final List<AudioClip> soundClips;
    private final AudioOptions audioOptions;

    public AudioPlayer(AudioOptions audioOptions) {
        musicClips = new ArrayList<>();
        soundClips = new ArrayList<>();
        this.audioOptions = audioOptions;
    }

    /**
     * is updating the current volume for the music
     */
    public void updateMusic() {
        musicClips.forEach(musicClip -> musicClip.setVolume(audioOptions.getMusicValue()));
    }

    /**
     * is updating the current volume for the sounds
     */
    public void updateSound() {
        soundClips.forEach(soundClip -> soundClip.setVolume(audioOptions.getSoundValue()));

        List.copyOf(soundClips)
                .forEach(soundClip -> {
                    if (soundClip.hasFinishedPlaying()) {
                        soundClip.cleanup();
                        soundClips.remove(soundClip);
                    }
                });
    }

    /**
     * a method for playing the music
     *
     * @param music the music clip
     */
    public void playMusic(Music music) {
        playMusic(music.getMusicPath(), 1);
    }

    /**
     * a method for playing the music
     *
     * @param filePath the music path
     * @param volume   the volume value for the playing music
     */
    public void playMusic(String filePath, float volume) {
        final Clip clip = getClip(filePath, "music");
        if (clip == null) {
            System.err.println("Couldn't find clip with path: music/" + filePath);
            return;
        }
        final AudioClip musicClip = new AudioClip(clip);
        musicClip.setVolume(audioOptions.getMusicValue() * volume);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        musicClip.start();
        musicClips.add(musicClip);
    }

    /**
     * a method for playing the sound
     *
     * @param sound the sound clip
     */
    public void playSound(Sound sound) {
        playSound(sound.getSoundPath(), 1);
    }

    /**
     * a method for playing the sound
     *
     * @param filePath the sound path
     * @param volume   the volume value for the playing music
     */
    public void playSound(String filePath, float volume) {
        final Clip clip = getClip(filePath, "sounds");
        if (clip == null) {
            System.err.println("Couldn't find clip with path: sounds/" + filePath);
            return;
        }
        final AudioClip soundclip = new AudioClip(clip);
        soundclip.setVolume(audioOptions.getSoundValue() * volume);
        soundclip.start();
        soundClips.add(soundclip);
    }

    /**
     * a getter for the clip to play it
     *
     * @param fileName the filepath for the clip
     * @param source   the source folder as a string
     * @return the clip
     */
    private static Clip getClip(String fileName, String source) {
        String path = source.toLowerCase() + "/" + fileName;
        ClassLoader classLoader = AudioPlayer.class.getClassLoader();
        if (!cachedClips.containsKey(path)) {
            try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
                byte[] bytes = Objects.requireNonNull(inputStream).readAllBytes();
                cachedClips.put(path, bytes);
            } catch (IOException | NullPointerException e) {
                System.err.println("Couldn't load bytes from file: " + path);
                e.printStackTrace();
            }
        }
        try {
            final Clip clip = AudioSystem.getClip();
            ByteArrayInputStream bis = new ByteArrayInputStream(cachedClips.get(path));
            AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
            clip.open(ais);
            return clip;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            System.err.println("Couldn't open clip from cached bytes: " + path);
            e.printStackTrace();
        }
        return null;
    }
}
