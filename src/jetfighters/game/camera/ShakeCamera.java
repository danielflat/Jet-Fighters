package jetfighters.game.camera;

import jetfighters.game.math.OpenSimplexNoise;
import jetfighters.game.math.Vector2;

import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * A camera that simulates "camera shake" through OpenSimplexNoise. <br>
 * Based on this <a href="https://kidscancode.org/godot_recipes/2d/screen_shake/">KidsCanCode article</a>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class ShakeCamera extends Camera {

    private final double decay;
    private final Vector2 maxOffset;
    private final double maxRoll;
    private final double traumaPower;
    private double trauma;

    private final OpenSimplexNoise noise;
    private final int noiseOffsetX;
    private int noiseOffsetY;

    private Vector2 shakeOffset;
    private double shakeRotation;

    /**
     * {@code decay} defaults to .8. <br>
     * {@code maxOffset} defaults to (3, 2). <br>
     * {@code maxRoll} defaults to .02. <br>
     * {@code traumaPower} defaults to 2.
     *
     * @see #ShakeCamera(double, Vector2, double, double)
     */
    public ShakeCamera() {
        this(.8);
    }

    /**
     * {@code maxOffset} defaults to (3, 2). <br>
     * {@code maxRoll} defaults to .02. <br>
     * {@code traumaPower} defaults to 2.
     *
     * @see #ShakeCamera(double, Vector2, double, double)
     */
    public ShakeCamera(double decay) {
        this(decay, new Vector2(3, 2));
    }

    /**
     * {@code maxRoll} defaults to .02. <br>
     * {@code traumaPower} defaults to 2.
     *
     * @see #ShakeCamera(double, Vector2, double, double)
     */
    public ShakeCamera(double decay, Vector2 maxOffset) {
        this(decay, maxOffset, .02);
    }

    /**
     * {@code traumaPower} defaults to 2.
     *
     * @see #ShakeCamera(double, Vector2, double, double)
     */
    public ShakeCamera(double decay, Vector2 maxOffset, double maxRoll) {
        this(decay, maxOffset, maxRoll, 2);
    }

    /**
     * Creates a new ShakeCamera with using {@link System#currentTimeMillis()} to initialize randomness.
     *
     * @param decay       the amount to reduce the trauma by every second (should be between 0 and 1 but can be higher). Should not be negative
     * @param maxOffset   the maximum offset the shaking can reach while shaking (in both positive and negative directions)
     * @param maxRoll     the maximum rotation the shaking can reach while shaking.
     * @param traumaPower the power the trauma will be raised to when calculating the amount of shaking each frame (the higher the value, the faster the shaking will slow down). Preferred values are 2 or 3
     */
    public ShakeCamera(double decay, Vector2 maxOffset, double maxRoll, double traumaPower) {
        this.decay = decay;
        this.maxOffset = maxOffset;
        this.maxRoll = maxRoll;
        this.traumaPower = traumaPower;

        Random random = new Random(System.currentTimeMillis());
        noise = new OpenSimplexNoise(System.currentTimeMillis());
        noiseOffsetX = random.nextInt(100000) + 1000;
        noiseOffsetY = 0;
        shakeOffset = Vector2.ZERO;
        shakeRotation = 0;
        trauma = 0;
    }

    /**
     * Adds trauma to this camera, causing it to shake. The more trauma, the more violent the shaking will be. <br>
     * The trauma will slowly decrease based on the {@code decay} set when creating this camera. <br>
     * Trauma that has not yet decayed will cumulate but will not exceed 1.0.
     *
     * @param amount the amount of trauma to add (should not exceed 1.0, as it is capped at 1.0)
     */
    public void addTrauma(double amount) {
        trauma = Math.min(trauma + amount, 1.0);
    }

    /**
     * {@inheritDoc} <br><br>
     * Will make the camera shake and make the trauma decay.
     */
    @Override
    public void update(double delta) {
        super.update(delta);

        if (trauma > 0) {
            shake();
            trauma = Math.max(trauma - decay * delta, 0);
        }
    }

    private void shake() {
        double amount = Math.pow(trauma, traumaPower);
        noiseOffsetY += 1;
        shakeRotation = maxRoll * amount * noise.eval(noiseOffsetX, noiseOffsetY, 0);
        shakeOffset = new Vector2(
                maxOffset.x * amount * noise.eval(noiseOffsetX * 2, noiseOffsetY, 0),
                maxOffset.y * amount * noise.eval(noiseOffsetX * 3, noiseOffsetY, 0)
        );
    }

    @Override
    public AffineTransform getTransform(int screenWidth, int screenHeight) {
        AffineTransform transform = AffineTransform.getRotateInstance(shakeRotation, screenWidth / 2.0, screenHeight / 2.0);
        transform.translate(shakeOffset.x, shakeOffset.y);
        return transform;
    }

    @Override
    public AffineTransform getInverseTransform(int screenWidth, int screenHeight) {
        AffineTransform transform = AffineTransform.getRotateInstance(-shakeRotation, screenWidth / 2.0, screenHeight / 2.0);
        transform.translate(-shakeOffset.x, -shakeOffset.y);
        return transform;
    }
}
