package jetfighters.tests.windows;

import jetfighters.windows.Window;
import jetfighters.windows.states.MenuState;
import jetfighters.windows.states.MenuStateManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class MenuStateManagerTest {

    private static final Window window = new Window(true);

    private MenuStateManager menuStateManager;

    private final MenuState[] menuStates = new MenuState[]{MenuState.Menu, MenuState.Choose, MenuState.Choose,
            MenuState.Versus, MenuState.Single, MenuState.Coop, MenuState.GameOver, MenuState.OC, MenuState.PL};
    private final MenuState[] menuStatesNoMenu = new MenuState[]{MenuState.Choose, MenuState.Choose,
            MenuState.Versus, MenuState.Single, MenuState.Coop, MenuState.GameOver, MenuState.OC, MenuState.PL};
    private final MenuState[] menuStatesNoChoose = new MenuState[]{MenuState.Choose, MenuState.Choose,
            MenuState.Versus, MenuState.Single, MenuState.Coop, MenuState.GameOver, MenuState.OC, MenuState.PL};

    @BeforeEach
    void setup() {
        menuStateManager = window.getMenuStateManager();
        menuStateManager.setMenuState(MenuState.Menu);
    }

    @Test
    void testInAny() {
        assertTrue(menuStateManager.inAny(MenuState.Menu));
        assertFalse(menuStateManager.inAny(MenuState.Options));
        assertFalse(menuStateManager.inAny(MenuState.Choose));
        assertFalse(menuStateManager.inAny(MenuState.Versus));
        assertFalse(menuStateManager.inAny(MenuState.Single));
        assertFalse(menuStateManager.inAny(MenuState.Coop));
        assertFalse(menuStateManager.inAny(MenuState.GameOver));
        assertFalse(menuStateManager.inAny(MenuState.OC));
        assertFalse(menuStateManager.inAny(MenuState.PL));
        assertFalse(menuStateManager.inAny((MenuState) null));

        assertTrue(menuStateManager.inAny(menuStates));
        assertFalse(menuStateManager.inAny(menuStatesNoMenu));
        assertFalse(menuStateManager.inAny(menuStatesNoChoose));
    }

    @Test
    void testMenuStateManager() {
        assertSame(MenuState.Menu, menuStateManager.getMenuState());
        assertNotSame(MenuState.Versus, menuStateManager.getMenuState());
        assertNotNull(menuStateManager.getMenuState());
        assertNotSame(MenuState.Menu, menuStateManager.getTempMenuState());
        assertNull(menuStateManager.getTempMenuState());

        menuStateManager.setMenuState(MenuState.Pause);

        assertSame(MenuState.Pause, menuStateManager.getMenuState());
        assertNotSame(MenuState.Menu, menuStateManager.getMenuState());
        assertNotSame(MenuState.Versus, menuStateManager.getMenuState());
        assertNotNull(menuStateManager.getMenuState());
        assertNotSame(MenuState.Menu, menuStateManager.getTempMenuState());
        assertNotSame(MenuState.Pause, menuStateManager.getTempMenuState());
        assertNotSame(MenuState.Versus, menuStateManager.getTempMenuState());
        assertNull(menuStateManager.getTempMenuState());

        menuStateManager.setMenuState(MenuState.Options);
        menuStateManager.setTempState(MenuState.Pause);

        assertSame(MenuState.Options, menuStateManager.getMenuState());
        assertNotSame(MenuState.Pause, menuStateManager.getMenuState());
        assertNotSame(MenuState.Versus, menuStateManager.getMenuState());
        assertNotNull(menuStateManager.getMenuState());
        assertSame(MenuState.Pause, menuStateManager.getTempMenuState());
        assertNotSame(MenuState.Options, menuStateManager.getTempMenuState());
        assertNotSame(MenuState.Versus, menuStateManager.getTempMenuState());
        assertNotNull(menuStateManager.getTempMenuState());
    }
}
