package jetfighters.windows.listeners;

import jetfighters.windows.Window;
import jetfighters.windows.states.MenuState;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class CustomWindowListener implements WindowListener {
    private final Window window;

    public CustomWindowListener(Window window) {
        this.window = window;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * if the window is getting deactivated while the game is running, the game is getting pause
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
        if ((window.getMenuStateManager().getMenuState() == MenuState.Versus
                || window.getMenuStateManager().getMenuState() == MenuState.Single
                || window.getMenuStateManager().getMenuState() == MenuState.Coop)
                && window.getMenuStateManager().getMenuState() != MenuState.Pause) {
            window.getGameWindow().pauseGame();

        }
    }

}
