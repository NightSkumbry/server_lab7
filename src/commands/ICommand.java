package commands;

import connections.Response;
import controls.CollectionControl;
import controls.CommandControl;
import controls.IRequestProceeder;

/**
 * The ICommand interface extends {@link IRequestProceeder} and serves as a blueprint
 * for command implementations in the application. It provides methods for retrieving
 * essential command details and controlling execution.
 */
public interface ICommand extends IRequestProceeder {

    /**
     * Retrieves the unique identifier of the command.
     *
     * @return The command ID.
     */
    int getId();

    /**
     * Provides the {@link CommandControl} instance associated with the command.
     *
     * @return The {@link CommandControl} instance.
     */
    CommandControl getCommandControl();

    /**
     * Provides the {@link CollectionControl} instance associated with the command.
     *
     * @return The {@link CollectionControl} instance.
     */
    CollectionControl getCollectionControl();

    /**
     * Retrieves the last {@link Response} generated by the command.
     *
     * @return The last {@link Response}.
     */
    Response getLastResponse();
}
