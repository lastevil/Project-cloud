package gb.project.cloud.server.message;

import java.io.Serializable;

public interface CloudMessage extends Serializable {
    MessageType getMessageType();
}