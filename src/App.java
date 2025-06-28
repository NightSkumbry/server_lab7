import connections.Request;
import connections.RequestType;
import connections.Response;
import connections.ResponseType;
import controls.CommandControl;
import controls.IRequestProceeder;
import controls.IResponseProceeder;
import controls.ModeControl;


/**
 * The App class serves as the entry point for the application, which manages 
 * the interaction between commands and modes to facilitate the operations of 
 * a collection manager.
 * 
 * <p>This class is responsible for initializing the application, processing requests, 
 * and handling responses in a continuous loop until the application is terminated. 
 * It utilizes environment variables to set up the collection file.</p>
 */
public class App {
    /**
     * A static reference to the command control handler, implementing {@link IRequestProceeder}.
     */
    public static IRequestProceeder commandControl;

    /**
     * A static reference to the mode control handler, implementing {@link IResponseProceeder}.
     */
    public static IResponseProceeder modeControl;


    /**
     * The main method serves as the entry point of the application.
     * 
     * @param args Command-line arguments (not used in this application).
     * 
     * <p>This method runs an infinite loop where:
     * <ul>
     *   <li>If the request type is {@link RequestType#EXIT}, the application exits.</li>
     *   <li>Commands are processed through {@link IRequestProceeder}.</li>
     *   <li>Responses are processed through {@link IResponseProceeder} to create subsequent requests.</li>
     * </ul>
     * </p>
     * When exiting, it prints a closing message: "Закрываемся..."
     */
    public static void main( String[] args ) {
         try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found!");
            return;
        }
        Request request = init();
        Response response;

        while (true) {
            if (request.getType() == RequestType.EXIT) break;
            
            response = commandControl.proceedRequest(request);

            if (response.getType() == ResponseType.EXIT) break;

            request = modeControl.proceedResponse(response);
        }

        System.out.println("Закрываемся...");
        System.exit(0);
    }
    
    
    /**
     * Initializes the application and sets up the {@link IRequestProceeder} 
     * and {@link IResponseProceeder} for command and mode control, respectively.
     * 
     * @return A {@link Request} object representing the initial request, either with 
     *         a valid collection file or an error message if the environment variable 
     *         "collection_file" is not set.
     * 
     * <p>This method uses the environment variable "collection_file" to determine 
     * the collection file name. If it's absent, an error response is returned.</p>
     */
    public static Request init() {
        commandControl = new CommandControl();
        modeControl = new ModeControl(commandControl);

        // переменная: collection_file
        Request request;
        // String collectionFile = System.getenv("collection_file");

        Response resp = commandControl.proceedRequest(new Request(-1, RequestType.EXECUTE_COMMAND, "change_user"));

        request = modeControl.proceedResponse(resp);
        
        // Request request = modeControl.proceedResponse(new Response(-1, ResponseType.SUCCESS, PrintType.LINE, "Менеджер коллекции квартир готов к работе.\n\nАвтор: NightSkumbry.\nВариант лабы 5: 1995\n\nНапиши help, чтобы узнать о доступных командах."));
        return request;
    }
}
