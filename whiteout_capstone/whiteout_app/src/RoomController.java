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


public class RoomController extends Thread{

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

        servSock = new ServerSocket(port);
        this.room = new Vector<ConnectedClient>();
        this.start();
    }

    @Override
    public void run(){
        while(true){
            System.out.println("Waiting for connection...");
            //socket creation
            
            try{
            sock = servSock.accept();
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            System.out.println("New buffers established..");

            cc = new CanvasController();
            System.out.println("New canvas established..");
            cc.establishRoom(this);
            //System.out.println("New canvas established..");

            connection = cc.getClient();
            System.out.println("Canvas to Client link established..");
            connection.buildClient(port, servSock, sock, in, out, idValue, this);
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
        }
    }

    public Vector<ConnectedClient> getRoom(){
        return this.room;
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
            System.exit(0);//placeholder, will close room when multiple rooms are implemented.
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
