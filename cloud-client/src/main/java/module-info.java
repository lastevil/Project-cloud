module gb.project.cloud.client.cloudclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires cloud.objects;
    requires io.netty.codec;
    requires org.slf4j;
    requires io.netty.transport;
    requires lombok;

    opens gb.project.cloud.client to javafx.fxml;
    exports gb.project.cloud.client;
}