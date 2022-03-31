package gb.project.cloud.objects;

import lombok.Data;

@Data
public class PathFileGet implements CloudMessage {
    private long gatedBytes;
    private long sizeFile;
    private String typeMes;

    public PathFileGet(String type, long currentSize, long fileSize){
        typeMes=type;
        gatedBytes = currentSize;
        sizeFile = fileSize;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PATH_GET;
    }
}
