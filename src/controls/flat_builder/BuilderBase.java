package controls.flat_builder;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;

import connections.PrintType;
import connections.Response;
import connections.ResponseType;
import controls.CollectionControl;
import models.Flat;
import models.Transport;
import models.View;
import util.ArgumentTypeConverter;

/**
 * The BuilderBase class provides a base for constructing {@link Flat} objects with 
 * validation and constraints on their properties. It includes methods to set individual 
 * fields, ensuring data integrity and meeting specified requirements.
 */
public abstract class BuilderBase implements IBuilder{

    private static final long serialVersionUID = 1L;
    /**
     * The unique identifier of the flat. Must not be null, greater than 0, and automatically generated.
     */
    protected Long id;

    /**
     * The name of the flat. Must not be null or empty.
     */
    protected String name;

    /**
     * The x-coordinate of the flat's location. Must not be null and greater than -817.
     */
    protected Float coordinates_x;

    /**
     * The y-coordinate of the flat's location. Must not be null.
     */
    protected Double coordinates_y;

    /**
     * The creation date of the flat. Must not be null and automatically generated.
     */
    protected LocalDate creationDate;

    /**
     * The area of the flat. Must be greater than 0.
     */
    protected float area;

    /**
     * The number of rooms in the flat. Must not be null and greater than 0.
     */
    protected Long numberOfRooms;

    /**
     * The time to reach the metro by transport. Must be greater than 0.
     */
    protected float timeToMetroByTransport;

    /**
     * The view type of the flat. Can be null.
     */
    protected View view;

    /**
     * The transport type of the flat. Can be null.
     */
    protected Transport transport;

    /**
     * The name of the house. Must not be null.
     */
    protected String house_name;

    /**
     * The year the house was built. Must be greater than 0.
     */
    protected int house_year;

    /**
     * The number of floors in the house. Must be between 1 and 65.
     */
    protected Long house_numberOfFloors;

    /**
     * The number of flats per floor in the house. Must be greater than 0.
     */
    protected long house_numberOfFlatsOnFloor;

    /**
     * The {@link CollectionControl} instance for managing collection-related operations.
     */
    protected CollectionControl collection;


    /**
     * Constructs a BuilderBase object, initializing it with a {@link CollectionControl} instance.
     *
     * @param collection The {@link CollectionControl} instance to be used for validation and management.
     */
    protected BuilderBase(CollectionControl collection) {
        super();

        this.collection = collection;
    }

    public BuilderBase() {
        super();
    }

