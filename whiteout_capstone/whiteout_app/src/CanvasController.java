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
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.*;
//import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
import java.awt.*;
import java.net.*;
import java.io.*;

public class CanvasController {
    @FXML private Canvas c;
    @FXML private VBox box;
    @FXML private AnchorPane ap;
    @FXML private ColorPicker colorPicker; 

    private boolean flag;
    private int actionCount;
    private int redoLimit;
    private String eventString;

    private ConnectedClient client;

    GraphicsContext gc;
//This is used for the undo function.
//private Stack<WritableImage> canvasSnapshotStack = new Stack<>();
private Deque<String> canvasSnapshotdeque = new ArrayDeque<>();
private Stack<String> redoStack = new Stack<>();

    

    public void initialize() {

        c.setOnMousePressed(this::handleMousePressed);
        c.setOnMouseDragged(this::handleMouseDragged);
        c.setOnMouseReleased(this::handleMouseReleased);

        c.setOpacity(0.5);
        c.setStyle("fx-background: transparent");

        gc = c.getGraphicsContext2D();

        eventString = "";//stores coordinate data to be sent
        actionCount = 0;//the number of actions currently stored for undo
        redoLimit = 5;//the maximum number of actions that will be remembered

        //necessary to build the client this way. Internally originating pointer carries over nodes
        this.client = new ConnectedClient(this);
        
    }

    public ConnectedClient getClient(){
        return client;
    }
    public void set(ConnectedClient client){
        this.client = client;
    }

//eventString modifiers
    public String getEventString(){
        return this.eventString;
    }
    public void clearEventString(){
        eventString = "";
    }
    public void setEventString(String event){
        this.eventString = event;
    }

    //This method writes the client's stroke to the server's canvas
    public void writeToCanvas(){
        String subStr = ""; //holds sanitized value of x or y as string
        double x = 0, y = 0;
        boolean begin = true;//indicates a 'onMouseClick' event

        for(int i = 0; i < eventString.length(); i ++){
            if (eventString.charAt(i) == ','){//reads the value of the x coordinate
                x = Double.parseDouble(subStr);
                subStr = "";
            }
            else if( eventString.charAt(i) == 'z'){//reads the value of the y coordinate
                y = Double.parseDouble(subStr);
                subStr = "";
                if (begin){//code for 'onMouseClick'
                    gc.beginPath();
                    gc.moveTo(x, y);
                    gc.stroke();
                    begin = false;
                }
                else{//code for 'onMouseDrag'
                    gc.lineTo(x, y);
                    gc.stroke();
                }
            }
            else if (eventString.charAt(i) == '\n'){
                break;
            }
            else{//add sanitized digit to substring
                subStr += Character.toString(eventString.charAt(i));
            }
        }

        this.clearEventString();//nuke the eventString for the next stroke
        
    }

    public boolean getFlag(){
        return this.flag;
    }

    private void handleMousePressed(MouseEvent event) {
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
        
        // add to eventString and strokes
        String stroke = (event.getX()) + "," + (event.getY()) + "z";
        eventString += stroke;
        
    }

    private void handleMouseDragged(MouseEvent event) {
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
        
        // add to eventString and strokes
        String stroke = (event.getX()) + "," + (event.getY()) + "z";
        eventString += stroke;
        
    }

    private void handleMouseReleased(MouseEvent event) {
        gc.closePath();
    
        // push the current snapshot of the canvas to the stack
        //canvasSnapshotStack.push(c.snapshot(new SnapshotParameters(), null));
        
        //send eventString
        try{
            eventString += "\n";
            actionBackup(eventString);
            redoStack.clear();
            client.sendString();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    @FXML
    void clearCanvasClick(ActionEvent event) {
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        clearEventString();
        actionBackup("\n");
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
    if (!redoStack.isEmpty()) {
        // clear the canvas
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        // restore the previous snapshot to the canvas{
        String[] arrOfStrings = redoStack.peek().split("\n", -3);
        for(int i = 0; i < arrOfStrings.length; i++){
            eventString = arrOfStrings[i];
            eventString += "\n";
            System.out.println("\nAction no: "+ i+1 + " of " + arrOfStrings.length);
            writeToCanvas();
         }
        actionBackup(redoStack.peek());
        redoStack.pop();
    }
}

    @FXML
    void saveCanvasClick(ActionEvent event) {

    }

    @FXML
    void textButtonToggle(ActionEvent event) {
       
    }

    @FXML
    private void colorPickerAction(ActionEvent event) {
        Color color = colorPicker.getValue();
        gc.setStroke(color);
        gc.setFill(color);
    }

void actionBackup(String event){
    actionCount++;
    if ( event == "\n'"){
        if (actionCount > redoLimit ){//if too many actions are stored, remove the earliest
            canvasSnapshotdeque.removeFirst();
            actionCount--;
        }
        canvasSnapshotdeque.push("\n");
    }
    else if(!canvasSnapshotdeque.isEmpty()){
        if (actionCount > redoLimit ){//if too many actions are stored, remove the earliest
            canvasSnapshotdeque.removeFirst();
            actionCount--;
        }
        //create a new node with the newly added stroke
        canvasSnapshotdeque.push(canvasSnapshotdeque.peek() + event);
    }
    else{
        canvasSnapshotdeque.push(event);
    }
    
}


    //This Function does not work and instead of redo the last draw item, deletes the whole drawing on the canvas.
@FXML
void undoClick(ActionEvent event) {
    if (!canvasSnapshotdeque.isEmpty()) {
        // pop the previous snapshot from the stack
        redoStack.push(canvasSnapshotdeque.peek());
        canvasSnapshotdeque.pop();
        // clear the canvas
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        // restore the previous snapshot to the canvas
        actionCount --;
        if (!canvasSnapshotdeque.isEmpty()) {
            //gc.drawImage(canvasSnapshotStack.peek(), 0, 0);
            String[] arrOfStrings = canvasSnapshotdeque.peek().split("\n", -3);
            for(int i = 0; i < arrOfStrings.length; i++){
                eventString = arrOfStrings[i];
                eventString += "\n";
                System.out.println("\nAction no: "+ i+1 + " of " + arrOfStrings.length);
                writeToCanvas();
            }
        }
    }
}


    @FXML
    void exitCanvasClick(ActionEvent event){
        Parent root;
        try {
            client.closeSock();

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

    //purposefully naive canvas replication    
    // public Canvas getCanvas(){
    //     Canvas canvas = new Canvas();
    //     canvas = this.c;
    //     return canvas;
    // }
    // public void setCanvas(Canvas c2){
    //     SnapshotParameters params = new SnapshotParameters();
    //     params.setFill(Color.TRANSPARENT);
    //     WritableImage image = c2.snapshot(params, null);
    //     this.c.getGraphicsContext2D().drawImage(image, 0, 0);
    // }

}
