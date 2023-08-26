package jetfighters.game.entities;

import jetfighters.game.math.Vector2;

import java.awt.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Represents an entity that can move with acceleration, velocity and angular velocity. <br>
 * Also provides methods to aid physics calculations on moving entities.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public abstract class MovingEntity extends Entity {

    protected Vector2 velocity;
    protected double angularVelocity;
    private Vector2 lastPosition;
    private Vector2 lastVelocity; // Used for physics calculation should not be exposed, as it is set each update (with velocity)
    protected double maxVelocity;
    protected Vector2 acceleration;

    /**
     * {@code initialVelocity} defaults to {@link Vector2#ZERO}.
     * {@code initialAcceleration} defaults to {@link Vector2#ZERO}.
     *
     * @see #MovingEntity(Vector2, Shape, int, int, double, Vector2, Vector2)
     */
    public MovingEntity(Vector2 position, Shape shape, int collisionLayer, int collisionMask, double maxVelocity) {
        this(position, shape, collisionLayer, collisionMask, maxVelocity, Vector2.ZERO);
    }

    /**
     * {@code initialAcceleration} defaults to {@link Vector2#ZERO}.
     *
     * @see #MovingEntity(Vector2, Shape, int, int, double, Vector2, Vector2)
     */
    public MovingEntity(Vector2 position, Shape shape, int collisionLayer, int collisionMask, double maxVelocity, Vector2 initialVelocity) {
        this(position, shape, collisionLayer, collisionMask, maxVelocity, initialVelocity, Vector2.ZERO);
    }

    /**
     * Creates a new MovingEntity at {@code position} with the shape {@code shape} (used for collision) on {@code collisionLayer}
     * detecting collisions on {@code collisionMask} having a maximum velocity of {@code maxVelocity}.
     * It also starts with {@code initialVelocity} and {@code initialAcceleration}.
     *
     * @param position            the initial position of this Entity
     * @param shape               the shape of this entity that is used for collision detection
     * @param collisionLayer      the layer to be detected on for collisions (see {@link jetfighters.game.handlers.CollisionHandler})
     * @param collisionMask       the mask to detect collision with (see {@link jetfighters.game.handlers.CollisionHandler})
     * @param maxVelocity         the maximum velocity this Entity can reach. if negative the velocity is not capped
     * @param initialVelocity     the velocity this Entity has right after creation
     * @param initialAcceleration the acceleration this Entity has right after creation
     * @see Entity#Entity(Vector2, Shape, int, int)
     */
    public MovingEntity(Vector2 position, Shape shape, int collisionLayer, int collisionMask, double maxVelocity, Vector2 initialVelocity, Vector2 initialAcceleration) {
        super(position, shape, collisionLayer, collisionMask);
        this.maxVelocity = maxVelocity;
        velocity = initialVelocity;
        acceleration = initialAcceleration;
    }

    /**
     * Sets this entity's velocity and clamps it to maxVelocity (meaning if {@code velocity.length() > maxVelocity})
     * the velocities length will be set to {@code maxVelocity}.
     *
     * @param velocity the new velocity
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
        if (maxVelocity >= 0 && this.velocity.length() > maxVelocity) {
            this.velocity = velocity.normalized().mult(maxVelocity);
        }
    }

    /**
     * Applies the angular velocity to the rotation with delta time. <br>
     * Should only be called manually if {@link #update(double)} is not called.
     *
     * @param delta delta time used for physics calculations
     */
    protected void applyAngularVelocity(double delta) {
        rotation += angularVelocity * delta;
    }

    @Override
    public void update(double delta) {
        applyAngularVelocity(delta);

        setVelocity(velocity.add(acceleration.mult(delta)));
        lastVelocity = velocity;

        position = position.add(lastVelocity.mult(delta));
        lastPosition = position;
    }

    /**
     * Returns the position of this object from the last update step (useful if position is changed but still needed)
     *
     * @return the position of the last update step
     */
    @SuppressWarnings("unused")
    public Vector2 getLastPosition() {
        return lastPosition;
    }

    /**
     * Returns the velocity this object had in the last update step (useful if velocity is changed but still needed)
     *
     * @return the velocity of the last update step
     */
    public Vector2 getLastVelocity() {
        return lastVelocity;
    }

    /**
     * @return the current velocity of this entity; could have been modified in the current update step (to get the real value use {@link #getLastVelocity()})
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * @return the maximum velocity this entity can reach
     */
    public double getMaxVelocity() {
        return maxVelocity;
    }

    /**
     * Sets the maximum velocity of this entity so that velocity may not exceed it and sets the current velocity to match this.
     * If negative the maximum velocity will not be capped.
     *
     * @param maxVelocity the maximum velocity for this entity
     */
    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
        setVelocity(velocity);
    }
}
