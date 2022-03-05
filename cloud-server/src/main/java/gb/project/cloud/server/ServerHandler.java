package gb.project.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Path serverDir;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client connected");
        serverDir= Paths.get("server");
        System.out.println(Files.list(serverDir)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client disconnected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Recived: {}", msg);
        ctx.writeAndFlush("From server "+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Error: ", cause);
    }
}
