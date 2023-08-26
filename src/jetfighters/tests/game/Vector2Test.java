package jetfighters.tests.game;

import jetfighters.game.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
class Vector2Test {

    private double isClose(double value, double to) {
        return isClose(value, to, .0001);
    }

    private double isClose(double value, double to, double threshold) {
        double dist = Math.abs(value - to);
        return dist < threshold ? 0 : dist;
    }

    private double isClose(Vector2 v1, Vector2 v2) {
        return isClose(v1.x - v2.x, 0) + isClose(v1.y - v2.y, 0);
    }

    @Test
    void toCartesian() {
        assertEquals(0, isClose(Vector2.LEFT, Vector2.toCartesian(1, Math.PI)));
        assertNotEquals(0, isClose(Vector2.RIGHT, Vector2.toCartesian(1, Math.PI)));
        assertEquals(0, isClose(Vector2.DOWN, Vector2.toCartesian(1, Math.PI / 2)));
        assertNotEquals(0, isClose(Vector2.UP, Vector2.toCartesian(1, Math.PI / 2)));
    }

    @Test
    void add() {
        assertEquals(new Vector2(2, 0), Vector2.RIGHT.add(1, 0));
        assertEquals(new Vector2(0, 0), Vector2.LEFT.add(1, 0));
        assertEquals(new Vector2(999, 0), Vector2.LEFT.add(1000, 0));
        assertEquals(new Vector2(-1, 1000), Vector2.LEFT.add(0, 1000));
        assertEquals(new Vector2(-2, 0), Vector2.LEFT.add(Vector2.LEFT));
        assertEquals(new Vector2(-2, -2), Vector2.LEFT.add(Vector2.LEFT.add(Vector2.UP).add(Vector2.UP)));
    }

    @Test
    void sub() {
        assertEquals(new Vector2(0, 0), Vector2.RIGHT.sub(1, 0));
        assertEquals(new Vector2(-2, 0), Vector2.LEFT.sub(1, 0));
        assertEquals(new Vector2(-1001, 0), Vector2.LEFT.sub(1000, 0));
        assertEquals(new Vector2(-1, -1000), Vector2.LEFT.sub(0, 1000));
        assertEquals(new Vector2(0, 0), Vector2.LEFT.sub(Vector2.LEFT));
        assertEquals(new Vector2(0, -2), Vector2.LEFT.sub(Vector2.LEFT.sub(Vector2.UP).sub(Vector2.UP)));
    }

    @Test
    void mult() {
        assertEquals(new Vector2(1, 0), Vector2.RIGHT.mult(Vector2.RIGHT));
        assertEquals(new Vector2(-1, 0), Vector2.RIGHT.mult(Vector2.LEFT));
        assertEquals(new Vector2(100, -100), new Vector2(10, -10).mult(10));
        assertEquals(new Vector2(-100, -100), new Vector2(10, -10).mult(-10, 10));
        assertEquals(new Vector2(-100, -100), new Vector2(10, -10).mult(new Vector2(-10, 10)));
        assertEquals(new Vector2(-100, -100), new Vector2(10, -10).mult(new Vector2(-2, 2).mult(5)));
    }

    @Test
    void div() {
        assertEquals(new Vector2(1, 0), Vector2.RIGHT.div(Vector2.ONE));
        assertEquals(new Vector2(-1, 0), Vector2.LEFT.div(Vector2.ONE));
        assertEquals(new Vector2(10, -10), new Vector2(100, -100).div(10));
        assertEquals(new Vector2(-10, -10), new Vector2(100, -100).div(-10, 10));
        assertEquals(new Vector2(-10, -10), new Vector2(100, -100).div(new Vector2(-10, 10)));
        assertEquals(new Vector2(-10, -10), new Vector2(100, -100).div(new Vector2(-20, 20).div(2)));
    }

    @Test
    void dot() {
        assertEquals(0, Vector2.RIGHT.dot(Vector2.UP));
        assertEquals(0, Vector2.RIGHT.dot(Vector2.DOWN));
        assertEquals(0, Vector2.LEFT.dot(Vector2.DOWN));
        assertEquals(1, Vector2.LEFT.dot(Vector2.LEFT));
        assertEquals(-1, Vector2.RIGHT.dot(Vector2.LEFT));
        assertEquals(-1, Vector2.LEFT.dot(Vector2.RIGHT));
        assertEquals(20, Vector2.RIGHT.mult(2).dot(Vector2.RIGHT.mult(10)));
        assertEquals(0, isClose(new Vector2(-13.5, 26.985).dot(new Vector2(12.249, 1024.23)), 27473.48505));
    }

    @Test
    void length() {
        assertEquals(0, Vector2.ZERO.length());
        assertEquals(1, Vector2.RIGHT.length());
        assertEquals(1, Vector2.UP.length());
        assertEquals(0, isClose(Math.sqrt(2), Vector2.ONE.length()));
        assertEquals(0, isClose(9523.80611627, new Vector2(219.123, -9521.285).length()));
    }

    @Test
    void distanceTo() {
        assertEquals(0, Vector2.ZERO.distanceTo(Vector2.ZERO));
        assertEquals(0, Vector2.ZERO.distanceTo(0, 0));
        assertEquals(1, Vector2.RIGHT.distanceTo(0, 0));
        assertEquals(1, Vector2.LEFT.distanceTo(0, 0));
        assertEquals(10, Vector2.LEFT.distanceTo(9, 0));
        assertEquals(9, Vector2.LEFT.distanceTo(new Vector2(-1, 9)));
        assertEquals(0, isClose(45407.6061799425, new Vector2(7521, -2592.4).distanceTo(52851.51, 52.5125)));
    }

