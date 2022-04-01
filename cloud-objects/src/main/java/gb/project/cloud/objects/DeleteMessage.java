package gb.project.cloud.objects;

import lombok.Data;

@Data
public class DeleteMessage implements CloudMessage {
    private String fileName;
    public DeleteMessage(String fileName){
        this.fileName = fileName;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.DELETE;
    }
}
