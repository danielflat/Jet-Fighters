package jetfighters.tests.game;

import jetfighters.game.entities.Entity;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public interface CollisionCallback {

    @SuppressWarnings("unused")
    void call(Entity otherEntity);

}
