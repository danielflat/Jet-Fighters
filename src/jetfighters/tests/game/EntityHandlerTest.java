package jetfighters.tests.game;

import jetfighters.game.handlers.EntityHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
class EntityHandlerTest {

    EntityHandler entityHandler;

    @BeforeEach
    void setUp() {
        entityHandler = new EntityHandler();
    }

    @Test
    void addEntity() {
        DummyEntity dummyEntity = new DummyEntity();
        DummyEntity dummyEntity2 = new DummyEntity();
        DummyEntity dummyEntity3 = new DummyEntity();
        entityHandler.addEntity(dummyEntity);
        entityHandler.updateEntities(0);
        assertTrue(entityHandler.getEntities().contains(dummyEntity));
        assertFalse(entityHandler.getEntities().contains(dummyEntity2));
        assertFalse(entityHandler.getEntities().contains(dummyEntity3));
        entityHandler.addEntity(dummyEntity3);
        entityHandler.updateEntities(0);
        assertTrue(entityHandler.getEntities().contains(dummyEntity3));
        assertFalse(entityHandler.getEntities().contains(dummyEntity2));
    }

    @Test
    void removeEntity() {
        DummyEntity dummyEntity = new DummyEntity();
        DummyEntity dummyEntity2 = new DummyEntity();
        DummyEntity dummyEntity3 = new DummyEntity();
        entityHandler.addEntity(dummyEntity);
        entityHandler.addEntity(dummyEntity2);
        entityHandler.addEntity(dummyEntity3);
        entityHandler.updateEntities(0);
        assertTrue(entityHandler.getEntities().contains(dummyEntity), "addEntity does not seem to work.");
        entityHandler.removeEntity(dummyEntity);
        entityHandler.updateEntities(0);
        assertFalse(entityHandler.getEntities().contains(dummyEntity));
        assertTrue(entityHandler.getEntities().contains(dummyEntity2));
        assertTrue(entityHandler.getEntities().contains(dummyEntity3));
        entityHandler.removeEntity(dummyEntity2);
        entityHandler.updateEntities(0);
        assertFalse(entityHandler.getEntities().contains(dummyEntity));
        assertFalse(entityHandler.getEntities().contains(dummyEntity2));
        assertTrue(entityHandler.getEntities().contains(dummyEntity3));
        entityHandler.removeEntity(dummyEntity3);
        entityHandler.updateEntities(0);
        assertFalse(entityHandler.getEntities().contains(dummyEntity));
        assertFalse(entityHandler.getEntities().contains(dummyEntity2));
        assertFalse(entityHandler.getEntities().contains(dummyEntity3));
        entityHandler.addEntity(dummyEntity);
        entityHandler.updateEntities(0);
        assertTrue(entityHandler.getEntities().contains(dummyEntity));
        entityHandler.removeEntity(dummyEntity);
        entityHandler.updateEntities(0);
        assertFalse(entityHandler.getEntities().contains(dummyEntity));
    }

    @Test
    void updateEntities() {
        DummyEntity dummyEntity = new DummyEntity();
        DummyEntity dummyEntity2 = new DummyEntity();
        entityHandler.addEntity(dummyEntity);
        entityHandler.addEntity(dummyEntity2);
        entityHandler.updateEntities(0.1);
        assertEquals(1, dummyEntity.getUpdateCount());
        assertEquals(1, dummyEntity2.getUpdateCount());
        entityHandler.updateEntities(0.1);
        assertEquals(2, dummyEntity.getUpdateCount());
        assertEquals(2, dummyEntity2.getUpdateCount());
        entityHandler.removeEntity(dummyEntity2);
        entityHandler.updateEntities(0.1);
        entityHandler.updateEntities(0.1);
        assertEquals(4, dummyEntity.getUpdateCount());
        assertEquals(2, dummyEntity2.getUpdateCount());
    }

    @Test
    void drawEntities() {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        Graphics2D g2d = (Graphics2D) frame.getGraphics();
        DummyEntity dummyEntity = new DummyEntity();
        DummyEntity dummyEntity2 = new DummyEntity();
        entityHandler.addEntity(dummyEntity);
        entityHandler.addEntity(dummyEntity2);
        entityHandler.updateEntities(0.1);
        entityHandler.drawEntities(g2d);
        assertEquals(1, dummyEntity.getDrawCount());
        assertEquals(1, dummyEntity2.getDrawCount());
        entityHandler.drawEntities(g2d);
        assertEquals(2, dummyEntity.getDrawCount());
        assertEquals(2, dummyEntity2.getDrawCount());
        entityHandler.removeEntity(dummyEntity2);
        entityHandler.updateEntities(0);
        entityHandler.drawEntities(g2d);
        entityHandler.drawEntities(g2d);
        assertEquals(4, dummyEntity.getDrawCount());
        assertEquals(2, dummyEntity2.getDrawCount());
    }
}