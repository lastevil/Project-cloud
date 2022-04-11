package gb.project.cloud.client.service.messages;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.objects.CloudMessage;
import gb.project.cloud.objects.FileMessage;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileMes implements ServiceMessage {
    private final ClientController client;

    public FileMes(ClientController client) {
        this.client = client;
    }

    @Override
    public void messageChecker(ChannelHandlerContext ctx, CloudMessage cm) throws IOException {
        FileMessage fm = (FileMessage) cm;
        if (client.getClientDir().resolve(fm.getName()).toFile().exists()) {
            Files.write(
                    client.getClientDir().resolve(fm.getName()),
                    fm.getBytes(),
                    StandardOpenOption.APPEND
            );
            client.progressCopy(
                    client.getClientDir().resolve(fm.getName()).toFile().length(),
                    fm.getSize()
            );
        } else {
            Files.write(client.getClientDir().resolve(fm.getName()), fm.getBytes());
            client.progressCopy(
                    client.getClientDir().resolve(fm.getName()).toFile().length(),
                    fm.getSize()
            );
        }

        if (client.getClientDir().resolve(fm.getName()).toFile().length() == fm.getSize()) {
            client.updateClientView();
        }
    }
}
