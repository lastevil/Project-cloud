package gb.project.cloud.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientController implements Initializable {
    private Path clientDir;
    private Path clientDir2;
    private ServerSettingsDialog serSetDig;
    private  NetClient client;
    private static String HOST;
    private static Integer PORT;
    private static boolean connect = false;


    @FXML
    private TextField clientField;
    @FXML
    private ListView<String> clientView;
    @FXML
    private ListView<String> serverView;
    @FXML
    private Label serverLabel;
    @FXML
    private TextField serverField;


    @FXML
    private void upload(ActionEvent actionEvent){
    }

    @FXML
    public void download(ActionEvent actionEvent) {
    }

    private void updateClientView() {
        clientField.setText(clientDir.toAbsolutePath().normalize().toString());
        Platform.runLater(() -> {
            clientView.getItems().clear();
            clientView.getItems().add("...");
            clientView.getItems()
                    .addAll(clientDir.toFile().list());
        });
        if (!connect){
            Platform.runLater(() -> {
                serverLabel.setText("this computer");
                serverField.setText(clientDir2.toAbsolutePath().normalize().toString());
                serverView.getItems().clear();
                serverView.getItems().add("...");
                serverView.getItems()
                        .addAll(clientDir2.toFile().list());
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            clientDir = Paths.get("\\");
            clientDir2 = Paths.get("\\");
            updateClientView();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setConnection(ActionEvent actionEvent) {
            serSetDig = new ServerSettingsDialog();
            serSetDig.DialogForm("Set server settings");
            Optional<String> s = serSetDig.showAndWait();
        if (!s.isEmpty()){
            String[] atr = s.get().split(":");
            HOST = atr[0];
            PORT = Integer.parseInt(atr[1]);
            System.out.println(HOST+":"+PORT);
        }
        else {
            Platform.runLater(() -> {
                serverLabel.setTextFill(Color.RED);
                serverLabel.setText("Неверные данные подключения\n");

            });
        }
    }
}