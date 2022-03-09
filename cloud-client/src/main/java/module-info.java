module gb.project.cloud.client.cloudclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.handler;
    requires io.netty.codec;
    requires lombok;

    opens gb.project.cloud.client to javafx.fxml;
    exports gb.project.cloud.client;
    exports gb.project.cloud.client.message;
    opens gb.project.cloud.client.message to javafx.fxml;
}