package jetfighters.windows;

import jetfighters.audio.Sound;
import jetfighters.windows.exceptions.ImageNotFoundException;
import jetfighters.windows.exceptions.StateNotFoundException;
import jetfighters.windows.listeners.UserInput;
import jetfighters.windows.states.MenuState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class GameOverWindow extends AbstractWindow {
    private JLabel titleLabel;
    private JLabel descriptionLabel;

    private JButton yesButton;
    private JButton noButton;

    private ImageIcon titleImage;
    private ImageIcon description1Image;
    private ImageIcon description2Image;
    private ImageIcon yesDefaultImage;
    private ImageIcon yesPressedImage;
    private ImageIcon noDefaultImage;
    private ImageIcon noPressedImage;

    /**
     * creates the game over window
     *
     * @param window the JFrame window as a reference
     */
    public GameOverWindow(Window window) {
        super(window);

        setupImages();
        setupLabels();
        setupButtons();
        setupActionListeners();
        setupKeyBindings();
        setupMouseListeners();
        setupOverWindow();

        this.add(background);
    }

    /**
     * setting up all JComponent
     */
    private void setupOverWindow() {
        background.removeAll();
        background.add(titleLabel);
        background.add(descriptionLabel);
        background.add(yesButton);
        background.add(noButton);
    }

    /**
     * is setting up all the images
     */
    private void setupImages() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            titleImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/GameOver_title.png"))));
            description1Image = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Game_Over_description_1.png"))));
            description2Image = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Game_Over_description_2.png"))));
            yesDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Yesbutton_default.png"))));
            yesPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Yesbutton_pressed.png"))));
            noDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Nobutton_default.png"))));
            noPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Nobutton_pressed.png"))));
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't load images in GameOver Window correctly:");
            e.printStackTrace();
        }
    }

    /**
     * is setting up the labels
     */
    private void setupLabels() {
        titleLabel = new JLabel();
        descriptionLabel = new JLabel();
        setupLabel(titleLabel, 175, 50, titleImage);
        setupLabel(descriptionLabel, 400, 275, description1Image);
    }

    /**
     * is setting up the buttons
     */
    private void setupButtons() {
        yesButton = new JButton();
        noButton = new JButton();
        setupButton(yesButton, 200, 525, yesDefaultImage);
        setupButton(noButton, 600, 525, noDefaultImage);
    }

    /**
     * is setting up the action listeners
     */
    private void setupActionListeners() {
        yesButton.addActionListener((e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            restartGame();
            resetButtons();
        }));
        noButton.addActionListener((e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            try {
                window.updateMenuState(MenuState.Menu);
                window.getMenuStateManager().setTempState(null);
            } catch (StateNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            resetButtons();
        }));
    }

    /**
     * is setting up the key bindings
     */
    private void setupKeyBindings() {
        setupKeyStroke("Yes", "1", this::restartGame);
        setupKeyStroke("No", "2", () -> {
            window.updateMenuState(MenuState.Menu);
            window.getMenuStateManager().setTempState(null);
        });
    }

    /**
     * is setting up the mouse listeners
     */
    private void setupMouseListeners() {
        setupMouseListeners(yesButton, yesPressedImage, yesDefaultImage);
        setupMouseListeners(noButton, noPressedImage, noDefaultImage);
    }

    /**
     * a help method to create the keystroke for the button
     *
     * @param button    the button for the keystroke
     * @param keyStroke the name for the keystroke
     * @param method    the action method
     */
    private void setupKeyStroke(String button, String keyStroke, UserInput method) {

        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("pressed " + keyStroke), keyStroke + "P");
        inputMap.put(KeyStroke.getKeyStroke("released " + keyStroke), keyStroke + "R");

        ActionMap actionMap = this.getActionMap();
        actionMap.put(keyStroke + "P", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setButtonStatus(button, false);
                } catch (ImageNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        actionMap.put(keyStroke + "R", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setButtonStatus(button, true);
                    window.getAudioPlayer().playSound(Sound.Button01);
                    method.clicked();
                    resetButtons();
                } catch (ImageNotFoundException | StateNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * a setter for the button status
     *
     * @param button the button
     * @param state  the state of the button
     * @throws ImageNotFoundException if there is no image for the button string
     */
    public void setButtonStatus(String button, boolean state) throws ImageNotFoundException {
        switch (button) {
            case "Yes":
                if (state) {
                    yesButton.setIcon(yesDefaultImage);
                } else {
                    yesButton.setIcon(yesPressedImage);
                }
                break;
            case "No":
                if (state) {
                    noButton.setIcon(noDefaultImage);
                } else {
                    noButton.setIcon(noPressedImage);
                }
                break;

            default:
                throw new ImageNotFoundException();
        }
    }

    /**
     * resetting the buttons
     */
    public void resetButtons() {
        yesButton.setIcon(yesDefaultImage);
        noButton.setIcon(noDefaultImage);
    }

    /**
     * is restarting the game
     */
    public void restartGame() {
        try {
            window.updateMenuState(window.getMenuStateManager().getTempMenuState());
            window.getMenuStateManager().setTempState(null);
        } catch (StateNotFoundException e) {
            throw new RuntimeException(e);
        }
        window.getGameWindow().restartGame();
    }

    /**
     * is setting the description for the window
     *
     * @param player the player who won
     */
    public void setDescription(int player) {
        switch (player) {
            case 1 -> descriptionLabel.setIcon(description1Image);
            case 2 -> descriptionLabel.setIcon(description2Image);

            default -> throw new RuntimeException();
        }
    }

    /**
     * is setting the background with a new image
     *
     * @param background the new image
     */
    public void setBackgroundImage(ImageIcon background) {
        this.background.setIcon(background);
    }
}
