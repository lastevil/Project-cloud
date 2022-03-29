package gb.project.cloud.server;


import gb.project.cloud.server.auth.AuthService;
import gb.project.cloud.server.service.MessageResponse;
import gb.project.cloud.server.service.ServiceMessage;

import java.nio.file.Path;
import java.nio.file.Paths;

import gb.project.cloud.objects.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<CloudMessage> {

    private Path serverDir;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        log.debug(cloudMessage.toString());
        MessageResponse mr = new MessageResponse(this);
        ServiceMessage sm = mr.getResponseMap().get(cloudMessage.getMessageType());
        sm.messageChecker(ctx, cloudMessage);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("Client connected");
        serverDir = Paths.get("server");
        AuthService db = new AuthService();
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
