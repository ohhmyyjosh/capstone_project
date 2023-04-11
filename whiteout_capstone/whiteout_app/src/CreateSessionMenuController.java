import java.io.IOException;
import javafx.event.EventHandler;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.FlowPane;
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
    
    private String command;
    private String name;
    private int clientLimit;
    private boolean drawAllowed;
    private boolean eraseAllAllowed;
    private boolean publicRoom;

    
    @FXML void backButtonClick(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/MainMenu.fxml"));
            Scene s = new Scene(root);
            s.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            final Node source = (Node) event.getSource();
            final Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            Stage window = new Stage();
            window.setScene(s);
            window.setMaximized(false);
            window.setResizable(false);
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    
    @FXML void createSessionButtonClick(ActionEvent event) {

        this.
        
        // get username - possibly replace max guests bar with a username input field or replace session name with host name ?
        // get session data - verify that all required fields have been filled out
        // send username and session data to server
        // on the server, process this data and create a session

        // createSessionButton.disableProperty().bind(Bindings.createBooleanBinding(() -> checkTextfields(sessionNameInputField.getText()),sessionNameInputField.textProperty()));


        createCanvas(event);

    }

    // private boolean checkTextfields(String text1) {
    //     return (text1.isEmpty());
    // }

    // private static boolean isNumeric(String str) {
    //     try {
    //         Double.parseDouble(str);
    //         return true;
    //     } catch (NumberFormatException e) {
    //         return false;
    //     }
    // }

    private void createCanvas(ActionEvent event){
        Parent root;
        try {
            String command = "h";
            //root = FXMLLoader.load(getClass().getResource("./fxml/Canvas.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/Canvas.fxml"));
            CanvasController cc = new CanvasController(command);
            loader.setController(cc);
            root = loader.load();
            Scene s = new Scene(root);
            s.setFill(Color.TRANSPARENT);

            final Node source = (Node) event.getSource();
            final Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            // Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage window = new Stage();
            window.setScene(s);
            window.setMaximized(true);
            window.initStyle(StageStyle.TRANSPARENT);
            window.show();
            

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

}
