package jetfighters.game.transitions;

import jetfighters.game.tween.TweenFinishAction;
import jetfighters.game.tween.TweenMode;

import java.awt.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class RectangleFadeTransition extends FadeTransition {

    public RectangleFadeTransition(double duration, TweenMode mode, TweenFinishAction finishedAction, Color color, double alphaFrom, double alphaTo) {
        super(new Rectangle(0, 0, 2000, 1500), duration, mode, finishedAction, color, alphaFrom, alphaTo);
    }
}
