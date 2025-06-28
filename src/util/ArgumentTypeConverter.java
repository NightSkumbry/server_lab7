package util;

/**
 * The ArgumentTypeConverter class provides utility methods for converting 
 * string arguments into various data types. It ensures safe parsing by handling 
 * null or empty string values gracefully.
 */
public class ArgumentTypeConverter {
    
    /**
     * Converts a string argument into a {@link Long}.
     *
     * @param arg The string argument to be converted.
     * @return The parsed {@link Long} value, or null if the input is null or empty.
     */
    public static Long getLong(String arg) {
        if (arg == null) return null;
        return arg.equals("") ? null : Long.parseLong(arg);
    }

    /**
     * Converts a string argument into a {@link Float}.
     *
     * @param arg The string argument to be converted.
     * @return The parsed {@link Float} value, or null if the input is null or empty.
     */
    public static Float getFloat(String arg) {
        if (arg == null) return null;
        return arg.equals("") ? null : Float.parseFloat(arg);
    }

    /**
     * Converts a string argument into a {@link Double}.
     *
     * @param arg The string argument to be converted.
     * @return The parsed {@link Double} value, or null if the input is null or empty.
     */
    public static Double getDouble(String arg) {
        if (arg == null) return null;
        return arg.equals("") ? null : Double.parseDouble(arg);
    }

    /**
     * Converts a string argument into an {@link Integer}.
     *
     * @param arg The string argument to be converted.
     * @return The parsed {@link Integer} value, or null if the input is null or empty.
     */
    public static Integer getInteger(String arg) {
        if (arg == null) return null;
        return arg.equals("") ? null : Integer.parseInt(arg);
    }

    /**
     * Returns a string argument as is, or null if the input is null or empty.
     *
     * @param arg The string argument to be returned.
     * @return The input string, or null if the input is null or empty.
     */
    public static String getString(String arg) {
        if (arg == null) return null;
        return arg.equals("") ? null : arg;
    }
}
