package gb.project.cloud.objects;


import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListMessage implements CloudMessage {

    private final List<String> files;
    private final String path;

    public ListMessage(Path path) throws IOException {
        String[] s1 = path.toString().split("\\\\");
        String[] s2 = path.toString().split("/");
        if (s1.length>2 || s2.length>2) {
            files = new LinkedList<>();
            files.add("...");
            files.addAll(Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList()));
        } else {
            files = Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        }
        this.path = path.toString();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LIST;
    }
}
