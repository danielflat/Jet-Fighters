package jetfighters.game.entities.enemies;

import jetfighters.game.Game;
import jetfighters.game.entities.JetEntity;
import jetfighters.game.handlers.CollisionHandler;
import jetfighters.game.math.Vector2;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Base class for all Enemies that are Jets
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public abstract class EnemyJet extends JetEntity {


    /**
     * Initializes this EnemyJet using {@link JetEntity#JetEntity(Game, Vector2, Shape, int, int, double, String)}
     *
     * @param game         the game object this exists in
     * @param position     the initial position of this jet
     * @param maxVelocity  the maximum velocity this jet should be able to reach
     * @param jetImagePath relative path to an image to use for this jet (relative to "sprites/")
     */
    public EnemyJet(Game game, Vector2 position, double maxVelocity, String jetImagePath) {
        super(game, position, new Ellipse2D.Double(0, 0, 32, 30), CollisionHandler.ENEMY_LAYER, CollisionHandler.PLAYER_LAYER, maxVelocity, jetImagePath);  //TODO: 30 anstatt 32
        shape = getOutline();
    }

    @Override
    public int getProjectileLayer() {
        return CollisionHandler.PROJECTILE_ENEMY_LAYER;
    }

    @Override
    public int getProjectileMask() {
        return CollisionHandler.PLAYER_LAYER;
    }
}
