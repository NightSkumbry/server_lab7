package controls.flat_builder;

import java.io.Externalizable;

import connections.Response;
import controls.CollectionControl;
import models.Flat;

/**
 * The IBuilder interface defines a blueprint for building {@link Flat} objects.
 * It provides methods for constructing a flat, generating prompts for user input,
 * and setting values for the flat's attributes.
 */
public interface IBuilder extends Externalizable {

    /**
     * Constructs and returns a {@link Flat} object using the current state of the builder.
     *
     * @return A fully built {@link Flat} object.
     */
    public Flat build();

    
    public void setCollection(CollectionControl collection);

    /**
     * Generates a prompt message for the current field or stage of the building process.
     *
     * @return A prompt message as a string.
     */
    public String getPrompt();

    /**
     * Sets the value for the current field based on the provided input.
     *
     * @param data The input data for the current field.
     * @return A {@link Response} indicating the success or failure of the operation.
     */
    public Response setValue(String data);
}
