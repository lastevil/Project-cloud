package gb.project.cloud.client.service;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.client.service.messages.*;
import gb.project.cloud.objects.*;

import java.util.HashMap;
import java.util.Map;

public class MessageResponse {
    private final static Map<MessageType, ServiceMessage> responseMap = new HashMap<>();

    public MessageResponse(ClientController client, String host, int port) {
        responseMap.put(MessageType.AUTH, new AuthMes(client));

        responseMap.put(MessageType.FILE, new FileMes(client));

        responseMap.put(MessageType.LIST, new FileReqMes(client, host, port));

        responseMap.put(MessageType.PATH_GET, new PathGetMes(client));
    }

    public static Map<MessageType, ServiceMessage> getResponseMap() {
        return responseMap;
    }
}
