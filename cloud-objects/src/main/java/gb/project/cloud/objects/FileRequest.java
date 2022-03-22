package gb.project.cloud.objects;

import lombok.Data;

@Data
public class FileRequest implements CloudMessage{
    private final String name;

    public FileRequest(String name) {
            this.name = name;
        }

        @Override
        public MessageType getMessageType() {
            return MessageType.FILE_REQUEST;
    }
}
