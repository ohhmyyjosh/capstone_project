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

    private String destIP = "127.0.0.0";
    private int port = 5001;
    private int buffer = 0;

    private ClientController client;
    private InputHandler input;
    
    public SocketController(CanvasController cc) throws IOException{
        ClientController(destIP,port,cc);
        cc.setSockCon(this);
    }

    public ClientController getClient(){
        return this.client;
    }

    //establishes a client socket and attempts to send canvas data on a separate thread
    private void ClientController(String destIP, int port, CanvasController cc) throws IOException{
        client = new ClientController(destIP, port, cc);
        input = new InputHandler(client);
        input.start();
    }

}
