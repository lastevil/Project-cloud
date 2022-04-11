package gb.project.cloud.client.service.messages;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.ListMessage;
import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;

public class FileReqMes implements ServiceMessage {
    private final ClientController client;
    private final String host;
    private final int port;

    public FileReqMes(ClientController client, String host, int port) {
        this.client = client;
        this.host = host;
        this.port = port;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) {
        ListMessage lm = (ListMessage) cm;
        Platform.runLater(() -> client.updateServerView(host + ":" + port, lm.getFiles(), lm.getPath()));
    }
}
