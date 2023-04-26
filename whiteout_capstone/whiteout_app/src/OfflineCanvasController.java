import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
//import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import java.util.List;
import java.util.Stack;


public class OfflineCanvasController {
    ResizableCanvas c;
    @FXML
    private AnchorPane ap;

    @FXML
    private VBox box;

    @FXML
    private ChoiceBox<Double> brushSizeChoiceBox;

    @FXML
    private Button clearMyDrawingsButton;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ImageView dragToolbarButton;

    @FXML
    private SplitMenuButton drawButton;

    @FXML
    private ToggleButton eraserToggleButton;

    @FXML
    private Button exitCanvasButton;

    @FXML
    private ToggleButton mouseToggleButton;

    @FXML
    private ToolBar tb;

    
    private double size;
    private double transferSize;

    private double xOffset;
    private double yOffset;
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

    private boolean mouseTransparent = false;

    private boolean eraserSelected = false;
    private boolean eraserMode = false;
    
    public void initialize() {
        c = new ResizableCanvas();
        ap.getChildren().add(c);


        c.setOnMousePressed(this::handleMousePressed);
        c.setOnMouseDragged(this::handleMouseDragged);
        c.setOnMouseReleased(this::handleMouseReleased);

        tb.setOnMousePressed(event -> {
            tb.setCursor(Cursor.CLOSED_HAND);
            xOffset = event.getSceneX() - tb.getTranslateX();
            yOffset = event.getSceneY() - tb.getTranslateY();
        });
    
        tb.setOnMouseDragged(event -> {
            tb.setTranslateX(event.getSceneX() - xOffset);
            tb.setTranslateY(event.getSceneY() - yOffset);
        });
    
        tb.setOnMouseReleased(event -> {
            tb.setCursor(Cursor.DEFAULT);
        });

        tb.setMouseTransparent(false);
        tb.toFront();

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

    
        ObservableList<Double> brushSizes = FXCollections.observableArrayList(1.0, 2.0, 3.0, 4.0, 5.0);
        brushSizeChoiceBox.setItems(brushSizes);
        brushSizeChoiceBox.setValue(1.0); // Default brush size
        brushSizeChoiceBox.setOnAction(event -> {
            this.size = (double) brushSizeChoiceBox.getValue();
            gc.setLineWidth(size);
        });

        c.heightProperty().bind(ap.heightProperty());
        c.widthProperty().bind(ap.widthProperty());

        gc = c.getGraphicsContext2D();

        color = color.valueOf("#ffffff");

        eventString = "";//stores coordinate data to be sent
        actionCount = 0;//the number of actions currently stored for undo
        redoLimit = 5;//the maximum number of actions that will be remembe
        
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
                //System.out.println("Color transfered");
                gc.setStroke(transferColor);
                gc.setFill(transferColor);
                subStr = "";
                break;
            }
            subStr += eventString.charAt(index);
            index++;
        }
        //read brush size
        while(index < eventString.length()){
            if (eventString.charAt(index) == '/'){
                index++;
                transferSize = Double.valueOf(subStr);
                //System.out.println("Brush transfered");
                gc.setLineWidth(transferSize);
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
        this.size = (double) brushSizeChoiceBox.getValue();
        gc.setLineWidth(size);
        if (eraserMode) {
            gc.clearRect(event.getX() - size / 2, event.getY() - size / 2, size, size);
        } else {
            gc.setStroke(color);
            gc.setFill(color);
        }
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
        colorStr = color.toString();
        
        // add to eventString and strokes
        String stroke = colorStr + "*" + Double.toString(size) + "/" + (event.getX()) + "," + (event.getY()) + "z";
        eventString += stroke;
        
    }
    private void handleMouseDragged(MouseEvent event) {
        if (eraserMode) {
            gc.clearRect(event.getX() - size / 2, event.getY() - size / 2, size, size);
        }else {
            gc.setStroke(color);
            gc.setFill(color);
        }
        gc.lineTo(event.getX(), event.getY());
        gc.stroke();
        
        // add to eventString and strokes
        String stroke = (event.getX()) + "," + (event.getY()) + "z";
        eventString += stroke;
        
    }
    private void handleMouseReleased(MouseEvent event) {
        gc.closePath();

        //Declare end of stroke
        eventString += "~";
        actionBackup(eventString);
        redoStack.clear();
    } 

void actionBackup(String event){
    actionCount++;
    if ( event == "~"){
        if (actionCount > redoLimit ){//if too many actions are stored, remove the earliest
            canvasSnapshotdeque.removeLast();
            actionCount--;
        }
        canvasSnapshotdeque.push("~");
    }
    else if(!canvasSnapshotdeque.isEmpty()){
        if (actionCount > redoLimit ){//if too many actions are stored, remove the earliest
            canvasSnapshotdeque.removeLast();
            actionCount--;
        }
        //create a new node with the newly added stroke
        canvasSnapshotdeque.push(canvasSnapshotdeque.peek() + event);
    }
    else if(event == ""){
    }
    else{
        canvasSnapshotdeque.push(event);
    }
    clearEventString();
    
}

    @FXML
    void eraserButtonToggle(ActionEvent event) {
        eraserMode = !eraserMode;
        if (eraserMode) {
            gc.setStroke(Color.TRANSPARENT);
            gc.setFill(Color.TRANSPARENT);
        } else {
            gc.setStroke(color);
            gc.setFill(color);
        }
    }
    
    @FXML
    void mouseButtonToggle(ActionEvent event) {

    }

    @FXML
    void textButtonToggle(ActionEvent event) {

    }

    @FXML
    void clearCanvasClick(ActionEvent event) {
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        clearEventString();
        actionBackup("~");
    }

    @FXML
    private void colorPickerAction(ActionEvent event) {
        color = colorPicker.getValue();
        gc.setStroke(color);
        gc.setFill(color);
        this.colorStr = color.toString();
    }

    @FXML
    void exitCanvasClick(ActionEvent event){
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
    void redoClick(ActionEvent event) {
        if (!redoStack.isEmpty()) {
            // clear the canvas
            gc.clearRect(0, 0, c.getWidth(), c.getHeight());
            // restore the previous snapshot to the canvas{
            String[] arrOfStrings = redoStack.peek().split("~", -3);
            for(int i = 0; i < arrOfStrings.length; i++){
                eventString = arrOfStrings[i];
                eventString += "~";

                //test statement to determine the number of operations being performed
                //System.out.println("\nAction no: "+ (i+1) + " of " + arrOfStrings.length);
                writeToCanvas();
            }
            actionBackup(redoStack.peek());
            redoStack.pop();
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
    void undoClick(ActionEvent event) {
        if (!canvasSnapshotdeque.isEmpty()) {
            // pop the previous snapshot from the stack
            redoStack.push(canvasSnapshotdeque.peek());
            canvasSnapshotdeque.pop();

            // clear the canvas
            gc.clearRect(0, 0, c.getWidth(), c.getHeight());
            setEventString("~");

            // restore the previous snapshot to the canvas
            actionCount --;
            if (!canvasSnapshotdeque.isEmpty()) {
                //gc.drawImage(canvasSnapshotStack.peek(), 0, 0);
                String[] arrOfStrings = canvasSnapshotdeque.peek().split("~", -3);
                for(int i = 0; i < arrOfStrings.length; i++){
                    eventString = arrOfStrings[i];
                    eventString += "~";
                    
                    //Test statement to determine the number of operations being performed
                    //System.out.println("\nAction no: "+ (i+1) + " of " + arrOfStrings.length);
                    writeToCanvas();
                }
            }
        }
    }

}

    
