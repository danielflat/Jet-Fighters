package jetfighters.game.powerupseffects;

import jetfighters.game.entities.JetEntity;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Base class for an effect that is applied by a PowerUp to a Jet. <br>
 * It has a duration after which it should be removed from the Jet.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public abstract class PowerUpEffect {

    protected final double duration;
    protected double timePassed;

    /**
     * Creates a new PowerUpEffect.
     *
     * @param duration the duration this effect should last for before being removed
     */
    public PowerUpEffect(double duration) {
        this.duration = duration;
    }

    /**
     * Applies this effect to {@code jetEntity}. <br>
     * Should be removed again with {@link #removeEffect(JetEntity)} when {@link #shouldRemove()} is true.
     *
     * @param jetEntity the entity to apply this effect to
     */
    public abstract void applyEffect(JetEntity jetEntity);

    /**
     * Removes this effect from {@code jetEntity}. <br>
     * Should be called when {@link #shouldRemove()} is true.
     *
     * @param jetEntity the entity to remove this effect from
     */
    public abstract void removeEffect(JetEntity jetEntity);

    /**
     * Updates the time passed for this powerup, if the internal timer reaches the duration time, this should be removed
     * (by checking {@link #shouldRemove()}).
     *
     * @param delta the delta time to update the internal timer with
     */
    public void update(double delta) {
        timePassed += delta;
    }

    /**
     * @return true if this effect should be removed, false otherwise
     */
    public boolean shouldRemove() {
        return timePassed >= duration && duration >= 0;
    }

    /**
     * Resets this power ups internal timer so that it will last for {@code duration} from the moment this method is called.
     */
    public void resetTime() {
        timePassed = 0;
    }

}
