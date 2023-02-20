import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SettingsMenuController {

    @FXML private void saveSettingsButtonClick(ActionEvent event){

    }

    @FXML private void backSettingsButtonClick(ActionEvent event){
        ViewSwitcher.switchTo(View.MAIN);
    }

}
