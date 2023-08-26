package jetfighters.game.transitions;

import jetfighters.game.math.Vector2;
import jetfighters.game.tween.Tween;
import jetfighters.game.tween.TweenFinishAction;
import jetfighters.game.tween.TweenMode;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class VocalCircleTransition extends ScreenTransition {

    private final Vector2 vocalPoint;
    private final double minRadius;
    private final double maxRadius;
    private final boolean fadeIn;
    private final double progressPow;

    /**
     * @param vocalPoint     the point the circle will focus on and zoom in / out of
     * @param minRadius      the minimum radius of the circle
     * @param maxRadius      the maximum radius of the circle
     * @param fadeIn         if true the circle will zoom in, if false it will zoom out
     * @param duration       the duration of this transition
     * @param finishedAction the action to be called when the transition finishes
     * @param color          the color of the shape
     */
    public VocalCircleTransition(Vector2 vocalPoint, double minRadius, double maxRadius, boolean fadeIn, double progressPow, double duration, TweenMode mode, TweenFinishAction finishedAction, Color color) {
        super(getShapeFromRadius(vocalPoint, fadeIn ? maxRadius : minRadius, false),
                duration, mode, finishedAction, color);
        this.vocalPoint = vocalPoint;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.fadeIn = fadeIn;
        this.progressPow = progressPow;
        tween.addTweenAction(this::setVocalTransitionShape);
    }

    public void setVocalTransitionShape(double progress) {
        double radius = Tween.interpolate(maxRadius, minRadius, fadeIn ? Math.pow(progress, progressPow) : Math.pow(1 - progress, progressPow), TweenMode.Linear);
        transitionShape = getShapeFromRadius(vocalPoint, radius, false);
    }

    private static Shape getShapeFromRadius(Vector2 vocalPoint, double radius, boolean inverse) {
        Ellipse2D baseShape = new Ellipse2D.Double(vocalPoint.x - radius, vocalPoint.y - radius, radius * 2, radius * 2);
        if (!inverse)
            return baseShape;
        Rectangle2D screenShape = new Rectangle(0, 0, 2000, 2000);
        Area area = new Area(screenShape);
        area.subtract(new Area(baseShape));
        return area;
    }
}