    public void setCollection(CollectionControl collection) {
        this.collection = collection;
    }

    
    /**
     * Validates and sets the ID of the flat.
     *
     * @param data The ID value as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setId(String data) {
        try {
            Long id = ArgumentTypeConverter.getLong(data);
            if (id == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение id не может быть null");
            if (id <= 0l) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение id должно быть больше нуля");
            this.id = id;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение id должно быть целым числом.");
        }

    }

    /**
     * Validates and sets the name of the flat.
     *
     * @param name The name of the flat as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setName(String name) {
        name = ArgumentTypeConverter.getString(name);
        if (name == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение названия квартиры не может быть null");
        this.name = name;
        return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
    }

    /**
     * Validates and sets the x-coordinate of the flat's location.
     *
     * @param data The x-coordinate value as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setCoordinates_x(String data) {
        try {
            Float coordinates_x = ArgumentTypeConverter.getFloat(data);
            if (coordinates_x == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение координаты x не может быть null");
            if (coordinates_x <= -817f) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение координаты x должно быть больше -817");
            this.coordinates_x = coordinates_x;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение координаты x должно быть дробным числом.");
        }
    }

    /**
     * Validates and sets the y-coordinate of the flat's location.
     *
     * @param data The y-coordinate value as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setCoordinates_y(String data) {
        try {
            Double coordinates_y = ArgumentTypeConverter.getDouble(data);
            if (coordinates_y == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение координаты y не может быть null");
            this.coordinates_y = coordinates_y;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение координаты y должно быть дробным числом.");
        }
    }

    /**
     * Validates and sets the creation date of the flat.
     *
     * @param creationDate The creation date as a {@link LocalDate}.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setCreationDate(LocalDate creationDate) {
        if (creationDate == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение даты создания не может быть null");
        this.creationDate = creationDate;
        return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
    }

    /**
     * Validates and sets the area of the flat.
     *
     * @param data The area value as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setArea(String data) {
        try {
            Float area = ArgumentTypeConverter.getFloat(data);
            if (area == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение площади квартиры не может быть null");
            if (area <= 0f) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение площади квартиры должно быть больше нуля");
            this.area = area;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение площади квартиры должно быть дробным числом.");
        }
    }
    
    /**
     * Validates and sets the number of rooms in the flat.
     *
     * @param data The number of rooms value as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setNumberOfRooms(String data) {
        try {
            Long numberOfRooms = ArgumentTypeConverter.getLong(data);
            if (numberOfRooms == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества комнат не может быть null");
            if (numberOfRooms <= 0l) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества комнат должно быть больше нуля");
            this.numberOfRooms = numberOfRooms;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества комнат должно быть целым числом.");
        }
    }
        
    /**
     * Validates and sets the time to metro by transport.
     *
     * @param data The time to metro value as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setTimeToMetroByTransport(String data) {
        try {
            Float timeToMetroByTransport = ArgumentTypeConverter.getFloat(data);
            if (timeToMetroByTransport == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение времени до метро на транспорте не может быть null");
            if (timeToMetroByTransport <= 0f) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение времени до метро на транспорте должно быть больше нуля");
            this.timeToMetroByTransport = timeToMetroByTransport;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение времени до метро на транспорте должно быть дробным числом.");
        }
    }

    /**
     * Validates and sets the view type of the flat.
     *
     * @param view The view type as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setView(String view) {
        view = ArgumentTypeConverter.getString(view.toUpperCase());
        if (view == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение вида не может быть null");
        try {
            this.view = View.valueOf(view);
        }
        catch (IllegalArgumentException e){
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение вида не может быть \"" + view + "\"");
        }
        return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
    }
    
    /**
     * Validates and sets the transport type of the flat.
     *
     * @param transport The transport type as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setTransport(String transport) {
        transport = ArgumentTypeConverter.getString(transport.toUpperCase());
        if (transport == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение транспорта не может быть null");
        try {
            this.transport = Transport.valueOf(transport);
        }
        catch (IllegalArgumentException e){
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение транспорта не может быть \"" + transport + "\"");
        }
        return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
    }

    /**
     * Validates and sets the name of the house.
     *
     * @param house_name The name of the house as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setHouse_name(String house_name) {
        house_name = ArgumentTypeConverter.getString(house_name);
        if (house_name == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение названия дома не может быть null");
        this.house_name = house_name;
        return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
    }

    /**
     * Validates and sets the year the house was built.
     *
     * @param data The year as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setHouse_year(String data) {
        try {
            Integer house_year = ArgumentTypeConverter.getInteger(data);
            if (house_year == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение года постройки дома не может быть null");
            if (house_year <= 0) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение года постройки дома должно быть больше нуля");
            this.house_year = house_year;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
            catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение года постройки дома должно быть целым числом.");
        }
    }

    /**
     * Validates and sets the number of floors in the house.
     *
     * @param data The number of floors as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setHouse_numberOfFloors(String data) {
        try {
            Long house_numberOfFloors = ArgumentTypeConverter.getLong(data);
            if (house_numberOfFloors <= 0l) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества этажей должно быть больше нуля");
            if (house_numberOfFloors > 65l) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества этажей должно быть не больше 65");
            this.house_numberOfFloors = house_numberOfFloors;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества этажей должно быть целым числом.");
        }
    }

    /**
     * Validates and sets the number of flats per floor in the house.
     *
     * @param data The number of flats as a string.
     * @return A {@link Response} indicating success or failure with validation details.
     */
    protected Response setHouse_numberOfFlatsOnFloor(String data) {
        try {
            Long house_numberOfFlatsOnFloor = ArgumentTypeConverter.getLong(data);
            if (house_numberOfFlatsOnFloor == null) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества квартир на этаже не может быть null");
            if (house_numberOfFlatsOnFloor <= 0l) return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества квартир на этаже должно быть больше нуля");
            this.house_numberOfFlatsOnFloor = house_numberOfFlatsOnFloor;
            return new Response(-1, ResponseType.SUCCESS, PrintType.NONE, "Успешно");
        }
        catch (NumberFormatException e) {
            return new Response(-1, ResponseType.INVALID_VALUE, PrintType.ERROR, "значение количества квартир на этаже должно быть целым числом.");
        }
    }


