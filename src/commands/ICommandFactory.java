package commands;

import connections.Request;
import controls.CollectionControl;
import controls.CommandControl;

/**
 * The ICommandFactory interface provides methods for creating command instances
 * and retrieving their descriptions. It serves as a blueprint for factory classes
 * responsible for instantiating commands and offering metadata about them.
 */
public interface ICommandFactory {

    /**
     * Creates a new command instance based on the provided parameters.
     *
     * @param id               The unique identifier of the command.
     * @param arguments        The arguments required for the command execution.
     * @param commandControl   The {@link CommandControl} instance for managing commands.
     * @param collectionControl The {@link CollectionControl} instance for managing collections.
     * @return A new {@link ICommand} instance configured with the provided parameters.
     */
    ICommand create(int id, String arguments, CommandControl commandControl, CollectionControl collectionControl, Request request);
    
    /**
     * Retrieves a brief description of the command functionality.
     *
     * @return A short description of the command.
     */
    String getDescription();

    /**
     * Retrieves an extended description of the command, detailing its requirements and behavior.
     *
     * @return A detailed description of the command.
     */
    String getExtendedDescription();
}
