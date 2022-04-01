package gb.project.cloud.objects;

import lombok.Data;

@Data
public class RenameMessage implements CloudMessage {

    private String oldName;
    private String newName;

    public RenameMessage(String oldName, String newName){
        this.oldName=oldName;
        this.newName=newName;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.RENAME;
    }
}
