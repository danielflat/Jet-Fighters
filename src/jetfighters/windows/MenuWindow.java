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
public class MenuWindow extends AbstractWindow {

    private JLabel titleLabel;
    private JButton startButton;
    private JButton optionsButton;
    private JButton quitButton;

    private ImageIcon titleImage;
    private ImageIcon startDefaultImage;
    private ImageIcon startPressedImage;
    private ImageIcon optionsDefaultImage;
    private ImageIcon optionsPressedImage;
    private ImageIcon quitDefaultImage;
    private ImageIcon quitPressedImage;

    /**
     * creates the menu window
     *
     * @param window the JFrame window as a reference
     */
    public MenuWindow(Window window) {
        super(window);

        setupImages();
        setupLabels();
        setupButtons();
        setupActionListeners();
        setupKeybindings();
        setupMouseListeners();
        setupMenuWindow();

        this.add(background);
    }

    /**
     * setting up all JComponent
     */
    public void setupMenuWindow() {
        background.removeAll();
        background.add(titleLabel);
        background.add(startButton);
        background.add(optionsButton);
        background.add(quitButton);
    }

    /**
     * is setting up all the images
     */
    private void setupImages() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            titleImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Jet_Fighters_title.png"))));
            startDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Startbutton_default.png"))));
            startPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Startbutton_pressed.png"))));
            optionsDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Optionsbutton_default.png"))));
            optionsPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Optionsbutton_pressed.png"))));
            quitDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Quitbutton_default.png"))));
            quitPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Quitbutton_pressed.png"))));
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't load images in MenuWindow correctly:");
            e.printStackTrace();
        }
    }

    /**
     * is setting up the labels
     */
    private void setupLabels() {
        titleLabel = new JLabel();
        setupLabel(titleLabel, 50, 75, titleImage);
    }

    /**
     * is setting up the buttons
     */
    private void setupButtons() {
        startButton = new JButton();
        optionsButton = new JButton();
        quitButton = new JButton();
        setupButton(startButton, 400, 300, startDefaultImage);
        setupButton(optionsButton, 400, 425, optionsDefaultImage);
        setupButton(quitButton, 400, 550, quitDefaultImage);
    }

    /**
     * is setting up the action listeners
     */
    private void setupActionListeners() {
        startButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            try {
                window.updateMenuState(MenuState.Choose);
            } catch (StateNotFoundException ex) {
                throw new RuntimeException(ex);
            }
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
        quitButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            System.exit(0);
            resetButtons();
        });
    }

    /**
     * is setting up the key bindings
     */
    private void setupKeybindings() {
        setupKeyStroke("Start", "1", () -> window.updateMenuState(MenuState.Choose));
        setupKeyStroke("Options", "2", () -> window.updateMenuState(MenuState.Options));
        setupKeyStroke("Quit", "3", () -> System.exit(0));
    }

    /**
     * is setting up the mouse listeners
     */
    private void setupMouseListeners() {
        setupMouseListeners(startButton, startPressedImage, startDefaultImage);
        setupMouseListeners(optionsButton, optionsPressedImage, optionsDefaultImage);
        setupMouseListeners(quitButton, quitPressedImage, quitDefaultImage);
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
                    setButtonstatus(button, false);
                } catch (ImageNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        actionMap.put(keyStroke + "R", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setButtonstatus(button, true);
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
    public void setButtonstatus(String button, boolean state) throws ImageNotFoundException {
        switch (button) {
            case "Start":
                if (state) {
                    startButton.setIcon(startDefaultImage);
                } else {
                    startButton.setIcon(startPressedImage);
                }
                break;
            case "Options":
                if (state) {
                    optionsButton.setIcon(optionsDefaultImage);
                } else {
                    optionsButton.setIcon(optionsPressedImage);
                }
                break;
            case "Quit":
                if (state) {
                    quitButton.setIcon(quitDefaultImage);
                } else {
                    quitButton.setIcon(quitPressedImage);
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
        startButton.setIcon(startDefaultImage);
        optionsButton.setIcon(optionsDefaultImage);
        quitButton.setIcon(quitDefaultImage);
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
