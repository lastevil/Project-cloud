package gb.project.cloud.client.service.messages;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.objects.AuthMessage;
import gb.project.cloud.objects.CloudMessage;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;

public class AuthMes implements ServiceMessage{
    private final ClientController client;

    public AuthMes(ClientController client) {
        this.client = client;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) {
        AuthMessage am = (AuthMessage) cm;
        if (am.getTypeAuth() == 0) {
            Platform.runLater(client::choseWindow);
        } else {
            Platform.runLater(client::enterToServer);
            Platform.runLater(() -> client.messageDialog("Error", "Wrong username or password"));
        }
    }
}
