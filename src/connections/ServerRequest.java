package connections;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import util.UserWrapper;

public class ServerRequest extends Request implements Externalizable {
    private static final long serialVersionUID = 1L;
    private Externalizable contentExt;
    private int clientCommandId;
    private int clientId;
    private UserWrapper user;

    public ServerRequest() {
        super(-1, RequestType.PROCEED_COMMAND, "");
    }

    public ServerRequest(int commandId, RequestType type, String content, Externalizable contentExt) {
        super(-1, type, content);
        this.contentExt = contentExt;
        this.clientCommandId = commandId;
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

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public UserWrapper getUser() {
        return user;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {}

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        clientCommandId = in.readInt();
        setType(RequestType.valueOf((String) in.readObject()));
        setContent((String) in.readObject());
        contentExt = (Externalizable) in.readObject();
        user = (UserWrapper) in.readObject();
    }
    
}
