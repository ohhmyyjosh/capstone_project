import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

public class CanvasController {
    @FXML private Canvas c;
    @FXML private VBox box;
    @FXML private AnchorPane ap;

    GraphicsContext gc;
    
    public void initialize() {

        c.setOnMousePressed(this::handleMousePressed);
        c.setOnMouseDragged(this::handleMouseDragged);
        c.setOnMouseReleased(this::handleMouseReleased);

        c.setOpacity(0.5);
        c.setStyle("fx-background: transparent");

        gc = c.getGraphicsContext2D();
    }

    public Canvas getCanvas(){
        return this.c;
    }
    public void setCanvas(WritableImage image){
        c.getGraphicsContext2D().drawImage(image, 0, 0);
    }

    private void handleMousePressed(MouseEvent event) {
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
    }

    private void handleMouseDragged(MouseEvent event) {
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
    }

    private void handleMouseReleased(MouseEvent event) {
        gc.closePath();
    }

    @FXML
    void clearCanvasClick(ActionEvent event) {

    }

    @FXML
    void drawButtonToggle(ActionEvent event) {
        
    }

    @FXML
    void eraserButtonToggle(ActionEvent event) {

    }

    @FXML
    void mouseButtonToggle(ActionEvent event) {
        
    }

    @FXML
    void redoClick(ActionEvent event) {

    }

    @FXML
    void saveCanvasClick(ActionEvent event) {

    }

    @FXML
    void textButtonToggle(ActionEvent event) {

    }

    @FXML
    void undoClick(ActionEvent event) {

    }

    @FXML
    void exitCanvasClick(ActionEvent event){
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
