package connections;

/**
 * The PrintType enum represents different types of output styles 
 * that can be used when displaying information or errors in the application.
 */
public enum PrintType {
    /**
     * Represents no output.
     */
    NONE,

    /**
     * Represents output displayed as a single line.
     */
    NORMAL,

    /**
     * Represents output displayed as a single line with \n.
     */
    LINE,

    /**
     * Represents a warning output.
     */
    WARNING,

    /**
     * Represents an error output.
     */
    ERROR;
}
