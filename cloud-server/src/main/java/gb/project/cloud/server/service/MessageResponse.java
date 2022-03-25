package gb.project.cloud.server.service;

import gb.project.cloud.objects.*;
import gb.project.cloud.server.ServerHandler;
import gb.project.cloud.server.auth.AuthService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MessageResponse {
    private static Map<MessageType, ServiceMessage> responseMap = new HashMap<>();
    private ServerHandler server;

    public MessageResponse(ServerHandler sh) {
        server=sh;
        //AUTH
        responseMap.put(MessageType.AUTH, (ctx, cm) -> {
            AuthMessage am = (AuthMessage) cm;
            AuthService db = new AuthService();
            db.open();
            if (am.getTypeAuth() == 1) {
                if (db.existUserByLoginPass(am.getLogin(), am.getPassword())) {
                    server.setServerDir(Paths.get(server.getServerDir().toString(), am.getLogin()));
                    ctx.writeAndFlush(new ListMessage(server.getServerDir()));
                } else {
                    ctx.writeAndFlush(new AuthMessage(1, "error", "wrong user data"));
                }
            } else {
                if (db.registration(am.getLogin(), am.getPassword())) {
                    server.setServerDir(Paths.get(server.getServerDir().toString(), am.getLogin()));
                    Files.createDirectories(server.getServerDir());
                    ctx.writeAndFlush(new ListMessage(server.getServerDir()));
                } else {
                    ctx.writeAndFlush(new AuthMessage(1, "error", "user exist!"));
                }
            }
            db.close();
        });
        //FILE
        responseMap.put(MessageType.FILE, (ctx, cm) -> {
            FileMessage fm = (FileMessage) cm;
            Files.write(server.getServerDir().resolve(fm.getName()), fm.getBytes());
            ctx.writeAndFlush(new ListMessage(server.getServerDir()));
        });
        //FILE_REQUEST
        responseMap.put(MessageType.FILE_REQUEST, (ctx, cm) -> {
            FileRequest fr = (FileRequest) cm;
            ctx.writeAndFlush(new FileMessage(server.getServerDir().resolve(fr.getName())));
        });
        //DIRECTORY
        responseMap.put(MessageType.DIRECTORY, (ctx, cm) -> {
            DirMessage dm = (DirMessage) cm;
            Path drm = Paths.get(server.getServerDir().toString(), dm.getFile());
            if (dm.getFile().equals("...")) {
                server.setServerDir(server.getServerDir().getParent());
            } else if (drm.toFile().isDirectory()) {
                server.setServerDir(drm);
            }
            ctx.writeAndFlush(new ListMessage(server.getServerDir()));
        });
        //MKDIR
        responseMap.put(MessageType.MKDIR, (ctx, cm) -> {
            MkdirMassage mkdir = (MkdirMassage) cm;
            Path dir = Paths.get(server.getServerDir().toString(), mkdir.getDir());
            Files.createDirectories(dir);
            ctx.writeAndFlush(new ListMessage(server.getServerDir()));
        });
    }

    public static Map<MessageType, ServiceMessage> getResponseMap() {
        return responseMap;
    }
}
