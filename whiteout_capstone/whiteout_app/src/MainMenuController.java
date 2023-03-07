import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainMenuController {
    
    @FXML private void exitButtonClick(ActionEvent event) {
         Platform.exit();
    }

    @FXML private void settingsButtonClick(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/SettingsMenu.fxml"));
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
    
    @FXML private void createSessionButtonClick(ActionEvent event){
        
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/Canvas.fxml"));
            Scene s = new Scene(root);
            s.setFill(Color.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/Canvas.fxml"));
            CanvasController cc = loader.getController();

            SocketController test = new SocketController(cc);


            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(s);
            //window.setMaximized(true);
            window.show();
            

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    @FXML private void joinSessionButtonClick(ActionEvent event){
        
    }
}