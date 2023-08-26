package jetfighters.windows;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public abstract class AbstractWindow extends JPanel {
    protected final Window window;
    protected final JLabel background;
    private final int width;
    private final int height;

    /**
     * is setting up the window for the subwindows
     *
     * @param window the JFrame for the reference
     */
    public AbstractWindow(Window window) {
        this.window = window;

        background = new JLabel();
        BackgroundImage backgroundImage = new BackgroundImage(window);  //nur relevant für die Initialisierung
        setupLabel(background, 0, 0, backgroundImage.getBackgroundImage());

        this.width = 1068;
        this.height = 685;

        this.setBounds(0, 0, width, height);
        this.setLayout(null);
    }

    /**
     * is setting up the JLabel with the coordinates and an images
     *
     * @param label     the JLabel to set up
     * @param x         the x position for the JLabel
     * @param y         the y position for the JLabel
     * @param imageIcon the image for the JLabel
     */
    protected void setupLabel(JLabel label, int x, int y, ImageIcon imageIcon) {
        label.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        label.setIcon(imageIcon);
    }

    /**
     * is setting up the JButton with the coordinates and an images
     *
     * @param button    the JButton to set up
     * @param x         the x position for the JButton
     * @param y         the y position for the JButton
     * @param imageIcon the image for the JButton
     */
    protected void setupButton(JButton button, int x, int y, ImageIcon imageIcon) {
        button.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        button.setIcon(imageIcon);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);

    }

    /**
     * is setting up the MouseListener for the JButton
     *
     * @param button       the JButton
     * @param pressedImage the pressed image of the JButton
     * @param defaultImage the default image of the JButton
     */
    protected void setupMouseListeners(JButton button, ImageIcon pressedImage, ImageIcon defaultImage) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(pressedImage);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setIcon(defaultImage);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }


}
