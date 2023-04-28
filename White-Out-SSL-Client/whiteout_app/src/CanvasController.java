import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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




public class CanvasController {
    // @FXML private Canvas c;
    ResizableCanvas c;
    @FXML private VBox box;
    @FXML private AnchorPane ap;
    @FXML private ToolBar tb;
    @FXML private ColorPicker colorPicker; 
    @FXML private ChoiceBox<Double> brushSizeChoiceBox;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox scrollVBox;
    @FXML ToolBar userListToolBar;
    @FXML ImageView dragUserListButton;
    @FXML ImageView dragToolBarButton;
    @FXML Label roomCodeLabel;
    @FXML ToggleButton mouseToggleButton;
    @FXML ToggleButton eraserToggleButton;

    @FXML AnchorPane userAnchorPane;
    @FXML ScrollPane usernameScrollPane;
    @FXML Label usernameLabel;
    @FXML CheckBox userDrawCheckbox;
    @FXML CheckBox userEraseCheckbox;
    @FXML Button kickUserButton;

    @FXML AnchorPane userAnchorPane1;
    @FXML ScrollPane usernameScrollPane1;
    @FXML Label usernameLabel1;
    @FXML CheckBox userDrawCheckbox1;
    @FXML CheckBox userEraseCheckbox1;
    @FXML Button kickUserButton1;

    @FXML AnchorPane userAnchorPane2;
    @FXML ScrollPane usernameScrollPane2;
    @FXML Label usernameLabel2;
    @FXML CheckBox userDrawCheckbox2;
    @FXML CheckBox userEraseCheckbox2;
    @FXML Button kickUserButton2;

    @FXML AnchorPane userAnchorPane3;
    @FXML ScrollPane usernameScrollPane3;
    @FXML Label usernameLabel3;
    @FXML CheckBox userDrawCheckbox3;
    @FXML CheckBox userEraseCheckbox3;
    @FXML Button kickUserButton3;

    @FXML AnchorPane userAnchorPane4;
    @FXML ScrollPane usernameScrollPane4;
    @FXML Label usernameLabel4;
    @FXML CheckBox userDrawCheckbox4;
    @FXML CheckBox userEraseCheckbox4;
    @FXML Button kickUserButton4;

    @FXML AnchorPane userAnchorPane5;
    @FXML ScrollPane usernameScrollPane5;
    @FXML Label usernameLabel5;
    @FXML CheckBox userDrawCheckbox5;
    @FXML CheckBox userEraseCheckbox5;
    @FXML Button kickUserButton5;

    @FXML AnchorPane userAnchorPane6;
    @FXML ScrollPane usernameScrollPane6;
    @FXML Label usernameLabel6;
    @FXML CheckBox userDrawCheckbox6;
    @FXML CheckBox userEraseCheckbox6;
    @FXML Button kickUserButton6;

    @FXML AnchorPane userAnchorPane7;
    @FXML ScrollPane usernameScrollPane7;
    @FXML Label usernameLabel7;
    @FXML CheckBox userDrawCheckbox7;
    @FXML CheckBox userEraseCheckbox7;
    @FXML Button kickUserButton7;

    @FXML AnchorPane userAnchorPane8;
    @FXML ScrollPane usernameScrollPane8;
    @FXML Label usernameLabel8;
    @FXML CheckBox userDrawCheckbox8;
    @FXML CheckBox userEraseCheckbox8;
    @FXML Button kickUserButton8;

    @FXML AnchorPane userAnchorPane9;
    @FXML ScrollPane usernameScrollPane9;
    @FXML Label usernameLabel9;
    @FXML CheckBox userDrawCheckbox9;
    @FXML CheckBox userEraseCheckbox9;
    @FXML Button kickUserButton9;

    @FXML Separator separator,separator1,separator2,separator3,separator4,separator5,separator6,separator7,separator8,separator9,separator10;

    List<Label> usernameLabelsList;
    List<CheckBox> userDrawCheckboxesList;
    List<CheckBox> userEraseCheckboxesList;
    List<Button> userKickButtons;
    List<Separator> seperatorList;

    private boolean flag;
    private int actionCount;
    private int redoLimit;
    private String eventString;
    private String colorStr;
    private Color transferColor;
    private Color color;
    private double size;
    private double transferSize;

    private double xOffset;
    private double yOffset;

    private SocketController sockCon;
    private String command;
    private String roomCode;

    private int numUsers;

