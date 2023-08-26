package jetfighters.game.sprites;

import java.awt.image.BufferedImage;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class AnimatedSprite extends Sprite {

    private BufferedImage[] frames;
    private int activeFrameCount;
    private double frameDelay;
    private double animationSpeed;
    private boolean paused;
    private boolean looping;

    private double timer;
    private int currentFrame;

    /**
     * Creates a new Animated sprite from a path, the number of frames and the delay between each frame
     * It will loop by default.
     *
     * @param imagePath  Should not end in a number as it will have a number and .png appended (e.g. "sprites/jet/Jet" turns into "sprites/jet/Jet_1.png" and so on)
     * @param frameCount the amount of frames that should be loaded (needs images up to "[imagePath]_[frameCount].png")
     * @param frameDelay the delay between each frame
     */
    public AnimatedSprite(String imagePath, int frameCount, double frameDelay) {
        this(imagePath, frameCount, frameDelay, true);
    }

    /**
     * Creates a new Animated sprite from a path, the number of frames and the delay between each frame
     *
     * @param imagePath  Should not end in a number as it will have a number and .png appended (e.g. "sprites/jet/Jet_" turns into "sprites/jet/Jet_1.png" and so on)
     * @param frameCount the amount of frames that should be loaded (needs images up to "[imagePath]_[frameCount].png")
     * @param frameDelay the delay between each frame
     * @param looping    if this is true the animation will loop
     */
    public AnimatedSprite(String imagePath, int frameCount, double frameDelay, boolean looping) {
        super(imagePath + "1.png");
        setup(frameDelay, frameCount, looping);
        frames[0] = image;

        for (int i = 1; i < frameCount; i++) {
            int frameNumber = i + 1;
            frames[i] = readImage(imagePath + frameNumber + ".png");
        }
    }

    /**
     * Creates a new Animated sprite from a path to a sprite-sheet, the number of horizontal frames, the number of vertical frames,
     * their distance and the delay between each frame. <br>
     * The sheet should not have any gaps or there will be empty frames. <br>
     * The separation between frames should be equal.
     *
     * @param imagePath        the path to the sprite-sheet. Will NOT have .png appended
     * @param hFrameCount      the maximum amount of frames the sheet has horizontally
     * @param vFrameCount      the maximum amount of frames the sheet has vertically
     * @param hSeparation      the spacing between each one of the frames horizontally (0 if there is no extra space between frames)
     * @param vSeparation      the spacing between each one of the frames vertically (0 if there is no extra space between frames)
     * @param frameDelay       the delay between each frame
     */
    public AnimatedSprite(String imagePath, int hFrameCount, int vFrameCount, int hSeparation, int vSeparation, double frameDelay) {
        this(imagePath, hFrameCount, vFrameCount, hSeparation, vSeparation, frameDelay, true);
    }

    /**
     * Creates a new Animated sprite from a path to a sprite-sheet, the number of horizontal frames, the number of vertical frames,
     * their distance and the delay between each frame. <br>
     * The sheet should not have any gaps or there will be empty frames. <br>
     * The separation between frames should be equal.
     *
     * @param imagePath        the path to the sprite-sheet. Will NOT have .png appended
     * @param hFrameCount      the maximum amount of frames the sheet has horizontally
     * @param vFrameCount      the maximum amount of frames the sheet has vertically
     * @param hSeparation      the spacing between each one of the frames horizontally (0 if there is no extra space between frames)
     * @param vSeparation      the spacing between each one of the frames vertically (0 if there is no extra space between frames)
     * @param frameDelay       the delay between each frame
     * @param looping          if this is true the animation will loop
     */
    public AnimatedSprite(String imagePath, int hFrameCount, int vFrameCount, int hSeparation, int vSeparation, double frameDelay, boolean looping) {
        this(imagePath, hFrameCount, vFrameCount, hSeparation, vSeparation, frameDelay, looping, hFrameCount * vFrameCount);
    }

    /**
     * Creates a new Animated sprite from a path to a sprite-sheet, the number of horizontal frames, the number of vertical frames,
     * their distance and the delay between each frame. <br>
     * The sheet should not have any gaps or there will be empty frames. <br>
     * The separation between frames should be equal.
     *
     * @param imagePath        the path to the sprite-sheet. Will NOT have .png appended
     * @param hFrameCount      the maximum amount of frames the sheet has horizontally
     * @param vFrameCount      the maximum amount of frames the sheet has vertically
     * @param hSeparation      the spacing between each one of the frames horizontally (0 if there is no extra space between frames)
     * @param vSeparation      the spacing between each one of the frames vertically (0 if there is no extra space between frames)
     * @param frameDelay       the delay between each frame
     * @param looping          if this is true the animation will loop
     * @param activeFrameCount the actual number of frames, if there is empty space at the end of the sheet
     *                         (e.g. a 3x3 sheet where the frame in position 3,3 is empty would have an activeFrameCount of 8)
     */
    public AnimatedSprite(String imagePath, int hFrameCount, int vFrameCount, int hSeparation, int vSeparation, double frameDelay, boolean looping, int activeFrameCount) {
        super(imagePath);

        setup(frameDelay, activeFrameCount, looping);

        BufferedImage fullImage = image;
        int subWidth = (fullImage.getWidth() - hSeparation * (hFrameCount - 1)) / hFrameCount;
        int subHeight = (fullImage.getHeight() - vSeparation * (vFrameCount - 1)) / vFrameCount;
        for (int yFrame = 0; yFrame < vFrameCount; yFrame++) {
            for (int xFrame = 0; xFrame < hFrameCount; xFrame++) {
                frames[yFrame * hFrameCount + xFrame] = fullImage.getSubimage(
                        (subWidth + hSeparation) * xFrame, (subHeight + vSeparation) * yFrame,
                        subWidth, subHeight
                );
            }
        }
        image = frames[0];
    }

    private void setup(double frameDelay, int frameCount, boolean looping) {
        this.frameDelay = frameDelay;
        this.looping = looping;
        activeFrameCount = frameCount;

        animationSpeed = 1.0;
        timer = 0;
        paused = false;
        currentFrame = 0;

        frames = new BufferedImage[frameCount];
    }

    public void update(double delta) {
        if (paused)
            return;

        timer += delta;
        while (timer >= frameDelay / animationSpeed) {
            timer -= frameDelay;
            nextFrame();
        }
    }

    /**
     * Advances the current frame and wraps it with activeFrameCount if looping is true. <br>
     * If looping is false and the last frame is reached the animation will pause and the sprite will turn invisible.
     */
    public void nextFrame() {
        if (!looping && currentFrame + 1 >= activeFrameCount) {
            currentFrame = activeFrameCount;
            paused = true;
            setVisible(false);
            System.out.println("not looping");
        }
        currentFrame = (currentFrame + 1) % activeFrameCount;
        image = frames[currentFrame];
    }

    /**
     * Sets the current frame of this animated sprite. <br>
     * If {@code frame} is bigger than {@link #activeFrameCount} it will be wrapped.
     *
     * @param frame the frame to set this animation to (starting from 0)
     */
    public void setFrame(int frame) {
        currentFrame = frame % activeFrameCount;
        image = frames[currentFrame];
    }

    /**
     * Resets the animation to the first frame.
     */
    public void reset() {
        currentFrame = 0;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int getActiveFrameCount() {
        return activeFrameCount;
    }
}
