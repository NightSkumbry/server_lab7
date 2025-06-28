package models;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;

/**
 * The Flat class represents a real estate object with various attributes such as its
 * identification number, name, coordinates, creation date, area, number of rooms, proximity to metro,
 * view type, transport availability, and associated house details.
 * <p>
 * It also implements the {@link Comparable} interface to allow comparison based on the area of the flat.
 */
public class Flat implements Comparable<Flat>, Externalizable{
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the flat. Must not be null, greater than 0, and automatically generated.
     */
    private Long id;

    /**
     * The name of the flat. Must not be null or empty.
     */
    private String name;

    /**
     * The coordinates of the flat's location. Must not be null.
     */
    private Coordinates coordinates;

    /**
     * The creation date of the flat. Must not be null and automatically generated.
     */
    private LocalDate creationDate;

    /**
     * The area of the flat. Must be greater than 0.
     */
    private float area;

    /**
     * The number of rooms in the flat. Must not be null and greater than 0.
     */
    private Long numberOfRooms;

    /**
     * The time required to reach the metro by transport. Must be greater than 0.
     */
    private float timeToMetroByTransport;

    /**
     * The view type of the flat. Can be null.
     */
    private View view;

    /**
     * The transport availability for the flat. Can be null.
     */
    private Transport transport;

    /**
     * The details of the house associated with the flat. Must not be null.
     */
    private House house;


    public Flat() {
        super();
    }

    /**
     * Constructs a new Flat object with the specified attributes.
     *
     * @param id                    The unique identifier for the flat. Must not be null or less than or equal to 0.
     * @param name                  The name of the flat. Must not be null or empty.
     * @param coordinates           The coordinates of the flat. Must not be null.
     * @param creationDate          The creation date of the flat. Must not be null.
     * @param area                  The area of the flat. Must be greater than 0.
     * @param numberOfRooms         The number of rooms in the flat. Must not be null and greater than 0.
     * @param timeToMetroByTransport The time required to reach the metro by transport. Must be greater than 0.
     * @param view                  The view type of the flat. Can be null.
     * @param transport             The transport availability for the flat. Can be null.
     * @param house                 The details of the house associated with the flat. Must not be null.
     */
    public Flat(
        Long id, String name, Coordinates coordinates, LocalDate creationDate, float area, 
        Long numberOfRooms, float timeToMetroByTransport, View view, Transport transport, House house
    ) {
        super();

        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.timeToMetroByTransport = timeToMetroByTransport;
        this.view = view;
        this.transport = transport;
        this.house = house;
    }


    /**
     * Retrieves the unique identifier of the flat.
     *
     * @return The unique identifier as a {@link Long}.
     */
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the flat.
     *
     * @return The name as a {@link String}.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the coordinates of the flat's location.
     *
     * @return The {@link Coordinates} of the flat.
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
     * Retrieves the creation date of the flat.
     *
     * @return The creation date as a {@link LocalDate}.
     */
    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    /**
     * Retrieves the area of the flat.
     *
     * @return The area as a {@code float}.
     */
    public float getArea() {
        return this.area;
    }

    /**
     * Retrieves the number of rooms in the flat.
     *
     * @return The number of rooms as a {@link Long}.
     */
    public Long getNumberOfRooms() {
        return this.numberOfRooms;
    }

    /**
     * Retrieves the time required to reach the metro by transport.
     *
     * @return The time as a {@code float}.
     */
    public float getTimeToMetroByTransport() {
        return this.timeToMetroByTransport;
    }

    /**
     * Retrieves the view type of the flat.
     *
     * @return The {@link View} type of the flat.
     */
    public View getView() {
        return this.view;
    }

    /**
     * Retrieves the transport availability for the flat.
     *
     * @return The {@link Transport} availability of the flat.
     */
    public Transport getTransport() {
        return this.transport;
    }

    /**
     * Retrieves the details of the house associated with the flat.
     *
     * @return The {@link House} associated with the flat.
     */
    public House getHouse() {
        return this.house;
    }


    /**
     * Returns a string representation of the flat in JSON-like format.
     *
     * @return A string representation of the flat.
     */
    @Override
    public String toString() {
        String result = "{\n" + //
                        "  id: " + getId() + ",\n" + //
                        "  name: \"" + getName() + "\",\n" + //
                        "  coordinates: " + getCoordinates() + ",\n" + //
                        "  creation_date: " + getCreationDate() + ",\n" + //
                        "  area: " + getArea() + ",\n" + //
                        "  number_of_rooms: " + getNumberOfRooms() + ",\n" + //
                        "  time_to_metro_by_transport: " + getTimeToMetroByTransport() + ",\n" + //
                        "  view: " + getView() + ",\n" + //
                        "  transport: " + getTransport() + ",\n" + //
                        "  house: " + getHouse() + "\n}";
        return result;
    }
    
    /**
     * Compares this flat to another based on their area.
     *
     * @param other The other {@link Flat} object to compare with.
     * @return -1 if this flat's area is less, 1 if greater, and 0 if equal.
     */
    @Override
    public int compareTo(Flat other) {
        if (this.getArea() < other.getArea()) return -1;
        else if (this.getArea() > other.getArea()) return 1;
        return 0;
    }

    /**
     * Generates a hash code for the flat based on its attributes.
     *
     * @return The hash code as an integer.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + Float.floatToIntBits(area);
        result = prime * result + ((numberOfRooms == null) ? 0 : numberOfRooms.hashCode());
        result = prime * result + Float.floatToIntBits(timeToMetroByTransport);
        result = prime * result + ((view == null) ? 0 : view.hashCode());
        result = prime * result + ((transport == null) ? 0 : transport.hashCode());
        result = prime * result + ((house == null) ? 0 : house.hashCode());
        return result;
    }

    /**
     * Checks if this flat is equal to another object based on its attributes.
     *
     * @param obj The object to compare with.
     * @return True if the object is equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Flat other = (Flat) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (Float.floatToIntBits(area) != Float.floatToIntBits(other.area))
            return false;
        if (numberOfRooms == null) {
            if (other.numberOfRooms != null)
                return false;
        } else if (!numberOfRooms.equals(other.numberOfRooms))
            return false;
        if (Float.floatToIntBits(timeToMetroByTransport) != Float.floatToIntBits(other.timeToMetroByTransport))
            return false;
        if (view != other.view)
            return false;
        if (transport != other.transport)
            return false;
        if (house == null) {
            if (other.house != null)
                return false;
        } else if (!house.equals(other.house))
            return false;
        return true;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(id);
        out.writeObject(name);
        out.writeObject(coordinates);
        out.writeObject(creationDate.toString());
        out.writeFloat(area);
        out.writeLong(numberOfRooms);
        out.writeFloat(timeToMetroByTransport);
        out.writeObject(view.name());
        out.writeObject(transport.name());
        out.writeObject(house);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readLong();
        name = (String) in.readObject();
        coordinates = (Coordinates) in.readObject();
        creationDate = LocalDate.parse((String) in.readObject());
        area = in.readFloat();
        numberOfRooms = in.readLong();
        timeToMetroByTransport = in.readFloat();
        view = View.valueOf((String) in.readObject());
        transport = Transport.valueOf((String) in.readObject());
        house = (House) in.readObject();
    }
}
