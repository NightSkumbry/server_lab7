package connections;

/**
 * The RequestType enum defines the various types of requests
 * that can be handled by the application.
 */
public enum RequestType {
    /**
     * Represents a request to execute a command.
     */
    EXECUTE_COMMAND,

    /**
     * Represents a request to exit the program.
     */
    EXIT,

    /**
     * Represents a request to complete the initialization process.
     */
    FINISH_INIT,

    SERVER_RESPONSE,

    /**
     * Represents a request to proceed with the same command.
     */
    PROCEED_COMMAND;
}
