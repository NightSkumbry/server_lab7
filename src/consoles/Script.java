package consoles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import connections.PrintType;
import connections.Request;
import connections.RequestType;
import connections.Response;
import connections.ResponseType;

/**
 * The Script class implements the {@link IMode} interface and provides functionality
 * to execute commands from a script file. It reads the file line by line and processes
 * each command sequentially while handling potential errors.
 */
public class Script implements IMode{

    /**
     * The {@link Printer} instance for outputting messages to the console.
     */
    private Printer printer;

    /**
     * The name of the script file.
     */
    private Scanner scanner;

    /**
     * The unique identifier of the command that initiated the script.
     */
    private String filename;

    /**
     * The last {@link Request} generated during script execution.
     */
    private int initiatorCommandId;

    /**
     * The last command executed from the script file.
     */
    private Request lastRequest;

    /**
     * The {@link Scanner} instance used to read the script file.
     */
    private String lastCommand;


    /**
     * Constructs a new Script instance and initializes the script execution.
     * Validates the script file's existence and access permissions.
     *
     * @param printer           The {@link Printer} instance for outputting messages.
     * @param filename          The name of the script file to be executed.
     * @param initiatorCommandId The ID of the initiating command.
     */
    public Script(Printer printer, String filename, int initiatorCommandId) {
        super();

        this.printer = printer;
        this.filename = filename;
        this.initiatorCommandId = initiatorCommandId;

        File file = new File(filename);
        if (!file.exists()) {
            lastRequest = new Request(initiatorCommandId, RequestType.PROCEED_COMMAND, "Файл " + filename + " не найден, скрипт не выполнен");
        }
        else if (!file.canRead()) {
            lastRequest = new Request(initiatorCommandId, RequestType.PROCEED_COMMAND, "Отсутствует доступ к файлу " + filename + ", скрипт не выполнен");
        }
        else {
            try {
                this.scanner = new Scanner(file);
                proceedResponse(new Response(-1, ResponseType.SUCCESS, PrintType.LINE, ""));
            }
            catch (FileNotFoundException e) {
                printer.print(e);
                lastRequest = new Request(initiatorCommandId, RequestType.PROCEED_COMMAND, "Файл " + filename + " не найден, скрипт не выполнен");
            }
        }

    }


    /**
     * Retrieves the name of the script file.
     *
     * @return The name of the script file.
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Reads a line from the script file. If the file has reached its end,
     * the scanner is closed and null is returned.
     *
     * @param prompt Ignored in this implementation.
     * @return The next line from the script file, or null if the file has ended.
     */
    public String getLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        scanner.close();
        return null;
    }

    /**
     * Retrieves the last {@link Request} generated during script execution.
     *
     * @return The last {@link Request}.
     */
    public Request getLastRequest() {
        return lastRequest;
    }
    

    /**
     * Processes the provided {@link Response} and generates the next {@link Request}.
     * Handles different response types, iterates through the script, and validates commands.
     *
     * @param response The {@link Response} object containing the result of command execution.
     * @return The next {@link Request} based on the script and response.
     */
    @Override
    public Request proceedResponse(Response response) {
        if (response.getType() == ResponseType.SUCCESS && response.getContent() != null) printer.println(response.getContent() + "\n");
        if (response.getType() == ResponseType.INVALID_ARGUMENT || response.getType() == ResponseType.INVALID_VALUE) printer.printWarning("Не удалось выполнить команду " + lastCommand + ":\n" + response.getContent() + "\n");
        String line = getLine();
        if (line == null) {
            Request r = new Request(initiatorCommandId, RequestType.PROCEED_COMMAND, "Скрипт " + getFilename() + " завершил работу");
            lastRequest = r;
            return r;
        }
        if (response.getType() != ResponseType.REQUEST_VALUES) lastCommand = line;

        Request resultRequest;

        if (response.getType() == ResponseType.REQUEST_VALUES) resultRequest = new Request(response.getCommandId(), RequestType.PROCEED_COMMAND, line);
        else resultRequest = new Request(-1, RequestType.EXECUTE_COMMAND, line);
        lastRequest = resultRequest;
        return resultRequest;
    }


    @Override
    public void saveResponse(Response response) {}


}
