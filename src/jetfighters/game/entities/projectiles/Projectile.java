package jetfighters.game.entities.projectiles;

import jetfighters.game.entities.Entity;
import jetfighters.game.entities.MovingEntity;
import jetfighters.game.entities.Player;
import jetfighters.game.math.Vector2;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * A Projectile that can be fired from a position with a velocity by a specific {@link ProjectileOwner}
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 21.07.22
 */
public class Projectile extends MovingEntity {

    private final double maxDistance;
    private final Vector2 startPosition;
    private final ProjectileOwner owner;

    /**
     * Creates a new Projectile from an initial position, a direction to fire in and the speed to fire at.
     *
     * @param owner     the owner of this projectile (should mostly be the entity that fired this projectile)
     * @param position  the initial position
     * @param direction the direction to fire in (will be normalized to prevent unwanted bugs)
     * @param speed     the speed at which the projectile will move
     */
    public Projectile(ProjectileOwner owner, Vector2 position, Vector2 direction, double speed) {
        this(owner, position, direction.normalized().mult(speed));
    }

    /**
     * Creates a new Projectile from an initial position and velocity.
     *
     * @param owner           the owner this projectile (should mostly be the entity that fired this projectile)
     * @param position        the initial position
     * @param initialVelocity the initial velocity
     */
    public Projectile(ProjectileOwner owner, Vector2 position, Vector2 initialVelocity) {
        super(position, new RoundRectangle2D.Double(
                        -2, -5,
                        4, 10, 3, 3),
                owner.getProjectileLayer(),
                owner.getProjectileMask(),
                initialVelocity.length(), initialVelocity);
        this.owner = owner;
        maxDistance = 1200;
        startPosition = position;
        rotation = Vector2.UP.angleTo(initialVelocity);
    }

    @Override
    public void update(double delta) {
        super.update(delta);

        if (startPosition.distanceTo(position) > maxDistance)
            destroy();
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.fill(getShape());
    }

    @Override
    public void handleCollision(Entity otherEntity) {
        if (otherEntity instanceof Player otherPlayer) {
            if (otherPlayer == owner)
                return;
            otherPlayer.damage(owner.getProjectileBaseDamage());
            destroy();
        } else if (otherEntity instanceof Projectile otherProjectile) {
            if (owner == otherProjectile.owner)
                return;
            destroy();
        }
    }
}
