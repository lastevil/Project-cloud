package gb.project.cloud.client.dialog;
import javafx.scene.control.*;

public class ChoseDialog extends Dialog<String> {

    public void DialogForm(String title,  String header, String button1, String button2) {
        setTitle(title);
        setHeaderText(header);
        ButtonType okButton = new ButtonType(button1, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(button2, ButtonBar.ButtonData.CANCEL_CLOSE);
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
