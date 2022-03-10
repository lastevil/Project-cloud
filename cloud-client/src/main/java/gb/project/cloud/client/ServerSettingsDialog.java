package gb.project.cloud.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ServerSettingsDialog extends Dialog<String> {
        private TextField HOST;
        private TextField PORT;
        private Label hostLabel;
        private Label portLabel;
        private boolean isNumber;


    public void DialogForm(String title) {
            Thread.currentThread().isDaemon();
            setTitle(title);
            setHeaderText("Please enter server settings");
            HOST = new TextField();
            PORT = new TextField();
            hostLabel = new Label();
            portLabel = new Label();
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
            getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
            hostLabel.setText("Address:");
            portLabel.setText("Port:");

            VBox vBox = new VBox();
            VBox hBox1 = new VBox();
            VBox hBox2 = new VBox();
            vBox.getChildren().add(hBox1);
            hBox1.getChildren().add(hostLabel);
            hBox1.getChildren().add(HOST);
            hBox1.setPadding(new Insets(10));
            HBox.setHgrow(HOST, Priority.ALWAYS);
            vBox.getChildren().add(hBox2);
            hBox2.getChildren().add(portLabel);
            hBox2.getChildren().add(PORT);
            hBox2.setPadding(new Insets(10));
            getDialogPane().setContent(vBox);
            Platform.runLater(() -> HOST.requestFocus());
            setResultConverter(dialogButton->{
                if(dialogButton.getButtonData() == okButton.getButtonData()){
                    try {
                        Integer.parseInt(PORT.getText());
                        isNumber=true;
                    } catch (NumberFormatException e) {
                        isNumber=false;
                    }
                   if (HOST==null || PORT==null || HOST.equals(" ") || PORT.equals(" ") || !isNumber){
                        return null;
                    }
                    return HOST.getText()+":"+PORT.getText();
                }
                if (dialogButton.getButtonData() == cancelButton.getButtonData()){
                    return null;
                }
                return null;
            });
        }

}
