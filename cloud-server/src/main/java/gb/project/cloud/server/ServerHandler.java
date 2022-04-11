package gb.project.cloud.server;


import gb.project.cloud.server.auth.AuthService;
import gb.project.cloud.server.service.MessageResponse;
import gb.project.cloud.server.service.messages.ServiceMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gb.project.cloud.objects.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<CloudMessage> {

    private Path serverDir;
    private MessageResponse mr;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cm) throws Exception {
        mr.getResponseMap().get(cm.getMessageType()).messageChecker(ctx, cm);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        log.debug("Client connected");
        mr = new MessageResponse(this);
        serverDir = Paths.get("server");
        if (!serverDir.toFile().exists()) {
            Files.createDirectory(serverDir);
        }
        new AuthService();
        ctx.writeAndFlush(new AuthMessage(0, "title", "Please authenticate or register"));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("Client disconnected");
    }

    public Path getServerDir() {
        return serverDir;
    }

    public void setServerDir(Path serverDir) {
        this.serverDir = serverDir;
    }
}
