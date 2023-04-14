import java.net.*;
import java.io.*;
import java.util.Scanner;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ConnectedClient extends Thread {

    private int port;
    private int idValue;

    private Socket sock;
    private ServerSocket servSock;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    private BufferedReader in = null;
    private BufferedWriter out = null;

    private CanvasController cc;
    private RoomController room;

    private String buffer; 
    
    private Boolean allowDraw;
    private Boolean allowErase;

    public ConnectedClient (CanvasController cc){
        this.cc = cc;
    }

    public void buildClient (int port, Socket sock, 
    BufferedReader in, BufferedWriter out, int idValue, 
    RoomController room) throws IOException{
        System.out.println("Client Building..");
        this.port = port;
        this.sock = sock;
        this.in = in;
        this.out = out;
        this.idValue = idValue;
        this.room = room;

        System.out.println("Client Connected.");

    }

    public void setDraw(Boolean perm){
        this.allowDraw = perm;
    }
    public Boolean getDraw(){
        return this.allowDraw;
    }
    public void setErase(Boolean perm){
        this.allowErase = perm;
    }
    public Boolean getErase(){
        return this.allowErase;
    }

    public void adjustId(){
        this.idValue --;
    }
    public int getIdValue(){
        return idValue;
    }
    public CanvasController getCC(){
        return this.cc;
    }

    //send stroke to server
    public void sendString(String eventString) throws IOException{
        System.out.println("Sending string..");//print test

        if (!eventString.endsWith("\n")){
            System.out.println("Correcting Event String for delivery");
            eventString += "\n";
        }
    
        out.write(eventString);//send eventString
        out.flush();
    
        //System.out.println(cc.getEventString());print test
        //cc.clearEventString();//nuke eventString for next stroke
    }

    public void closeSock() throws IOException{
        System.out.println("closing socket");
        sock.close();
        this.cc = null;
        room.removeClient((idValue));
    }

    @Override
    public void run(){
        //
        while(true){
            try{

                System.out.println("Waiting on client input..");
                try{
                    buffer = String.valueOf(in.readLine());
                    if (buffer.charAt(0) == 'w'){//write string
                        cc.setEventString(buffer.substring(1));
                        cc.writeToCanvas();
                    }
                    else if (buffer.charAt(0) == 'u'){//undo
                        cc.undoClick();
                    }
                    else if (buffer.charAt(0) == 'r'){//redo
                        cc.redoClick();
                    }
                    else if (buffer.charAt(0) == 'c'){//clear canvas
                        cc.clearCanvas();
                    }
                    else if(buffer == "null"){//happens if the socket is terminated
                        System.out.println("closing socket");
                        sock.close();
                        this.cc = null;
                        room.removeClient((idValue));
                        //System.exit(0);
                        break;
                    }
                    else{
                        System.out.println("closing socket");
                        sock.close();
                        this.cc = null;
                        room.removeClient((idValue));
                        //System.exit(0);
                        break;
                    }
                }
                catch(IOException e){//in case some eggregious error happens
                    System.out.println("closing socket");
                    sock.close();
                    this.cc = null;
                    room.removeClient((idValue));
                    //System.exit(0);
                    break;
                }
                System.out.println("input recieved");
                buffer = "";
            }
            catch(Exception e){
                System.out.println(e);
            }
        }

        try{
            sock.close();
        }
        catch(IOException e){
            //failed
        }
    }
}
    

