package gb.project.cloud.objects;

import lombok.Data;

@Data
public class PathFileGet implements CloudMessage {
    private long gatedBytes;
    private long sizeFile;

    public PathFileGet(long currentSize, long fileSize){
        gatedBytes = currentSize;
        sizeFile = fileSize;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PATH_GET;
    }
}
