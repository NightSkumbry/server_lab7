package connections;

/**
 * The Request class represents a data structure that encapsulates information
 * about a command request in the application. It includes details such as the
 * command ID, the type of the request, and its content.
 */
public class Request {

    /**
     * The unique identifier of the command associated with this request.
     */
    private int commandId;

    /**
     * The type of the request, represented by the {@link RequestType} enum.
     */
    private RequestType type;

    /**
     * The content of the request, providing additional data or arguments.
     */
    private String content;


    /**
     * Constructs a new Request object with the specified parameters.
     *
     * @param commandId The unique identifier of the command.
     * @param type      The type of the request.
     * @param content   The content of the request.
     */
    public Request(int commandId, RequestType type, String content) {
        this.commandId = commandId;
        this.type = type;
        this.content = content;
    }


    /**
     * Retrieves the command ID associated with this request.
     *
     * @return The command ID.
     */
    public int getCommandId() {
        return commandId;
    }

    /**
     * Retrieves the type of the request.
     *
     * @return The {@link RequestType} of the request.
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Retrieves the content of the request.
     *
     * @return The content of the request as a string.
     */
    public String getContent() {
        return content;
    }


    public void setType(RequestType type) {
        this.type = type;
    }


    public void setContent(String content) {
        this.content = content;
    }

    
}
