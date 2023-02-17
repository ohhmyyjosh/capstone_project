import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {
    
    @FXML private void exitAppButton(ActionEvent event) {
         Platform.exit();
    }
    
}