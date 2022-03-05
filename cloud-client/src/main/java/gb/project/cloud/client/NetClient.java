package gb.project.cloud.client;

import gb.project.cloud.client.messageRecive.FileRequest;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.scene.control.ListView;

import java.io.IOException;

public class NetClient {

    private ObjectEncoderOutputStream oos;
    private ObjectDecoderInputStream ois;
    private ClientController client;

    public NetClient(ClientController clientController) {
        client = clientController;
    }

    public void download(ListView<String> serverView) throws IOException {
        oos.writeObject(new FileRequest(serverView.getSelectionModel().getSelectedItem()));
    }
}