    @Test
    void angle() {
        assertEquals(0, Vector2.RIGHT.angle());
        assertEquals(0, isClose(Math.PI / 2, Vector2.DOWN.angle()));
        assertEquals(0, isClose(Math.PI, Vector2.LEFT.angle()));
        assertEquals(0, isClose(-Math.PI / 2, Vector2.UP.angle()));
        assertEquals(0, isClose(Math.PI / 4, Vector2.ONE.angle()));
        assertEquals(0, isClose(-Math.PI * 3 / 4, new Vector2(-10, -10).angle()));
        assertEquals(0, isClose(-0.064678731, new Vector2(91582.15, -5931.691).angle()));
    }

    @Test
    void angleTo() {
        assertEquals(0, Vector2.LEFT.angleTo(Vector2.LEFT));
        assertNotEquals(0, Vector2.LEFT.angleTo(Vector2.RIGHT));
        assertEquals(0, isClose(Math.PI, Vector2.RIGHT.angleTo(Vector2.LEFT)));
        assertEquals(0, isClose(Math.PI, Vector2.UP.angleTo(Vector2.DOWN)));
        assertEquals(0, isClose(Math.PI * 3 / 2, Vector2.UP.angleTo(Vector2.LEFT)));
        assertEquals(0, isClose(Math.PI / 2, Vector2.UP.angleTo(Vector2.RIGHT)));
        assertEquals(0, isClose(-Math.PI / 2, Vector2.DOWN.angleTo(Vector2.RIGHT)));
    }

    @Test
    void normalized() {
        assertEquals(1, Vector2.RIGHT.normalized().length());
        assertEquals(0, isClose(1, Vector2.ONE.normalized().length()));
        assertEquals(0, isClose(1, new Vector2(5129, 10251.2591).normalized().length()));
        assertEquals(0, isClose(new Vector2(1 / Math.sqrt(2), 1 / Math.sqrt(2)), Vector2.ONE.normalized()));
        assertEquals(0, isClose(new Vector2(-1 / Math.sqrt(2), -1 / Math.sqrt(2)), Vector2.ONE.reversed().normalized()));
        assertEquals(0, isClose(new Vector2(.8944271, .4472136), new Vector2(100, 50).normalized()));
    }

    @Test
    void reversed() {
        assertEquals(0, isClose(Vector2.LEFT, Vector2.RIGHT.reversed()));
        assertEquals(0, isClose(Vector2.RIGHT, Vector2.LEFT.reversed()));
        assertEquals(0, isClose(Vector2.UP, Vector2.DOWN.reversed()));
        assertEquals(0, isClose(Vector2.DOWN, Vector2.UP.reversed()));
        assertEquals(new Vector2(-1, -1), Vector2.ONE.reversed());
        assertEquals(new Vector2(-25285, .52815), new Vector2(25285, -.52815).reversed());
    }

    @Test
    void rotated() {
        assertEquals(0, isClose(Vector2.UP, Vector2.RIGHT.rotated(-Math.PI / 2)));
        assertEquals(0, isClose(Vector2.LEFT, Vector2.RIGHT.rotated(-Math.PI)));
        assertEquals(0, isClose(Vector2.LEFT, Vector2.RIGHT.rotated(Math.PI)));
        assertEquals(0, isClose(Vector2.DOWN, Vector2.RIGHT.rotated(Math.PI / 2)));
        assertEquals(0, isClose(Vector2.RIGHT, Vector2.UP.rotated(Math.PI / 2)));
        assertEquals(0, isClose(Vector2.LEFT, Vector2.UP.rotated(-Math.PI / 2)));
        assertEquals(0, isClose(new Vector2(1 / Math.sqrt(2), 1 / Math.sqrt(2)), Vector2.RIGHT.rotated(Math.PI / 4)));
    }


    @Test
    void lerp() {
        Vector2 from = new Vector2(5, 5);
        Vector2 to = new Vector2(10, -10);
        assertEquals(0, isClose(from, from.lerp(to, 0)));
        assertEquals(0, isClose(new Vector2(5.5, 3.5), from.lerp(to, .1)));
        assertEquals(0, isClose(new Vector2(6.25, 1.25), from.lerp(to, .25)));
        assertEquals(0, isClose(new Vector2(6.665, 0.005), from.lerp(to, .333)));
        assertEquals(0, isClose(new Vector2(7.5, -2.5), from.lerp(to, .5)));
        assertEquals(0, isClose(new Vector2(8.75, -6.25), from.lerp(to, .75)));
        assertEquals(0, isClose(to, from.lerp(to, 1)));
    }

    @Test
    void testEquals() {
        assertEquals(new Vector2(1, 1), new Vector2(1, 1));
        assertEquals(new Vector2(1, .5), new Vector2(1, .5));
        assertEquals(new Vector2(-10, 7.5), new Vector2(-10, 7.5));
    }

    @Test
    void testDefaultVectors() {
        assertEquals(Vector2.ZERO, new Vector2(0, 0));
        assertEquals(Vector2.RIGHT, new Vector2(1, 0));
        assertEquals(Vector2.LEFT, new Vector2(-1, 0));
        assertEquals(Vector2.DOWN, new Vector2(0, 1));
        assertEquals(Vector2.UP, new Vector2(0, -1));
        assertEquals(Vector2.ONE, new Vector2(1, 1));
    }
}