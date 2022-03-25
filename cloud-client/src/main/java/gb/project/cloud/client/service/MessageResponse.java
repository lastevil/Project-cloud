package gb.project.cloud.client.service;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.objects.*;
import javafx.application.Platform;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class MessageResponse {
    private static Map<MessageType, ServiceMessage> responseMap = new HashMap<>();
    private ClientController client;
    private String host;
    private int port;

    public MessageResponse(ClientController client, String host, int port) {
        this.host = host;
        this.port = port;
        this.client = client;
        //AUTH
        responseMap.put(MessageType.AUTH, (ctx, cm) -> {
            AuthMessage am = (AuthMessage) cm;
            if (am.getTypeAuth() == 0) {
                Platform.runLater(client::choseWindow);
            } else {
                Platform.runLater(() -> client.userDataWindow("Login", am.getPassword()));
            }
        });
        //FILE
        responseMap.put(MessageType.FILE, (ctx, cm) -> {
            FileMessage fm = (FileMessage) cm;
            Files.write(client.getClientDir().resolve(fm.getName()), fm.getBytes());
            client.updateClientView();
        });
        //FILE_REQUEST
        responseMap.put(MessageType.LIST, (ctx, cm) -> {
            ListMessage lm = (ListMessage) cm;
            Platform.runLater(() -> client.updateServerView(host + ":" + port, lm.getFiles(), lm.getPath()));
        });
    }

    public static Map<MessageType, ServiceMessage> getResponseMap() {
        return responseMap;
    }
}
