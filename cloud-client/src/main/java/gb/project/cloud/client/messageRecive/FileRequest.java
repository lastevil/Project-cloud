package gb.project.cloud.client.messageRecive;

import gb.project.cloud.client.messageRecive.MessageType;

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
