module gb.project.cloud.client.cloudclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.handler;
    requires io.netty.codec;

    opens gb.project.cloud.client to javafx.fxml;
    exports gb.project.cloud.client;
    exports gb.project.cloud.client.messageRecive;
    opens gb.project.cloud.client.messageRecive to javafx.fxml;
}