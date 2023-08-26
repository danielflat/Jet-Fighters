package jetfighters.game.entities.powerups;

import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.HealthEffect;
import jetfighters.game.sprites.AnimatedSprite;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Heals the player that collected this.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class HealthKitEntity extends PowerUpEntity {

    public HealthKitEntity(Vector2 position, Vector2 initialVelocity, double healAmount) {
        super(position, initialVelocity, new HealthEffect(healAmount), new AnimatedSprite("sprites/health_kit/Health_Kit_", 8, .125));
    }
}
