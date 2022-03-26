package gb.project.cloud.objects;

public class MkdirMassage implements CloudMessage {

    private final String dir;

    public MkdirMassage(String directory) {
        dir = directory;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MKDIR;
    }

    public String getDir() {
        return dir;
    }
}
