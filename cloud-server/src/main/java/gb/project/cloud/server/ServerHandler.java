package gb.project.cloud.server;


import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import gb.project.cloud.server.message.CloudMessage;
import gb.project.cloud.server.message.FileMessage;
import gb.project.cloud.server.message.FileRequest;
import gb.project.cloud.server.message.ListMessage;
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
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client connected");
        serverDir = Paths.get("server");
        ctx.writeAndFlush(new ListMessage(serverDir));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Error: ", cause);
    }

}
