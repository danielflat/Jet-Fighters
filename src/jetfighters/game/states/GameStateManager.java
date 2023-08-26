package jetfighters.game.states;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class GameStateManager {

    private GameState currenState;

    public GameStateManager() {
        setState(GameState.Menu);
    }

    public void setState(GameState nextState) {
        currenState = nextState;
    }

    public GameState getState() {
        return currenState;
    }

    public boolean inAny(GameState... states) {
        for (GameState state : states) {
            if (currenState == state) {
                return true;
            }
        }
        return false;
    }

}
