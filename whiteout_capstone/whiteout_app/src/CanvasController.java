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
    private String buffer;

    private ConnectedClient client;
    private RoomController room;

    //GraphicsContext gc;
    //This is used for the undo function.
    //private Stack<WritableImage> canvasSnapshotStack = new Stack<>();
    private Deque<String> canvasSnapshotdeque = new ArrayDeque<>();
    private Stack<String> redoStack = new Stack<>();

    

    public CanvasController() {

        buffer = "";//stores temporary data
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
        try{
            actionBackup(eventString);
            room.update(eventString, client.getIdValue());
        }
        catch(IOException e){
            System.out.println(e);
        }
        this.clearEventString();//nuke the eventString for the next stroke
    }
    public void writeToCanvas(Boolean clearFlag){
        try{
            room.update(eventString, client.getIdValue(), clearFlag);
        }
        catch(IOException e){
            System.out.println(e);
        }
        this.clearEventString();//nuke the eventString for the next stroke
    }

    public boolean getFlag(){
        return this.flag;
    }

    public void clearCanvas() {
        clearEventString();
        actionBackup("~");
        setEventString("~");
        writeToCanvas(flag);
    }
    
    public void redoClick() {
    if (!redoStack.isEmpty()) {
        // clear the canvas
       clearEventString();
        // restore the previous snapshot to the canvas{
        String[] arrOfStrings = redoStack.peek().split("~", -3);
        for(int i = 0; i < arrOfStrings.length; i++){
            buffer = arrOfStrings[i];
            buffer += "~";
            System.out.println("\nAction no: "+ i+1 + " of " + arrOfStrings.length);
            eventString += buffer;
        }
        System.out.println("Canvas level test: redo event " + eventString);
        writeToCanvas(flag);
        actionBackup(redoStack.peek());
        redoStack.pop();
        }
    }

    public void actionBackup(String event){
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
            System.out.println("Empty deque detected. Adding first snapshot" + event);
        }
    
    }


    //This Function does not work and instead of redo the last draw item, deletes the whole drawing on the canvas.
    public void undoClick() {
        if (!canvasSnapshotdeque.isEmpty()) {
            // pop the previous snapshot from the stack
            redoStack.push(canvasSnapshotdeque.peek());
            canvasSnapshotdeque.pop();
            // clear the canvas
            //gc.clearRect(0, 0, c.getWidth(), c.getHeight());
            setEventString("\n");
            // restore the previous snapshot to the canvas
            actionCount --;
            if (!canvasSnapshotdeque.isEmpty()) {
                clearEventString();
                //gc.drawImage(canvasSnapshotStack.peek(), 0, 0);
                String[] arrOfStrings = canvasSnapshotdeque.peek().split("\n", -3);
                for(int i = 0; i < arrOfStrings.length; i++){
                    buffer = arrOfStrings[i] + "\n";
                    System.out.println("\nAction no: "+ i+1 + " of " + arrOfStrings.length);
                    eventString += buffer;
                }
            }
            writeToCanvas(flag);
        }
    }

    public String getCurrentString(){
        return this.canvasSnapshotdeque.peek();
    }

}
