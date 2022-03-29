package gb.project.cloud.objects;

import lombok.Data;

@Data
public class MkdirMassage implements CloudMessage {

    private final String dir;

    public MkdirMassage(String directory) {
        dir = directory;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MKDIR;
    }
}
