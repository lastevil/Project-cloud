package gb.project.cloud.server.service;

import gb.project.cloud.objects.*;
import gb.project.cloud.server.ServerHandler;
import gb.project.cloud.server.service.messages.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MessageResponse {
    private final static Map<MessageType, ServiceMessage> RESPONSE_MAP = new HashMap<>();

    public MessageResponse(ServerHandler sh) {

        RESPONSE_MAP.put(MessageType.AUTH, new AuthMes(sh));

        RESPONSE_MAP.put(MessageType.FILE, new FileMes(sh));

        RESPONSE_MAP.put(MessageType.FILE_REQUEST, new FileReqMes(sh));

        RESPONSE_MAP.put(MessageType.DIRECTORY, new DirMes(sh));

        RESPONSE_MAP.put(MessageType.MKDIR, new MkdirMes(sh));

        RESPONSE_MAP.put(MessageType.DELETE, new DelMes(sh));

        RESPONSE_MAP.put(MessageType.RENAME, new RenameMes(sh));
    }

    public static Map<MessageType, ServiceMessage> getResponseMap() {
        return RESPONSE_MAP;
    }
}
