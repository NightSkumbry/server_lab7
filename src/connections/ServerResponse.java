package connections;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ServerResponse extends Response implements Externalizable {
    private Externalizable contentExt;
    private int clientCommandId;
    private int clientId;

    public ServerResponse() {
        super(-1, ResponseType.SUCCESS, PrintType.NONE, "");
    }

    public ServerResponse(int commandId, ResponseType type, PrintType printType, Externalizable content, int clientId) {
        super(-1, type, printType, content.toString());
        this.contentExt = content;
        this.clientCommandId = commandId;
        this.clientId = clientId;
    }

    public Externalizable getContentExt() {
        return contentExt;
    }

    public int getClientCommandId() {
        return clientCommandId;
    }

    public int getClientId() {
        return clientId;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(clientCommandId);
        out.writeObject(getType().name());
        out.writeObject(getPrintType().name());
        out.writeObject(contentExt);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {}
    
}
