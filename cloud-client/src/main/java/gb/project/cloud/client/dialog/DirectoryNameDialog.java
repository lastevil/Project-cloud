package gb.project.cloud.client.dialog;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DirectoryNameDialog extends Dialog<String> {
    private TextField textField;

    public void DialogForm(String title) {
        setTitle(title);
        setHeaderText("Please enter directory name");
        textField = new TextField();
        textField.setPromptText("Directory name");
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        VBox vBox = new VBox();
        HBox hBox1 = new HBox();

        vBox.getChildren().add(hBox1);
        hBox1.getChildren().add(textField);
        hBox1.setPadding(new Insets(10));
        HBox.setHgrow(textField, Priority.ALWAYS);
        getDialogPane().setContent(vBox);
        Platform.runLater(() -> textField.requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == okButton.getButtonData()) {
                if (!textField.getText().equals("")) {
                    return textField.getText();
                } else return null;
            }
            if (dialogButton.getButtonData() == cancelButton.getButtonData()) {
                return null;
            }
            return null;
        });
    }
}
