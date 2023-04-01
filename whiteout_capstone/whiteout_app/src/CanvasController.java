import java.io.IOException;

//import javafx.event.ActionEvent;

//import javafx.scene.SnapshotParameters;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;

public class CanvasController {

    private boolean flag;
    private int actionCount;
    private int redoLimit;
    private String eventString;

    private ConnectedClient client;
    private RoomController room;

    //GraphicsContext gc;
//This is used for the undo function.
//private Stack<WritableImage> canvasSnapshotStack = new Stack<>();
private Deque<String> canvasSnapshotdeque = new ArrayDeque<>();
private Stack<String> redoStack = new Stack<>();

    

    public CanvasController() {

        eventString = "";//stores coordinate data to be sent
        actionCount = 0;//the number of actions currently stored for undo
        redoLimit = 5;//the maximum number of actions that will be remembered

        //necessary to build the client this way. Internally originating pointer carries over nodes
        this.client = new ConnectedClient(this);
        
    }

    public void establishRoom(RoomController room){
        this.room = room;
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
                    // gc.beginPath();
                    // gc.moveTo(x, y);
                    // gc.stroke();
                    begin = false;
                }
                else{//code for 'onMouseDrag'
                    // gc.lineTo(x, y);
                    // gc.stroke();
                }
            }
            else if (eventString.charAt(i) == '\n'){
                break;
            }
            else{//add sanitized digit to substring
                subStr += Character.toString(eventString.charAt(i));
            }
        }

        try{
            room.update(eventString, client.getIdValue());
        }
        catch(IOException e){
            System.out.println(e);
        }
        this.clearEventString();//nuke the eventString for the next stroke
        
    }

    public boolean getFlag(){
        return this.flag;
    }

    // private void handleMousePressed(MouseEvent event) {
    //     gc.beginPath();
    //     gc.moveTo(event.getX(), event.getY());
    //     gc.stroke();
        
    //     // add to eventString and strokes
    //     String stroke = (event.getX()) + "," + (event.getY()) + "z";
    //     eventString += stroke;
        
    // }

    // private void handleMouseDragged(MouseEvent event) {
    //     gc.lineTo(event.getX(), event.getY());
    //     gc.stroke();
        
    //     // add to eventString and strokes
    //     String stroke = (event.getX()) + "," + (event.getY()) + "z";
    //     eventString += stroke;
        
    // }

    // private void handleMouseReleased(MouseEvent event) {
    //     gc.closePath();
    
    //     // push the current snapshot of the canvas to the stack
    //     //canvasSnapshotStack.push(c.snapshot(new SnapshotParameters(), null));
        
    //     //send eventString
    //     // try{
    //         eventString += "\n";
    //         actionBackup(eventString);
    //         redoStack.clear();
    //         //client.sendString();
    //     // }
    //     // catch(IOException e){
    //     //     System.out.println(e);
    //     // }
    // }

    void clearCanvas() {
        clearEventString();
        actionBackup("\n");
    }
    
void redoClick() {
    if (!redoStack.isEmpty()) {
        // clear the canvas
       clearEventString();
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

    // @FXML
    // private void colorPickerAction(ActionEvent event) {
    //     Color color = colorPicker.getValue();
    //     gc.setStroke(color);
    //     gc.setFill(color);
    // }

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
void undoClick() {
    if (!canvasSnapshotdeque.isEmpty()) {
        // pop the previous snapshot from the stack
        redoStack.push(canvasSnapshotdeque.peek());
        canvasSnapshotdeque.pop();
        // clear the canvas
        //gc.clearRect(0, 0, c.getWidth(), c.getHeight());
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

    // void exitCanvasClick(ActionEvent event){
    //     Parent root;
    //     try {
    //         client.closeSock();

    //         root = FXMLLoader.load(getClass().getResource("./fxml/MainMenu.fxml"));
    //         Scene s = new Scene(root);
    //         s.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
    //         Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    //         window.setMaximized(false);
    //         window.setScene(s);
    //         window.centerOnScreen();
    //         window.show();

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     } 
    // }

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
