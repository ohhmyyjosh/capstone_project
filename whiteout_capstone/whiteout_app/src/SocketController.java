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
    Scanner in;
    private String init = "To open a socket connection identify this machine:\n1. Client\n2. Server";
    private String configDest = "Please enter your target's IP";
    private String configPort = "Please enter the desired port";
    private String destIP = "localhost";
    private int port = 0;
    private int buffer = 0;

    private ClientController client;
    private ServerController server;
    
    public SocketController(CanvasController cc) throws IOException{
        
        // determine client or server
        System.out.println(init);
        in = new Scanner(System.in);
        while(true){
            try{
                buffer = in.nextInt();
                if(buffer == 1 || buffer == 2){
                    break;
                }
                else{
                    System.out.println(init);
                }
            }
            catch(Exception exception){
                System.out.println(init);
            }
        }

        //input required socket parameters
        if( buffer == 1){
            System.out.println(configDest);
            destIP = in.next();
        }
        System.out.println(configPort);
        try{
            while(true){
                port = in.nextInt();
                break;
            }
        }catch(Exception exception){
            System.out.println(configPort);
        }
        in.close();

        //create socket objects
        if(buffer == 1){
            ClientController(destIP,port,cc);
        }
        else{
            ServerController(port,cc);
        }

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
    }

    //establishes a server socket and attempts to recieve canvas data on a separate thread
    private void ServerController( int port, CanvasController cc) throws IOException{
        server = new ServerController(port, cc);
        server.start();
    }

}
