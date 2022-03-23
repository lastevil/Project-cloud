package gb.project.cloud.objects;

public class GetListMessage implements CloudMessage {

    @Override
    public MessageType getMessageType() {
        return MessageType.GET_LIST;
    }
}
