package util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class UserWrapper implements Externalizable {
    private String name;
    private String password;

    public UserWrapper() {}

    public UserWrapper(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(password);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        password = (String) in.readObject();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
