package jetfighters.game.transitions;

import jetfighters.game.tween.Tween;
import jetfighters.game.tween.TweenFinishAction;
import jetfighters.game.tween.TweenMode;

import java.awt.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public abstract class ScreenTransition {

    protected Shape transitionShape;
    protected Color color;

    protected final Tween.Double tween;

    public ScreenTransition(Shape transitionShape, double duration, TweenMode tweenMode, TweenFinishAction finishedAction, Color color) {
        this.transitionShape = transitionShape;
        this.color = color;
        tween = new Tween.Double(duration, 0, 1, tweenMode, null, finishedAction);
    }

    public void update(double delta) {
        tween.update(delta);
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill(transitionShape);
    }

    public boolean isFinished() {
        return tween.isFinished();
    }
}
