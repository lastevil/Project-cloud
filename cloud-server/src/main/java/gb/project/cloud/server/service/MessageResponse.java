package gb.project.cloud.server.service;

import gb.project.cloud.objects.*;
import gb.project.cloud.server.ServerHandler;
import gb.project.cloud.server.auth.AuthService;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MessageResponse {
    private final static Map<MessageType, ServiceMessage> responseMap = new HashMap<>();
    private final ServerHandler server;

    public MessageResponse(ServerHandler sh) {
        server = sh;

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
            if (server.getServerDir().resolve(fm.getName()).toFile().exists()) {
                Files.write(server.getServerDir().resolve(fm.getName()), fm.getBytes(),
                        StandardOpenOption.APPEND);
                ctx.writeAndFlush(new PathFileGet("upload",
                        server.getServerDir().resolve(fm.getName()).toFile().length(),
                        fm.getSize())
                );
            } else {
                Files.write(server.getServerDir().resolve(fm.getName()), fm.getBytes());
                ctx.writeAndFlush(new PathFileGet("upload",
                        server.getServerDir().resolve(fm.getName()).toFile().length(),
                        fm.getSize())
                );
            }
            log.debug("get: " +
                    server.getServerDir().resolve(fm.getName()).toFile().length() +
                    " size: " +
                    fm.getSize());
            if (fm.getSize() == server.getServerDir().resolve(fm.getName()).toFile().length()) {
                ctx.writeAndFlush(new ListMessage(server.getServerDir()));
            }
        });

        //FILE_REQUEST

        responseMap.put(MessageType.FILE_REQUEST, (ctx, cm) -> {
            FileRequest fr = (FileRequest) cm;
            Path uploadFile = server.getServerDir().resolve(fr.getName());
            long size = uploadFile.toFile().length();
            long appacity = size/2;
            while (appacity >= 8 * 1000000) {
                appacity /= 1024;
                appacity++;
            }
            if (appacity == 0) {
                appacity++;
            }
            while (size % appacity != 0){
                appacity++;
            }
            ByteBuffer buf = ByteBuffer.allocate((int) appacity);
            RandomAccessFile aFile = new RandomAccessFile(uploadFile.toFile(), "rw");
            FileChannel inChannel = aFile.getChannel();
            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {
                buf.flip();
                ChannelFuture f = ctx.writeAndFlush(new FileMessage(uploadFile, buf.array(), size));
                f.sync();
                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            aFile.close();
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
