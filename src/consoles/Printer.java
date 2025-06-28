package consoles;

/**
 * The Printer class provides utility methods for printing messages to the console.
 * It supports printing normal messages, errors, warnings, and prompts.
 */
public class Printer {
    
    /**
     * Prints the given object to the console without a newline.
     *
     * @param object The object to be printed.
     */
    public void print(Object object) {
        System.out.print(object);
    }

    /**
     * Prints the given object to the console with a newline.
     *
     * @param object The object to be printed.
     */
    public void println(Object object) {
        System.out.println(object);
    }

    /**
     * Prints the given object as an error message, prefixed with "Error: ".
     *
     * @param object The error message to be printed.
     */
    public void printError(Object object) {
        System.out.println("Error: " + object);
    }

    /**
     * Prints the given object as a warning message, prefixed with "Warning: ".
     *
     * @param object The warning message to be printed.
     */
    public void printWarning(Object object) {
        System.out.println("Warning: " + object);
    }

    /**
     * Prints a prompt indicator ("--> ") to the console.
     */
    public void printPrompt() {
        print("--> ");
    }
}
