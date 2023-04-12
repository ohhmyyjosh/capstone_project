import sun.misc.SignalHandler;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;


public class RoomController{

    private String key;
    private ServerController server;

    private int buffer = 0;
    private int idValue;
    
    private int port;
    private Socket sock;
    private ServerSocket servSock;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    private BufferedReader in = null;
    private BufferedWriter out = null;

    private CanvasController cc;

    private ConnectedClient connection;
    private Vector<ConnectedClient> room;
    private int roomSize;

    private String roomString;
    
    public RoomController() throws IOException{
        this.port = 5001;

        this.roomSize = 3;
        this.idValue = 1;

        this.roomString = "";

        this.room = new Vector<ConnectedClient>();
    }
    public RoomController(Socket sock, BufferedReader in, BufferedWriter out, 
    String hostInit, ServerController server){
        try{
            this.port = 5001;
            this.roomSize = 3;
            this.idValue = 1;
            this.roomString = "";
            this.sock = sock;
            this.in = in;
            this.out = out;
            this.key = "";
            this.server = server;

            this.room = new Vector<ConnectedClient>();
            addClient(sock, in, out, hostInit);
            connection.sendString("m"+ key);
        }
        catch(IOException e){
            System.out.println (e);
        }
    }

    public void setKey(String key){
        this.key = key;
    }

    public Boolean addClient(Socket sock, BufferedReader in, BufferedWriter out, String guestInit){
        try{
            cc = new CanvasController();
            System.out.println("New canvas established..");
            cc.establishRoom(this);
            //System.out.println("New canvas established..");

            connection = cc.getClient();
            System.out.println("Canvas to Client link established..");
            connection.buildClient(port, sock, in, out, idValue, this, guestInit);
            System.out.println("Client fully built..");

            room.add(connection);
            room.elementAt(idValue-1).start();
            idValue++;
            System.out.println("Client thread started successfully...");
            System.out.println("Client connected: " + (roomSize - room.size()) + " slots remaining.");
            }
            
            catch(Exception exception){
                System.out.println(exception);
            }
        return true;
    }

    public Vector<ConnectedClient> getRoom(){
        return this.room;
    }
    public void destroyRoom(){
        this.server.removeRoom(key);
    }

    public void removeClient(int idValue){
        room.setElementAt(null, idValue-1);
        room.remove(idValue-1);
        if (!room.isEmpty()){
            for(int i = idValue-1; i < room.size(); i++){
                room.elementAt(idValue-1).adjustId();
            }
            System.out.println("Client disconnected: " + room.size() + " of " + roomSize + " remaining.");
            this.idValue --;
        }
        else{
            try{
                this.room.clear();
                this.sock.close();
                destroyRoom();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void update(String eventString, int idValue) throws IOException{ 
        for(int i = 0; i < room.size(); i ++){
            if ( i != (idValue-1)){
                room.elementAt(i).sendString("w" + eventString);
            }
        }
    }
    public void update(String eventString, int idValue, Boolean clearFlag) throws IOException{ 
        for (int i = 0; i < room.size(); i ++){
            if ( i != (idValue-1)){
                roomString += room.elementAt(i).getCC().getCurrentString();
            }
            else{
                roomString += eventString;
            }
        }
        for(int i = 0; i < room.size(); i ++){
            room.elementAt(i).sendString("c" + roomString);
        }
        this.roomString = "";
    }

}
