package jetfighters.windows;

import jetfighters.windows.exceptions.ImageNotFoundException;
import jetfighters.windows.exceptions.StateNotFoundException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class BackgroundImage {
    private final Window window;

    private ImageIcon backgroundImage;

    private static final FileFilter PNG_FILTER = new FileNameExtensionFilter(
            "PNG image files (*.png)", "png");

    private ImageIcon morningImage;
    private ImageIcon daytimeImage;
    private ImageIcon nightImage;
    private ImageIcon importImage;

    private final int width;

    private final int height;

    public BackgroundImage(Window window) {
        this.window = window;
        this.width = 1068;
        this.height = 685;
        setupImages();
    }

    /**
     * a method for setting up the preloaded background images
     */
    private void setupImages() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            morningImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "backgrounds/Morning.png"))));
            daytimeImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "backgrounds/Daytime.png"))));
            nightImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "backgrounds/Night.png"))));
            importImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "backgrounds/Morning.png"))));
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't load images in BackgroundImage correctly:");
            e.printStackTrace();
        }
        backgroundImage = scaleImageIcon(morningImage);
    }

    /**
     * a method for scaling an image
     *
     * @param imageIcon the image to scale
     * @return the scaled image
     */
    public ImageIcon scaleImageIcon(ImageIcon imageIcon) {
        return scaleImageIcon(imageIcon, 1, 1);
    }

    /**
     * a method for scaling an image
     *
     * @param imageIcon the image to scale
     * @param scaleX    the x factor
     * @param scaleY    the y factor
     * @return the scaled image
     */
    public ImageIcon scaleImageIcon(ImageIcon imageIcon, double scaleX, double scaleY) {
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance((int) (width * scaleX), (int) (height * scaleY), Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    /**
     * a method for the import of a new background image
     *
     * @param backgroundState the current background state
     * @throws StateNotFoundException if there is no state for this integer value
     */
    public void importBackgroundImage(int backgroundState) throws StateNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(PNG_FILTER);
        int fcOption = fileChooser.showOpenDialog(null);
        if (fcOption == JFileChooser.APPROVE_OPTION) {
            File loadFile = fileChooser.getSelectedFile();
            importImage = new ImageIcon(loadFile.getAbsolutePath());
            window.updateBackgroundImage(4);
        } else {
            try {
                restorePreviousBackground(backgroundState);
            } catch (StateNotFoundException e) {
                throw new StateNotFoundException();
            }
        }
    }

    public void restorePreviousBackground(int backgroundState) throws StateNotFoundException {
        if (backgroundState == 1) {
            window.getOptionsWindow().updateBackgroundButtons(1);
            window.getOptionsWindow().setBackgroundState(1);
        } else if (backgroundState == 2) {
            window.getOptionsWindow().updateBackgroundButtons(2);
            window.getOptionsWindow().setBackgroundState(2);
        } else if (backgroundState == 3) {
            window.getOptionsWindow().updateBackgroundButtons(3);
            window.getOptionsWindow().setBackgroundState(3);
        } else if (backgroundState == 4) {
            window.getOptionsWindow().updateBackgroundButtons(4);
            window.getOptionsWindow().setBackgroundState(4);
        } else throw new StateNotFoundException();
    }

    /**
     * a getter for the preloaded background images
     *
     * @param image the integer for the image
     * @return the background image
     * @throws ImageNotFoundException if there is no image for the integer value
     */
    public ImageIcon getImage(int image) throws ImageNotFoundException {
        return switch (image) {
            case 1 -> morningImage;
            case 2 -> daytimeImage;
            case 3 -> nightImage;
            case 4 -> importImage;
            default -> throw new ImageNotFoundException();
        };
    }

    /**
     * a getter for the scaled preloaded background images
     *
     * @param image the integer for the image
     * @return the scaled background image
     * @throws ImageNotFoundException if there is no image for the integer value
     */
    public ImageIcon getScaledImage(int image) throws ImageNotFoundException {
        return switch (image) {
            case 1 -> scaleImageIcon(morningImage);
            case 2 -> scaleImageIcon(daytimeImage);
            case 3 -> scaleImageIcon(nightImage);
            case 4 -> scaleImageIcon(importImage);
            default -> throw new ImageNotFoundException();
        };
    }

    /**
     * a getter for the current background image which is getting scaled
     *
     * @param scaleX the scale factor for the x coordinate
     * @param scaleY the scale factor for the y coordinate
     * @return the scaled current image
     */
    public ImageIcon getBackgroundImageScaled(double scaleX, double scaleY) {
        return scaleImageIcon(backgroundImage, scaleX, scaleY);
    }

    /**
     * a getter for the current background image
     *
     * @return the current background image
     */
    public ImageIcon getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * is setting the current background image with a new image
     *
     * @param background the new background image
     */
    public void setBackgroundImage(ImageIcon background) {
        backgroundImage = scaleImageIcon(background);
    }
}
