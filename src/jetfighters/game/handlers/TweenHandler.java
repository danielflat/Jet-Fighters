package jetfighters.game.handlers;

import jetfighters.game.tween.Tween;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Handles updating of tweens. <br>
 * Tweens can be added, removed and a list of all registered tweens can be retrieved.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class TweenHandler {

    private final List<Tween> tweens;
    private final List<Tween> tweensToAdd;
    private final List<Tween> tweensToRemove;

    public TweenHandler() {
        tweens = new ArrayList<>();
        tweensToAdd = new ArrayList<>();
        tweensToRemove = new ArrayList<>();
    }

    public void updateTweens(double delta) {
        tweens.removeAll(tweensToRemove);
        tweensToRemove.clear();
        tweens.addAll(tweensToAdd);
        tweensToAdd.clear();
        for (Tween tween : tweens) {
            tween.update(delta);
            if (tween.isFinished()) {
                removeEntity(tween);
            }
        }
        tweens.removeAll(tweensToRemove); // Needs to happen before and after to remove outside of update and if tween finished.
        tweensToRemove.clear();
    }

    public void addTween(Tween tween) {
        tweensToAdd.add(tween);
    }

    public void removeEntity(Tween tween) {
        tweensToRemove.add(tween);
    }

    public List<Tween> getTweens() {
        return new ArrayList<>(tweens);
    }

}
