package gb.project.cloud.client.service;

import gb.project.cloud.client.ClientController;
import gb.project.cloud.client.service.messages.*;
import gb.project.cloud.objects.*;

import java.util.HashMap;
import java.util.Map;

public class MessageResponse {
    private final static Map<MessageType, ServiceMessage> responseMap = new HashMap<>();
    private final String host;
    private final int port;

    public MessageResponse(ClientController client, String h, int p) {
        this.host=h;
        this.port=p;

        responseMap.put(MessageType.AUTH, new AuthMes(client));

        responseMap.put(MessageType.FILE, new FileMes(client));

        responseMap.put(MessageType.LIST, new FileReqMes(client, host, port));

        responseMap.put(MessageType.PATH_GET, new PathGetMes(client));
    }

    public static Map<MessageType, ServiceMessage> getResponseMap() {
        return responseMap;
    }
}
