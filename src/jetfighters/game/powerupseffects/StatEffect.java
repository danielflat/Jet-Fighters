package jetfighters.game.powerupseffects;

import jetfighters.game.entities.JetEntity;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * A PowerUpEffect that modifies a single stat of a JetEntity. <br>
 * The modification can be additive or multiplicative.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class StatEffect extends PowerUpEffect {

    private final String statName;
    private final double value;
    private final boolean multiplicative;
    private boolean isApplied;

    /**
     * Creates a new StatEffect that modifies the stat of a JetEntity.
     *
     * @param statName       the name of the field of the stat from JetEntity that should be modified (e.g. {@link JetEntity#speed}).
     * @param value          the value to add to or multiply the jet stat with
     * @param multiplicative if this is false the value will be added, if true it will multiply the stat.
     */
    @SuppressWarnings("JavadocReference")
    public StatEffect(String statName, double value, boolean multiplicative, double duration) {
        super(duration);
        this.statName = statName;
        this.value = value;
        this.multiplicative = multiplicative;
    }

    @Override
    public void applyEffect(JetEntity jetEntity) {
        if (isApplied)
            return;
        JetStat stat = jetEntity.getStat(statName);
        if (stat == null) {
            System.err.println("Couldn't find stat: " + statName + " in JetEntity " + jetEntity.getClass().getName());
            return;
        }
        if (multiplicative) {
            stat.addBaseMultiplier(value);
        } else {
            stat.addAdditive(value);
        }
        isApplied = true;
    }

    @Override
    public void removeEffect(JetEntity jetEntity) {
        if (!isApplied) return;
        JetStat stat = jetEntity.getStat(statName);
        if (stat == null) {
            System.err.println("Couldn't find stat: " + statName + " in JetEntity " + jetEntity.getClass().getName());
            return;
        }
        if (multiplicative) {
            stat.removeBaseMultiplier(value);
        } else {
            stat.removeAdditive(value);
        }
        isApplied = false;
    }

    /**
     * @return the name of the field of the stat that is modified by this effect
     */
    public String getStatName() {
        return statName;
    }
}
