package gb.project.cloud.server.service.messages;

import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.ListMessage;
import gb.project.cloud.objects.MkdirMassage;
import gb.project.cloud.server.ServerHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MkdirMes implements ServiceMessage {
    private final ServerHandler server;

    public MkdirMes(ServerHandler server) {
        this.server = server;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws IOException {
        MkdirMassage mkdir = (MkdirMassage) cm;
        Path dir = Paths.get(server.getServerDir().toString(), mkdir.getDir());
        Files.createDirectories(dir);
        ctx.writeAndFlush(new ListMessage(server.getServerDir()));
    }
}
