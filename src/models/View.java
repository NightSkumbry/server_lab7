package models;

/**
 * The View enum represents various types of views available for a flat.
 * It includes predefined values such as street, park, and others.
 */
public enum View {
    /**
     * Represents a street view.
     */
    STREET,

    /**
     * Represents a park view.
     */
    PARK,

    /**
     * Represents a bad view.
     */
    BAD,

    /**
     * Represents a normal view.
     */
    NORMAL,

    /**
     * Represents a good view.
     */
    GOOD;


    /**
     * Returns a comma-separated string of all view enum names.
     *
     * @return A string containing all enum values separated by commas.
     */
    public static String names() {
        String result = "";
        for (var view : values()) {
            result += view.name() + ", ";
        }
        return result.substring(0, result.length()-2);
	}
}
