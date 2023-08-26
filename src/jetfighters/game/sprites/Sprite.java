package jetfighters.game.sprites;

import jetfighters.game.math.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class Sprite {

    protected Vector2 position;
    protected double rotation;
    protected Vector2 scale;

    protected BufferedImage image;

    private boolean visible;

    protected final String imagePath;

    public Sprite(String imagePath) {
        this.imagePath = imagePath;
        image = readImage(imagePath);

        position = Vector2.ZERO;
        rotation = 0;
        scale = Vector2.ONE;
        visible = true;
    }

    protected BufferedImage readImage(String imagePath) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(imagePath));
            return ImageIO.read(inputStream);
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't load image " + imagePath + " in Sprite:");
            e.printStackTrace();
            return null;
        }
    }

    public void draw(Graphics2D g2d) {
        if (image == null || !isVisible())
            return;
        g2d.transform(getTransform());
        g2d.drawImage(image, 0, 0, null);
        try {
            g2d.transform(getTransform().createInverse());
        } catch (NoninvertibleTransformException e) {
            System.err.println("Couldn't inverse transform in Sprite:");
            e.printStackTrace();
        }
    }

    /**
     * Returns the transform that is applied to this sprite
     *
     * @return the transform
     */
    public AffineTransform getTransform() {
        AffineTransform transform = new AffineTransform();
        transform.rotate(rotation, position.x + image.getWidth() * scale.x / 2.0, position.y + image.getHeight() * scale.y / 2.0);
        transform.translate(position.x, position.y);
        transform.scale(scale.x, scale.y);
        return transform;
    }

    /**
     * Scales the sprite uniformly with {@code factor}
     *
     * @param factor the factor to scale with
     */
    public void scale(double factor) {
        scale(factor, factor);
    }

    public void scale(double x, double y) {
        scale = scale.mult(x, y);
    }

    public void translate(double x, double y) {
        position = position.add(x, y);
    }

    /**
     * Rotates the sprite by {@code theta}.
     *
     * @param theta rotation in radians
     */
    public void rotate(double theta) {
        rotation += theta;
    }

    /**
     * @return true if this sprite has a loaded image and therefore returns sensible values for {@link #getWidth()} and others.
     */
    public boolean hasValidImage() {
        return image != null;
    }

    public int getWidth() {
        return image != null ? image.getWidth() : 0;
    }

    public int getHeight() {
        return image != null ? image.getHeight() : 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getImagePath() {
        return imagePath;
    }
}