    /**
     * Prompts the user to input the name of the flat.
     *
     * @return A prompt message as a string.
     */
    protected String promptName() {
        return "Введите значение названия квартиры: не пустую строку";
    }

    /**
     * Prompts the user to input the ID of the flat.
     *
     * @return A prompt message as a string.
     */
    protected String promptId() {
        return "Введите значение id: целое число, большее нуля";
    }

    /**
     * Prompts the user to input the creation date of the flat.
     *
     * @return A prompt message as a string.
     */
    protected String promptCreationDate() {
        return "Введите значение даты создания: строка в формате yyyy-MM-dd";
    }

    /**
     * Prompts the user to input the x-coordinate of the flat's location.
     *
     * @return A prompt message as a string.
     */
    protected String promptCoordinates_x() {
        return "Введите значение координаты x: дробное число, большее -817";
    }

    /**
     * Prompts the user to input the y-coordinate of the flat's location.
     *
     * @return A prompt message as a string.
     */
    protected String promptCoordinates_y() {
        return "Введите значение координаты y: дробное число";
    }

    /**
     * Prompts the user to input the area of the flat.
     *
     * @return A prompt message as a string.
     */
    protected String promptArea() {
        return "Введите значение площади квартиры: дробное число, большее нуля";
    }

    /**
     * Prompts the user to input the number of rooms in the flat.
     *
     * @return A prompt message as a string.
     */
    protected String promptNumberOfRooms() {
        return "Введите значение количества комнат в квартире: целое число, большее нуля";
    }

    /**
     * Prompts the user to input the time to the metro by transport.
     *
     * @return A prompt message as a string.
     */
    protected String promptTimeToMetroByTransport() {
        return "Введите значение времени до метро на транспорте в минутах: дробное число, большее нуля";
    }

    /**
     * Prompts the user to input the view type of the flat.
     *
     * @return A prompt message as a string including valid options.
     */
    protected String promptView() {
        return "Введите значение вида: значение из списка: " + View.names();
    }

    /**
     * Prompts the user to input the transport type of the flat.
     *
     * @return A prompt message as a string including valid options.
     */
    protected String promptTransport() {
        return "Введите значение транспорта: значение из списка: " + Transport.names();
    }

    /**
     * Prompts the user to input the name of the house.
     *
     * @return A prompt message as a string.
     */
    protected String promptHouse_name() {
        return "Введите значение названия дома: не пустую строку";
    }

    /**
     * Prompts the user to input the year the house was built.
     *
     * @return A prompt message as a string.
     */
    protected String promptHouse_year() {
        return "Введите значение года постройки дома: целое число, большее нуля";
    }

    /**
     * Prompts the user to input the number of floors in the house.
     *
     * @return A prompt message as a string.
     */
    protected String promptHouse_numberOfFloors() {
        return "Введите значение количества этажей в доме: целое число больше 0, но не большее 65";
    }

    /**
     * Prompts the user to input the number of flats per floor in the house.
     *
     * @return A prompt message as a string.
     */
    protected String promptHouse_numberOfFlatsOnFloor() {
        return "Введите значение количества комнат на этаже в доме: целое число больше 0";
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
