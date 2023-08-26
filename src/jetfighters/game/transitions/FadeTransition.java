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
public abstract class FadeTransition extends ScreenTransition {

    private final double alphaFrom;
    private final double alphaTo;

    public FadeTransition(Shape transitionShape, double duration, TweenMode tweenMode, TweenFinishAction finishedAction, Color color, double alphaFrom, double alphaTo) {
        super(transitionShape, duration, tweenMode, finishedAction, color);
        this.alphaFrom = alphaFrom;
        this.alphaTo = alphaTo;
        tween.addTweenAction(this::setAlpha);
    }

    private void setAlpha(double alphaProgress) {
        int alpha = (int) (Tween.interpolate(alphaFrom, alphaTo, alphaProgress, TweenMode.Linear) * 255);
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

}
