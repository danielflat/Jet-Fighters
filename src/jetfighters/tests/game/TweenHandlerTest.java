package jetfighters.tests.game;

import jetfighters.game.handlers.TweenHandler;
import jetfighters.game.tween.Tween;
import jetfighters.game.tween.TweenMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
class TweenHandlerTest {

    TweenHandler tweenHandler;

    @BeforeEach
    void setUp() {
        tweenHandler = new TweenHandler();
    }

    @Test
    void addTween() {
        Tween dummyTween = new Tween.Double(1, 0, 0, TweenMode.Linear, (val) -> {
        });
        Tween dummyTween2 = new Tween.Double(1, 0, 0, TweenMode.Linear, (val) -> {
        });
        Tween dummyTween3 = new Tween.Double(1, 0, 0, TweenMode.Linear, (val) -> {
        });
        tweenHandler.addTween(dummyTween);
        tweenHandler.updateTweens(0);
        assertTrue(tweenHandler.getTweens().contains(dummyTween));
        assertFalse(tweenHandler.getTweens().contains(dummyTween2));
        assertFalse(tweenHandler.getTweens().contains(dummyTween3));
        tweenHandler.addTween(dummyTween3);
        tweenHandler.updateTweens(0);
        assertTrue(tweenHandler.getTweens().contains(dummyTween3));
        assertFalse(tweenHandler.getTweens().contains(dummyTween2));
    }

    @Test
    void removeEntity() {
        Tween dummyTween = new Tween.Double(1, 0, 0, TweenMode.Linear, (val) -> {
        });
        Tween dummyTween2 = new Tween.Double(1, 0, 0, TweenMode.Linear, (val) -> {
        });
        Tween dummyTween3 = new Tween.Double(1, 0, 0, TweenMode.Linear, (val) -> {
        });
        tweenHandler.addTween(dummyTween);
        tweenHandler.addTween(dummyTween2);
        tweenHandler.addTween(dummyTween3);
        tweenHandler.updateTweens(0);
        assertTrue(tweenHandler.getTweens().contains(dummyTween), "addTween does not seem to work.");
        tweenHandler.removeEntity(dummyTween);
        tweenHandler.updateTweens(0);
        assertFalse(tweenHandler.getTweens().contains(dummyTween));
        assertTrue(tweenHandler.getTweens().contains(dummyTween2));
        assertTrue(tweenHandler.getTweens().contains(dummyTween3));
        tweenHandler.removeEntity(dummyTween2);
        tweenHandler.updateTweens(0);
        assertFalse(tweenHandler.getTweens().contains(dummyTween));
        assertFalse(tweenHandler.getTweens().contains(dummyTween2));
        assertTrue(tweenHandler.getTweens().contains(dummyTween3));
        tweenHandler.removeEntity(dummyTween3);
        tweenHandler.updateTweens(0);
        assertFalse(tweenHandler.getTweens().contains(dummyTween));
        assertFalse(tweenHandler.getTweens().contains(dummyTween2));
        assertFalse(tweenHandler.getTweens().contains(dummyTween3));
        tweenHandler.addTween(dummyTween);
        tweenHandler.updateTweens(0);
        assertTrue(tweenHandler.getTweens().contains(dummyTween));
        tweenHandler.removeEntity(dummyTween);
        tweenHandler.updateTweens(0);
        assertFalse(tweenHandler.getTweens().contains(dummyTween));
    }

    @Test
    void updateTweens() {
        AtomicInteger updateCount1 = new AtomicInteger();
        AtomicInteger updateCount2 = new AtomicInteger();
        Tween dummyTween = new Tween.Double(1, 0, 1, TweenMode.Linear, (val) -> updateCount1.incrementAndGet());
        Tween dummyTween2 = new Tween.Double(1, 0, 1, TweenMode.Linear, (val) -> updateCount2.incrementAndGet());
        tweenHandler.addTween(dummyTween);
        tweenHandler.addTween(dummyTween2);
        tweenHandler.updateTweens(0);
        assertEquals(1, updateCount1.get());
        assertEquals(1, updateCount2.get());
        tweenHandler.updateTweens(0.2);
        assertEquals(2, updateCount1.get());
        assertEquals(2, updateCount2.get());
        tweenHandler.removeEntity(dummyTween2);
        tweenHandler.updateTweens(0.2);
        tweenHandler.updateTweens(0.2);
        assertEquals(4, updateCount1.get());
        assertEquals(2, updateCount2.get());
        tweenHandler.updateTweens(.400001); // Needs to be slightly higher due to rounding errors
        assertEquals(5, updateCount1.get());
        assertFalse(tweenHandler.getTweens().contains(dummyTween), "The tween is not automatically removed when it is finished");
    }
}