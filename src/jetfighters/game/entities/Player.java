package jetfighters.game.entities;

import jetfighters.game.Game;
import jetfighters.game.handlers.CollisionHandler;
import jetfighters.game.math.Vector2;

import java.awt.geom.Ellipse2D;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class Player extends JetEntity {

    private boolean turnLeft;
    private boolean turnRight;
    private boolean moveForward;
    private boolean moveBackwards;

    private final int playerID;

    private Player opponent;

    /**
     * Creates a new player that can be controlled through the input methods: <br>
     * {@link #setTurnLeft(boolean)}, {@link #setTurnRight(boolean)}, {@link #setMoveForward(boolean)} and {@link #setMoveBackwards(boolean)}.
     *
     * @param playerID the ID used for this player (should normally be 1 or 2)
     * @param position the initial position of this player
     * @param game     the game the player should exist in
     */
    public Player(int playerID, Vector2 position, Game game) {
        super(game, position, new Ellipse2D.Double(0, 0, 32, 30),   //TODO: 30 statt 32 bei height?
                CollisionHandler.PLAYER_LAYER, CollisionHandler.PLAYER_LAYER | CollisionHandler.ENEMY_LAYER,
                300, "jet/jet" + playerID + "/Jet_" + playerID + "_1" + ".png");
        this.playerID = playerID;
        shape = getOutline(); // Needs to happen after constructor as jetSprite is needed for the outline
    }

    @Override
    public void update(double delta) {
        int turnDir;
        int moveDir = 0;
        if (canControl()) {
            moveDir = (moveBackwards ? -1 : 0) + (moveForward ? 1 : 0);
            turnDir = (turnLeft ? -1 : 0) + (turnRight ? 1 : 0);
            angularVelocity = turnDir * rotationSpeed.getValue();
            acceleration = getDirection().mult(speed.getValue()).mult(moveDir); // Brauch kein delta, da gesetzt wird, nicht addiert.
        }
        if (shouldSlowDown()) {
            // Slowly stop the plane when no input is given
            if (moveDir == 0) {
                velocity = velocity.lerp(Vector2.ZERO, delta);
            } else {
                boolean fastVel = Math.abs(getDirection().mult(moveDir).angleTo(velocity)) > Math.PI / 4.0;
                // Make the plane have sharper turns when turning quickly.
                velocity = velocity.lerp(velocity.rotatedTo(getDirection().mult(moveDir).angle()), fastVel ? .05 : .01);
            }
        }

        super.update(delta); // Applies velocity and acceleration, shoots, applies status effects like spinning and screen-wraps
    }

    @Override
    public void die() {
        super.die();
        game.gameOver(this);
    }

    /**
     * This method should be used to set the current state of the corresponding key for this action. <br>
     * Therefore this should be set to true whenever the key to turn this player to the left is pressed and false whenever it is released.
     *
     * @param turnLeft true if the player should turn left, false otherwise
     */
    public void setTurnLeft(boolean turnLeft) {
        this.turnLeft = turnLeft;
    }

    /**
     * This method should be used to set the current state of the corresponding key for this action. <br>
     * Therefore this should be set to true whenever the key to turn this player to the right is pressed and false whenever it is released.
     *
     * @param turnRight true if the player should turn right, false otherwise
     */
    public void setTurnRight(boolean turnRight) {
        this.turnRight = turnRight;
    }

    /**
     * This method should be used to set the current state of the corresponding key for this action. <br>
     * Therefore this should be set to true whenever the key to move this player forwards (/accelerate) is pressed and false whenever it is released.
     *
     * @param moveForward true if the player should turn right, false otherwise
     */
    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }

    /**
     * This method should be used to set the current state of the corresponding key for this action. <br>
     * Therefore this should be set to true whenever the key to move this player backwards (/decelerate) is pressed and false whenever it is released.
     *
     * @param moveBackwards true if the player should turn right, false otherwise
     */
    public void setMoveBackwards(boolean moveBackwards) {
        this.moveBackwards = moveBackwards;
    }

    /**
     * @return the id of this player
     */
    public int getPlayerID() {
        return playerID;
    }

    public int getProjectileLayer() {
        if (playerID == 1) {
            return CollisionHandler.PROJECTILE1_LAYER;
        } else if (playerID == 2) {
            return CollisionHandler.PROJECTILE2_LAYER;
        }
        return 0;
    }

    public int getProjectileMask() {
        int layer = CollisionHandler.PLAYER_LAYER;
        if (playerID == 1) {
            layer = layer | CollisionHandler.PROJECTILE2_LAYER;
        } else if (playerID == 2) {
            layer = layer | CollisionHandler.PROJECTILE1_LAYER;
        }
        return layer;
    }

    /**
     * Sets the opponent of this player, should be set whenever playing versus mode.
     *
     * @param opponent the new opponent of this player
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * @return the current opponent of this player
     */
    public Player getOpponent() {
        return opponent;
    }
}
