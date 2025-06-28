package util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class StringWrapper implements Externalizable {
    private String value;

    public StringWrapper() {}

    public StringWrapper(String value) {
        this.value = value;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(value);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        value = (String) in.readObject();
    }

    public String getValue() {
        return value;
    }
}
