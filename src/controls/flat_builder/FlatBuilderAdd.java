package controls.flat_builder;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;

import connections.PrintType;
import connections.Response;
import connections.ResponseType;
import controls.CollectionControl;
import models.Coordinates;
import models.Flat;
import models.House;
import models.Transport;
import models.View;

/**
 * The FlatBuilderAdd class extends the {@link BuilderBase} class and facilitates the construction
 * of {@link Flat} objects by sequentially setting their attributes based on input data.
 * It follows a stage-based process to prompt and validate each field of the flat.
 */
public class FlatBuilderAdd extends BuilderBase {
    private static final long serialVersionUID = 1L;

    /**
     * The current stage of the flat-building process.
     */
    private FlatBuilderStage stage;


    /**
     * Constructs a new FlatBuilderAdd instance and initializes it with a {@link CollectionControl}.
     * The building process starts with the {@link FlatBuilderStage#NAME} stage.
     *
     * @param collection The {@link CollectionControl} instance for managing collection-related operations.
     */
    public FlatBuilderAdd(CollectionControl collection) {
        super(collection);

        stage = FlatBuilderStage.NAME;
    }

    public FlatBuilderAdd() {
        super();
        stage = FlatBuilderStage.NAME;
    }


    /**
     * Builds and returns a {@link Flat} object by setting the ID and creation date automatically
     * and using the previously validated field values.
     *
     * @return A new {@link Flat} object with all attributes set.
     */
    public Flat build() {
        setCreationDate(LocalDate.now());
        return new Flat(
            0L, name, new Coordinates(coordinates_x, coordinates_y), creationDate, area, numberOfRooms,
            timeToMetroByTransport, view, transport, new House(house_name, house_year, house_numberOfFloors, house_numberOfFlatsOnFloor)
        ); 
    }

    /**
     * Provides a prompt message for the current stage of the flat-building process.
     *
     * @return A prompt message as a string, or null if the process is complete.
     */
    public String getPrompt() {
        if (stage == FlatBuilderStage.NAME) return promptName();
        if (stage == FlatBuilderStage.COORDINATES_X) return promptCoordinates_x();
        if (stage == FlatBuilderStage.COORDINATES_Y) return promptCoordinates_y();
        if (stage == FlatBuilderStage.AREA) return promptArea();
        if (stage == FlatBuilderStage.NUMBER_OF_ROOMS) return promptNumberOfRooms();
        if (stage == FlatBuilderStage.TIME_TO_METRO_BY_TRANSPORT) return promptTimeToMetroByTransport();
        if (stage == FlatBuilderStage.VIEW) return promptView();
        if (stage == FlatBuilderStage.TRANSPORT) return promptTransport();
        if (stage == FlatBuilderStage.HOUSE_NAME) return promptHouse_name();
        if (stage == FlatBuilderStage.HOUSE_YEAR) return promptHouse_year();
        if (stage == FlatBuilderStage.HOUSE_NUMBER_OF_FLOORS) return promptHouse_numberOfFloors();
        if (stage == FlatBuilderStage.HOUSE_NUMBER_OF_FLATS_ON_FLOOR) return promptHouse_numberOfFlatsOnFloor();
        return null;
    }

    /**
     * Sets the value for the current field based on the input data and progresses
     * to the next stage if the value is valid.
     *
     * @param data The input data for the current field.
     * @return A {@link Response} indicating the success or failure of the operation.
     */
    public Response setValue(String data) {
        if (stage == FlatBuilderStage.NAME) {
            Response resp = setName(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.COORDINATES_X;
            return resp;
        }
        if (stage == FlatBuilderStage.COORDINATES_X) {
            Response resp = setCoordinates_x(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.COORDINATES_Y;
            return resp;
        }
        if (stage == FlatBuilderStage.COORDINATES_Y) {
            Response resp = setCoordinates_y(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.AREA;
            return resp;
        }
        if (stage == FlatBuilderStage.AREA) {
            Response resp = setArea(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.NUMBER_OF_ROOMS;
            return resp;
        }
        if (stage == FlatBuilderStage.NUMBER_OF_ROOMS) {
            Response resp = setNumberOfRooms(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.TIME_TO_METRO_BY_TRANSPORT;
            return resp;
        }
        if (stage == FlatBuilderStage.TIME_TO_METRO_BY_TRANSPORT) {
            Response resp = setTimeToMetroByTransport(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.VIEW;
            return resp;
        }
        if (stage == FlatBuilderStage.VIEW) {
            Response resp = setView(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.TRANSPORT;
            return resp;
        }
        if (stage == FlatBuilderStage.TRANSPORT) {
            Response resp = setTransport(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.HOUSE_NAME;
            return resp;
        }
        if (stage == FlatBuilderStage.HOUSE_NAME) {
            Response resp = setHouse_name(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.HOUSE_YEAR;
            return resp;
        }
        if (stage == FlatBuilderStage.HOUSE_YEAR) {
            Response resp = setHouse_year(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.HOUSE_NUMBER_OF_FLOORS;
            return resp;
        }
        if (stage == FlatBuilderStage.HOUSE_NUMBER_OF_FLOORS) {
            Response resp = setHouse_numberOfFloors(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.HOUSE_NUMBER_OF_FLATS_ON_FLOOR;
            return resp;
        }
        if (stage == FlatBuilderStage.HOUSE_NUMBER_OF_FLATS_ON_FLOOR) {
            Response resp = setHouse_numberOfFlatsOnFloor(data);
            if (resp.getType() == ResponseType.SUCCESS) this.stage = FlatBuilderStage.READY;
            return resp;
        }
        return new Response(-1, ResponseType.INVALID_STAGE, PrintType.ERROR, "А всё");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        coordinates_x = in.readFloat();
        coordinates_y = in.readDouble();
        area = in.readFloat();
        numberOfRooms = in.readLong();
        timeToMetroByTransport = in.readFloat();
        view = View.valueOf((String) in.readObject());
        transport = Transport.valueOf((String) in.readObject());
        house_name = (String) in.readObject();
        house_year = in.readInt();
        house_numberOfFloors = in.readLong();
        house_numberOfFlatsOnFloor = in.readLong();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeFloat(coordinates_x);
        out.writeDouble(coordinates_y);
        out.writeFloat(area);
        out.writeLong(numberOfRooms);
        out.writeFloat(timeToMetroByTransport);
        out.writeObject(view.name());
        out.writeObject(transport.name());
        out.writeObject(house_name);
        out.writeInt(house_year);
        out.writeLong(house_numberOfFloors);
        out.writeLong(house_numberOfFlatsOnFloor);
    }
}
