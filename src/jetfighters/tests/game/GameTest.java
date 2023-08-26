package jetfighters.tests.game;

import jetfighters.game.Game;
import jetfighters.game.tween.Tween;
import jetfighters.game.tween.TweenMode;
import jetfighters.windows.Window;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
class GameTest {

    private Game game;
    private static final Window window = new Window(true);
    private static final int TEST_WIDTH = 100;
    private static final int TEST_HEIGHT = 100;

    @BeforeEach
    void setUp() {
        game = new Game(TEST_WIDTH, TEST_HEIGHT, window);
        window.add(game);
        game.initBuffer();
        game.startGameLoop();
    }

    @AfterEach
    void tearDown() {
        game.stopGameLoop();
        window.remove(game);
    }

    @Test
    void initBuffer() {
        assertNotNull(game.getBufferStrategy(), "BufferStrategy was not properly initialized from setup (initBuffer should have been called before this test).");

    }

    @Test
    void addEntityAndUpdate() {
        LimitedTimeCondition entityUpdated = new LimitedTimeCondition(1, TimeUnit.SECONDS);
        LimitedTimeCondition entityRendered = new LimitedTimeCondition(1, TimeUnit.SECONDS);
        DummyEntity dummy = new DummyEntity(entityRendered, entityUpdated);
        game.addEntity(dummy);
        assertTrue(entityUpdated.waitForConditionToBeMet(), "entity was not updated after adding to game.");
        assertTrue(entityRendered.waitForConditionToBeMet(), "entity was not rendered after adding to game.");
    }

    @Test
    void addTweenAndUpdate() {
        LimitedTimeCondition tweenFinished = new LimitedTimeCondition(1, TimeUnit.SECONDS);
        Tween tween = new Tween.Double(.1, 0, 1, TweenMode.Linear, (val) -> {
        }, tweenFinished::conditionWasMet);
        game.addTween(tween);
        assertTrue(tweenFinished.waitForConditionToBeMet(), "tween didn't finish in reasonable time after being added.");
    }

    @Test
    void getCanvasWidth() {
        assertEquals(TEST_WIDTH, game.getCanvasWidth(), "canvas wasn't set to the correct width with init buffer");
    }

    @Test
    void getCanvasHeight() {
        assertEquals(TEST_HEIGHT, game.getCanvasHeight(), "canvas wasn't set to the correct height with init buffer");
    }

    @Test
    void getPlayer1AndOpponent() {
        assertNotNull(game.getPlayer1());
        assertNotNull(game.getPlayer1().getOpponent());
        assertSame(game.getPlayer1().getOpponent(), game.getPlayer2());
        assertNotSame(game.getPlayer1().getOpponent(), game.getPlayer1());
    }

    @Test
    void getPlayer2AndOpponent() {
        assertNotNull(game.getPlayer2());
        assertNotNull(game.getPlayer2().getOpponent());
        assertSame(game.getPlayer2().getOpponent(), game.getPlayer1());
        assertNotSame(game.getPlayer2().getOpponent(), game.getPlayer2());
    }

    @Test
    void setGameSpeed() {
        assertEquals(1, game.getGameSpeed());
        game.setGameSpeed(.5);
        assertEquals(.5, game.getGameSpeed());
        game.setGameSpeed(1.25);
        assertEquals(1.25, game.getGameSpeed());
        game.setGameSpeed(1);
        assertEquals(1, game.getGameSpeed());
    }
}