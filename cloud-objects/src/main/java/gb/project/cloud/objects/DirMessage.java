package gb.project.cloud.objects;

import lombok.Data;

@Data
public class DirMessage implements CloudMessage {
    private final String file;

    public DirMessage(String file) {
        this.file = file;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.DIRECTORY;
    }
}
