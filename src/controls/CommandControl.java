package controls;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import commands.*;
import connections.PrintType;
import connections.Request;
import connections.RequestType;
import connections.Response;
import connections.ResponseType;
import connections.ServerRequest;
import consoles.Printer;
import exceptions.UnknownCommandException;
import models.Flat;

/**
 * The CommandControl class is responsible for managing and executing commands
 * in the application. It maintains a registry of available commands and their
 * factories, tracks command execution history, and interacts with the
 * {@link CollectionControl} to manage the collection of {@link Flat} objects.
 */
public class CommandControl implements IRequestProceeder{

    /**
     * A map of command names to their corresponding {@link ICommandFactory} instances.
     */
    private Map<String, ICommandFactory> commands = new LinkedHashMap<>();
    
    /**
     * A history list of all executed commands.
     */
    private List<ICommand> commandHistory = new ArrayList<>();
    
    /**
     * The {@link CollectionControl} instance for managing the collection of flats.
     */

    private CollectionControl collectionControl = new CollectionControl(new Printer());


    /**
     * Constructs a new CommandControl instance and registers all supported commands.
     */
    public CommandControl() {
        registerCommand("help", new Help.Factory());
        registerCommand("info", new Info.Factory());
        registerCommand("show", new Show.Factory());
        registerCommand("add", new Add.Factory());
        registerCommand("add_random", new AddRandom.Factory());
        registerCommand("update", new Update.Factory());
        registerCommand("remove_by_id", new RemoveById.Factory());
        registerCommand("clear", new Clear.Factory());
        registerCommand("save", new Save.Factory());
        registerCommand("execute_script", new ExecuteScript.Factory());
        registerCommand("exit", new Exit.Factory());
        registerCommand("add_if_max", new AddIfMax.Factory());
        registerCommand("remove_greater", new RemoveGreater.Factory());
        registerCommand("remove_lower", new RemoveLower.Factory());
        registerCommand("max_by_creation_date", new MaxByCreationDate.Factory());
        registerCommand("print_descending", new PrintDescending.Factory());
        registerCommand("print_unique_time_to_metro_by_transport", new PrintUniqueTimeToMetroByTransport.Factory());
        registerCommand("get_by_id", new GetById.Factory());
        registerCommand("log_in", new LogIn.Factory());
    }


    /**
     * Retrieves the map of registered commands.
     *
     * @return A map of command names to {@link ICommandFactory} instances.
     */
    protected Map<String, ICommandFactory> getCommands() {
        return commands;
    }

    /**
     * Retrieves the history of executed commands.
     *
     * @return A list of {@link ICommand} instances representing the command history.
     */
    protected List<ICommand> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Registers a new command by associating a name with an {@link ICommandFactory}.
     *
     * @param name           The name of the command.
     * @param commandFactory The factory instance for creating the command.
     */
    protected void registerCommand(String name, ICommandFactory commandFactory) {
        getCommands().put(name, commandFactory);
    }

    /**
     * Retrieves the set of registered command names.
     *
     * @return A set of command names.
     */
    public Set<String> getCommandNameSet() {
        return getCommands().keySet();
    }

    /**
     * Retrieves the description of a specified command.
     *
     * @param commandName The name of the command.
     * @return The description of the command.
     * @throws UnknownCommandException If the command name is not recognized.
     */
    public String getCommandDescription(String commandName) {
        ICommandFactory commandF = getCommands().get(commandName);
        if (commandF == null) throw new UnknownCommandException(commandName);
        return commandF.getDescription();
    }

    /**
     * Retrieves the extended description of a specified command.
     *
     * @param commandName The name of the command.
     * @return The extended description of the command.
     * @throws UnknownCommandException If the command name is not recognized.
     */
    public String getCommandExtendedDescription(String commandName) {
        ICommandFactory commandF = getCommands().get(commandName);
        if (commandF == null) throw new UnknownCommandException(commandName);
        return commandF.getExtendedDescription();
    }

    /**
     * Processes a {@link Request} and executes or delegates the appropriate command.
     *
     * @param request The {@link Request} object containing the command details.
     * @return A {@link Response} representing the result of the command execution.
     */
    @Override
    public Response proceedRequest(Request request) {
        String[] parts = request.getContent().split("\\s+");
        if (request.getType() == RequestType.EXECUTE_COMMAND) {
            ICommandFactory commandF = commands.get(parts[0]);
            if (commandF == null) {
                return new Response(-1, ResponseType.INVALID_COMMAND, PrintType.ERROR, "Введена не существующая команда. Ознакомьтесь со списком, введя help");
            }
            ICommand command = commandF.create(commandHistory.size(), parts.length >= 2 ? parts[1] : null, this, collectionControl, request);
            if (request instanceof ServerRequest && !parts[0].equals("add_random") && !parts[0].equals("clear")) {
                command.proceedRequest(request);
            }
            commandHistory.add(command);
            return command.getLastResponse();
        }
        if (request.getType() == RequestType.PROCEED_COMMAND) {
            ICommand command = commandHistory.get(request.getCommandId());
            if (command == null) {
                return new Response(-1, ResponseType.INVALID_COMMAND, PrintType.ERROR, "Команды с ID " + request.getCommandId() + " не существует.");
            }
            return command.proceedRequest(request);
        }
        return new Response(-1, ResponseType.INVALID_REQUEST, PrintType.ERROR, "Пришёл запрос с недействительным типом: " + request.getType());
    }
}
