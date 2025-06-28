package controls;

import java.util.Stack;

import connections.PrintType;
import connections.Request;
import connections.RequestType;
import connections.Response;
import connections.ResponseType;
import connections.ServerResponse;
import consoles.Console;
import consoles.IMode;
import consoles.Printer;
import consoles.Script;

/**
 * The ModeControl class implements the {@link IResponseProceeder} interface and manages
 * various modes of operation, including console interaction and script execution.
 * It facilitates transitioning between modes based on responses and handles recursion prevention.
 */
public class ModeControl implements IResponseProceeder{

    /**
     * The current console mode instance.
     */
    private IMode console;

    /**
     * A stack of active scripts for managing nested script execution.
     */
    private Stack<Script> scriptStack = new Stack<>();

    /**
     * A stack of filenames to prevent recursion in script execution.
     */
    private Stack<String> filenameStack = new Stack<>();

    /**
     * The {@link Printer} instance for outputting messages to the console.
     */
    private Printer printer;

    private ConnectionControl connections;

    private IRequestProceeder commandControl;


    private Thread connectionThread = new Thread(() -> {
            try {
                while (true) {
                    Request r = connections.read();
                    Thread.sleep(10);
                    if (r != null) {
                        new Thread(() -> {
                            ServerResponse resp = (ServerResponse) commandControl.proceedRequest(r);
                            connections.sendResponse(resp);
                        }).start();
                    }
                    
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    
    

    /**
     * Constructs a new ModeControl instance and initializes the console mode.
     */
    public ModeControl(IRequestProceeder commandControl) {
        super();

        this.commandControl = commandControl;

        printer = new Printer();
        console = new Console(printer);
        connections = new ConnectionControl(25566, printer);
        connectionThread.start();
    }


    /**
     * Processes the provided {@link Response} and determines the appropriate mode or action.
     * Handles console initialization, script execution, recursion prevention, and transitioning
     * between different modes.
     *
     * @param response The {@link Response} object containing the result of a command execution.
     * @return A {@link Request} representing the next action or state.
     */
    @Override
    public Request proceedResponse(Response response) {
        Request resultRequest;

        // Если это ServerResponse, отправляем его через connections
        if (response instanceof ServerResponse) {
            connections.sendResponse((ServerResponse) response);
            return getRequest(); 
        }

        // Обработка этапов инициализации
        if (response.getType() == ResponseType.START_INIT) {
            if (response.getPrintType() == PrintType.ERROR) {
                printer.printWarning(response.getContent());
                printer.println("Без данного значения, команда save не может быть выполнена.");
                resultRequest = new Request(-1, RequestType.FINISH_INIT, "");

                console = new Console(printer);
                connectionThread.start();
                return this.proceedResponse(new Response(-1, ResponseType.SUCCESS, PrintType.LINE, resultRequest.getContent() + "\n\nМенеджер коллекции квартир готов к работе.\n\nАвтор: NightSkumbry.\nВариант лабы 5: 1995\n\nНапиши help, чтобы узнать о доступных командах."));
            
                }
            else {
                resultRequest = console.getLastRequest();
                if (resultRequest.getType() == RequestType.FINISH_INIT) {
                    console = new Console(printer);
                    connectionThread.start();
                    return this.proceedResponse(new Response(-1, ResponseType.SUCCESS, PrintType.LINE, resultRequest.getContent() + "\n\nМенеджер коллекции квартир готов к работе.\n\nАвтор: NightSkumbry.\nВариант лабы 5: 1995\n\nНапиши help, чтобы узнать о доступных командах."));
                }
                return resultRequest;
            }
        }

        // Обработка открытия скрипта
        if (response.getType() == ResponseType.OPEN_SCRIPT) {
            if (filenameStack.contains(response.getContent())) {
                response = new Response(response.getCommandId(), ResponseType.INVALID_ARGUMENT, PrintType.ERROR, "Скрипт " + response.getContent() + " уже был вызван ранее. Рекурсия запрещена, скрипт не будет выполнен.");
            }
            else {
                Script script = new Script(printer, response.getContent(), response.getCommandId());
                scriptStack.push(script);
                filenameStack.push(response.getContent());
                resultRequest = script.getLastRequest();
                return resultRequest;
            }
        }

        // Обработка закрытия скрипта
        if (response.getType() == ResponseType.CLOSE_SCRIPT) {
            scriptStack.pop();
            filenameStack.pop();
            response = new Response(response.getCommandId(), ResponseType.SUCCESS, response.getPrintType(), response.getContent());
        }

        if (scriptStack.size() == 0) {
                if (console instanceof Console) {
                    if (response.getPrintType() == PrintType.ERROR) printer.printError(response.getContent());
                    if (response.getPrintType() == PrintType.WARNING) printer.printWarning(response.getContent());
                    if (response.getPrintType() == PrintType.LINE) printer.println(response.getContent());
                    if (response.getPrintType() == PrintType.NORMAL) printer.print(response.getContent());

                    if (response.getPrompt() != null) printer.println(response.getPrompt());
                    // printer.printPrompt();
                }
                console.saveResponse(response);
                resultRequest = getRequest(); 
                if (resultRequest.getType() == RequestType.FINISH_INIT) {
                    console = new Console(printer);
                    return this.proceedResponse(new Response(-1, ResponseType.SUCCESS, PrintType.LINE, 
                        "Менеджер коллекции квартир готов к работе.\n\nАвтор: NightSkumbry.\nВариант лабы 5: 1995\nВариант лабы 6: 7892\n\nНапиши help, чтобы узнать о доступных командах."));
                }

                // while (true) {
                //     if (console.isReady()) {
                //         resultRequest = console.proceedResponse(response);
                //         if (resultRequest.getType() == RequestType.FINISH_INIT) {
                //             console = new Console(printer);
                //             return this.proceedResponse(new Response(-1, ResponseType.SUCCESS, PrintType.LINE, resultRequest.getContent() + "\n\nМенеджер коллекции квартир готов к работе.\n\nАвтор: NightSkumbry.\nВариант лабы 5: 1995\n\nНапиши help, чтобы узнать о доступных командах."));
                //         }
                //         return resultRequest;
                //     }
                //     if (connections.isReady()) {
                //         return connections.read();
                //     }
                // }
            }

        else resultRequest = scriptStack.peek().proceedResponse(response);

        return resultRequest;
    }

    /**
     * Заглушка для метода getRequest.
     * В будущем здесь будет реализован цикл с неблокирующим вводом.
     *
     * @return Заглушка {@link Request}.
     */
    private Request getRequest() {
        while (true) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                printer.printError("Ошибка при ожидании ввода: " + e.getMessage());
            }

            if (console.isReady()) {
                Request r = console.proceedResponse(null);
                if (r != null) return r;
            }
            
            // if (r != null) return r;
        }
    }

}
