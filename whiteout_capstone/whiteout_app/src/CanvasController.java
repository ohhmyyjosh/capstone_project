import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.scene.input.*;
//import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
import java.awt.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;



public class CanvasController {
    // @FXML private Canvas c;
    ResizableCanvas c;
    @FXML private VBox box;
    @FXML private AnchorPane ap;
    @FXML private ToolBar tb;
    @FXML private ColorPicker colorPicker; 

    private boolean flag;
    private int actionCount;
    private int redoLimit;
    private String eventString;
    private String colorStr;
    private Color transferColor;
    private Color color;

    private double xOffset;
    private double yOffset;

    private SocketController sockCon;
    private String command;

    GraphicsContext gc;
//This is used for the undo function.
//private Stack<WritableImage> canvasSnapshotStack = new Stack<>();
    private Deque<String> canvasSnapshotdeque = new ArrayDeque<>();
    private Stack<String> redoStack = new Stack<>();

    public CanvasController(String command){
        this.command = command;
    }
    public void initialize() {
        
        c = new ResizableCanvas();
        ap.getChildren().add(c);

        c.setOnMousePressed(this::handleMousePressed);
        c.setOnMouseDragged(this::handleMouseDragged);
        c.setOnMouseReleased(this::handleMouseReleased);

        tb.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - tb.getTranslateX();
            yOffset = event.getSceneY() - tb.getTranslateY();
        });
    
        tb.setOnMouseDragged(event -> {
            tb.setTranslateX(event.getSceneX() - xOffset);
            tb.setTranslateY(event.getSceneY() - yOffset);
        });
    
        tb.setOnMouseReleased(event -> {
            // Save toolbar position, if needed
        });

        tb.setMouseTransparent(false);
        tb.toFront();

        c.addEventFilter(MouseEvent.ANY, event -> {
            if (tb.getBoundsInParent().intersects(event.getSceneX(), event.getSceneY(), 1, 1)) {
                event.consume();
            }
        });

        tb.setMaxHeight(Control.USE_PREF_SIZE);
        tb.setMaxWidth(Control.USE_PREF_SIZE);

        //box.setStyle("-fx-background-color: rgba(0,0,0,0.0);");
        box.setBackground(Background.EMPTY);
        ap.setStyle("-fx-background-color: rgba(0,0,0,0.1);");
        tb.setOpacity(1);
        c.setOpacity(1);

        ap.setMaxHeight(-1);
        ap.setMaxWidth(-1);

        box.setVgrow(ap, Priority.ALWAYS);

        c.heightProperty().bind(ap.heightProperty());
        c.widthProperty().bind(ap.widthProperty());

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
        if (this.command != null){
            String[] commands = command.split("\n", 2);
            System.out.println("Command set successfully");
            System.out.println("Command is " + commands[0]);
            System.out.println("Command 2 is " + commands[1]);
        }
    }

    public void setString(String command){
        this.command = command;
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
            else if (eventString.charAt(i) == '~'){
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
        gc.setStroke(color);
        gc.setFill(color);
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
            eventString += "~";
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
        actionBackup("~");
    }

    @FXML
    void clearCanvasClick(ActionEvent event) {
        try{
            this.sockCon.getClient().sendCommand("c\n");
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
            this.sockCon.getClient().sendCommand("r\n");
        }
        catch(IOException e){
            System.out.println (e);
        }
}

    @FXML
    void saveCanvasClick(ActionEvent event) {
        WritableImage writableImage = new WritableImage((int) c.getWidth(), (int) c.getHeight());
        c.snapshot(null, writableImage);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Canvas");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                String fileName = file.getName();
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i + 1);
                }
                if (!extension.equalsIgnoreCase("png") && !extension.equalsIgnoreCase("jpg")) {
                    fileName = fileName + ".png"; // default to PNG
                }
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), extension, new File(file.getParent(), fileName));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
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
        if ( event == "~"){
            if (actionCount > redoLimit ){//if too many actions are stored, remove the earliest
                canvasSnapshotdeque.removeFirst();
                actionCount--;
            }
            canvasSnapshotdeque.push("~");
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
        this.sockCon.getClient().sendCommand("u\n");
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
            // Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            // window.setMaximized(false);
            // window.setScene(s);
            // window.centerOnScreen();
            // window.show();
            
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
