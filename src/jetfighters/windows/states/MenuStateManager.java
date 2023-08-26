package jetfighters.windows.states;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class MenuStateManager {
    private MenuState currentState;
    private MenuState tempState;

    public MenuStateManager() {
    }

    /**
     * looks if one of the states in the array is the current state
     *
     * @param states the states to test
     * @return if one of the states is the current state, no otherwise
     */
    public boolean inAny(MenuState... states) {
        for (MenuState state : states) {
            if (currentState == state) {
                return true;
            }
        }
        return false;
    }

    /**
     * a getter for the current menu state
     *
     * @return the current menu state
     */
    public MenuState getMenuState() {
        return currentState;
    }

    /**
     * a setter for the current menu state
     *
     * @param nextState the new current menu state
     */
    public void setMenuState(MenuState nextState) {
        currentState = nextState;
    }

    /**
     * a getter for the temporary menu state
     *
     * @return the temporary menu state
     */
    public MenuState getTempMenuState() {
        return tempState;
    }

    /**
     * a setter for the temporary menu state
     *
     * @param nextState the new temporary menu state
     */
    public void setTempState(MenuState nextState) {
        tempState = nextState;
    }
}
