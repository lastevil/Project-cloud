package gb.project.cloud.client.service.messages;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.PathFileGet;
import io.netty.channel.ChannelHandlerContext;

public class PathGetMes implements ServiceMessage {
    private final ClientController client;

    public PathGetMes(ClientController client) {
        this.client = client;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) {
        PathFileGet gp = (PathFileGet) cm;
        client.progressCopy(gp.getGatedBytes(), gp.getSizeFile());
    }
}
