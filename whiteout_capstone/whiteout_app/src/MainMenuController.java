import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {
    
    @FXML private void exitButtonClick(ActionEvent event) {
         Platform.exit();
    }

    @FXML private void settingsButtonClick(ActionEvent event){
        ViewSwitcher.switchTo(View.SETTINGS);
    }
    
    @FXML private void createSessionButtonClick(ActionEvent event){
        WhiteboardController wbc = new WhiteboardController();
        wbc.createCanvas();
    }

    @FXML private void joinSessionButtonClick(ActionEvent event){
        
    }
}