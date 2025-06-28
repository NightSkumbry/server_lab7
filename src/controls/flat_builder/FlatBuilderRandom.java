package controls.flat_builder;

import connections.Response;
import controls.CollectionControl;
import models.Coordinates;
import models.Flat;
import models.House;
import models.Transport;
import models.View;

import java.time.LocalDate;
import java.util.Random;

/**
 * The FlatBuilderRandom class extends the {@link BuilderBase} class and facilitates
 * the construction of randomly generated {@link Flat} objects.
 * <p>
 * This builder assigns random, valid values to all fields of a {@link Flat}, ensuring
 * that all constraints are satisfied.
 */
public class FlatBuilderRandom extends BuilderBase{

    /**
     * An instance of {@link Random} used for generating random values.
     */
    Random random = new Random();


    /**
     * Constructs a new FlatBuilderRandom instance with the specified {@link CollectionControl}.
     *
     * @param collection The {@link CollectionControl} instance for managing collection-related operations.
     */
    public FlatBuilderRandom(CollectionControl collection) {
        super(collection);
    }


    /**
     * Builds and returns a {@link Flat} object with random attributes.
     * <p>
     * Generates random values for each field, including a random date within the valid range,
     * random coordinates, and random enum values for {@link View} and {@link Transport}.
     *
     * @return A new {@link Flat} object with randomly assigned values.
     */
    public Flat build() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = random.nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return new Flat(
            0L, 
            "random flat", 
            new Coordinates(random.nextFloat(-816, 1000), random.nextDouble(-1000, 1000)), 
            randomDate, 
            random.nextFloat(1, 1000), 
            random.nextLong(1, 20),
            random.nextFloat(1, 1000), 
            View.values()[random.nextInt(View.values().length)], 
            Transport.values()[random.nextInt(Transport.values().length)], 
            new House("house name for random flat", random.nextInt(1, 2025), random.nextLong(1, 65), random.nextLong(1, 1000))
        ); 
    }

    /**
     * Returns a prompt message for the current builder stage.
     * <p>
     * For FlatBuilderRandom, this method always returns null since no user input is required.
     *
     * @return null, as this builder does not require prompts.
     */
    public String getPrompt() {
        return null;
    }

    /**
     * Sets a value based on the input data.
     * <p>
     * For FlatBuilderRandom, this method always returns null since no user input is processed.
     *
     * @param data The input data (not used in this implementation).
     * @return null, as this builder does not handle user-provided values.
     */
    public Response setValue(String data) {
        
        return null;
    }
}
