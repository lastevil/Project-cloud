package gb.project.cloud.server.service.messages;

import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.DirMessage;
import gb.project.cloud.objects.ListMessage;
import gb.project.cloud.server.ServerHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirMes implements ServiceMessage {
    private final ServerHandler server;

    public DirMes(ServerHandler server) {
        this.server = server;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws  IOException {
        DirMessage dm = (DirMessage) cm;
        Path drm = Paths.get(server.getServerDir().toString(), dm.getFile());
        if (dm.getFile().equals("...")) {
            server.setServerDir(server.getServerDir().getParent());
        } else if (drm.toFile().isDirectory()) {
            server.setServerDir(drm);
        }
        ctx.writeAndFlush(new ListMessage(server.getServerDir()));
    }
}
