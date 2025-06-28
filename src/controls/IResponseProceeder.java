package controls;

import connections.Request;
import connections.Response;

/**
 * The IResponseProceeder interface defines a contract for handling {@link Response} objects
 * and generating corresponding {@link Request} objects. It facilitates the interaction
 * between different components of the application.
 */
public interface IResponseProceeder {

    /**
     * Processes the provided {@link Response} and returns a corresponding {@link Request}.
     * This method allows the system to transition based on the outcome of the response.
     *
     * @param response The {@link Response} object to be processed.
     * @return A {@link Request} representing the next action or state.
     */
    Request proceedResponse(Response response);
}
