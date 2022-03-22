package gb.project.cloud.objects;

import java.io.Serializable;

public interface CloudMessage extends Serializable {
    MessageType getMessageType();
}