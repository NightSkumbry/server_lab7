package exceptions;

/**
 * The UnknownCommandException class extends {@link RuntimeException} and represents
 * an exception that is thrown when an unknown or unrecognized command is encountered
 * during command processing.
 */
public class UnknownCommandException extends RuntimeException {

    /**
     * Constructs a new UnknownCommandException with the specified error message.
     *
     * @param message The detail message describing the exception.
     */
    public UnknownCommandException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnknownCommandException with the specified error message
     * and a cause for the exception.
     *
     * @param message The detail message describing the exception.
     * @param cause   The cause of the exception.
     */
    public UnknownCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    
    /**
     * Overrides the default {@link RuntimeException#getMessage()} method to provide
     * a custom error message indicating the unknown command.
     *
     * @return A custom error message including the unknown command.
     */
    @Override
    public String getMessage() {
        return "Ошибка: введена неизвестная команда " + super.getMessage();
    }
}
