package gb.project.cloud.client.dialog;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LogDialog extends Dialog<String> {
    private PasswordField passwordField;
    private TextField textField;

    public void DialogForm(String title,String lable) {
        setTitle(title);
        setHeaderText(lable);
        setHeaderText("Please enter your userdata.");
        textField = new TextField();
        textField.setPromptText("Login");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        VBox vBox = new VBox();
        HBox hBox1 = new HBox();

        HBox hBox2 = new HBox();
        vBox.getChildren().add(hBox1);
        hBox1.getChildren().add(textField);
        hBox1.setPadding(new Insets(10));
        HBox.setHgrow(textField, Priority.ALWAYS);
        vBox.getChildren().add(hBox2);
        hBox2.getChildren().add(passwordField);
        hBox2.setPadding(new Insets(10));
        getDialogPane().setContent(vBox);
        Platform.runLater(() -> textField.requestFocus());

        setResultConverter(dialogButton->{
            if(dialogButton.getButtonData() == okButton.getButtonData()){
                if (!textField.getText().equals("") && !passwordField.getText().equals("")){
                return textField.getText()+" "+passwordField.getText();
                }
                else return "error";
            }
            if (dialogButton.getButtonData() == cancelButton.getButtonData()){
                return null;
            }
            return null;
        });
    }
}
