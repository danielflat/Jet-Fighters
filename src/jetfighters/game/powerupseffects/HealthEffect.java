package jetfighters.game.powerupseffects;

import jetfighters.game.entities.JetEntity;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * A PowerUpEffect that heals a player as soon as it is applied. <br>
 * As it has a duration of 0 it will only be applied once and removed in the next update.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class HealthEffect extends PowerUpEffect {

    private final double healAmount;

    /**
     * Creates a new PowerupEffect that adds {@code healAmount} health to the {@link JetEntity} this is applied to.
     *
     * @param healAmount the amount
     */
    public HealthEffect(double healAmount) {
        super(0);
        this.healAmount = healAmount;
    }

    @Override
    public void applyEffect(JetEntity jetEntity) {
        if (healAmount >= 0)
            jetEntity.heal(healAmount);
        else
            jetEntity.damage(healAmount);
    }

    @Override
    public void removeEffect(JetEntity jetEntity) {

    }

}
