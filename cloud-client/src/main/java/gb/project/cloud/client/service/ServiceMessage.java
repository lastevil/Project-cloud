package gb.project.cloud.client.service;

import gb.project.cloud.objects.CloudMessage;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

@FunctionalInterface
public interface ServiceMessage {
    void messageChecker(ChannelHandlerContext ctx, CloudMessage msg) throws IOException;
}
