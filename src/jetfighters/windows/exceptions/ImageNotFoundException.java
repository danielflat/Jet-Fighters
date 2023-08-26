package jetfighters.windows.exceptions;

/**
 * <h3>PTP 2022 - Projekt "JetFighters"</h3>
 *
 * @author do04 - Daniel Flat, Moritz Junge
 * @version 13.07.22
 */
public class ImageNotFoundException extends Exception {
    public ImageNotFoundException() {
    }

    public ImageNotFoundException(String message) {
        super(message);
    }

    public ImageNotFoundException(Throwable cause) {
        super(cause);
    }

    public ImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
