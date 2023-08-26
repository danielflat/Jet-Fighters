package jetfighters.tests.game;

import jetfighters.game.Game;
import jetfighters.game.entities.JetEntity;
import jetfighters.game.math.Vector2;
import jetfighters.game.powerupseffects.JetStat;
import jetfighters.game.powerupseffects.StatEffect;
import jetfighters.windows.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
class JetEntityTest {

    private static final Window window = new Window(true);
    private static final int TEST_WIDTH = 100;
    private static final int TEST_HEIGHT = 100;
    private Game game;
    private JetEntity jetEntity;

    @BeforeEach
    void setUp() {
        game = new Game(TEST_WIDTH, TEST_HEIGHT, window);
        jetEntity = new JetEntity(game, new Vector2(0, 0), new Ellipse2D.Double(0, 0, 32, 32), 0, 0, -1) {
            @Override
            public int getProjectileLayer() {
                return 0;
            }

            @Override
            public int getProjectileMask() {
                return 0;
            }
        };
        game.addEntity(jetEntity);
    }

    @Test
    void moveOnUpdate() {
        Vector2 initialPosition = jetEntity.getPosition();
        Vector2 vel = Vector2.LEFT.mult(10);
        jetEntity.setVelocity(vel);
        assertEquals(initialPosition, jetEntity.getPosition());
        jetEntity.update(1);
        assertEquals(initialPosition.add(vel), jetEntity.getPosition());
        jetEntity.update(2);
        assertEquals(initialPosition.add(vel.mult(1 + 2)), jetEntity.getPosition());
    }

    @Test
    void maxVelocity() {
        jetEntity.setMaxVelocity(-1);
        assertEquals(-1, jetEntity.getMaxVelocity());
        Vector2 highVelocity = Vector2.RIGHT.mult(100000);
        jetEntity.setVelocity(highVelocity);
        assertEquals(highVelocity, jetEntity.getVelocity());
        jetEntity.setMaxVelocity(500);
        assertEquals(500, jetEntity.getMaxVelocity());
        assertEquals(Vector2.RIGHT.mult(500), jetEntity.getVelocity());
        jetEntity.setVelocity(Vector2.LEFT.mult(200));
        assertEquals(Vector2.LEFT.mult(200), jetEntity.getVelocity());
        jetEntity.setMaxVelocity(250);
        jetEntity.setVelocity(Vector2.UP.mult(1000));
        assertEquals(Vector2.UP.mult(250), jetEntity.getVelocity());
    }

    @Test
    void spinning() {
        assertFalse(jetEntity.isSpinning());
        assertTrue(jetEntity.canShoot());
        assertTrue(jetEntity.canControl());
        jetEntity.startSpinning(1, 10);
        assertTrue(jetEntity.isSpinning());
        assertFalse(jetEntity.canShoot());
        assertFalse(jetEntity.canControl());
        jetEntity.update(10);
        assertFalse(jetEntity.isSpinning());
        assertTrue(jetEntity.canShoot());
        assertTrue(jetEntity.canControl());
    }

    @Test
    void heal() {
        jetEntity.setCurrentHealth(1);
        assertEquals(1, jetEntity.getCurrentHealth(), "setCurrentHealth does not seem to work");
        jetEntity.heal(1);
        assertEquals(2, jetEntity.getCurrentHealth());
        jetEntity.heal(0);
        assertEquals(2, jetEntity.getCurrentHealth());
        jetEntity.heal(4);
        assertEquals(6, jetEntity.getCurrentHealth());
    }

    @Test
    void damage() {
        double originalHealth = jetEntity.getCurrentHealth();
        jetEntity.damage(1, true);
        assertEquals(originalHealth - 1, jetEntity.getCurrentHealth());
        jetEntity.damage(5, true);
        assertEquals(originalHealth - 6, jetEntity.getCurrentHealth());
        jetEntity.setCurrentHealth(10);
        assertEquals(10, jetEntity.getCurrentHealth());
        jetEntity.damage(5);
        assertEquals(5, jetEntity.getCurrentHealth());
        jetEntity.damage(5);
        assertEquals(5, jetEntity.getCurrentHealth(), "immunity is not working.");
    }

    @Test
    void dieOnDamage() {
        double originalHealth = jetEntity.getCurrentHealth();
        assertFalse(jetEntity.shouldDestroy());
        jetEntity.damage(originalHealth);
        assertTrue(jetEntity.shouldDestroy());
    }

    @Test
    void addAndRemovePowerUpEffects() {
        JetStat speedStat = jetEntity.getStat("speed");
        double initialValue = speedStat.getValue();
        StatEffect statEffect = new StatEffect("speed", 2, false, 1);
        jetEntity.addPowerUpEffect(statEffect);
        assertEquals(initialValue + 2, speedStat.getValue());
        jetEntity.removePowerUpEffect(statEffect);
        assertEquals(initialValue, speedStat.getValue());
        jetEntity.addPowerUpEffect(statEffect);
        jetEntity.addPowerUpEffect(statEffect);
        assertEquals(initialValue + 2, speedStat.getValue(), "Stat duration resetting does not work.");
        jetEntity.update(1);
        assertEquals(initialValue, speedStat.getValue());
    }
}