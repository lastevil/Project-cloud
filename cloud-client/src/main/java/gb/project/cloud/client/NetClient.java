package gb.project.cloud.client;

import gb.project.cloud.objects.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class NetClient {

    private ObjectEncoderOutputStream oos;
    private ObjectDecoderInputStream ois;
    private ClientController client;
    private String host;
    private Integer port;
    private Socket socket;

    public NetClient(ClientController clientController,String host, Integer port) {
        client = clientController;
        this.host=host;
        this.port=port;
        try {
            socket = new Socket(host,port);
            oos = new ObjectEncoderOutputStream(socket.getOutputStream());
            ois = new ObjectDecoderInputStream(socket.getInputStream());
            Thread readThread = new Thread(this::read);
            readThread.setDaemon(true);
            readThread.start();
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

    public void directory(String dir){
        try {
            oos.writeObject(new DirMessage(dir));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void read() {
        try {
            while (true) {
                CloudMessage msg =  (CloudMessage) ois.readObject();
                switch (msg.getMessageType()) {
                    case FILE:
                        FileMessage fm = (FileMessage) msg;
                        Files.write(client.getClientDir().resolve(fm.getName()), fm.getBytes());
                        client.updateClientView();
                        break;
                    case LIST:
                        ListMessage lm = (ListMessage) msg;
                        Platform.runLater(() -> {
                             client.updateServerView(host+":"+port,lm.getFiles(),lm.getPath());
                        });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
