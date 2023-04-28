import java.net.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Scanner;

import javafx.application.Platform;
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

        try {
            // Create socket
            System.out.println("Creating client socket...");
            sock = new Socket(destIP, port);
            System.out.println("Post socket creation");
    
            // Create data streams
            in = new BufferedReader(new InputStreamReader(sock.getInputStream())); // receiving
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())); // sending
        } catch (IOException e) {
            System.out.println("Error occurred while initializing ClientController: " + e.getMessage());
            System.out.println("Server not found.");
            Platform.runLater(() -> {
                cc.closeCurrentStageAndOpenMainMenu();
            });
        }

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

        out.write("w" + cc.getEventString()+ "\n");//send eventString
        out.flush();

        System.out.println(cc.getEventString());
        cc.clearEventString();//nuke eventString for next stroke
    }
    public void sendCommand(String command) {
        if (this.out != null) {
            try {
                this.out.write(command);
                this.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else return;

       
    }

    //kill the client
    public void closeSock() throws IOException{
        try {
            System.out.println("closing socket");
            sock.close();
            in.close();
            out.close();
        } catch (Exception e){
            System.out.println("Error closing socket.");
        }
        
    }
}

