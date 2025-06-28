package models;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

/**
 * The House class represents a building's attributes, including its name,
 * year of construction, number of floors, and number of flats per floor.
 * It validates each field to ensure proper values are assigned.
 */
public class House implements Externalizable {
    /**
     * The name of the house. Must not be null.
     */
    private String name;

    /**
     * The year the house was built. Must be greater than 0.
     */
    private int year;

    /**
     * The number of floors in the house. Must be greater than 0 and not exceed 65.
     */
    private Long numberOfFloors;

    /**
     * The number of flats per floor in the house. Must be greater than 0.
     */
    private long numberOfFlatsOnFloor;


    public House() {
        super();
    }

    /**
     * Constructs a new House instance with the specified attributes.
     *
     * @param name                 The name of the house. Cannot be null.
     * @param year                 The year the house was built. Must be greater than 0.
     * @param numberOfFloors       The number of floors in the house. Must be greater than 0 and not exceed 65.
     * @param numberOfFlatsOnFloor The number of flats per floor in the house. Must be greater than 0.
     */
    public House(String name, int year, Long numberOfFloors, long numberOfFlatsOnFloor) {
        super();

        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }


    /**
     * Retrieves the name of the house.
     *
     * @return The name of the house as a string.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the year the house was built.
     *
     * @return The year of construction as an integer.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Retrieves the number of floors in the house.
     *
     * @return The number of floors as a {@link Long}.
     */
    public Long getNumberOfFloors() {
        return this.numberOfFloors;
    }

    /**
     * Retrieves the number of flats per floor in the house.
     *
     * @return The number of flats per floor as a long.
     */
    public long getNumberOfFlatsOnFloor() {
        return this.numberOfFlatsOnFloor;
    }


    /**
     * Returns a string representation of the house in the format:
     * "House: {name: <value>, year: <value>, number_of_floors: <value>, number_of_flats_on_floor: <value>}".
     *
     * @return A string representation of the house.
     */
    @Override
    public String toString() {
        String result = "House: {name: \"" + getName() + "\", year: " + getYear() + ", number_of_floors: " + getNumberOfFloors() + ", number_of_flats_on_floor: " + getNumberOfFlatsOnFloor() + "}";

        return result;
    }

    /**
     * Generates a hash code for the house object based on its attributes.
     *
     * @return The hash code as an integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getYear(), getNumberOfFloors(), getNumberOfFlatsOnFloor());
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(year);
        out.writeLong(numberOfFloors);
        out.writeLong(numberOfFlatsOnFloor);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        year = in.readInt();
        numberOfFloors = in.readLong();
        numberOfFlatsOnFloor = in.readLong();
    }
}
