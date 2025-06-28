package consoles;

import java.io.IOException;
import java.util.Scanner;

import connections.PrintType;
import connections.Request;
import connections.RequestType;
import connections.Response;
import connections.ResponseType;

/**
 * The Console class implements the {@link IMode} interface and handles user interaction
 * through a console interface. It processes user input, displays responses, and maintains
 * a connection between requests and responses.
 */
public class Console implements IMode {
    // private ReadableByteChannel channel = Channels.newChannel(System.in);
    // private ByteBuffer buffer = ByteBuffer.allocate(1024);
    // private int bytesRead = 0;
    private boolean isRead = true;
    private String input;
    private Scanner scanner;

    /**
     * The {@link Printer} instance used to display messages to the console.
     */
    private Printer printer;

    /**
     * The last {@link Request} made by the user.
     */
    private Request lastRequest;

    /**
     * Поле для хранения последнего сохраненного {@link Response}.
     */
    private Response lastResponse;

    private Thread readerThread = new Thread(() -> {
            try {
                while (true) {
                    if (isRead) {
                        if (scanner.hasNextLine()) input = scanner.nextLine();
                        else {    
                            startNewScanner();
                            input = null;
                        }
                        isRead = false;
                    }
                    else Thread.sleep(10);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    /**
     * Constructs a new Console instance with the specified {@link Printer}.
     *
     * @param printer The {@link Printer} instance used to handle console output.
     */
    public Console(Printer printer) {
        this.printer = printer;
        startNewScanner();
        readerThread.start();

        // try {
        //     readerThread.join();
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        // }
    }

    private void startNewScanner() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Initializes a new {@link Scanner} instance for reading user input.
     */
    public boolean isReady() {
        return !isRead;
    }

    /**
     * Читает данные из Pipe в неблокирующем режиме.
     *
     * @return Прочитанная строка или null, если данных нет.
     */
    public String read() throws IOException {
        if (isReady()) {
            isRead = true;
            return input;
        }
        return null;
    }

    /**
     * Reads a line of input from the console using the provided prompt.
     *
     * @param prompt The prompt message displayed before reading input.
     * @return The input line as a {@link String}.
     */
    public String getLine() {
        try {
            return read();
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves the last created {@link Request}.
     *
     * @return The last {@link Request}.
     */
    public Request getLastRequest() {
        return lastRequest;
    }

    /**
     * Сохраняет предоставленный {@link Response} для дальнейшей обработки или использования.
     *
     * @param response Объект {@link Response}, который нужно сохранить.
     */
    @Override
    public void saveResponse(Response response) {
        this.lastResponse = response;
    }

    /**
     * Processes the provided {@link Response} and generates the next {@link Request}.
     * The method handles different response types, user prompts, and input validation.
     *
     * @param response The {@link Response} object containing the command response details.
     * @return The next {@link Request} generated based on the user's input and the response.
     */
    @Override
    public Request proceedResponse(Response response) {
        if (response == null) response = this.lastResponse;

        Request resultRequest;
        String input;
        try {
            input = read();
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
            return null;
        }

        if (response.getType() == ResponseType.SUCCESS || response.getType() == ResponseType.INVALID_ARGUMENT || response.getType() == ResponseType.INVALID_COMMAND) {
            if (input == null) {
                lastRequest = new Request(-1, RequestType.EXIT, "");
                return lastRequest;
            }
            input = input.trim();
            if (input.equals("")) {
                printer.printError("Не можно не ввести команду");
                return null;
            }
            resultRequest = new Request(-1, RequestType.EXECUTE_COMMAND, input);
        } else {
            if (input == null) {
                resultRequest = this.proceedResponse(new Response(-1, ResponseType.SUCCESS, PrintType.LINE, "(Ctrl+D) Возврат к режиму ввода команды."));
                lastRequest = resultRequest;
                return resultRequest;
            }
            input = input.trim();
            resultRequest = new Request(response.getCommandId(), RequestType.PROCEED_COMMAND, input);
        }

        lastRequest = resultRequest;
        return resultRequest;
    }

    
}
