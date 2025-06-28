package util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDateTime;

public class InfoWrapper implements Externalizable {
    private String type;
    private int elementCount;
    private LocalDateTime initTime;
    private LocalDateTime lastSaveTime;

    public InfoWrapper() {}

    public InfoWrapper(String type, int elementCount, LocalDateTime initTime, LocalDateTime lastSaveTime) {
        this.type = type;
        this.elementCount = elementCount;
        this.initTime = initTime;
        this.lastSaveTime = lastSaveTime;
    }

    public String getType() {
        return type;
    }

    public int getElementCount() {
        return elementCount;
    }

    public LocalDateTime getInitTime() {
        return initTime;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(type);
        out.writeInt(elementCount);
        out.writeObject(initTime.toString());
        out.writeObject(lastSaveTime == null ? "" : lastSaveTime.toString());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        type = (String) in.readObject();
        elementCount = in.readInt();
        initTime = LocalDateTime.parse((String) in.readObject());
        lastSaveTime = LocalDateTime.parse((String) in.readObject());
    }

    @Override
    public String toString() {
        return "Тип: " + type + ",\nКоличество элементов: " + elementCount + ",\nДата инициализации: " + initTime
                + ",\nДата последнего сохранения: " + lastSaveTime + "\n";
    }

    

}
