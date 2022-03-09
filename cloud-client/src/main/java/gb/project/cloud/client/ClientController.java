package gb.project.cloud.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private Path clientDir;
    private Path clientDir2;
    private ServerSettingsDialog serSetDig;
    private  NetClient clientNet;
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
        String filename = clientView.getSelectionModel().getSelectedItem();
        if(connect){
            clientNet.upload(clientDir.resolve(filename));
        }
        else{
            Path selected = clientDir.resolve(filename);
            Path destFile = Paths.get(clientDir2.normalize().toAbsolutePath().toString(),selected.getFileName().toString());
            try {
                Files.copy(selected, destFile,
                        StandardCopyOption.REPLACE_EXISTING);
                updateClientView();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void download(ActionEvent actionEvent) {
        String filename = serverView.getSelectionModel().getSelectedItem();
        if (connect){
                clientNet.download(filename);
        }
        else {
            Path selected = clientDir2.resolve(filename);
            Path destFile = Paths.get(clientDir.normalize().toAbsolutePath().toString(),selected.getFileName().toString());
            try {
                Files.copy(selected, destFile,
                        StandardCopyOption.REPLACE_EXISTING);
                updateClientView();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateClientView() {
        Platform.runLater(() -> {
            clientField.setText(clientDir.toAbsolutePath().normalize().toString());
            clientView.getItems().clear();
            if (clientDir.getParent()!=null){
            clientView.getItems().add("...");
            }
            clientView.getItems()
                    .addAll(clientDir.toFile().list());
        });
    }

    public void updateServerView(String label,List<String> names) {
        if (!connect){
            Platform.runLater(() -> {
                serverLabel.setText(label);
                serverField.setText(clientDir2.toAbsolutePath().normalize().toString());
                serverView.getItems().clear();
                if (clientDir2.getParent()!=null){
                    serverView.getItems().add("...");
                }
                serverView.getItems()
                        .addAll(names);
            });
        }
        else{
            Platform.runLater(()->{
                serverLabel.setText(label);
                serverField.setText(clientNet.getServerPath().toString());
                serverView.getItems().clear();
                serverView.getItems().add("...");

                serverView.getItems().addAll(clientNet.getServerPath().toFile().list());
            });
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            clientDir = Paths.get("\\");
            clientDir2 = Paths.get("\\");
            updateClientView();
            List<String> names = List.of(clientDir.toFile().list());
            updateServerView("this computer",names);
            //обработчик окна файлов компьютера
            clientView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String item = clientView.getSelectionModel().getSelectedItem();
                //если в окне нажали на пустую строку
                if (item==null){return;}
                if (item.equals("...")) {
                    clientDir = clientDir.getParent();
                    updateClientView();
                }
                else {
                    Path selected = clientDir.resolve(item);
                    if (selected.toFile().isDirectory()) {
                        clientDir = selected;
                        updateClientView();
                    }
                }
            }
            });
            //обработчик окна файлов сервера (если нет соединения то сервер = компьютеру)
                serverView.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        String item = serverView.getSelectionModel().getSelectedItem();
                        //если в окне нажали на пустую строку
                        if (item==null){return;}
                        if (item.equals("...")) {
                            //если не подключен к серверу
                            if (!connect){
                                clientDir2 = clientDir2.getParent();
                                List<String> names2 = List.of(clientDir2.toFile().list());
                                updateServerView("this computer",names2);
                            }
                            //при подключенном
                            else{

                            }
                        }
                        else {
                            //если не подключен к серверу
                            if (!connect){
                                Path selected = clientDir2.resolve(item);
                                if (selected.toFile().isDirectory()) {
                                    clientDir2 = selected;
                                    List<String> names2 = List.of(clientDir2.toFile().list());
                                    updateServerView("this computer",names2);
                                }
                            }
                            //при подключенном
                            else{

                            }
                        }
                    }
                });

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
            clientNet = new NetClient(this,HOST,PORT);
            connect=true;
        }
        else {
            Platform.runLater(() -> {
                serverLabel.setTextFill(Color.RED);
                serverLabel.setText("Неверные данные подключения\n");

            });
        }
    }

    public Path getClientDir() {
        return clientDir;
    }
}