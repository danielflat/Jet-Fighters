package jetfighters.game.camera;

import jetfighters.game.math.Vector2;

import java.awt.geom.AffineTransform;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Camera class to manage camera transformation matrix calculations to be applied in render method.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class Camera {

    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    private Vector2 position;

    /**
     * Creates a new Camera at position {@link Vector2#ZERO}.
     */
    public Camera() {
        position = Vector2.ZERO;
    }

    /**
     * Updates the camera. <br>
     * Should be called each time the game updates, while this camera is in use.
     *
     * @param delta the time that has passed since the last update
     */
    public void update(double delta) {

    }

    /**
     * Calculates the view matrix of this camera. Use this with {@link java.awt.Graphics2D#transform(AffineTransform)}
     * before drawing any objects in the game world to use this camera.
     *
     * @param screenWidth  the width of the visible screen area
     * @param screenHeight the height of the visible screen area
     * @return the view matrix of this camera
     */
    public AffineTransform getTransform(int screenWidth, int screenHeight) {
        return AffineTransform.getTranslateInstance(position.x, position.y);
    }

    /**
     * Calculates the inverse view matrix of this camera. Use this with {@link java.awt.Graphics2D#transform(AffineTransform)}
     * after drawing any objects in the game world to stop other drawn objects to be affected by this camera.
     *
     * @param screenWidth  the width of the visible screen area
     * @param screenHeight the height of the visible screen area
     * @return the inverse view matrix of this camera
     */
    public AffineTransform getInverseTransform(int screenWidth, int screenHeight) {
        return AffineTransform.getTranslateInstance(-position.x, -position.y);
    }
}
