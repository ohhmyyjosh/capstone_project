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


public class SocketController extends Thread{

    private String destIP = "54.227.107.50";
    private int port = 5001;
    private int buffer = 0;

    private ClientController client;
    private ServerController server;
    private InputHandler input;
    
    public SocketController(CanvasController cc) throws IOException{
        ClientController(destIP,port,cc);
        cc.setSockCon(this);
    }

    public ClientController getClient(){
        return this.client;
    }

    public ServerController getServer(){
        return this.server;
    }

    //establishes a client socket and attempts to send canvas data on a separate thread
    private void ClientController(String destIP, int port, CanvasController cc) throws IOException{
        client = new ClientController(destIP, port, cc);
        client.start();
        input = new InputHandler(client);
        input.start();
    }

    //establishes a server socket and attempts to recieve canvas data on a separate thread
    private void ServerController( int port, CanvasController cc) throws IOException{
        server = new ServerController(port, cc);
        server.start();
    }

}
