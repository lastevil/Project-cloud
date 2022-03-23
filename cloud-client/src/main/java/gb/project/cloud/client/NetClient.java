package gb.project.cloud.client;

import gb.project.cloud.objects.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j

public class NetClient {

    private final ClientController client;
    private final String host;
    private final Integer port;
    private SocketChannel sChannel;

    public NetClient(ClientController clientController, String host, Integer port) {
        client = clientController;
        this.host = host;
        this.port = port;
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        //.option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                sChannel = ch;
                                ch.pipeline().addLast(
                                        new ObjectEncoder(),
                                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        new ClientHandler()
                                );
                            }
                        });
                ChannelFuture f = b.connect(host, port).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                log.error("Connection data error");
            }
        }).start();
    }

    public void download(String downloadedFile) {
        sChannel.writeAndFlush(new FileRequest(downloadedFile));
    }

    public void upload(Path uploadedFile) {
        try {
            sChannel.writeAndFlush(new FileMessage(uploadedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void directory(String dir) {
        sChannel.writeAndFlush(new DirMessage(dir));
    }

    public void closeChannel() {
        sChannel.close();
    }

    public class ClientHandler
            extends SimpleChannelInboundHandler<CloudMessage> {

        public void channelActive(ChannelHandlerContext ctx) {
            log.debug("Client connected to server");
            ctx.writeAndFlush(new GetListMessage());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cm) throws Exception {
            switch (cm.getMessageType()) {
                case FILE:
                    FileMessage fm = (FileMessage) cm;
                    Files.write(client.getClientDir().resolve(fm.getName()), fm.getBytes());
                    client.updateClientView();
                    break;
                case LIST:
                    ListMessage lm = (ListMessage) cm;
                    Platform.runLater(() -> client.updateServerView(host + ":" + port, lm.getFiles(), lm.getPath()));
                    break;
            }
        }
    }
}

