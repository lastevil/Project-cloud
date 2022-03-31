package gb.project.cloud.server.service;

import gb.project.cloud.objects.CloudMessage;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.sql.SQLException;

@FunctionalInterface
public interface ServiceMessage {
    void messageChecker(ChannelHandlerContext ctx, CloudMessage msg) throws SQLException, IOException, InterruptedException;
}
