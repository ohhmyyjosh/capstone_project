import java.net.*;
import java.awt.Desktop.Action;
import java.io.*;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


public class SocketController extends Thread{
    private String serverIP;
    private int port = 5001;
    private int buffer = 0;

    private ClientController client;
    private InputHandler input;
    

    public SocketController(CanvasController cc, String IP) throws IOException{
        this.serverIP = IP;
        ClientController(this.serverIP,port, cc);
        cc.setSockCon(this);
        this.serverIP = IP;
    }

    public ClientController getClient(){
        return this.client;
    }

    //establishes a client socket and attempts to send canvas data on a separate thread
    private void ClientController(String IP, int port, CanvasController cc) throws IOException{
        this.serverIP = IP;
        client = new ClientController(this.serverIP, port, cc);
        try {
            input = new InputHandler(client);
            input.start();
        } catch (Exception e){
            System.out.println("");
        }
        
    }

}
