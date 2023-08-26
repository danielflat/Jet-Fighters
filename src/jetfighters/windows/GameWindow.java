package jetfighters.windows;

import jetfighters.audio.Sound;
import jetfighters.game.Game;
import jetfighters.game.GetPlayerMethod;
import jetfighters.game.entities.PlayerActionMethod;
import jetfighters.game.entities.PlayerInputMethod;
import jetfighters.game.states.GameState;
import jetfighters.windows.exceptions.StateNotFoundException;
import jetfighters.windows.states.MenuState;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class GameWindow extends AbstractWindow {

    private Game game;

    /**
     * creates the game window
     *
     * @param window the JFrame window as a reference
     */
    public GameWindow(Window window) {
        super(window);
    }

    /**
     * a method for starting the game
     *
     * @param menuState the menu state for the play mode
     */
    public void startGame(MenuState menuState) {
        try {
            window.updateMenuState(menuState);
            background.removeAll();
            startGame();
        } catch (StateNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * is resuming the game state manager
     */
    public void resumeGame() {
        game.setGameState(GameState.Running);
    }

    /**
     * is pausing the game state manager and changing to the pause window
     */
    public void pauseGame() {
        game.setGameState(GameState.Paused);
        window.getPauseWindow().setTempMenuState(window.getMenuStateManager().getMenuState());
        window.getAudioPlayer().playSound(Sound.Button01);
        try {
            window.updateMenuState(MenuState.Pause);
        } catch (StateNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * is setting up everything for the game
     */
    private void startGame() {
        if (game != null) {
            game.stopGameLoop();
            this.remove(game);
        }
        game = new Game(getWidth(), getHeight(), window);
        this.add(game);
        game.initBuffer();

        addPauseKeystroke(this::pauseGame);
        addPlayerKeybindings(1, game::getPlayer1, "W", "A", "S", "D", "C");
        addPlayerKeybindings(2, game::getPlayer2, "I", "J", "K", "L", "N");

        game.startGameLoop();
    }

    /**
     * is restarting the game state manager
     */
    public void restartGame() {
        SwingUtilities.invokeLater(this::startGame);
    }

    /**
     * is adding the key bindings of the players
     *
     * @param playerID   the player id
     * @param player     the player
     * @param keyStrokes the array of keystrokes
     */
    private void addPlayerKeybindings(int playerID, GetPlayerMethod player, String... keyStrokes) {
        if (keyStrokes.length != 5) {
            System.err.println("Player keybindings need exactly 5 keystrokes");
        }
        addPlayerBoolAction(playerID, keyStrokes[0], "MoveForwards", (val) -> player.getPlayer().setMoveForward(val));
        addPlayerBoolAction(playerID, keyStrokes[1], "TurnLeft", (val) -> player.getPlayer().setTurnLeft(val));
        addPlayerBoolAction(playerID, keyStrokes[2], "MoveBackwards", (val) -> player.getPlayer().setMoveBackwards(val));
        addPlayerBoolAction(playerID, keyStrokes[3], "TurnRight", (val) -> player.getPlayer().setTurnRight(val));
        addPlayerBoolAction(playerID, keyStrokes[4], "Shoot", (val) -> player.getPlayer().setShooting(val));
    }

    /**
     * is adding the key bindings of the players
     *
     * @param player     the player id
     * @param keyStroke  the string of the keystrokes
     * @param actionName the action name
     * @param method     the action method
     */
    private void addPlayerBoolAction(int player, String keyStroke, String actionName, PlayerInputMethod method) {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        String playerActionName = player + actionName;
        inputMap.put(KeyStroke.getKeyStroke("pressed " + keyStroke), playerActionName + "P");
        inputMap.put(KeyStroke.getKeyStroke("released " + keyStroke), playerActionName + "R");

        ActionMap actionMap = this.getActionMap();
        actionMap.put(playerActionName + "P", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                method.set(true);
            }
        });
        actionMap.put(playerActionName + "R", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                method.set(false);
            }
        });
    }

    /**
     * is adding the pause keystroke
     *
     * @param method the action method
     */
    private void addPauseKeystroke(PlayerActionMethod method) {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("released ESCAPE"), "EscapeR");

        ActionMap actionMap = this.getActionMap();
        actionMap.put("EscapeR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                method.execute();
            }
        });
    }
}
