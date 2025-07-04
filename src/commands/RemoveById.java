package commands;

import connections.PrintType;
import connections.Request;
import connections.RequestType;
import connections.Response;
import connections.ResponseType;
import connections.ServerRequest;
import connections.ServerResponse;
import controls.CollectionControl;
import controls.CommandControl;
import models.Flat;
import util.LongWrapper;
import util.StringWrapper;

/**
 * The RemoveById class implements the {@link ICommand} interface and provides functionality
 * to remove a flat from the collection based on its unique identifier.
 */
public class RemoveById implements ICommand{

    /**
     * The unique identifier of the command.
     */
    private int id;

    /**
     * The {@link CommandControl} instance for managing command-related operations.
     */
    private CommandControl commandControl;

    /**
     * The {@link CollectionControl} instance for managing the collection of flats.
     */
    private CollectionControl collectionControl;

    /**
     * The last {@link Response} generated during the command execution.
     */
    private Response lastResponse;


    /**
     * Constructs a new RemoveById command.
     *
     * @param id               The unique identifier for this command.
     * @param arguments        The ID of the flat to be removed (required).
     * @param commandControl   The {@link CommandControl} instance for managing commands.
     * @param collectionControl The {@link CollectionControl} instance for managing the collection.
     */
    public RemoveById(int id, String arguments, CommandControl commandControl, CollectionControl collectionControl) {
        super();
        
        this.id = id;
        this.commandControl = commandControl;
        this.collectionControl = collectionControl;
        proceedRequest(new Request(id, RequestType.EXECUTE_COMMAND, arguments));
    }


    /**
     * Returns the unique identifier of the command.
     *
     * @return The command ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the {@link CommandControl} instance associated with this command.
     *
     * @return The {@link CommandControl} instance.
     */
    public CommandControl getCommandControl() {
        return commandControl;
    }

    /**
     * Returns the {@link CollectionControl} instance associated with this command.
     *
     * @return The {@link CollectionControl} instance.
     */
    public CollectionControl getCollectionControl() {
        return collectionControl;
    }

    /**
     * Returns the last {@link Response} generated by this command.
     *
     * @return The last {@link Response}.
     */
    public Response getLastResponse() {
        return lastResponse;
    }


    /**
     * Executes the command by attempting to remove a flat from the collection using the provided ID.
     * If the ID is invalid or not found in the collection, an error response is returned.
     *
     * @param request The {@link Request} object containing the command details.
     * @return A {@link Response} indicating the outcome of the removal operation.
     */
    private Response executeCommand(Request request) {
        if (request instanceof ServerRequest) {
            ServerRequest req = (ServerRequest) request;
            Long num = ((LongWrapper) req.getContentExt()).getValue();
            Flat res = collectionControl.removeById(num, req.getUser().getName());
            if (res == null) return new ServerResponse(req.getClientCommandId(), ResponseType.INVALID_ARGUMENT, PrintType.WARNING, new StringWrapper("no_data"), req.getClientId());
            return new ServerResponse(req.getClientCommandId(), ResponseType.SUCCESS, PrintType.LINE, res, req.getClientId());
        }

        try {
            if (request.getContent() == null) return new Response(getId(), ResponseType.INVALID_ARGUMENT, PrintType.ERROR, "Команда update remove_by_id в обязательном порядке принимает аргумент id.\nЧто бы узнать про команды подробнее, введите help.");
                Long num = Long.parseLong(request.getContent());
                Flat res = collectionControl.removeById(num, "ADMIN");
                
                if (res == null) return new Response(getId(), ResponseType.INVALID_ARGUMENT, PrintType.ERROR, "не существует квартиры с таким ID.");
                return new Response(getId(), ResponseType.SUCCESS, PrintType.LINE, "Успешно удалена квартира:\n" + res.toString());
            }
            catch (NumberFormatException e) {
                return new Response(getId(), ResponseType.INVALID_ARGUMENT, PrintType.ERROR, request.getContent() + "Неверный формат id");
            }
    }

    /**
     * Processes the provided request and delegates it to the appropriate command handler.
     *
     * @param request The {@link Request} to be processed.
     * @return A {@link Response} representing the outcome of the request processing.
     */
    @Override
    public Response proceedRequest(Request request) {
        if (request.getType() == RequestType.EXECUTE_COMMAND) {
            lastResponse = executeCommand(request);
            return lastResponse;
        }

        return new Response(getId(), ResponseType.SUCCESS, PrintType.WARNING, "Not correct request type");
    }


    /**
     * The Factory class provides methods to create instances of the RemoveById command
     * and describe its functionality.
     */
    public static class Factory implements ICommandFactory {
        
        /**
         * Creates a new instance of the RemoveById command.
         *
         * @param id               The unique identifier for the command.
         * @param arguments        The ID of the flat to be removed (required).
         * @param commandControl   The {@link CommandControl} instance for managing commands.
         * @param collectionControl The {@link CollectionControl} instance for managing the collection.
         * @return A new instance of the RemoveById command.
         */
        @Override
        public ICommand create(int id, String arguments, CommandControl commandControl, CollectionControl collectionControl, Request request) {
            return new RemoveById(id, arguments, commandControl, collectionControl);
        }

        /**
         * Returns a brief description of the RemoveById command.
         *
         * @return A short description of the command functionality.
         */
        @Override
        public String getDescription() {
            return "remove_by_id id: удалить квартиру из коллекции, id которой равен id";
        }

        /**
         * Returns an extended description of the RemoveById command, detailing its behavior.
         *
         * @return A detailed description of the command functionality.
         */
        @Override
        public String getExtendedDescription() {
            return "remove_by_id id: удалить квартиру из коллекции, id которой равен id.";
        }
    }
}
