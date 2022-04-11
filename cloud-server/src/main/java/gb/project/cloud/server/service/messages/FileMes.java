package gb.project.cloud.server.service.messages;

import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.FileMessage;
import gb.project.cloud.objects.ListMessage;
import gb.project.cloud.objects.PathFileGet;
import gb.project.cloud.server.ServerHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileMes implements ServiceMessage {
    private final ServerHandler server;

    public FileMes(ServerHandler srv) {
        this.server = srv;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws IOException {
        FileMessage fm = (FileMessage) cm;
        if (server.getServerDir().resolve(fm.getName()).toFile().exists()) {
            Files.write(server.getServerDir().resolve(fm.getName()), fm.getBytes(),
                    StandardOpenOption.APPEND);
            ctx.writeAndFlush(new PathFileGet(
                    server.getServerDir().resolve(fm.getName()).toFile().length(),
                    fm.getSize())
            );
        } else {
            Files.write(server.getServerDir().resolve(fm.getName()), fm.getBytes());
            ctx.writeAndFlush(new PathFileGet(
                    server.getServerDir().resolve(fm.getName()).toFile().length(),
                    fm.getSize())
            );
        }
        if (fm.getSize() == server.getServerDir().resolve(fm.getName()).toFile().length()) {
            ctx.writeAndFlush(new ListMessage(server.getServerDir()));
        }
    }
}
