package jetfighters.game.entities.powerups;

import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.StatEffect;
import jetfighters.game.sprites.AnimatedSprite;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Makes the player that collected this faster for a set duration.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class SpeedBuffEntity extends PowerUpEntity {

    public SpeedBuffEntity(Vector2 position, Vector2 initialVelocity, double effectValue, double durationValue) {
        super(position, initialVelocity, new StatEffect("speed", effectValue, true,
                durationValue), new AnimatedSprite("sprites/speed_buff/Speed_Buff_",
                16, .1));
    }
}
