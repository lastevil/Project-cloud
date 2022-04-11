package gb.project.cloud.server.service.messages;

import gb.project.cloud.objects.AuthMessage;
import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.ListMessage;
import gb.project.cloud.server.ServerHandler;
import gb.project.cloud.server.auth.AuthService;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class AuthMes implements ServiceMessage {
    ServerHandler server;

    public AuthMes(ServerHandler srv) {
        this.server = srv;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws SQLException, IOException {
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
    }
}
