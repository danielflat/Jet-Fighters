package jetfighters.windows.exceptions;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class StateNotFoundException extends Exception {
    public StateNotFoundException() {
    }

    public StateNotFoundException(String message) {
        super(message);
    }

    public StateNotFoundException(Throwable cause) {
        super(cause);
    }

    public StateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

