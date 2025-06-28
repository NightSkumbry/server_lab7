package models;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

/**
 * The Coordinates class represents a set of two-dimensional coordinates (x and y).
 * It includes validation constraints for each field to ensure valid values.
 */
public class Coordinates implements Externalizable {

    /**
     * The x-coordinate. Must be greater than -817 and cannot be null.
     */
    private Float x;

    /**
     * The y-coordinate. Cannot be null.
     */
    private Double y;


    public Coordinates() {
        super();
    }

    /**
     * Constructs a new Coordinates object with the specified x and y values.
     *
     * @param x The x-coordinate. Must be greater than -817 and not null.
     * @param y The y-coordinate. Must not be null.
     */
    public Coordinates(Float x, Double y) {
        super();

        this.x = x;
        this.y = y;
    }


    /**
     * Retrieves the x-coordinate.
     *
     * @return The x-coordinate as a {@link Float}.
     */
    public Float getX() {
        return this.x;
    }

    /**
     * Retrieves the y-coordinate.
     *
     * @return The y-coordinate as a {@link Double}.
     */
    public Double getY() {
        return this.y;
    }


    /**
     * Returns a string representation of the coordinates in the format:
     * "Coordinates: {x: <value>, y: <value>}".
     *
     * @return A string representation of the coordinates.
     */
    @Override
    public String toString() {
        String result = "Coordinates: {x: " + getX() + ", y: " + getY() + "}";
        
        return result;
    }

    /**
     * Generates a hash code for the coordinates object based on its x and y values.
     *
     * @return The hash code as an integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(x);
        out.writeDouble(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readFloat();
        y = in.readDouble();
    }
}
