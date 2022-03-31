package gb.project.cloud.client;

import gb.project.cloud.client.service.MessageResponse;
import gb.project.cloud.client.service.ServiceMessage;
import gb.project.cloud.objects.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

@Slf4j
public class NetClient {

    private final ClientController client;
    private final String host;
    private final Integer port;
    private SocketChannel sChannel;
    private boolean isGet;

    public NetClient(ClientController clientController, String host, Integer port) {
        client = clientController;
        this.host = host;
        this.port = port;
        Thread net = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                sChannel = ch;
                                ch.pipeline().addLast(
                                        new ObjectDecoder(1487500 * 100, ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
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
                client.setDisconnect();
                log.error("Connection lost");
            }
        });
        net.setDaemon(true);
        net.start();
    }

    public void download(String downloadedFile) {
        sChannel.writeAndFlush(new FileRequest(downloadedFile));
    }

    public void upload(Path uploadedFile) {
        try {
            log.debug("File transfer start");
            long size = uploadedFile.toFile().length();
            RandomAccessFile aFile = new RandomAccessFile(uploadedFile.toFile(), "r");
            FileChannel inChannel = aFile.getChannel();
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
            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {
                buf.flip();
                if (buf.hasRemaining() && buf.hasArray()) {
                    ChannelFuture f =sChannel.writeAndFlush(new FileMessage(uploadedFile, buf.array(), size));
                    f.sync();
                }
                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            log.debug("File transfer finish");
            aFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void directory(String dir) {
        sChannel.writeAndFlush(new DirMessage(dir));
    }

    public void closeChannel() {
        if (sChannel != null) {
            sChannel.close();
        }
    }

    public void login(String login, String pass) {
        sChannel.writeAndFlush(new AuthMessage(1, login, pass));
    }

    public void registration(String login, String pass) {
        sChannel.writeAndFlush(new AuthMessage(2, login, pass));
    }

    public void makeDir(String s) {
        sChannel.writeAndFlush(new MkdirMassage(s));
    }

    public void pathUpload() {
        isGet=false;
    }

    private class ClientHandler
            extends SimpleChannelInboundHandler<CloudMessage> {

        public void channelActive(ChannelHandlerContext ctx) {
            log.debug("Client connected to server");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cm) throws Exception {
            MessageResponse mr = new MessageResponse(client, host, port);
            ServiceMessage sm = mr.getResponseMap().get(cm.getMessageType());
            sm.messageChecker(ctx, cm);
        }
    }
}

