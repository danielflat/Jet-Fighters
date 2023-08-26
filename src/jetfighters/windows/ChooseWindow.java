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
public class ChooseWindow extends AbstractWindow {
    private JLabel titleLabel;
    private JButton versusButton;
    private JButton singleButton;
    private JButton coopButton;
    private JButton backButton;

    private ImageIcon titleImage;
    private ImageIcon versusDefaultImage;
    private ImageIcon versusPressedImage;
    private ImageIcon singleDefaultImage;
    private ImageIcon singlePressedImage;
    private ImageIcon coopDefaultImage;
    private ImageIcon coopPressedImage;
    private ImageIcon backDefaultImage;
    private ImageIcon backPressedImage;

    /**
     * creates the choose-window
     *
     * @param window the JFrame window as a reference
     */
    public ChooseWindow(Window window) {
        super(window);

        setupImages();
        setupLabels();
        setupButtons();
        setupActionListeners();
        setupKeybindings();
        setupMouseListeners();
        setupChooseWindow();

        this.add(background);
    }

    /**
     * setting up all JComponent
     */
    public void setupChooseWindow() {
        background.removeAll();
        background.add(titleLabel);
        background.add(versusButton);
        background.add(singleButton);
        background.add(coopButton);
        background.add(backButton);
    }

    /**
     * is setting up all the images
     */
    private void setupImages() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            titleImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/ChooseWindow_title.png"))));
            versusDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Versusbutton_default.png"))));
            versusPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Versusbutton_pressed.png"))));
            singleDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Singlebutton_default.png"))));
            singlePressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Singlebutton_pressed.png"))));
            coopDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Coopbutton_default.png"))));
            coopPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Coopbutton_pressed.png"))));
            backDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Backbutton_default.png"))));
            backPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Backbutton_pressed.png"))));
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't load images in ChooseWindow correctly:");
            e.printStackTrace();
        }
    }

    /**
     * is setting up the labels
     */
    private void setupLabels() {
        titleLabel = new JLabel();
        setupLabel(titleLabel, 100, 50, titleImage);
    }

    /**
     * is setting up the buttons
     */
    private void setupButtons() {
        versusButton = new JButton();
        singleButton = new JButton();
        coopButton = new JButton();
        backButton = new JButton();

        setupButton(versusButton, 150, 300, versusDefaultImage);
        setupButton(singleButton, 600, 300, singleDefaultImage);
        setupButton(coopButton, 150, 500, coopDefaultImage);
        setupButton(backButton, 600, 500, backDefaultImage);
    }

    /**
     * is setting up the action listeners
     */
    private void setupActionListeners() {
        versusButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            window.getGameWindow().startGame(MenuState.Versus);
            resetButtons();
        });
        singleButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            //window.getGameWindow().startGame(MenuState.Single);   //TODO:command it back again
            resetButtons();
        });
        coopButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            //window.getGameWindow().startGame(MenuState.Coop);     //TODO:command it back again
            resetButtons();
        });
        backButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            try {
                window.updateMenuState(MenuState.Menu);
            } catch (StateNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            resetButtons();
        });
    }

    /**
     * is setting up the key bindings
     */
    private void setupKeybindings() {
        setupKeyStroke("Versus", "1", () -> window.getGameWindow().startGame(MenuState.Versus));
        setupKeyStroke("Single", "2", () -> {
            //window.getGameWindow().startGame(MenuState.Single);   //TODO: Command
        });
        setupKeyStroke("Coop", "3", () -> {
            //window.getGameWindow().startGame(MenuState.Coop);     //TODO: Command
        });
        setupKeyStroke("Back", "4", () -> window.updateMenuState(MenuState.Menu));
    }

    /**
     * is setting up the mouse listeners
     */
    private void setupMouseListeners() {

        setupMouseListeners(versusButton, versusPressedImage, versusDefaultImage);
        setupMouseListeners(singleButton, singlePressedImage, singleDefaultImage);
        setupMouseListeners(coopButton, coopPressedImage, coopDefaultImage);
        setupMouseListeners(backButton, backPressedImage, backDefaultImage);
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
            case "Versus":
                if (state) {
                    versusButton.setIcon(versusDefaultImage);
                } else {
                    versusButton.setIcon(versusPressedImage);
                }
                break;
            case "Single":
                if (state) {
                    singleButton.setIcon(singleDefaultImage);
                } else {
                    singleButton.setIcon(singlePressedImage);
                }
                break;
            case "Coop":
                if (state) {
                    coopButton.setIcon(coopDefaultImage);
                } else {
                    coopButton.setIcon(coopPressedImage);
                }
                break;
            case "Back":
                if (state) {
                    backButton.setIcon(backDefaultImage);
                } else {
                    backButton.setIcon(backPressedImage);
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
        versusButton.setIcon(versusDefaultImage);
        singleButton.setIcon(singleDefaultImage);
        coopButton.setIcon(coopDefaultImage);
        backButton.setIcon(backDefaultImage);
    }

    /**
     * a getter for the image of the button
     *
     * @param image   the button name
     * @param pressed the state of the button
     * @return the image of the button
     * @throws ImageNotFoundException if there is no image for the button string
     */
    public ImageIcon getImage(String image, boolean pressed)
            throws ImageNotFoundException {
        switch (image) {
            case "Versus":
                if (!pressed) {
                    return versusDefaultImage;
                } else {
                    return versusPressedImage;
                }
            case "Single":
                if (!pressed) {
                    return singleDefaultImage;
                } else {
                    return singlePressedImage;
                }
            case "Coop":
                if (!pressed) {
                    return coopDefaultImage;
                } else {
                    return coopPressedImage;
                }
            case "Back":
                if (!pressed) {
                    return backDefaultImage;
                } else {
                    return backPressedImage;
                }

            default:
                throw new ImageNotFoundException();
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
