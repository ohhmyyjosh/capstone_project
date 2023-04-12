import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;

public class JoinSessionMenuController {

    private String command = "";

    @FXML
    private Button backButton;

    @FXML
    private Button joinSessionButton;

    @FXML
    private TextField sessionCodeInputField;

    @FXML
    void backButtonClick(ActionEvent event) {
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

    @FXML
    void joinSessionButtonClick(ActionEvent event) {
        command = "";

        command += "j" + sessionCodeInputField.getText() + "~";
        createCanvas(event);
    }

    private void createCanvas(ActionEvent event){
        Parent root;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Canvas.fxml"));
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
        }
        catch(IOException e){
            System.out.println (e);
            e.printStackTrace();
        }
    }
}
