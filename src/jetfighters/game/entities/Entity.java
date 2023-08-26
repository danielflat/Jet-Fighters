package jetfighters.game.entities;

import jetfighters.game.math.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Base Entity class. Represents an object in the game world which has a position, rotation, scale and shape. <br>
 * It can also collide with other entities based on it's shape and position.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public abstract class Entity {

    protected Vector2 position;
    protected double rotation; // Rotation des Schiffs in Bogenmaß
    protected Vector2 scale;
    protected Shape shape;

    protected int collisionLayer;
    protected int collisionMask;

    private boolean queueDestroy; // True if it will be removed next update()

    /**
     * Creates a new Entity
     *
     * @param position       the initial position
     * @param shape          the shape used for collision detection
     * @param collisionLayer the layer this entity should exist on
     * @param collisionMask  the layer this entity detects collisions on
     */
    public Entity(Vector2 position, Shape shape, int collisionLayer, int collisionMask) {
        this.position = position;
        this.shape = shape;
        this.collisionLayer = collisionLayer;
        this.collisionMask = collisionMask;
        rotation = 0;
        scale = Vector2.ONE;
    }

    /**
     * Update method that should be called every time the game updates with the time since the last update (delta time).
     * This method should handle everything that is timing or physics related.
     *
     * @param delta the time that passed since the last time the update function was called (while unpaused)
     */
    public abstract void update(double delta);

    /**
     * Render method that should be called every time the game is drawn on the screen.
     * This method should draw the object it represents on the screen with the graphics object provided.
     *
     * @param g2d the graphics context this entity should be drawn to
     */
    public abstract void draw(Graphics2D g2d);

    /**
     * Marks this entity for destruction. It will be destroyed after this and before the next {@link #update(double)} call.
     * It will no longer be able to collide, as it will have its collision layer and mask set to 0.
     */
    public void destroy() {
        queueDestroy = true;
        collisionLayer = 0;
        collisionMask = 0;
    }

    /**
     * Returns the transform that is applied to this entity
     *
     * @return the transform
     */
    public AffineTransform getTransform() {
        AffineTransform transform = new AffineTransform();
        transform.rotate(rotation, position.x + shape.getBounds().getWidth() * scale.x / 2.0, position.y + shape.getBounds().getHeight() * scale.y / 2.0);
        transform.translate(position.x, position.y);
        transform.scale(scale.x, scale.y);
        return transform;
    }

    /**
     * Returns a scaled, rotated and translated version of this entities shape.
     *
     * @return the transformed shape of this entity
     */
    public Shape getTransformedShape() {
        return getTransform().createTransformedShape(shape);
    }

    /**
     * This will be called whenever this entity collides with another entity (an entity whose layer is equal to this mask)
     *
     * @param otherEntity the entity that was collided with
     */
    public void handleCollision(Entity otherEntity) {
    } // left empty to be overwritten, not abstract as it is not mandatory

    /**
     * Checks if this entity is currently colliding with {@code otherEntity}.
     *
     * @param otherEntity the entity to check collision with
     * @return true if the two entities are colliding
     */
    public boolean isCollidingWith(Entity otherEntity) {
        Area area = new Area(getTransformedShape());
        area.intersect(new Area(otherEntity.getTransformedShape()));
        return !area.isEmpty();
    }

    /**
     * @return the direction this entity is facing
     */
    public Vector2 getDirection() {
        return Vector2.UP.rotated(rotation);
    }

    /**
     * @return true if this entity is marked for destruction and therefore should be removed.
     */
    public boolean shouldDestroy() {
        return queueDestroy;
    }

    /**
     * @return the current position of this entity
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @return the shape this entity uses for collision detection
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * @return this entity's collision layer (the layers it exists on)
     */
    public int getCollisionLayer() {
        return collisionLayer;
    }

    /**
     * @return this entity's collision mask (the layers it should detect collisions on)
     */
    public int getCollisionMask() {
        return collisionMask;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * @return the current rotation of this entity in radians
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of this entity
     *
     * @param rotation the new rotation of this entity in radians
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * @return the scale of this entity as a vector (x-component = width-scaling, y-component = height-scaling)
     */
    public Vector2 getScale() {
        return scale;
    }

    /**
     * Sets the scale of this entity
     *
     * @param scale the new scale of this entity
     */
    public void setScale(Vector2 scale) {
        this.scale = scale;
    }
}
