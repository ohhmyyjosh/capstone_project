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

    private Boolean allowDraw;
    private Boolean allowErase;
    
    public RoomController() throws IOException{
        this.port = 5001;

        this.roomSize = 3;
        this.idValue = 1;

        this.roomString = "";

        this.room = new Vector<ConnectedClient>();
    }
    public RoomController(Socket sock, BufferedReader in, BufferedWriter out, 
    String hostInit, ServerController server){
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
        addHost(sock, in, out, hostInit);
    }

    public void setKey(String key){
        this.key = key;
    }

    public Boolean addHost(Socket sock, BufferedReader in, BufferedWriter out, String hostInit){
        try{
            cc = new CanvasController();
            System.out.println("New canvas established..");
            cc.establishRoom(this);
            //System.out.println("New canvas established..");

            connection = cc.getClient();
            System.out.println("Canvas to Client link established..");
            connection.buildClient(port, sock, in, out, idValue, this);
            parseHostString(hostInit);
            System.out.println("Client fully built..");

            room.add(connection);
            room.elementAt(idValue-1).start();
            room.elementAt(idValue-1).sendString("x" + room.elementAt(idValue-1).getNickName());
            idValue++;
            System.out.println("Client thread started successfully...");
            System.out.println("Client connected: " + (roomSize - room.size()) + " slots remaining.");
            }
            
            catch(Exception exception){
                System.out.println(exception);
            }
        return true;
    }
    public Boolean addClient(Socket sock, BufferedReader in, BufferedWriter out, String guestInit){
        try{
            cc = new CanvasController();
            System.out.println("New canvas established..");
            cc.establishRoom(this);
            //System.out.println("New canvas established..");

            connection = cc.getClient();
            System.out.println("Canvas to Client link established..");
            connection.buildClient(port, sock, in, out, idValue, this);
            System.out.println("Client fully built..");
            parseGuestString(guestInit);

            room.add(connection);
            room.elementAt(idValue-1).start();
            room.elementAt(idValue-1).sendString("x" + room.elementAt(idValue-1).getNickName());
            idValue++;
            System.out.println("Client thread started successfully...");
            System.out.println("Client connected: " + (roomSize - room.size()) + " slots remaining.");
            }
            
            catch(Exception exception){
                System.out.println(exception);
            }
        return true;
    }
    private void parseHostString(String hostInit){
        String buffer = "";
        int index = 1;
        //parse name
        for (int i = index; i < hostInit.length(); i++){
            if (Character.isDigit(hostInit.charAt(i))){
                connection.setNickName(buffer);
                buffer = "";
                index = i;
                break;
            } 
            else{
                buffer += hostInit.charAt(i);
            }
        }
        //parse room size limit
        for (int i = index; i < hostInit.length(); i ++){
            if (!Character.isDigit(hostInit.charAt(i))){
                this.roomSize = Integer.valueOf(buffer);
                buffer = "";
                index = i;
                break;
            }
            else{
                buffer += hostInit.charAt(i);
            }
        }
        if(hostInit.charAt(index) == 't'){
            this.allowDraw = true;
        }
        else{
            this.allowDraw = false;
        }
        index++;
        if (hostInit.charAt(index) == 't'){
            this.allowErase = true;
        }
        else{
            this.allowErase = false;
        }
    }

    private void parseGuestString(String guestInit){
        String buffer = "";
        int index = 1;
        //parse name
        for (int i = index; i < guestInit.length(); i++){
            if (guestInit.charAt(i) == '~'){
                connection.setNickName(buffer);
                buffer = "";
                index = i;
                break;
            } 
            else{
                buffer += guestInit.charAt(i);
            }
            connection.setDraw(allowDraw);
            connection.setErase(allowErase);
        }
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
            try{
                refresh();
            }
            catch(IOException e){
                e.printStackTrace();
            }
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
    public void refresh() throws IOException{
        for (int i = 0; i < room.size(); i ++){
            if ( room.elementAt(i).getCC().getCurrentString() != null){
                roomString += room.elementAt(i).getCC().getCurrentString();
            }
            else{
                roomString += "~";
            }
        }
        for (int i = 0; i < room.size(); i ++){
            room.elementAt(i).sendString(("c" + roomString));
        }
        this.roomString = "";
        clientList();
    }
    public void clientList(){
        for (int i = 0; i < room.size(); i ++){
            try{
                room.elementAt(i).sendString(
                "z" + room.elementAt(i).getNickName()
                + "~" + room.elementAt(i).getDraw()+ room.elementAt(i).getErase()
                );
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        this.roomString = "";

    }

}
