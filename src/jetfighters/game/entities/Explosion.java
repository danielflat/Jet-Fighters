package jetfighters.game.entities;

import jetfighters.game.math.Vector2;
import jetfighters.game.sprites.AnimatedSprite;

import java.awt.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Animates an explosion at a specific position in the game world. <br>
 * Will destory itself when the animation is finished.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class Explosion extends Entity {

    private final AnimatedSprite explosionSprite;

    /**
     * Creates a new explosion at {@code position} that will run automatically as soon as it is updated.
     *
     * @param position the position to create the Explosion at
     * @param scale    the scale of the explosion
     */
    public Explosion(Vector2 position, double scale) {
        super(position, new Rectangle(32, 32), 0, 0);
        explosionSprite = new AnimatedSprite("sprites/explosion/explosion.png",
                5, 1, 0, 0, .1, false);
        explosionSprite.scale(scale);
    }

    @Override
    public void update(double delta) {
        explosionSprite.update(delta);
        if (explosionSprite.getCurrentFrame() == explosionSprite.getActiveFrameCount()) {
            destroy();
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        explosionSprite.draw(g2d);
    }
}
