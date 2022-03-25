package gb.project.cloud.client.dialog;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class InfoDialog extends Dialog<String> {

    public void DialogForm(String title, String label) {
        setTitle(title);
        setHeaderText(label);
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButton);
        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == okButton.getButtonData()) {
                return "null";
            }
            return null;
        });
    }
}
