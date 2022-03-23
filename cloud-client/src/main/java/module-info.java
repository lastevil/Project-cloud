module gb.project.cloud.client.cloudclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires cloud.objects;
    requires io.netty.codec;
    requires org.slf4j;

    opens gb.project.cloud.client to javafx.fxml;
    exports gb.project.cloud.client;
}