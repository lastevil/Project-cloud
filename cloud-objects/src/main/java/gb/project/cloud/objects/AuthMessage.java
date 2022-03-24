package gb.project.cloud.objects;

import lombok.Data;

@Data
public class AuthMessage implements CloudMessage {

    private final int typeAuth;
    private final String login;
    private final String password;

    public AuthMessage(int typeAuth, String login, String password) {
        this.typeAuth = typeAuth;
        this.login = login;
        this.password = password;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.AUTH;
    }
}
