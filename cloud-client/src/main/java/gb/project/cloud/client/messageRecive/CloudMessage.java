package gb.project.cloud.client.messageRecive;

import java.io.Serializable;

public interface CloudMessage extends Serializable {
    MessageType getMessageType();
}