package controls;

import connections.Request;
import connections.Response;

/**
 * The IRequestProceeder interface defines a contract for processing {@link Request} objects.
 * Implementations of this interface are responsible for handling requests and generating
 * appropriate {@link Response} objects.
 */
public interface IRequestProceeder {

    /**
     * Processes the provided {@link Request} and returns a corresponding {@link Response}.
     *
     * @param request The {@link Request} object to be processed.
     * @return A {@link Response} representing the result of the request processing.
     */
    Response proceedRequest(Request request);
}
