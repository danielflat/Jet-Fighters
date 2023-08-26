package jetfighters.game.entities.enemies;

import jetfighters.game.Game;
import jetfighters.game.math.Vector2;
import jetfighters.game.tween.Tween;
import jetfighters.game.tween.TweenMode;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * An enemy jet that simply flies in a straight line while shooting constantly.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
@SuppressWarnings("unused")
public class StrafingEnemyJet extends EnemyJet {

    private final Tween tween;

    /**
     * Creates a new StrafingEnemyJet. <br>
     * This uses a Tween to move instead of the JetEntity velocity and acceleration.
     *
     * @param game         the game object this exists in
     * @param from         the position this jet starts at
     * @param to           the position this jet ends at
     * @param movementMode the type of movement this jet will use (see {@link TweenMode})
     */
    public StrafingEnemyJet(Game game, Vector2 from, Vector2 to, TweenMode movementMode) {
        super(game, from, 0, "jet/jet2/Jet_2_1.png");
        rotation = Vector2.UP.angleTo(to.sub(from));
        tween = new Tween.Vector(4, from, to, movementMode, this::setPosition, this::destroy);
        velocity = Vector2.ZERO;
        shooting = true;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        tween.update(delta);
    }
}
