package controls.flat_builder;

/**
 * The FlatBuilderStage enum represents the stages of the flat-building process
 * in a step-by-step manner. Each constant corresponds to a specific field or
 * attribute that needs to be set during the construction of a {@link Flat} object.
 */
public enum FlatBuilderStage {
    ID,
    NAME,
    COORDINATES_X,
    COORDINATES_Y,
    CREATION_DATE,
    AREA,
    NUMBER_OF_ROOMS,
    TIME_TO_METRO_BY_TRANSPORT,
    VIEW,
    TRANSPORT,
    HOUSE_NAME,
    HOUSE_YEAR,
    HOUSE_NUMBER_OF_FLOORS,
    HOUSE_NUMBER_OF_FLATS_ON_FLOOR,
    READY;
}
