package gb.project.cloud.client.service;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.objects.*;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class MessageResponse {
    private final static Map<MessageType, ServiceMessage> responseMap = new HashMap<>();

    public MessageResponse(ClientController client, String host, int port) {

        //AUTH

        responseMap.put(MessageType.AUTH, (ctx, cm) -> {
            AuthMessage am = (AuthMessage) cm;
            if (am.getTypeAuth() == 0) {
                Platform.runLater(client::choseWindow);
            } else {
                Platform.runLater(client::enterToServer);
                Platform.runLater(() -> client.messageDialog("Error", "Wrong username or password"));
            }
        });

        //FILE

        responseMap.put(MessageType.FILE, (ctx, cm) -> {
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
        });

        //FILE_REQUEST

        responseMap.put(MessageType.LIST, (ctx, cm) -> {
            ListMessage lm = (ListMessage) cm;
            Platform.runLater(() -> client.updateServerView(host + ":" + port, lm.getFiles(), lm.getPath()));
        });

        responseMap.put(MessageType.PATH_GET, (ctx, cm) -> {
            PathFileGet gp = (PathFileGet) cm;
            client.progressCopy(gp.getGatedBytes(), gp.getSizeFile());
        });
    }

    public static Map<MessageType, ServiceMessage> getResponseMap() {
        return responseMap;
    }
}
