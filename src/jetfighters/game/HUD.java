package jetfighters.game;

import jetfighters.game.entities.Entity;
import jetfighters.game.entities.JetEntity;
import jetfighters.game.handlers.EntityHandler;
import jetfighters.game.transitions.ScreenTransition;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * /** <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Represents the "Heads Up Display" of the game. It uses an entity Handler to draw healthbars, scores and game time among others.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class HUD {

    private final EntityHandler entityHandler;

    private ScreenTransition currentTransition;

    /**
     * Creates a new HUD using an EntityHandler to draw healthbars for its entities.
     * @param entityHandler the entity handler to draw healthbars for
     */
    public HUD(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
    }

    /**
     * Updates the HUD, progressing the current screen transition.
     * @param delta the time that has passed since the last update
     */
    public void update(double delta) {
        if (currentTransition != null) {
            currentTransition.update(delta);
            if (currentTransition.isFinished())
                currentTransition = null;
        }
    }

    /**
     * Renders all HUD elements to the screen. Should be called last to ensure it is on top of everything else.
     * This also draws healthbars for {@link JetEntity}
     *
     * @param g2d             Graphics object to draw to
     * @param currentGameTime the current game time
     */
    public void draw(Graphics2D g2d, AffineTransform cameraTransform, AffineTransform cameraInverseTransform, double currentGameTime) {
        g2d.setColor(Color.RED);
        g2d.setFont(g2d.getFont().deriveFont(30f));
        g2d.drawString("" + ((int) currentGameTime), 500, 30);

        g2d.transform(cameraTransform);
        for (Entity entity : entityHandler.getEntities()) {
            if (entity instanceof JetEntity jet)
                jet.drawHealthBar(g2d);
        }
        g2d.transform(cameraInverseTransform);

        if (currentTransition != null) {
            currentTransition.render(g2d);
        }
    }

    /**
     * Starts a screen transition, rendering it over every HUD-element and updating it automatically. <br>
     * The transition will automatically be removed once it is finished, but will still get it's {@link jetfighters.game.tween.TweenFinishAction}
     * called.
     * @param transition the transition to run
     */
    public void transition(ScreenTransition transition) {
        currentTransition = transition;
    }

}
