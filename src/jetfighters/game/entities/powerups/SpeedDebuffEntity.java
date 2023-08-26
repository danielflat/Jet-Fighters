package jetfighters.game.entities.powerups;

import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.StatEffect;
import jetfighters.game.sprites.AnimatedSprite;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Makes the opponent of the player that collected this slower for a set duration.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class SpeedDebuffEntity extends DebuffEntity {

    public SpeedDebuffEntity(Vector2 position, Vector2 initialVelocity, double effectValue, double durationValue) {
        super(position, initialVelocity, new StatEffect("speed", effectValue, true,
                durationValue), new AnimatedSprite("sprites/speed_debuff/Speed_Debuff_",
                16, .1));
    }
}
