package gb.project.cloud.server;


import gb.project.cloud.server.auth.AuthService;
import gb.project.cloud.server.auth.DBConnect;
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
    private int count;

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
                break;
            case AUTH:
                AuthMessage am = (AuthMessage) cloudMessage;
                AuthService db = new AuthService();
                db.open();
                if (am.getTypeAuth() == 1) {
                    if (db.existUserByLoginPass(am.getLogin(), am.getPassword())) {
                        serverDir = Paths.get(serverDir.toString(), am.getLogin());
                        ctx.writeAndFlush(new ListMessage(serverDir));
                    } else {
                        ctx.writeAndFlush(new AuthMessage(1, "error", "wrong user data"));
                    }
                } else {
                    if (db.registration(am.getLogin(), am.getPassword())) {
                        serverDir = Paths.get(serverDir.toString(), am.getLogin());
                        Files.createDirectories(serverDir);
                        ctx.writeAndFlush(new ListMessage(serverDir));
                    } else {
                        ctx.writeAndFlush(new AuthMessage(1, "error", "user exist!"));
                    }
                }
                db.close();
                break;
        }
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

}
