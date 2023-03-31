import java.net.*;
import java.io.*;
import java.util.Scanner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ClientController extends Thread{

    private Socket sock;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private PrintWriter pr;

    private String destIP;
    private int port;
    private CanvasController cc;

    private BufferedReader in = null;
    private BufferedWriter out = null;

    //private String bufferEventString;

    public ClientController(String destIP, int port, CanvasController cc) throws IOException{

        this.destIP = destIP;
        this.port = port;
        this.cc = cc;

        //create socket
        System.out.println("Creating client socket...");
        try{
            sock = new Socket(destIP, port);
            System.out.println("Post socket creation");
        }
        catch(Exception exception){
            System.out.println("Socket creation failed.");
            return;
        }

        //create data stream
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));//recieving
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));//sending

        //outputStream = sock.getOutputStream();
        //objectOutputStream = new ObjectOutputStream(outputStream);

        //pr = new PrintWriter(sock.getOutputStream());
        //pr.println("successful connection");
        //pr.flush();

    }

    public CanvasController getCanvas(){
        return this.cc;
    }
    public Socket getSock(){
        return this.sock;
    }
    public BufferedReader getReader(){
        return this.in;
    }

    //send stroke to server
    public void sendString() throws IOException{
        System.out.println("Sending string..");//print test

        out.write(cc.getEventString());//send eventString
        out.flush();

        System.out.println(cc.getEventString());
        cc.clearEventString();//nuke eventString for next stroke
    }

    //kill the client
    public void closeSock() throws IOException{
        System.out.println("closing socket");
        sock.close();
        in.close();
        out.close();
    }
}

