package gb.project.cloud.server.service.messages;

import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.FileMessage;
import gb.project.cloud.objects.FileRequest;
import gb.project.cloud.objects.ListMessage;
import gb.project.cloud.server.ServerHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class FileReqMes implements ServiceMessage {
    private final ServerHandler server;
    private final int SIZE_MB_8 = 8_000_000;

    public FileReqMes(ServerHandler server) {
        this.server = server;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws IOException, InterruptedException {
        FileRequest fr = (FileRequest) cm;
        Path uploadFile = server.getServerDir().resolve(fr.getName());
        long size = uploadFile.toFile().length();
        ByteBuffer buf = ByteBuffer.allocate(SIZE_MB_8);
        RandomAccessFile aFile = new RandomAccessFile(uploadFile.toFile(), "rw");
        FileChannel inChannel = aFile.getChannel();
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();
            if (buf.hasRemaining()) {
                byte[] bytes = new byte[buf.limit()];
                ctx.writeAndFlush(new FileMessage(uploadFile, bytes, size)).sync();
            }
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
        ctx.writeAndFlush(new ListMessage(server.getServerDir()));
    }
}
