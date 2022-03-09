package gb.project.cloud.client.message;

import java.io.Serializable;

public interface CloudMessage extends Serializable {
    MessageType getMessageType();
}