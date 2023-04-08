import java.io.IOException;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;

public class CreateSessionMenuController {

    @FXML private Button backButton;

    @FXML private Button createSessionButton;

    @FXML private CheckBox drawPermissionsButton;

    @FXML private CheckBox eraseAllPermissionsButton;

    @FXML private Spinner<?> maxGuestsInputField;

    @FXML private RadioButton privateSessionButton;

    @FXML private Label publicSessionButton;

    @FXML private TextField sessionNameInputField;

    @FXML private ToggleGroup sessionPrivacyToggleGroup;

    
    @FXML void backButtonClick(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/MainMenu.fxml"));
            Scene s = new Scene(root);
            s.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setMaximized(false);
            window.setScene(s);
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    
    @FXML void createSessionButtonClick(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/Canvas.fxml"));
            Scene s = new Scene(root);
            s.setFill(Color.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/Canvas.fxml"));
            CanvasController cc = loader.getController();

            final Node source = (Node) event.getSource();
            final Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            // Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage window = new Stage();
            window.setScene(s);
            window.setMaximized(false);
            window.initStyle(StageStyle.TRANSPARENT);
            window.show();
            

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

}
