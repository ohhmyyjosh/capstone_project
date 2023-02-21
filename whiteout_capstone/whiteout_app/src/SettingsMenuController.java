import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SettingsMenuController {

    @FXML private void saveSettingsButtonClick(ActionEvent event){

    }

    @FXML private void backSettingsButtonClick(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/MainMenu.fxml"));
            Scene s = new Scene(root);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setMaximized(false);
            window.setScene(s);
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

}
