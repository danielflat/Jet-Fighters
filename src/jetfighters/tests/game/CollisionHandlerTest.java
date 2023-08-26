package jetfighters.tests.game;

import jetfighters.game.handlers.CollisionHandler;
import jetfighters.game.handlers.EntityHandler;
import jetfighters.game.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
class CollisionHandlerTest {

    EntityHandler entityHandler;
    CollisionHandler collisionHandler;

    @BeforeEach
    void setUp() {
        entityHandler = new EntityHandler();
        collisionHandler = new CollisionHandler(entityHandler);
    }

    @Test
    void basicCollision() {
        AtomicInteger dummy1Collisions = new AtomicInteger();
        AtomicInteger dummy2Collisions = new AtomicInteger();
        AtomicInteger dummy3Collisions = new AtomicInteger();
        DummyEntity dummyEntity = new DummyEntity(new Vector2(0, 0), new Rectangle(10, 10),
                1, 1, (otherEntity) -> dummy1Collisions.getAndIncrement());
        DummyEntity dummyEntity2 = new DummyEntity(new Vector2(5, 5), new Rectangle(10, 10),
                1, 1, (otherEntity) -> dummy2Collisions.getAndIncrement());
        DummyEntity dummyEntity3 = new DummyEntity(new Vector2(15, 15), new Rectangle(10, 10),
                1, 1, (otherEntity) -> dummy3Collisions.getAndIncrement());
        entityHandler.addEntity(dummyEntity);
        entityHandler.addEntity(dummyEntity2);
        entityHandler.addEntity(dummyEntity3);
        entityHandler.updateEntities(0);
        collisionHandler.update();
        assertEquals(1, dummy1Collisions.get());
        assertEquals(1, dummy2Collisions.get());
        assertEquals(0, dummy3Collisions.get());
    }

    @Test
    void layerCollision() {
        AtomicInteger dummy1Collisions = new AtomicInteger();
        AtomicInteger dummy2Collisions = new AtomicInteger();
        AtomicInteger dummy3Collisions = new AtomicInteger();
        AtomicInteger dummy4Collisions = new AtomicInteger();
        DummyEntity dummyEntity = new DummyEntity(new Vector2(0, 0), new Rectangle(10, 10),
                1, 0, (otherEntity) -> dummy1Collisions.getAndIncrement());
        DummyEntity dummyEntity2 = new DummyEntity(new Vector2(5, 5), new Rectangle(10, 10),
                1, 1, (otherEntity) -> dummy2Collisions.getAndIncrement());
        DummyEntity dummyEntity3 = new DummyEntity(new Vector2(10, 10), new Rectangle(10, 10),
                4, 1, (otherEntity) -> dummy3Collisions.getAndIncrement());
        DummyEntity dummyEntity4 = new DummyEntity(new Vector2(7.5, 7.5), new Rectangle(10, 10),
                0, 1 | 2, (otherEntity) -> dummy4Collisions.getAndIncrement());
        entityHandler.addEntity(dummyEntity);
        entityHandler.addEntity(dummyEntity2);
        entityHandler.addEntity(dummyEntity3);
        entityHandler.addEntity(dummyEntity4);
        entityHandler.updateEntities(0);
        collisionHandler.update();
        assertEquals(0, dummy1Collisions.get(), "Collisions still happen even without mask");
        assertEquals(1, dummy2Collisions.get(), "Collisions work incorrectly with the same layer and mask");
        assertEquals(1, dummy3Collisions.get(), "Collisions work incorrectly with different layer and mask");
        assertEquals(2, dummy4Collisions.get(), "Multiple layer collisions don't seem to work");
    }

    @Test
    void complexShapes() {
        AtomicInteger dummy1Collisions = new AtomicInteger();
        AtomicInteger dummy2Collisions = new AtomicInteger();
        AtomicInteger dummy3Collisions = new AtomicInteger();
        DummyEntity dummyEntity = new DummyEntity(new Vector2(0, 0), new Ellipse2D.Double(0, 0, 10, 10),
                1, 1, (otherEntity) -> dummy1Collisions.getAndIncrement());
        DummyEntity dummyEntity2 = new DummyEntity(new Vector2(8, 8), new Ellipse2D.Double(0, 0, 10, 10),
                1, 1, (otherEntity) -> dummy2Collisions.getAndIncrement());
        DummyEntity dummyEntity3 = new DummyEntity(new Vector2(8 + 7, 8 + 7), new Ellipse2D.Double(0, 0, 10, 10),
                1, 1, (otherEntity) -> dummy3Collisions.getAndIncrement());
        entityHandler.addEntity(dummyEntity);
        entityHandler.addEntity(dummyEntity2);
        entityHandler.addEntity(dummyEntity3);
        entityHandler.updateEntities(0);
        collisionHandler.update();
        assertEquals(0, dummy1Collisions.get());
        assertEquals(1, dummy2Collisions.get());
        assertEquals(1, dummy3Collisions.get());
    }
}