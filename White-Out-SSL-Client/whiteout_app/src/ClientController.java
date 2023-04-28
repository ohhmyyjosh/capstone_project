import java.net.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ClientController extends Thread{

    private SSLSocket sock;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private PrintWriter pr;

    private String destIP;
    private int port;
    private CanvasController cc;

    private BufferedReader in = null;
    private BufferedWriter out = null;

    private String trustFileName;
    private String storePath;

    //private String bufferEventString;

    public ClientController(String destIP, int port, CanvasController cc) throws IOException{

        trustFileName = 

        this.destIP = destIP;
        this.port = port;
        this.cc = cc;

        //create socket
        System.out.println("Creating client socket...");
        try{
            SSLSocketFactory sslsf = (SSLSocketFactory)SSLSocketFactory.getDefault();
            sock = (SSLSocket)sslsf.createSocket(destIP, port);
            System.out.println("Post socket creation");
        }
        catch(Exception exception){
            System.out.println("Socket creation failed.");
            exception.printStackTrace();
            return;
        }

        //create data stream
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));//recieving
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));//sending

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

        out.write("w" + cc.getEventString()+ "\n");//send eventString
        out.flush();

        System.out.println(cc.getEventString());
        cc.clearEventString();//nuke eventString for next stroke
    }
    public void sendCommand(String command) throws IOException{
        System.out.println("Sending command " + command + "..");
        out.write(command);
        out.flush();
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

