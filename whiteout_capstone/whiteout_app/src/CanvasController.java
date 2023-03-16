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

    private boolean flag;
    private String eventString;
    private boolean send;

    GraphicsContext gc;
    
    public void initialize() {

        flag = false;
        eventString = "";
        send = false;

        c.setOnMousePressed(this::handleMousePressed);
        c.setOnMouseDragged(this::handleMouseDragged);
        c.setOnMouseReleased(this::handleMouseReleased);

        c.setOpacity(0.5);
        c.setStyle("fx-background: transparent");

        gc = c.getGraphicsContext2D();
    }

    public void setSend(boolean flag){
        this.send = flag;
    }
    public boolean getSend(){
        return this.send;
    }

    public String getEventString(){
        return this.eventString;
    }
    public void clearEventString(){
        eventString = "";
    }
    public void setEventString(String event){
        this.eventString = event;
    }

    public Canvas getCanvas(){
        Canvas canvas = new Canvas();
        canvas = this.c;
        return canvas;
    }
    public void setCanvas(Canvas c2){
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage image = c2.snapshot(params, null);
        this.c.getGraphicsContext2D().drawImage(image, 0, 0);
    }

    public void writeToCanvas(){
        String subStr = "";
        boolean setX = true;
        int x = 0;
        int y = 0;
        boolean begin = true;
        for(int i = 0; i < eventString.length(); i ++){
            if (eventString.charAt(i) == ','){
                if(setX){
                    x = Integer.parseInt(subStr);
                    subStr = "";
                    setX = false;
                }
                else{
                    y = Integer.parseInt(subStr);
                    subStr = "";
                    setX = true;
                }
            }
            else if( eventString.charAt(i) == 'z'){
                if (begin){
                    gc.beginPath();
                    gc.moveTo(x, y);
                    gc.stroke();
                    subStr = "";
                }
                else{
                    gc.lineTo(x, y);
                    gc.stroke();
                    subStr = "";
                }
            }
            else{
                subStr += Character.toString(eventString.charAt(i));
            }
        }
    }

    public boolean getFlag(){
        return this.flag;
    }

    private void handleMousePressed(MouseEvent event) {
        if (send == false){
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
            //add to eventString
            eventString += (event.getX()) + "," + (event.getY()) + "z";
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (send == false){
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
            //add to eventString
            eventString += (event.getX()) + "," + (event.getY()) + "z";
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        gc.closePath();
        send = true;
        //send eventString
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
            this.flag = true;
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
