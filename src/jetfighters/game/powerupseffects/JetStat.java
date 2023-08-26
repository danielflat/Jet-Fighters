package jetfighters.game.powerupseffects;

import java.util.LinkedList;
import java.util.List;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Represents a numerical "stat" of a jet like it's speed or the damage it deals.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class JetStat {

    private final double value;

    private final List<Double> baseMultipliers; // Applied to base value
    private final List<Double> additives; // Added to multiplied base value
    private final List<Double> fullMultipliers; // Applied to added value

    /**
     * Creates a new JetStat
     *
     * @param value the initial value
     */
    public JetStat(double value) {
        this.value = value;
        baseMultipliers = new LinkedList<>();
        additives = new LinkedList<>();
        fullMultipliers = new LinkedList<>();
    }

    /**
     * @return the value of this stat with all of its modifiers applied
     */
    public double getValue() {
        double val = value;
        for (Double d : baseMultipliers) {
            val *= d;
        }
        for (Double d : additives) {
            val += d;
        }
        for (Double d : fullMultipliers) {
            val *= d;
        }
        return val;
    }

    /**
     * Adds a new base multiplier that is applied to the base value of this stat before adding additive values.
     *
     * @param multiplier the base multiplier that should be added
     */
    public void addBaseMultiplier(double multiplier) {
        baseMultipliers.add(multiplier);
    }

    /**
     * Adds a new additive value to the stat that is to the final stat after applying the baseMultipliers, but before
     * applying full multipliers (therefore it will be multiplied by all full multipliers).
     *
     * @param additive the additive value that should be added
     */
    public void addAdditive(double additive) {
        additives.add(additive);
    }

    /**
     * Adds a new full multiplier that is applied to the final value after the base multipliers and additives are applied.
     * Therefor all additives will be multiplied with this value.
     *
     * @param multiplier the full multiplier that should be added
     */
    @SuppressWarnings("unused")
    public void addFullMultiplier(double multiplier) {
        fullMultipliers.add(multiplier);
    }

    /**
     * Removes the base multiplier with the value {@code multiplier}. <br>
     * If multiple multipliers with the same value are present only one will be removed.
     *
     * @param multiplier the base multiplier that should be removed
     * @see #addBaseMultiplier(double)
     */
    public void removeBaseMultiplier(double multiplier) {
        baseMultipliers.remove(multiplier);
    }

    /**
     * Removes the additive with the value {@code additive}. <br>
     * If multiple additives with the same value are present only one will be removed.
     *
     * @param additive the additive that should be removed
     * @see #addAdditive(double)
     */
    public void removeAdditive(double additive) {
        additives.remove(additive);
    }

    /**
     * Removes the full multiplier with the value {@code multiplier}. <br>
     * If multiple multipliers with the same value are present only one will be removed.
     *
     * @param multiplier the full multiplier that should be removed
     * @see #addFullMultiplier(double)
     */
    @SuppressWarnings("unused")
    public void removeFullMultiplier(double multiplier) {
        fullMultipliers.remove(multiplier);
    }

    /**
     * @return the base value of this stat without applying any additives or multipliers.
     */
    @SuppressWarnings("unused")
    public double getBaseValue() {
        return value;
    }


}
