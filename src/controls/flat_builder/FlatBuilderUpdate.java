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
import util.ArgumentTypeConverter;

/**
 * The FlatBuilderUpdate class extends the {@link BuilderBase} class and facilitates the
 * process of updating an existing {@link Flat} object in a collection. It validates and
 * sets new values for each attribute of the flat sequentially, following a stage-based
 * approach.
 */
public class FlatBuilderUpdate extends BuilderBase{
    private static final long serialVersionUID = 1L;

    /**
     * The current stage of the flat update process.
     */
    private FlatBuilderStage stage;


    /**
     * Constructs a new FlatBuilderUpdate instance and initializes it with a {@link CollectionControl}.
     * The update process starts at the {@link FlatBuilderStage#ID} stage.
     *
     * @param collection The {@link CollectionControl} instance for managing the collection and validating data.
     */
    public FlatBuilderUpdate(CollectionControl collection) {
        super(collection);

        stage = FlatBuilderStage.ID;
    }
    
    public FlatBuilderUpdate() {
        super();
        stage = FlatBuilderStage.ID;
    }


    /**
     * Builds and returns an updated {@link Flat} object using the validated and modified attributes.
     *
     * @return A new {@link Flat} object with updated values.
     */
    public Flat build() {
        return new Flat(
            id, name, new Coordinates(coordinates_x, coordinates_y), creationDate, area, numberOfRooms,
            timeToMetroByTransport, view, transport, new House(house_name, house_year, house_numberOfFloors, house_numberOfFlatsOnFloor)
        ); 
    }

    /**
     * Provides a prompt message corresponding to the current stage of the update process.
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
     * Updates the value of the current field based on the input data and progresses
     * to the next stage if the value is valid. If the flat ID is invalid or not found
     * in the collection, an error response is returned.
     *
     * @param data The input data for updating the current field.
     * @return A {@link Response} indicating success or failure of the update operation.
     */
    public Response setValue(String data) {
        if (stage == FlatBuilderStage.ID) {
            try {
                Long num = ArgumentTypeConverter.getLong(data);
                Flat flat = collection.getById(num);
                if (flat == null) return new Response(-1, ResponseType.INVALID_ARGUMENT, PrintType.ERROR, data + " не существует квартиры с таким ID.");
                Response resp = setId(num.toString());
                setCreationDate(flat.getCreationDate());
                if (resp.getType() == ResponseType.SUCCESS) {
                    this.stage = FlatBuilderStage.NAME;
                    return new Response(resp.getCommandId(), resp.getType(), resp.getPrintType(), flat.toString());
                }
                return new Response(resp.getCommandId(), ResponseType.INVALID_ARGUMENT, resp.getPrintType(), resp.getContent(), resp.getPrompt());
                
            }
            catch (NumberFormatException e) {
                return new Response(-1, ResponseType.INVALID_ARGUMENT, PrintType.ERROR, "значение id должно быть целым числом.");
            }
        }
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(id);
        out.writeObject(name);
        out.writeFloat(coordinates_x);
        out.writeDouble(coordinates_y);
        out.writeObject(creationDate.toString());
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

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readLong();
        name = (String) in.readObject();
        coordinates_x = in.readFloat();
        coordinates_y = in.readDouble();
        creationDate = LocalDate.parse((String) in.readObject());
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

}
