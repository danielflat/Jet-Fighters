package jetfighters.game.entities.projectiles;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 21.07.22
 */
public interface ProjectileOwner {

    /**
     * @return the collision layer of the projectiles originating from this
     */
    int getProjectileLayer();

    /**
     * @return the collision mask of the projectiles originating from this (defines what the projectiles collide with)
     */
    int getProjectileMask();

    /**
     * @return the base amount of damage the projectiles should
     */
    double getProjectileBaseDamage();

}
