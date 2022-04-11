package gb.project.cloud.server.service.messages;

import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.ListMessage;
import gb.project.cloud.objects.RenameMessage;
import gb.project.cloud.server.ServerHandler;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RenameMes implements ServiceMessage {
    private final ServerHandler server;

    public RenameMes(ServerHandler server) {
        this.server = server;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws IOException {
        RenameMessage rm = (RenameMessage) cm;
        Path fileToMovePath = Paths.get(server.getServerDir().toString(), rm.getOldName());
        Path targetPath = Paths.get(server.getServerDir().toString(), rm.getNewName());
        if (fileToMovePath.toFile().exists()) {
            Files.move(fileToMovePath, targetPath);
        }
        ctx.writeAndFlush(new ListMessage(server.getServerDir()));
    }
}
