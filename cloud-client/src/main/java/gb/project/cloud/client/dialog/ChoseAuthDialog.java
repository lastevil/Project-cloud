package gb.project.cloud.client.dialog;
import javafx.scene.control.*;

public class ChoseAuthDialog extends Dialog<String> {

    public void DialogForm(String title) {
        setTitle(title);
        setHeaderText("Chose Authentication type");
        ButtonType okButton = new ButtonType("Authenticate", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Registration", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == okButton.getButtonData()) {
                return "1";
            }
            if (dialogButton.getButtonData() == cancelButton.getButtonData()) {
                return "2";
            }
            return null;
        });
    }
}