    GraphicsContext gc;

    private ArrayList<AnchorPane> userAnchorPanes = new ArrayList<>();
    private boolean mouseTransparent = false;

    private boolean eraserSelected = false;
    private Boolean host = false;

    private boolean drawPermission = false;
    private boolean erasePermission = false;
    

//This is used for the undo function.
//private Stack<WritableImage> canvasSnapshotStack = new Stack<>();
    //private Deque<String> canvasSnapshotdeque = new ArrayDeque<>();
    //private Stack<String> redoStack = new Stack<>();

    public CanvasController(String command, Boolean host){
        this.command = command;
        this.host = host;
    }
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

        userListToolBar.setOnMousePressed(event -> {
            userListToolBar.setCursor(Cursor.CLOSED_HAND);
            xOffset = event.getSceneX() - scrollPane.getTranslateX();
            yOffset = event.getSceneY() - scrollPane.getTranslateY();
        });
    
        userListToolBar.setOnMouseDragged(event -> {
            scrollPane.setTranslateX(event.getSceneX() - xOffset);
            scrollPane.setTranslateY(event.getSceneY() - yOffset);
        });
    
        userListToolBar.setOnMouseReleased(event -> {
            userListToolBar.setCursor(Cursor.DEFAULT);
        });
        
        scrollPane.setMouseTransparent(false);
        scrollPane.toFront();

        tb.setMaxHeight(Control.USE_PREF_SIZE);
        tb.setMaxWidth(Control.USE_PREF_SIZE);

        usernameLabelsList = new ArrayList<>(Arrays.asList(usernameLabel, usernameLabel1, usernameLabel2, usernameLabel3, usernameLabel4, usernameLabel5, usernameLabel6, usernameLabel7, usernameLabel8, usernameLabel9));
        userDrawCheckboxesList = new ArrayList<>(Arrays.asList(userDrawCheckbox, userDrawCheckbox1, userDrawCheckbox2, userDrawCheckbox3, userDrawCheckbox4, userDrawCheckbox5, userDrawCheckbox6, userDrawCheckbox7, userDrawCheckbox8, userDrawCheckbox9));
        userEraseCheckboxesList = new ArrayList<>(Arrays.asList(userEraseCheckbox, userEraseCheckbox1, userEraseCheckbox2, userEraseCheckbox3, userEraseCheckbox4, userEraseCheckbox5, userEraseCheckbox6, userEraseCheckbox7, userEraseCheckbox8, userEraseCheckbox9));
        userKickButtons = new ArrayList<>(Arrays.asList(kickUserButton, kickUserButton1, kickUserButton2, kickUserButton3, kickUserButton4, kickUserButton5, kickUserButton6, kickUserButton7, kickUserButton8, kickUserButton9));
        seperatorList = new ArrayList<>(Arrays.asList(separator,separator1,separator2,separator3,separator4,separator5,separator6,separator7,separator8,separator9,separator10));
        
        //box.setStyle("-fx-background-color: rgba(0,0,0,0.0);");
        box.setBackground(Background.EMPTY);
        ap.setStyle("-fx-background-color: rgba(0,0,0,0.1);");
        tb.setOpacity(1);
        c.setOpacity(1);

        ap.setMaxHeight(-1);
        ap.setMaxWidth(-1);

