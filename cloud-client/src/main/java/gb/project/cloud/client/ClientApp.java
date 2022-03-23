package gb.project.cloud.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class ClientApp extends Application {
    private static ClientController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cloud exchange");
        stage.setScene(scene);
        stage.setMinHeight(320);
        stage.setMinWidth(500);
        stage.show();
        controller = fxmlLoader.getController();
        stage.setOnCloseRequest(event -> controller.exit(new ActionEvent()));
    }

    public static void main(String[] args) {
        launch();
    }
}