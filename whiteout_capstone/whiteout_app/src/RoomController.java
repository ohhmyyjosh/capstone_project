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
    
    public RoomController(CanvasController cc) throws IOException{
    this.cc = cc;
    this.port = 5000;

    this.roomSize = 3;
    this.idValue = 1;

    this.start();

    }

    @Override
    public void run(){
        while(true){
            System.out.println("Waiting for connection...");
            //socket creation
            
            try{
            servSock = new ServerSocket(port);
            sock = servSock.accept();
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            cc = new CanvasController();
            connection = cc.getClient();
            connection.buildClient(port, servSock, sock, in, out, idValue);
            room.add(connection);
            room.elementAt(idValue).start();
            idValue++;
            }
            
            catch(Exception exception){
            }
        }
    }


    public Vector<ConnectedClient> getRoom(){
        return this.room;
    }

}
