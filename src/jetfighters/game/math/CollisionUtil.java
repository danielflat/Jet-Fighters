package jetfighters.game.math;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 * Utility class for general use methods related to collisions.
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class CollisionUtil {

    /**
     * Calculates the outline of a buffered image by constructing a path around the edge of visible pixels to invisible pixels. <br>
     * All pixels with alpha value < {@code alphaThreshold} are considered invisible,
     * all pixels with alpha value >= {@code alphaThreshold} are considered visible.
     *
     * @param alphaThreshold the threshold of what pixels are considered visible and which are considered invisible
     * @param image          the image to construct the outline for
     * @return an Area containing the Path that was drawn on the edge of visible to invisible pixels.
     */
    public static Area getOutline(float alphaThreshold, BufferedImage image) {
        // construct the GeneralPath
        GeneralPath gp = new GeneralPath();

        boolean cont = false;
        for (int xx = 0; xx < image.getWidth(); xx++) {
            for (int yy = 0; yy < image.getHeight(); yy++) {
                if (new Color(image.getRGB(xx, yy), true).getAlpha() >= alphaThreshold) {
                    if (cont) {
                        gp.lineTo(xx, yy);
                        gp.lineTo(xx, yy + 1);
                        gp.lineTo(xx + 1, yy + 1);
                        gp.lineTo(xx + 1, yy);
                        gp.lineTo(xx, yy);
                    } else {
                        gp.moveTo(xx, yy);
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            cont = false;
        }
        gp.closePath();

        // construct the Area from the GP & return it
        return new Area(gp);
    }

}
