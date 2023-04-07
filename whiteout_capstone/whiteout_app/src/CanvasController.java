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
//import javafx.scene.image.WritableImage;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
import java.awt.*;

public class CanvasController {
    @FXML private Canvas c;
    @FXML private VBox box;
    @FXML private AnchorPane ap;
    @FXML private ColorPicker colorPicker; 

    private boolean flag;
    private int actionCount;
    private int redoLimit;
    private String eventString;
    private String colorStr;
    private Color transferColor;
    private Color color;

    private SocketController sockCon;

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

        color = color.valueOf("#000000");

        eventString = "";//stores coordinate data to be sent
        actionCount = 0;//the number of actions currently stored for undo
        redoLimit = 5;//the maximum number of actions that will be remembered
        try{
        this.sockCon = new SocketController(this);//everything breaks if this isn't created here
        }
        catch(IOException e){
            System.out.print(e);
        }
        
    }


//SocketController modifiers
    public void setSockCon(SocketController sockController){
        this.sockCon = sockController;
    }
    public SocketController getSockCon(){
        return this.sockCon;
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
        int index = 0;
        //read color
        while(index < eventString.length()){
            if (eventString.charAt(index) == '*'){
                index++;
                transferColor = transferColor.valueOf(subStr);
                System.out.println("Color transfered");
                gc.setStroke(transferColor);
                gc.setFill(transferColor);
                subStr = "";
                break;
            }
            subStr += eventString.charAt(index);
            index++;
        }

        //draw
        for(int i = index; i < eventString.length(); i ++){
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
        colorStr = color.toString();
        
        // add to eventString and strokes
        String stroke = colorStr + "*" + (event.getX()) + "," + (event.getY()) + "z";
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
            sockCon.getClient().sendString();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public void clearCanvas(){
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        clearEventString();
        actionBackup("\n");
    }

    @FXML
    void clearCanvasClick(ActionEvent event) {
        try{
            this.sockCon.getClient().sendCommand("c");
        }
        catch(IOException e){
            System.out.println (e);
        }
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
        try{
            this.sockCon.getClient().sendCommand("r");
        }
        catch(IOException e){
            System.out.println (e);
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
        color = colorPicker.getValue();
        gc.setStroke(color);
        gc.setFill(color);
        this.colorStr = color.toString();
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
    try{
        this.sockCon.getClient().sendCommand("u");
    }
    catch(IOException e){
        System.out.println (e);
    }
}


    @FXML
    void exitCanvasClick(ActionEvent event){
        Parent root;
        try {
            sockCon.getClient().closeSock();

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
