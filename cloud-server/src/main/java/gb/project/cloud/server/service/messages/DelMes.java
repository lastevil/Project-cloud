package gb.project.cloud.server.service.messages;

import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.DeleteMessage;
import gb.project.cloud.objects.ListMessage;
import gb.project.cloud.server.ServerHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

public class DelMes implements ServiceMessage {
    private final ServerHandler server;

    public DelMes(ServerHandler server) {
        this.server = server;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws IOException {
        DeleteMessage dm = (DeleteMessage) cm;
        Path f = Paths.get(server.getServerDir().toString(), dm.getFileName());
        if (f.toFile().exists()) {
            if (f.toFile().isFile()){
                f.toFile().delete();
                ctx.writeAndFlush(new ListMessage(server.getServerDir()));
            }
            else {
                Files.walk(Path.of(f.toString()))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                ctx.writeAndFlush(new ListMessage(server.getServerDir()));
            }
        }
    }
}
