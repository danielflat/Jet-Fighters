package jetfighters.tests.game;


import jetfighters.game.entities.Entity;
import jetfighters.game.math.Vector2;

import java.awt.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Dummy entity that is only used for testing to check if certain methods are called.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
class DummyEntity extends Entity {

    private int updateCount;
    private int drawCount;

    private LimitedTimeCondition renderedLatch;
    private LimitedTimeCondition updatedLatch;

    private CollisionCallback collisionCallback;

    public DummyEntity() {
        super(new Vector2(0, 0), new Rectangle(10, 10), 0, 0);
    }

    public DummyEntity(Vector2 position, Shape shape, int collisionLayer, int collisionMask, CollisionCallback collisionCallback) {
        this();
        this.position = position;
        this.shape = shape;
        this.collisionLayer = collisionLayer;
        this.collisionMask = collisionMask;
        this.collisionCallback = collisionCallback;
    }

    public DummyEntity(LimitedTimeCondition renderedLatch, LimitedTimeCondition updatedLatch) {
        this();
        this.renderedLatch = renderedLatch;
        this.updatedLatch = updatedLatch;
    }

    @Override
    public void update(double delta) {
        updateCount++;
        if (updateCount > 3 && updatedLatch != null)
            updatedLatch.conditionWasMet();
    }

    @Override
    public void draw(Graphics2D g2d) {
        drawCount++;
        if (drawCount > 3 && renderedLatch != null)
            renderedLatch.conditionWasMet();
    }

    @Override
    public void handleCollision(Entity otherEntity) {
        collisionCallback.call(otherEntity);
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public int getDrawCount() {
        return drawCount;
    }
}