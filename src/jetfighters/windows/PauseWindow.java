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
public class PauseWindow extends AbstractWindow {
    private static MenuState tempMenuState;
    private JLabel titleLabel;

    private JButton resumeButton;
    private JButton restartButton;
    private JButton optionsButton;
    private JButton leaveButton;

    private ImageIcon titleImage;
    private ImageIcon resumeDefaultImage;
    private ImageIcon resumePressedImage;
    private ImageIcon restartDefaultImage;
    private ImageIcon restartPressedImage;
    private ImageIcon optionsDefaultImage;
    private ImageIcon optionsPressedImage;
    private ImageIcon leaveDefaultImage;
    private ImageIcon leavePressedImage;

    /**
     * creates the pause window
     *
     * @param window the JFrame window as a reference
     */
    public PauseWindow(Window window) {
        super(window);

        setupImages();
        setupLabels();
        setupButtons();
        setupActionListeners();
        setupKeyBindings();
        setupMouseListeners();
        setupPauseWindow();

        this.add(background);
    }

    /**
     * setting up all JComponent
     */
    public void setupPauseWindow() {
        background.removeAll();
        background.add(titleLabel);
        background.add(resumeButton);
        background.add(restartButton);
        background.add(optionsButton);
        background.add(leaveButton);
    }

    /**
     * is setting up all the images
     */
    private void setupImages() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            titleImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Pause_title.png"))));
            resumeDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Resumebutton_default.png"))));
            resumePressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Resumebutton_pressed.png"))));
            restartDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Restartbutton_default.png"))));
            restartPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Restartbutton_pressed.png"))));
            optionsDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Optionsbutton_default.png"))));
            optionsPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Optionsbutton_pressed.png"))));
            leaveDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Leavebutton_default.png"))));
            leavePressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Leavebutton_pressed.png"))));
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't load images in PauseWindow correctly:");
            e.printStackTrace();
        }
    }

    /**
     * is setting up the labels
     */
    private void setupLabels() {
        titleLabel = new JLabel();
        setupLabel(titleLabel, 325, 50, titleImage);
    }

    /**
     * is setting up the buttons
     */
    private void setupButtons() {
        resumeButton = new JButton();
        restartButton = new JButton();
        optionsButton = new JButton();
        leaveButton = new JButton();

        setupButton(resumeButton, 150, 300, resumeDefaultImage);
        setupButton(restartButton, 600, 300, restartDefaultImage);
        setupButton(optionsButton, 150, 500, optionsDefaultImage);
        setupButton(leaveButton, 600, 500, leaveDefaultImage);
    }

    /**
     * is setting up the action listeners
     */
    private void setupActionListeners() {
        resumeButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            resumeGame();
            resetButtons();
        });
        restartButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            restartGame();
            resetButtons();
        });
        optionsButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            try {
                window.updateMenuState(MenuState.Options);
            } catch (StateNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            resetButtons();
        });
        leaveButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            try {
                window.updateMenuState(MenuState.PL);
            } catch (StateNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            resetButtons();
        });
    }

    /**
     * is setting up the key bindings
     */
    private void setupKeyBindings() {
        setupKeyStroke("Resume", "1", this::resumeGame);
        setupKeyStroke("Restart", "2", this::restartGame);
        setupKeyStroke("Options", "3", () -> window.updateMenuState(MenuState.Options));
        setupKeyStroke("Leave", "4", () -> window.updateMenuState(MenuState.PL));
    }

    /**
     * is setting up the mouse listeners
     */
    private void setupMouseListeners() {
        setupMouseListeners(resumeButton, resumePressedImage, resumeDefaultImage);
        setupMouseListeners(resumeButton, resumePressedImage, resumeDefaultImage);
        setupMouseListeners(optionsButton, optionsPressedImage, optionsDefaultImage);
        setupMouseListeners(leaveButton, leavePressedImage, leaveDefaultImage);
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
            case "Resume":
                if (state) {
                    resumeButton.setIcon(resumeDefaultImage);
                } else {
                    resumeButton.setIcon(resumePressedImage);
                }
                break;
            case "Restart":
                if (state) {
                    restartButton.setIcon(restartDefaultImage);
                } else {
                    restartButton.setIcon(restartPressedImage);
                }
                break;
            case "Options":
                if (state) {
                    optionsButton.setIcon(optionsDefaultImage);
                } else {
                    optionsButton.setIcon(optionsPressedImage);
                }
                break;
            case "Leave":
                if (state) {
                    leaveButton.setIcon(leaveDefaultImage);
                } else {
                    leaveButton.setIcon(leavePressedImage);
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
        resumeButton.setIcon(resumeDefaultImage);
        restartButton.setIcon(restartDefaultImage);
        optionsButton.setIcon(optionsDefaultImage);
        leaveButton.setIcon(leaveDefaultImage);
    }

    /**
     * is resuming the game
     */
    public void resumeGame() {
        try {
            window.updateMenuState(tempMenuState);
            window.getGameWindow().resumeGame();
        } catch (StateNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * is restarting the game
     */
    public void restartGame() {
        try {
            window.updateMenuState(tempMenuState);
            window.getGameWindow().restartGame();
        } catch (StateNotFoundException ex) {
            ex.printStackTrace();
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

    /**
     * a getter for the temp menu state
     *
     * @param menuState the temp menu state
     */
    public void setTempMenuState(MenuState menuState) {
        tempMenuState = menuState;
    }
}