        box.setVgrow(ap, Priority.ALWAYS);
        box.setVgrow(scrollPane, Priority.ALWAYS);

    
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
        redoLimit = 5;//the maximum number of actions that will be remembered
        try{
            this.sockCon = new SocketController(this);//everything breaks if this isn't created here
        }
        catch(IOException e){
            System.out.print(e);
        }
        try{
            this.sockCon.getClient().sendCommand(command+ "\n");
        }
        catch(IOException e){
            System.out.println(e);
        }
        // while(true){
        //     if(this.sockCon.getInputHandler().getReady()){
        //         roomCodeAlert();
        //         break;
        //     }
        // }
    }

    public void removeAnchorpanes(int nu) {
        this.numUsers = nu;
        ObservableList<Node> children = scrollVBox.getChildren();
        Iterator<Node> iterator = children.iterator();
        int count = 0;

        while(iterator.hasNext()){
            if (iterator.next() instanceof AnchorPane) {
                if (count > numUsers) {
                    iterator.remove();
                }
            }
            count++;
        }
    }

    // public void removeSeparators(int nu) { 
    //     ObservableList<Node> children = scrollVBox.getChildren();
    //     Iterator<Node> iterator = children.iterator();
    //     int count = 0;
    //     int skipped = 0;
    //     int numSeparators = (nu > 0) ? (nu - 1) : 0; // calculate the number of separators needed
    
    //     while(iterator.hasNext()){
    //         Node node = iterator.next();
    //         if (node instanceof Separator) {
    //             if (count > numSeparators + 1) { // add 1 to skip the first two separators
    //                 iterator.remove();
    //             } else {
    //                 skipped++;
    //             }
    //         }
    //         count++;
    //     }
    //     System.out.println("skipped: " + skipped);
    // }

    public void updateUserAnchorPane(String username, String drawPerms, String erasePerms, int nu, int userIndex) {
        userIndex = userIndex - 1;
        System.out.println("update method: " + userIndex);
        if (usernameLabelsList.get(userIndex) != null) {
            usernameLabelsList.get(userIndex).setText(username + userIndex);
        }

        // set index 0 controls invisible for everyone
        userDrawCheckboxesList.get(0).setVisible(false);
        userEraseCheckboxesList.get(0).setVisible(false);
        userKickButtons.get(0).setVisible(false);

        if (userDrawCheckboxesList.get(userIndex) != null) {
            if(drawPerms.charAt(0) == 't'){
                userDrawCheckboxesList.get(userIndex).setSelected(true);
                if (this.host){
                    userDrawCheckboxesList.get(userIndex).setVisible(true);
                    if (userKickButtons.get(userIndex) != null) {
                        userKickButtons.get(userIndex).setVisible(true);
                    }
                }
            }
            else if(drawPerms.charAt(0) == 'f'){
                userDrawCheckboxesList.get(userIndex).setSelected(false);
                if (this.host){
                    userDrawCheckboxesList.get(userIndex).setVisible(true);
                    if (userKickButtons.get(userIndex) != null) {
                        userKickButtons.get(userIndex).setVisible(true);
                    }
                }
            }
            else {
                userEraseCheckboxesList.get(userIndex).setSelected(false);
                userDrawCheckboxesList.get(userIndex).setSelected(false);
                userDrawCheckboxesList.get(userIndex).setVisible(false);
                userEraseCheckboxesList.get(userIndex).setVisible(false);
                userKickButtons.get(userIndex).setVisible(false);

            }
        }

        if (userEraseCheckboxesList.get(userIndex) != null){
            if(erasePerms.charAt(0) == 't'){
                userEraseCheckboxesList.get(userIndex).setSelected(true);
                if (this.host) {
                    userEraseCheckboxesList.get(userIndex).setVisible(true);
                    if (userKickButtons.get(userIndex) != null) {
                        userKickButtons.get(userIndex).setVisible(true);
                    }
                }

            }
            else if(erasePerms.charAt(0) == 'f'){
                userEraseCheckboxesList.get(userIndex).setSelected(false);
                if (this.host) {
                    userEraseCheckboxesList.get(userIndex).setVisible(true);
                    if (userKickButtons.get(userIndex) != null) {
                        userKickButtons.get(userIndex).setVisible(true);
                    }
                }
            }
            else {
                userEraseCheckboxesList.get(userIndex).setSelected(false);
                userDrawCheckboxesList.get(userIndex).setSelected(false);
                userDrawCheckboxesList.get(userIndex).setVisible(false);
                userEraseCheckboxesList.get(userIndex).setVisible(false);
                userKickButtons.get(userIndex).setVisible(false);
            }
        }
    }

    @FXML
    private void toggleDrawPermissionFXML(ActionEvent event) {
        System.out.println("toggleDrawPermissionFXML");
        CheckBox clickedCheckBox = (CheckBox) event.getSource();

        int userIndex = userDrawCheckboxesList.indexOf(clickedCheckBox);

        toggleDrawPermission(userIndex);
    }

    private void toggleDrawPermission(int userIndex){
        System.out.println("toggleDrawPermission");
        CheckBox userDrawPermsBox = userDrawCheckboxesList.get(userIndex);

        drawPermission = !userDrawPermsBox.isSelected();
        userDrawPermsBox.setSelected(drawPermission);

        try {
            this.sockCon.getClient().sendCommand("pd" + Integer.toString(userIndex) + "\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleErasePermissionFXML(ActionEvent event) {
        CheckBox clickedCheckBox = (CheckBox) event.getSource();

        int userIndex = userEraseCheckboxesList.indexOf(clickedCheckBox);

        toggleErasePermission(userIndex);
    }

    private void toggleErasePermission(int userIndex){

        CheckBox userErasePermsBox = userEraseCheckboxesList.get(userIndex);

        erasePermission = !userErasePermsBox.isSelected();
        userErasePermsBox.setSelected(erasePermission);

        try {
            this.sockCon.getClient().sendCommand("pe" + Integer.toString(userIndex) + "\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void kickUserFXML(ActionEvent event) {
        Button kickUserButtonClicked = (Button) event.getSource();
        
        int userIndex = userKickButtons.indexOf(kickUserButtonClicked);
        

        kickUser(userIndex);
    }

    private void kickUser(int userIndex) {
        try {
            toggleErasePermission(userIndex);
            toggleDrawPermission(userIndex);
            this.sockCon.getClient().sendCommand("k" + Integer.toString(userIndex) + "\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void clearBoard(ActionEvent event){
        try {
            this.sockCon.getClient().sendCommand("x\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setHostUserLabel(String username){
        usernameLabel.setText(username);
        userDrawCheckbox.setVisible(false);
        userEraseCheckbox.setVisible(false);
        kickUserButton.setVisible(false);
    }

    // public void removeUserFromList(String username) {
    //     // Find the index of the userAnchorPane to remove
    //     int indexToRemove = -1;
    //     for (int i = 0; i < userAnchorPanes.size(); i++) {
    //         AnchorPane anchorPane = userAnchorPanes.get(i);
    //         Label label = (Label) anchorPane.lookup("#usernameLabel");
    //         if (label.getText().equals(username)) {
    //             indexToRemove = i;
    //             break;
    //         }
    //     }

    //     // If a userAnchorPane was found, remove it from the list and from the scrollVBox
    //     if (indexToRemove != -1) {
    //         AnchorPane anchorPaneToRemove = userAnchorPanes.get(indexToRemove);
    //         userAnchorPanes.remove(indexToRemove);
    //         scrollVBox.getChildren().remove(anchorPaneToRemove);
    //     }
    // }

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
        //read brush size
        while(index < eventString.length()){
            if (eventString.charAt(index) == '/'){
                index++;
                transferSize = Double.valueOf(subStr);
                System.out.println("Brush transfered");
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

    public void setRoomCode(String rc) {
        roomCode = rc;
    }
    public String getRoomCode() {
        return roomCode;
    }
    public void setRoomCodeLabel(String rc){
        roomCodeLabel.setText("Room Code: " + rc);
    }

    public void roomCodeAlert(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Room Code");
        alert.setHeaderText("The room code is:");
        alert.setContentText(this.roomCode);
        alert.showAndWait();
    }

    private void handleMousePressed(MouseEvent event) {
        this.size = (double) brushSizeChoiceBox.getValue();
        gc.setLineWidth(size);
        gc.setStroke(color);
        gc.setFill(color);
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY());
        gc.stroke();
        colorStr = color.toString();
        
        // add to eventString and strokes
        String stroke = colorStr + "*" + Double.toString(size) + "/" + (event.getX()) + "," + (event.getY()) + "z";
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
            //actionBackup(eventString);
            //redoStack.clear();
            sockCon.getClient().sendString();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public void clearCanvas(){
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        clearEventString();
        //actionBackup("~");
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
        c.setMouseTransparent(false);
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

    // void actionBackup(String event){
    //     actionCount++;
    //     if ( event == "~"){
    //         if (actionCount > redoLimit ){//if too many actions are stored, remove the earliest
    //             canvasSnapshotdeque.removeFirst();
    //             actionCount--;
    //         }
    //         canvasSnapshotdeque.push("~");
    //     }
    //     else if(!canvasSnapshotdeque.isEmpty()){
    //         if (actionCount > redoLimit ){//if too many actions are stored, remove the earliest
    //             canvasSnapshotdeque.removeFirst();
    //             actionCount--;
    //         }
    //         //create a new node with the newly added stroke
    //         canvasSnapshotdeque.push(canvasSnapshotdeque.peek() + event);
    //     }
    //     else{
    //         canvasSnapshotdeque.push(event);
    //     }
    // }


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
    private void toggleUserList() {
        scrollPane.setVisible(!scrollPane.isVisible());
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
    //     this.c.getGraphic Context2D().drawImage(image, 0, 0);
    // }

}
