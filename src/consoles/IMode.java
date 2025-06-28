package consoles;

import connections.Request;
import connections.Response;
import controls.IResponseProceeder;

/**
 * The IMode interface extends {@link IResponseProceeder} and provides methods
 * for handling user interaction and managing requests in the application.
 * It serves as a blueprint for console-based or other interactive modes.
 */
public interface IMode extends IResponseProceeder{

    /**
     * Reads a line of input from the user using the provided prompt.
     *
     * @param prompt The prompt message displayed before reading input.
     * @return The input line as a {@link String}.
     */
    String getLine();

    void saveResponse(Response response);

    default public boolean isReady() {
        return true;
    }
    /**
     * Reads a line of input from the user using the provided prompt.
     *
     * @param prompt The prompt message displayed before reading input.
     * @return The input line as a {@link String}.
     */
    Request getLastRequest();
}
