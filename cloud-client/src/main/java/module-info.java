module gb.project.cloud.client.cloudclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires cloud.objects;
    requires io.netty.codec;
    requires io.netty.transport;

    opens gb.project.cloud.client to javafx.fxml;
    exports gb.project.cloud.client;
    exports gb.project.cloud.client.dialog;
    opens gb.project.cloud.client.dialog to javafx.fxml;
}