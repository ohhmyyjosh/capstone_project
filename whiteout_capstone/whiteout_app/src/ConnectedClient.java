import java.net.*;
import java.io.*;
import javax.net.ssl.*;
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
    private String name;

    private String drawString;
    private String eraseString;
    private Boolean deathFlag;

    public ConnectedClient (CanvasController cc){
        this.cc = cc;
        this.deathFlag = false;
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
    public void setNickName(String name){
        this.name = name;
    }
    public String getNickName(){
        return this.name;
    }
    public void setDraw(Boolean perm){
        this.allowDraw = perm;
        if (allowDraw){
            setDrawString("t");
        }
        else{
            setDrawString("f");
        }
    }
    public Boolean getDraw(){
        return this.allowDraw;
    }
    public void setDrawString(String perm){
        this.drawString = perm;
    }
    public String getDrawString(){
        return this.drawString;
    }
    
    public void setErase(Boolean perm){
        this.allowErase = perm;
        if (allowErase){
            setEraseString("t");
        }
        else{
            setEraseString("f");
        }
    }
    public Boolean getErase(){
        return this.allowErase;
    }
    public void setEraseString(String perm){
        this.eraseString = perm;
    }
    public String getEraseString(){
        return this.eraseString;
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
        if (!deathFlag){
            deathFlag = true;
            System.out.println("closing socket");
            sock.close();
            this.cc = null;
            if (this.idValue <= room.getRoom().size()){
                room.removeClient(idValue);
            }
        }
    }

    public void closeSock(Boolean deathFlag) throws IOException{
        System.out.println("closing socket");
        sock.close();
        this.cc = null;
        room.removeClient(idValue);
    }

    @Override
    public void run(){
        //
        while(true){
            try{
                System.out.println("Waiting on client input..");
                SSLSocket temporaryReference = (SSLSocket)sock;
                SSLSession sslSession = temporaryReference.getSession();
                System.out.println("Protocol version " + sslSession.getProtocol());
                try{
                    buffer = String.valueOf(in.readLine());
                    if (buffer.charAt(0) == 'w'){//write string
                        if (allowDraw){
                            cc.setEventString(buffer.substring(1));
                            cc.writeToCanvas();
                        }
                    }
                    else if (buffer.charAt(0) == 'u'){//undo
                        if (allowDraw){
                            cc.undoClick();
                        }
                    }
                    else if (buffer.charAt(0) == 'r'){//redo
                        if (allowDraw){
                            cc.redoClick();
                        }
                    }
                    else if (buffer.charAt(0) == 'c'){//clear canvas
                        if (allowDraw){
                            cc.clearCanvas();
                        }
                    }
                    else if (buffer.charAt(0) == 'x'){
                        System.out.println("Checking for clear all permissions...");
                        if (allowErase){
                            System.out.println("Clearing all");
                            room.clearAll();
                        }
                    }
                    else if (buffer.charAt(0) == 'k'){
                        System.out.println("kicking a user:");
                        System.out.println(buffer);
                        int index = Integer.valueOf(buffer.substring(1));
                        if (index < room.getRoom().size()){
                            room.getRoom().elementAt(index).closeSock();
                        }
                    }

                    else if(buffer == "null"){//happens if the socket is terminated
                        System.out.println("closing socket");
                        closeSock();
                        break;
                    }
                    else{
                        System.out.println("closing socket");
                        closeSock();
                        break;
                    }
                }
                catch(IOException e){//in case some eggregious error happens
                    this.room.setDeathFlag(true);
                    closeSock();
                    break;
                }
                System.out.println("input recieved");
                buffer = "";
            }
            catch(Exception e){
                System.out.println(e);
                e.printStackTrace();
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
    

