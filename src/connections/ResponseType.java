package connections;

/**
 * The ResponseType enum defines the various types of responses
 * that can be generated during the execution of commands in the application.
 */
public enum ResponseType {
    /**
     * Indicates that the operation was successful.
     */
    SUCCESS,

    /**
     * Indicates a request for additional values from the user.
     */
    REQUEST_VALUES,

    /**
     * Indicates that an invalid value was provided.
     */
    INVALID_VALUE,

    /**
     * Indicates that an invalid argument was provided.
     */
    INVALID_ARGUMENT,

    /**
     * Indicates that the operation is not valid at the current stage.
     */
    INVALID_STAGE,

    /**
     * Indicates that the command is invalid or not recognized.
     */
    INVALID_COMMAND,

    /**
     * Indicates that the request itself is invalid or malformed.
     */
    INVALID_REQUEST,

    /**
     * Indicates that a script execution should be initiated.
     */
    OPEN_SCRIPT,

    /**
     * Indicates that a script execution should be terminated.
     */
    CLOSE_SCRIPT,

    /**
     * Indicates the start of an initialization process.
     */
    START_INIT,

    SERVER_REQUEST,

    VALIDATION_FAILURE,

    AUTH_FAILURE,

    /**
     * Indicates a request to terminate the program.
     */
    EXIT;
}
