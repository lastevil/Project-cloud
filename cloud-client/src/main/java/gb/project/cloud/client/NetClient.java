package gb.project.cloud.client;

import gb.project.cloud.client.messageRecive.FileMessage;
import gb.project.cloud.client.messageRecive.FileRequest;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

public class NetClient {

    private ObjectEncoderOutputStream oos;
    private ObjectDecoderInputStream ois;
    private ClientController client;
    private Path serverPath;

    public NetClient(ClientController clientController,String host, Integer port) {
        client = clientController;
        Socket socket = null;
        try {
            socket = new Socket(host,port);
            oos = new ObjectEncoderOutputStream(socket.getOutputStream());
            ois = new ObjectDecoderInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String downloadedFile) {
        try {
            oos.writeObject(new FileRequest(downloadedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upload(Path uploadedFile) {
        try {
            oos.writeObject(new FileMessage(uploadedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getServerPath() {
        return serverPath;
    }


}
