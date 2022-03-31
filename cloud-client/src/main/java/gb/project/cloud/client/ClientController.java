package gb.project.cloud.client;

import gb.project.cloud.client.dialog.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ClientController implements Initializable {
    private Path clientDir;
    private Path clientDir2;
    private NetClient clientNet;
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
    private MenuItem close;
    @FXML
    private MenuItem enter;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    @FXML
    private void upload() {
        String filename = clientView.getSelectionModel().getSelectedItem();
        if (connect) {
            if (clientDir.resolve(filename).toFile().isFile()) {
                clientNet.upload(clientDir.resolve(filename));
            } else {
                messageDialog("Ошибка", "Выберете файл");
            }
        } else {
            if (clientDir.resolve(filename).toFile().isFile()) {
                Path selected = clientDir.resolve(filename);
                Path destFile = Paths.get(clientDir2.normalize().toAbsolutePath().toString(),
                        selected.getFileName().toString());
                try {
                    Files.copy(selected, destFile,
                            StandardCopyOption.REPLACE_EXISTING);
                    updateClientView();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                messageDialog("Ошибка", "Выберете файл");
            }
        }
    }

    @FXML
    public void download() {
        String filename = serverView.getSelectionModel().getSelectedItem();
        if (connect) {
            clientNet.download(filename);
        } else {
            Path selected = clientDir2.resolve(filename);
            Path destFile = Paths.get(clientDir.normalize().toAbsolutePath().toString(),
                    selected.getFileName().toString());
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
            if (clientDir.getParent() != null) {
                clientView.getItems().add("...");
            }
            clientView.getItems()
                    .addAll(clientDir.toFile().list());
        });
    }

    public void updateServerView(String label, List<String> names, String path) {
        Platform.runLater(() -> {
            serverLabel.setText(label);
            serverField.setText(path);
            serverView.getItems().clear();
            serverView.getItems().addAll(names);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientDir = Paths.get("\\");
        clientDir2 = Paths.get("\\");
        updateClientView();
        List<String> names = List.of(Objects.requireNonNull(clientDir.toFile().list()));
        updateServerView("this computer",
                names,
                clientDir2.toAbsolutePath().normalize().toString());
        //обработчик окна файлов компьютера
        clientView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                clientView2Click();
            }
        });
        //обработчик окна файлов сервера (если нет соединения то сервер = компьютеру)
        serverView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                serverView2Click();
            }
        });
    }

    public void exit() {
        if (connect) {
            clientNet.closeChannel();
        }
        System.exit(0);
    }

    public Path getClientDir() {
        return clientDir;
    }

    public void setConnection() {
        ServerSettingsDialog serSetDig = new ServerSettingsDialog();
        serSetDig.DialogForm("Set server settings");
        Optional<String> s = serSetDig.showAndWait();
        if (s.isPresent()) {
            String[] atr = s.get().split(":");
            String HOST = atr[0];
            Integer PORT = Integer.parseInt(atr[1]);
            clientNet = new NetClient(this, HOST, PORT);
            connect = true;
            close.setDisable(false);
            enter.setDisable(false);

        } else {
            Platform.runLater(() -> {
                serverLabel.setTextFill(Color.RED);
                serverLabel.setText("Неверные данные подключения\n");
            });
        }
    }

    public void choseWindow() {
        ChoseAuthDialog chseDiag = new ChoseAuthDialog();
        chseDiag.DialogForm("");
        Optional<String> s = chseDiag.showAndWait();
        if (s.isPresent()) {
            if (s.get().equals("1")) {
                String[] userData = userDataWindow("Authenticate", "");
                clientNet.login(userData[0], userData[1]);
            } else {
                String[] userData = userDataWindow("Registration", "");
                clientNet.registration(userData[0], userData[1]);
            }
        }
    }

    public String[] userDataWindow(String title, String label) {
        LogDialog dialog = new LogDialog();
        dialog.DialogForm(title, label);
        Optional<String> s = dialog.showAndWait();
        if (s.isPresent()) {
            if (s.get().equals("error")) {
                return userDataWindow(title, "Неверные данные");
            } else {
                return s.get().split(" ");
            }
        }
        setDisconnect();
        return null;
    }

    public void mkdir() throws IOException {
        DirectoryNameDialog dialog = new DirectoryNameDialog();
        dialog.DialogForm("Set name");
        Optional<String> s = dialog.showAndWait();
        if (s.isPresent()) {
            if (connect) {
                clientNet.makeDir(s.get());
            } else {
                Path dir = Paths.get(clientDir2.toString(), s.get());
                Files.createDirectories(dir);
                updateServerView("this computer",
                        List.of(Objects.requireNonNull(clientDir2.toFile().list())),
                        clientDir2.toString());
            }
        }
    }

    public void setDisconnect() {
        clientNet.closeChannel();
        connect = false;
        Platform.runLater(() -> messageDialog("Warring", "Server disconnected"));
        updateServerView("this computer",
                List.of(Objects.requireNonNull(clientDir2.toFile().list())),
                clientDir2.toAbsolutePath().toString());
        close.setDisable(true);
        enter.setDisable(true);
    }

    public void messageDialog(String title, String label) {
        InfoDialog info = new InfoDialog();
        info.DialogForm(title, label);
        info.showAndWait();
    }

    public void enterToServer() {
        String[] a = userDataWindow("!", "Enter userdata");
        clientNet.login(a[0], a[1]);
    }

    public void closeConnection() {
        setDisconnect();
    }

    public void progressCopy(String type, long current, long size) {
        if ("upload".equals(type)) {
            System.out.println("Msg: " + type + " get " + current + " size " + size);
            progressBar.setProgress(current / size);
            clientNet.pathUpload();
        }
        if ("get".equals(type)) {
            progressBar.setProgress(current / size);
        }
    }

    private void clientView2Click() {
        String item = clientView.getSelectionModel().getSelectedItem();
        //если в окне нажали на пустую строку
        if (item == null) {
            return;
        }
        if (item.equals("...")) {
            clientDir = clientDir.getParent();
            updateClientView();
        } else {
            Path selected = clientDir.resolve(item);
            if (selected.toFile().isDirectory()) {
                clientDir = selected;
                updateClientView();
            }
        }
    }

    private void serverView2Click() {
        String item = serverView.getSelectionModel().getSelectedItem();
        //если в окне нажали на пустую строку
        if (item == null) {
            return;
        }
        if (item.equals("...")) {
            //если не подключен к серверу
            if (!connect) {
                clientDir2 = clientDir2.getParent();
                List<String> names2;
                if (clientDir2.getParent() != null) {
                    names2 = new LinkedList<>();
                    names2.add("...");
                    names2.addAll(List.of(Objects.requireNonNull(clientDir2.toFile().list())));
                } else {
                    names2 = List.of(Objects.requireNonNull(clientDir2.toFile().list()));
                }
                updateServerView("this computer", names2, clientDir2.toAbsolutePath().normalize().toString());
            }
            //при подключенном
            else {
                clientNet.directory(item);
            }
        } else {
            //если не подключен к серверу
            if (!connect) {
                Path selected = clientDir2.resolve(item);
                if (selected.toFile().isDirectory()) {
                    clientDir2 = selected;
                    List<String> names2;
                    if (clientDir2.getParent() != null) {
                        names2 = new LinkedList<>();
                        names2.add("...");
                        names2.addAll(List.of(Objects.requireNonNull(clientDir2.toFile().list())));
                    } else {
                        names2 = List.of(Objects.requireNonNull(clientDir2.toFile().list()));
                    }
                    updateServerView("this computer",
                            names2,
                            clientDir2.toAbsolutePath().normalize().toString());
                }
            }
            //при подключенном
            else {
                clientNet.directory(item);
            }
        }
    }

}