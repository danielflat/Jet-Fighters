package jetfighters.game.entities.powerups;

import jetfighters.game.entities.Entity;
import jetfighters.game.entities.MovingEntity;
import jetfighters.game.entities.Player;
import jetfighters.game.handlers.CollisionHandler;
import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.PowerUpEffect;
import jetfighters.game.sprites.AnimatedSprite;

import java.awt.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Base class for all PowerUps that can be picked up by a player.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class PowerUpEntity extends MovingEntity {

    private final PowerUpEffect effect;
    private final AnimatedSprite sprite;

    //TODO: Add doc comments to inheriting constructors

    /**
     * Creates a new PowerUpEntity that can be collected by a player.
     *
     * @param position        the initial position of this PowerUp
     * @param initialVelocity the initial velocity of this PowerUp
     * @param effect          the effect that will be applied to the player that collected this PowerUp
     * @param sprite          the sprite that will be used to display this PowerUp
     */
    public PowerUpEntity(Vector2 position, Vector2 initialVelocity, PowerUpEffect effect, AnimatedSprite sprite) {
        super(position, new Rectangle(20, 20),
                CollisionHandler.POWERUP_LAYER, CollisionHandler.PLAYER_LAYER,
                initialVelocity.length(), initialVelocity);
        this.effect = effect;
        this.sprite = sprite;
    }

    @Override
    public void handleCollision(Entity otherEntity) {
        if (otherEntity instanceof Player player) {
            player.addPowerUpEffect(effect);
            destroy();
        }
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        sprite.update(delta);
        if (position.y >= 700) {
            destroy();
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        sprite.draw(g2d);
    }

    /**
     * @return this PowerUps effect
     */
    public PowerUpEffect getEffect() {
        return effect;
    }
}
