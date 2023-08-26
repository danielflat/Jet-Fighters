package jetfighters.game.entities.powerups;

import jetfighters.game.entities.Entity;
import jetfighters.game.entities.Player;
import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.PowerUpEffect;
import jetfighters.game.sprites.AnimatedSprite;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Base class for all Debuffs, that can be picked up by a player to incur a negative effect on the opponent.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class DebuffEntity extends PowerUpEntity {

    /**
     * {@code effect} will be applied to the opponent instead of the player that collected the powerup.
     *
     * @see PowerUpEntity#PowerUpEntity(Vector2, Vector2, PowerUpEffect, AnimatedSprite)
     */
    public DebuffEntity(Vector2 position, Vector2 initialVelocity, PowerUpEffect effect, AnimatedSprite sprite) {
        super(position, initialVelocity, effect, sprite);
    }

    @Override
    public void handleCollision(Entity otherEntity) {
        if (otherEntity instanceof Player player) {
            Player opponent = player.getOpponent();
            if (opponent == null)
                return;
            opponent.addPowerUpEffect(getEffect());
            destroy();
        }
    }
}
