package gb.project.cloud.classes;

import java.io.Serializable;

public interface CloudMessage extends Serializable {
    MessageType getMessageType();
}