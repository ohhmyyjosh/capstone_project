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
import javafx.stage.StageStyle;
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
            s.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            final Node source = (Node) event.getSource();
            final Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            Stage window = new Stage();
            window.setScene(s);
            window.setMaximized(false);
            window.setResizable(false);
            // window.initStyle(StageStyle.TRANSPARENT);
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    @FXML private void createSessionButtonClick(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/CreateSessionMenu.fxml"));
            Scene s = new Scene(root);
            s.setFill(Color.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/Canvas.fxml"));
            
            //loader.getController();



            s.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            final Node source = (Node) event.getSource();
            final Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            Stage window = new Stage();
            window.setScene(s);
            window.setMaximized(false);
            window.setResizable(false);
            // window.initStyle(StageStyle.TRANSPARENT);
            window.centerOnScreen();
            window.show();

            
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    @FXML private void joinSessionButtonClick(ActionEvent event){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/JoinSessionMenu.fxml"));
            Scene s = new Scene(root);
            s.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            final Node source = (Node) event.getSource();
            final Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            Stage window = new Stage();
            window.setScene(s);
            window.setMaximized(false);
            window.setResizable(false);
            // window.initStyle(StageStyle.TRANSPARENT);
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void createOfflineCanvas(ActionEvent event) {
        createCanvas(event);
    }

    private void createCanvas(ActionEvent event){
        Parent root;
        try {
            //root = FXMLLoader.load(getClass().getResource("./fxml/Canvas.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/OfflineCanvas.fxml"));
            OfflineCanvasController occ = new OfflineCanvasController();
            
            loader.setController(occ);
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