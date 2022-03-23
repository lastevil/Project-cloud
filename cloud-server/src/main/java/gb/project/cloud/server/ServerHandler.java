package gb.project.cloud.server;


import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gb.project.cloud.objects.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<CloudMessage> {

    private Path serverDir;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        log.debug(cloudMessage.toString());
        switch (cloudMessage.getMessageType()) {
            case FILE:
                FileMessage fm = (FileMessage) cloudMessage;
                Files.write(serverDir.resolve(fm.getName()), fm.getBytes());
                ctx.writeAndFlush(new ListMessage(serverDir));
                break;
            case FILE_REQUEST:
                FileRequest fr = (FileRequest) cloudMessage;
                ctx.writeAndFlush(new FileMessage(serverDir.resolve(fr.getName())));
                break;
            case DIRECTORY:
                DirMessage dm = (DirMessage) cloudMessage;
                Path drm = Paths.get(serverDir.toString(), dm.getFile());
                if (dm.getFile().equals("...")) {
                    serverDir = serverDir.getParent();
                } else if (drm.toFile().isDirectory()) {
                    serverDir = drm;
                }
                ctx.writeAndFlush(new ListMessage(serverDir));
                break;
            case GET_LIST:
                ctx.writeAndFlush(new ListMessage(serverDir));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("Client connected");
        serverDir = Paths.get("server");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("Client disconnected");
    }

}
