package jetfighters.game.tween;

import jetfighters.game.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public abstract class Tween {

    //TODO: doc-comments!!!
    private final double duration;
    protected final TweenMode mode;
    private final TweenFinishAction finishAction;

    private double timer;
    private boolean finished;

    /**
     * <h3>PTP 2022 - Projekt "JetFighters"</h3>
     *
     * @author do04 - Daniel Flat, Moritz Junge
     * @version 13.07.22
     */
    public static class Double extends Tween {

        private final double fromValue;
        private final double toValue;
        private final List<TweenDoubleAction> actions;

        public Double(double duration, double from, double to, TweenMode mode, TweenDoubleAction action) {
            this(duration, from, to, mode, action, null);
        }

        public Double(double duration, double from, double to, TweenMode mode, TweenDoubleAction action, TweenFinishAction finishAction) {
            super(duration, mode, finishAction);
            fromValue = from;
            toValue = to;
            this.actions = new ArrayList<>();
            if (action != null)
                actions.add(action);
        }

        @Override
        void applyProgress(double progress) {
            final double value = interpolate(fromValue, toValue, progress, mode);
            actions.forEach(tweenAction -> tweenAction.set(value));
        }

        public void addTweenAction(TweenDoubleAction action) {
            actions.add(action);
        }
    }

    /**
     * <h3>PTP 2022 - Projekt "JetFighters"</h3>
     *
     * @author do04 - Daniel Flat, Moritz Junge
     * @version 13.07.22
     */
    public static class Vector extends Tween {

        private final Vector2 fromValue;
        private final Vector2 toValue;
        private final List<TweenVectorAction> actions;

        public Vector(double duration, Vector2 from, Vector2 to, TweenMode mode, TweenVectorAction action) {
            this(duration, from, to, mode, action, null);
        }

        public Vector(double duration, Vector2 from, Vector2 to, TweenMode mode, TweenVectorAction action, TweenFinishAction finishAction) {
            super(duration, mode, finishAction);
            fromValue = from;
            toValue = to;
            this.actions = new ArrayList<>();
            if (action != null)
                actions.add(action);
        }

        @Override
        void applyProgress(double progress) {
            final Vector2 value = interpolate(fromValue, toValue, progress, mode);
            actions.forEach(tweenAction -> tweenAction.set(value));
        }

        public void addTweenAction(TweenVectorAction action) {
            actions.add(action);
        }
    }

    private Tween(double duration, TweenMode mode, TweenFinishAction finishAction) {
        this.duration = duration;
        this.mode = mode;
        this.finishAction = finishAction;
    }

    abstract void applyProgress(double progress);

    public void update(double delta) {
        if (isFinished())
            return;
        timer += delta;
        double progress = Math.min(1, timer / duration);

        applyProgress(progress);

        if (timer >= duration) {
            finished = true;
            if (finishAction != null)
                finishAction.execute();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public static double interpolate(double from, double to, double progress, TweenMode mode) {
        return switch (mode) {
            case Linear -> (to - from) * progress + from;
            case Quadratic -> (to - from) * Math.pow(progress, 2) + from;
            case Cubic -> (to - from) * Math.pow(progress, 3) + from;
        };
    }

    public static Vector2 interpolate(Vector2 from, Vector2 to, double progress, TweenMode mode) {
        return switch (mode) {
            case Linear -> from.lerp(to, progress);
            case Quadratic -> from.lerp(to, Math.pow(progress, 2));
            case Cubic -> from.lerp(to, Math.pow(progress, 3));
        };
    }
}
