package gb.project.cloud.objects;


public class DirMessage implements CloudMessage {
    private final String file;

    public DirMessage(String file) {
        this.file = file;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.DIRECTORY;
    }

    public String getFile() {
        return file;
    }
}
