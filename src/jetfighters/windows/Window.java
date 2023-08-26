package jetfighters.windows;

import jetfighters.audio.AudioOptions;
import jetfighters.audio.AudioPlayer;
import jetfighters.audio.Music;
import jetfighters.windows.exceptions.ImageNotFoundException;
import jetfighters.windows.exceptions.StateNotFoundException;
import jetfighters.windows.listeners.CustomWindowListener;
import jetfighters.windows.states.MenuState;
import jetfighters.windows.states.MenuStateManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class Window extends JFrame {

    private static final String NAME = "Jet Fighters";

    private MenuWindow menuWindow;
    private OptionsWindow optionsWindow;
    private ChooseWindow chooseWindow;
    private PauseWindow pauseWindow;
    private GameWindow gameWindow;
    private GameOverWindow gameOverWindow;
    private OptionsCancelWindow optionsCancelWindow;
    private PauseLeaveWindow pauseLeaveWindow;
    private AbstractWindow currentWindow;
    private AudioPlayer audioPlayer;
    private BackgroundImage backgroundImage;
    private MenuStateManager menuStateManager;

    /**
     * the constructor of the window
     */
    public Window() {
        this(false);
    }

    /**
     * the constructor of the window
     *
     * @param muted true, if no sounds and music should be played, false otherwise
     */
    public Window(boolean muted) {
        super(NAME);
        setupWindow(muted);
        setupStart();
    }

    /**
     * is setting up the menu
     *
     * @param muted true, if no sounds and music should be played, false otherwise
     */
    private void setupWindow(boolean muted) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            this.setIconImage(new ImageIcon(ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(
                    "sprites/jet/jet1/Jet_1_1.png")))).getImage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //is setting up the JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBounds(0, 0, 1080, 720);
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.addWindowListener(new CustomWindowListener(this));

        backgroundImage = new BackgroundImage(this);

        //initialise windows
        menuWindow = new MenuWindow(this);
        optionsWindow = new OptionsWindow(this);
        chooseWindow = new ChooseWindow(this);
        gameWindow = new GameWindow(this);
        pauseWindow = new PauseWindow(this);
        gameOverWindow = new GameOverWindow(this);
        optionsCancelWindow = new OptionsCancelWindow(this);
        pauseLeaveWindow = new PauseLeaveWindow(this);

        AudioOptions audioOptions = optionsWindow.getAudioOptions();
        if (muted) {
            audioOptions.setMusicValue(0);
            audioOptions.setSoundValue(0);
        }
        audioPlayer = new AudioPlayer(audioOptions);
        menuStateManager = new MenuStateManager();

        this.setVisible(true);
    }

    /**
     * is setting up the settings for the start
     */
    private void setupStart() {
        try {
            updateMenuState(MenuState.Menu); //Application starts with the menu
            audioPlayer.playMusic(Music.Tetris03);
        } catch (StateNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * is updating every window with the new background
     *
     * @param backgroundState the new background state
     */
    public void updateBackgroundImage(int backgroundState) {
        try {
            ImageIcon image = backgroundImage.getScaledImage(backgroundState);
            backgroundImage.setBackgroundImage(image);
            menuWindow.setBackgroundImage(image);
            optionsWindow.setBackgroundImage(image, backgroundState);
            chooseWindow.setBackgroundImage(image);
            pauseWindow.setBackgroundImage(image);
            gameOverWindow.setBackgroundImage(image);
            optionsCancelWindow.setBackgroundImage(image);
            pauseLeaveWindow.setBackgroundImage(image);

        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * is updating the window based on the menu state
     *
     * @param menuState the new menu state
     * @throws StateNotFoundException if menuState is no real menu state
     */
    public void updateMenuState(MenuState menuState) throws StateNotFoundException {
        if (currentWindow != null) {
            this.remove(currentWindow);
        }

        if (menuState == null) return;
        switch (menuState) {
            case Menu -> currentWindow = menuWindow;
            case Choose -> currentWindow = chooseWindow;
            case Options -> {
                if (menuStateManager.getMenuState() == MenuState.Menu) {
                    menuStateManager.setTempState(MenuState.Menu);
                } else if (menuStateManager.getMenuState() != MenuState.OC) {
                    menuStateManager.setTempState(MenuState.Pause);
                }
                currentWindow = optionsWindow;
            }
            case Pause -> currentWindow = pauseWindow;
            case Versus, Single, Coop -> currentWindow = gameWindow;
            case GameOver -> {
                menuStateManager.setTempState(menuStateManager.getMenuState());
                currentWindow = gameOverWindow;
            }
            case OC -> currentWindow = optionsCancelWindow;
            case PL -> currentWindow = pauseLeaveWindow;
            default -> throw new StateNotFoundException();
        }
        this.add(currentWindow);
        menuStateManager.setMenuState(menuState);
        currentWindow.repaint();
        this.setVisible(true);
    }

    /**
     * a getter for the audio player
     *
     * @return the audio player
     */
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    /**
     * a getter for the menu state manager
     *
     * @return the menu state manager
     */
    public MenuStateManager getMenuStateManager() {
        return menuStateManager;
    }

    /**
     * a getter for the background image
     *
     * @return the background image
     */
    public BackgroundImage getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * a getter for the options window
     *
     * @return the options window
     */
    public OptionsWindow getOptionsWindow() {
        return optionsWindow;
    }

    /**
     * a getter for the pause window
     *
     * @return the pause window
     */
    public PauseWindow getPauseWindow() {
        return pauseWindow;
    }

    /**
     * a getter for the game window
     *
     * @return the game window
     */
    public GameWindow getGameWindow() {
        return gameWindow;
    }

    /**
     * a getter for the game over window
     *
     * @return the game over window
     */
    public GameOverWindow getGameOverWindow() {
        return gameOverWindow;
    }

}
