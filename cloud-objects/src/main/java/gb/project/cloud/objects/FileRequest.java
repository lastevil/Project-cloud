package gb.project.cloud.objects;

public class FileRequest implements CloudMessage {
    private final String name;

    public FileRequest(String name) {
        this.name = name;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.FILE_REQUEST;
    }

    public String getName() {
        return name;
    }
}
