package models;

/**
 * The Transport enum represents different levels of transport availability.
 * It includes predefined values ranging from few to enough.
 */
public enum Transport {
	/**
     * Indicates a minimal amount of transport availability.
     */
    FEW,

	/**
     * Indicates a small amount of transport availability.
     */
    LITTLE,

	/**
     * Indicates a normal or average level of transport availability.
     */
    NORMAL,

	/**
     * Indicates an ample or sufficient amount of transport availability.
     */
    ENOUGH;


	/**
     * Returns a comma-separated string of all transport enum names.
     *
     * @return A string containing all enum values separated by commas.
     */
    public static String names() {
		String result = "";
		for (var transport : values()) {
			result += transport.name() + ", ";
		}
		return result.substring(0, result.length()-2);
	}
}
