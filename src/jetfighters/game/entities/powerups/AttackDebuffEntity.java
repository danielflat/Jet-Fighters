package jetfighters.game.entities.powerups;

import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.StatEffect;
import jetfighters.game.sprites.AnimatedSprite;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Reduces the attack damage of the opponent of the player that collected this for a set duration.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class AttackDebuffEntity extends DebuffEntity {

    public AttackDebuffEntity(Vector2 position, Vector2 initialVelocity, double effectValue, double durationValue) {
        super(position, initialVelocity, new StatEffect("projectileDamage", effectValue, true,
                durationValue), new AnimatedSprite("sprites/attack_debuff/Attack_Debuff_",
                20, .1));
    }
}
