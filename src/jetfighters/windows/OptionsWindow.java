package jetfighters.windows;

import jetfighters.audio.AudioOptions;
import jetfighters.audio.Sound;
import jetfighters.windows.exceptions.ImageNotFoundException;
import jetfighters.windows.exceptions.StateNotFoundException;
import jetfighters.windows.listeners.UserInput;
import jetfighters.windows.states.MenuState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class OptionsWindow extends AbstractWindow {
    private final AudioOptions audioOptions;
    private int backgroundState;
    private int tempBackgroundState;

    private JLabel background;
    private JLabel titleLabel;
    private JLabel musicLabel;
    private JLabel soundLabel;
    private JLabel backgroundLabel;

    private JSlider musicSlider;
    private JSlider soundSlider;
    private JButton morningButton;
    private JButton daytimeButton;
    private JButton nightButton;
    private JButton importButton;
    private JButton applyButton;
    private JButton cancelButton;

    private ImageIcon optionsTitleImage;
    private ImageIcon musicImage;
    private ImageIcon soundImage;
    private ImageIcon backgroundLabelImage;

    private ImageIcon applyDefaultImage;
    private ImageIcon applyPressedImage;
    private ImageIcon cancelDefaultImage;
    private ImageIcon cancelPressedImage;

    private ImageIcon morningDefaultImage;
    private ImageIcon morningPressedImage;
    private ImageIcon morningActivatedDefaultImage;
    private ImageIcon morningActivatedPressedImage;
    private ImageIcon daytimeDefaultImage;
    private ImageIcon daytimePressedImage;
    private ImageIcon daytimeActivatedDefaultImage;
    private ImageIcon daytimeActivatedPressedImage;
    private ImageIcon nightDefaultImage;
    private ImageIcon nightPressedImage;
    private ImageIcon nightActivatedDefaultImage;
    private ImageIcon nightActivatedPressedImage;
    private ImageIcon importDefaultImage;
    private ImageIcon importPressedImage;
    private ImageIcon importActivatedDefaultImage;
    private ImageIcon importActivatedPressedImage;

    /**
     * creates the options window
     *
     * @param window the JFrame window as a reference
     */
    public OptionsWindow(Window window) {
        super(window);
        audioOptions = new AudioOptions();
        backgroundState = 1;

        setupImages();
        setupLabels();
        setupSliders();
        setupButtons();
        setupChangeListeners();
        setupActionListeners();
        setupKeyBindings();
        setupMouseListeners();
        setupOptionsWindow();

        updateBackgroundButtons(1);

        this.add(background);
    }

    /**
     * setting up all JComponent
     */
    private void setupOptionsWindow() {
        tempBackgroundState = backgroundState;

        background.removeAll();
        background.add(titleLabel);
        background.add(musicLabel);
        background.add(soundLabel);
        background.add(backgroundLabel);
        background.add(musicSlider);
        background.add(soundSlider);
        background.add(morningButton);
        background.add(daytimeButton);
        background.add(nightButton);
        background.add(importButton);
        background.add(applyButton);
        background.add(cancelButton);
    }

    /**
     * is setting up all the images
     */
    private void setupImages() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            optionsTitleImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Options_title.png"))));
            musicImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Music_label.png"))));
            soundImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Sound_label.png"))));
            backgroundLabelImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "fonts/Background_label.png"))));

            applyDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Applybutton_default.png"))));
            applyPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Applybutton_pressed.png"))));
            cancelDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Cancelbutton_default.png"))));
            cancelPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Cancelbutton_pressed.png"))));

            morningDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Morningbutton_default.png"))));
            morningPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Morningbutton_pressed.png"))));
            morningActivatedDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Morningbutton_activated_default.png"))));
            morningActivatedPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Morningbutton_activated_pressed.png"))));
            daytimeDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Daytimebutton_default.png"))));
            daytimePressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Daytimebutton_pressed.png"))));
            daytimeActivatedDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Daytimebutton_activated_default.png"))));
            daytimeActivatedPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Daytimebutton_activated_pressed.png"))));
            nightDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Nightbutton_default.png"))));
            nightPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Nightbutton_pressed.png"))));
            nightActivatedDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Nightbutton_activated_default.png"))));
            nightActivatedPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Nightbutton_activated_pressed.png"))));
            importDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Importbutton_default.png"))));
            importPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "buttons/Importbutton_pressed.png"))));
            importActivatedDefaultImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Importbutton_activated_default.png"))));
            importActivatedPressedImage = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    classLoader.getResourceAsStream("buttons/Importbutton_activated_pressed.png"))));
        } catch (IOException | NullPointerException e) {
            System.err.println("Couldn't load images in OptionsWindow correctly:");
            e.printStackTrace();
        }
    }

    /**
     * is setting up the labels
     */
    private void setupLabels() {
        background = new JLabel();
        titleLabel = new JLabel();
        musicLabel = new JLabel();
        soundLabel = new JLabel();
        backgroundLabel = new JLabel();

        setupLabel(background, 0, 0, window.getBackgroundImage().getBackgroundImage());
        setupLabel(titleLabel, 250, 50, optionsTitleImage);
        setupLabel(musicLabel, 180, 225, musicImage);
        setupLabel(soundLabel, 180, 325, soundImage);
        setupLabel(backgroundLabel, 20, 425, backgroundLabelImage);
    }

    /**
     * is setting up the sliders
     */
    private void setupSliders() {
        musicSlider = new JSlider(0, 0, 10, 10);
        musicSlider.setBounds(350, 250, 600, 20);
        musicSlider.setOpaque(false);

        soundSlider = new JSlider(0, 0, 10, 10);
        soundSlider.setBounds(350, 350, 600, 20);
        soundSlider.setOpaque(false);
    }

    /**
     * is setting up the buttons
     */
    private void setupButtons() {
        morningButton = new JButton();
        daytimeButton = new JButton();
        nightButton = new JButton();
        importButton = new JButton();
        applyButton = new JButton();
        cancelButton = new JButton();
        setupButton(morningButton, 350, 430, morningDefaultImage);
        setupButton(daytimeButton, 500, 430, daytimeDefaultImage);
        setupButton(nightButton, 650, 430, nightDefaultImage);
        setupButton(importButton, 800, 430, importDefaultImage);
        setupButton(applyButton, 200, 525, applyDefaultImage);
        setupButton(cancelButton, 600, 525, cancelDefaultImage);
    }

    /**
     * is setting up the change listeners
     */
    private void setupChangeListeners() {
        musicSlider.addChangeListener(e -> {
            audioOptions.setMusicValue(musicSlider.getValue() / 10f);
            window.getAudioPlayer().updateMusic();
            window.getAudioPlayer().playSound(Sound.Button02);
        });
        soundSlider.addChangeListener(e -> {
            audioOptions.setSoundValue(soundSlider.getValue() / 10f);
            window.getAudioPlayer().updateSound();
            window.getAudioPlayer().playSound(Sound.Button02);
        });
    }

    /**
     * is setting up the action listeners
     */
    private void setupActionListeners() {
        applyButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            applyOptions();
            resetButtons();
        });
        cancelButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            cancelOptions();
            resetButtons();
        });
        morningButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            window.updateBackgroundImage(1);
            resetButtons();
        });
        daytimeButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            window.updateBackgroundImage(2);
            resetButtons();
        });
        nightButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            window.updateBackgroundImage(3);
            resetButtons();
        });
        importButton.addActionListener(e -> {
            window.getAudioPlayer().playSound(Sound.Button01);
            try {
                window.getBackgroundImage().importBackgroundImage(backgroundState);
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
        setupKeyStroke("Apply", "1", this::applyOptions);
        setupKeyStroke("Cancel", "2", this::cancelOptions);
        setupKeyStroke("Morning", "3", () -> window.updateBackgroundImage(1));
        setupKeyStroke("Daytime", "4", () -> window.updateBackgroundImage(2));
        setupKeyStroke("Night", "5", () -> window.updateBackgroundImage(3));
        setupKeyStroke("Import", "6", () -> window.getBackgroundImage().importBackgroundImage(backgroundState));
    }

    /**
     * is setting up the mouse listeners
     */
    private void setupMouseListeners() {
        setupMouseListeners(applyButton, applyPressedImage, applyDefaultImage);
        setupMouseListeners(cancelButton, cancelPressedImage, cancelDefaultImage);
        setupMouseListeners(morningButton, 1, morningPressedImage, morningDefaultImage,
                morningActivatedPressedImage, morningActivatedDefaultImage);
        setupMouseListeners(daytimeButton, 2, daytimePressedImage, daytimeDefaultImage,
                daytimeActivatedPressedImage, daytimeActivatedDefaultImage);
        setupMouseListeners(nightButton, 3, nightPressedImage, nightDefaultImage,
                nightActivatedPressedImage, nightActivatedDefaultImage);
        setupMouseListeners(importButton, 4, importPressedImage, importDefaultImage,
                importActivatedPressedImage, importActivatedDefaultImage);
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
                    method.clicked();
                    setButtonStatus(button, true);
                    window.getAudioPlayer().playSound(Sound.Button01);
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
            case "Apply":
                if (state) {
                    applyButton.setIcon(applyDefaultImage);
                } else {
                    applyButton.setIcon(applyPressedImage);
                }
                break;
            case "Cancel":
                if (state) {
                    cancelButton.setIcon(cancelDefaultImage);
                } else {
                    cancelButton.setIcon(cancelPressedImage);
                }
                break;
            case "Morning":
                if (state) {
                    updateBackgroundButtons(1);
                } else {
                    if (backgroundState == 1) {
                        morningButton.setIcon(morningActivatedPressedImage);
                    } else {
                        morningButton.setIcon(morningPressedImage);
                    }
                }
                break;
            case "Daytime":
                if (state) {
                    updateBackgroundButtons(2);
                } else {
                    if (backgroundState == 2) {
                        daytimeButton.setIcon(daytimeActivatedPressedImage);
                    } else {
                        daytimeButton.setIcon(daytimePressedImage);
                    }
                }
                break;
            case "Night":
                if (state) {
                    updateBackgroundButtons(3);
                } else {
                    if (backgroundState == 3) {
                        nightButton.setIcon(nightActivatedPressedImage);
                    } else {
                        nightButton.setIcon(nightPressedImage);
                    }
                }
                break;
            case "Import":
                if (state) {
                    updateBackgroundButtons(4);
                } else {
                    if (backgroundState == 4) {
                        importButton.setIcon(importActivatedPressedImage);
                    } else {
                        importButton.setIcon(importPressedImage);
                    }
                }
                break;

            default:
                throw new ImageNotFoundException();
        }
    }

    /**
     * the method for the close operation. it is determining the previous window and updating the background image
     */
    public void closeOptions() {
        updateValues(false);
        try {
            window.updateBackgroundImage(tempBackgroundState);
            if (window.getMenuStateManager().getTempMenuState() == MenuState.Menu) {
                window.updateMenuState(MenuState.Menu);
            } else {
                window.updateMenuState(MenuState.Pause);
            }
        } catch (StateNotFoundException e) {
            e.printStackTrace();
        }
        window.getMenuStateManager().setTempState(null);
    }

    /**
     * the method for the apply button
     */
    public void applyOptions() {
        updateValues(true);
        try {
            if (window.getMenuStateManager().getTempMenuState() == MenuState.Menu) {
                window.updateMenuState(MenuState.Menu);
            } else {
                window.updateMenuState(MenuState.Pause);
            }
        } catch (StateNotFoundException e) {
            e.printStackTrace();
        }
        window.getMenuStateManager().setTempState(null);
    }

    /**
     * the method for the cancel button
     */
    public void cancelOptions() {
        try {
            if (hasChanged()) {
                window.updateMenuState(MenuState.OC);
            } else {
                window.updateMenuState(window.getMenuStateManager().getTempMenuState());
            }
        } catch (StateNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * a method to check if the user changed something before closing the options window
     *
     * @return true if one of these values has changed, no otherwise
     */
    public boolean hasChanged() {
        return audioOptions.hasChanged() || backgroundState != tempBackgroundState;
    }

    /**
     * used to updating the values after closing the options window
     *
     * @param useTempValues if true, the music values will be set to the temp values, temp values will be set otherwise
     */
    public void updateValues(boolean useTempValues) {
        if (useTempValues) {
            audioOptions.setTempMusicValue(audioOptions.getMusicValue());
            audioOptions.setTempSoundValue(audioOptions.getSoundValue());
            tempBackgroundState = backgroundState;
        } else {
            audioOptions.setMusicValue(audioOptions.getTempMusicValue());
            audioOptions.setSoundValue(audioOptions.getTempSoundValue());
            musicSlider.setValue((int) audioOptions.getMusicValue() * 10);
            soundSlider.setValue((int) audioOptions.getSoundValue() * 10);
            updateBackgroundButtons(tempBackgroundState);
        }
    }

    /**
     * resetting the buttons
     */
    public void resetButtons() {
        morningButton.setIcon(morningDefaultImage);
        daytimeButton.setIcon(daytimeDefaultImage);
        nightButton.setIcon(nightDefaultImage);
        importButton.setIcon(importDefaultImage);
        applyButton.setIcon(applyDefaultImage);
        cancelButton.setIcon(cancelDefaultImage);
        updateBackgroundButtons(backgroundState);
    }

    /**
     * is updating the activated button
     *
     * @param button the integer of the new activated button
     */
    public void updateBackgroundButtons(int button) {
        deactivateButton(backgroundState);

        switch (button) {
            case 1 -> morningButton.setIcon(morningActivatedDefaultImage);
            case 2 -> daytimeButton.setIcon(daytimeActivatedDefaultImage);
            case 3 -> nightButton.setIcon(nightActivatedDefaultImage);
            case 4 -> importButton.setIcon(importActivatedDefaultImage);
        }
    }

    /**
     * is deactivating the previous button
     *
     * @param imageState the state of the background image choice
     */
    private void deactivateButton(int imageState) {
        switch (imageState) {
            case 1 -> morningButton.setIcon(morningDefaultImage);
            case 2 -> daytimeButton.setIcon(daytimeDefaultImage);
            case 3 -> nightButton.setIcon(nightDefaultImage);
            case 4 -> importButton.setIcon(importDefaultImage);
        }
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
            case "Morning":
                if (!pressed) {
                    return morningDefaultImage;
                } else {
                    return morningPressedImage;
                }
            case "Daytime":
                if (!pressed) {
                    return daytimeDefaultImage;
                } else {
                    return daytimePressedImage;
                }
            case "Night":
                if (!pressed) {
                    return nightDefaultImage;
                } else {
                    return nightPressedImage;
                }
            case "Import":
                if (!pressed) {
                    return importDefaultImage;
                } else {
                    return importPressedImage;
                }
            case "Apply":
                if (!pressed) {
                    return applyDefaultImage;
                } else {
                    return applyPressedImage;
                }
            case "Cancel":
                if (!pressed) {
                    return cancelDefaultImage;
                } else {
                    return cancelPressedImage;
                }

            default:
                throw new ImageNotFoundException();
        }
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public int getBackgroundState() {
        return backgroundState;
    }

    public void setBackgroundState(int backgroundState) {
        this.backgroundState = backgroundState;
    }

    /**
     * is setting the background with a new image
     *
     * @param background the new image
     * @param button     the button as an integer
     */
    public void setBackgroundImage(ImageIcon background, int button) {
        this.background.setIcon(background);
        updateBackgroundButtons(button);
        setBackgroundState(button);
    }

    /**
     * setting up the mouse listener for the background buttons
     *
     * @param button                the buttons
     * @param state                 the state of the button
     * @param pressedImage          the pressed image of the button
     * @param defaultImage          the default image of the button
     * @param activatedPressedImage the activated pressed image of the button
     * @param activatedDefaultImage the activated default image of the button
     */
    private void setupMouseListeners(JButton button, int state, ImageIcon pressedImage, ImageIcon defaultImage,
                                     ImageIcon activatedPressedImage, ImageIcon activatedDefaultImage) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (backgroundState == state)
                    button.setIcon(activatedPressedImage);
                else
                    button.setIcon(pressedImage);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (backgroundState == state)
                    button.setIcon(activatedDefaultImage);
                else
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
