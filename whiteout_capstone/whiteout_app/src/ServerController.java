import java.net.*;
import java.io.*;
import java.util.Scanner;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ServerController extends Thread {

    private Socket sock;
    private ServerSocket servSock;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    private CanvasController cc;

    public ServerController(int port, CanvasController cc) throws IOException{
        this.cc = cc;

        System.out.println("Creating server socket...");
        //socket creation
        try{
        servSock = new ServerSocket(port);
        sock = servSock.accept();
        System.out.println("Post socket Creation");
        }
        catch(Exception exception){
            System.out.println("Socket creation failed");
        }

        inputStream = sock.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);

        System.out.println("Client Connected.");
    }

    @Override
    public void run(){
        //
        while(true){
            try{
                if(cc.getFlag() == true){
                    break;
                }
                Canvas c2 = new Canvas();
                c2 = (Canvas)objectInputStream.readObject();
                cc.setCanvas(c2);
            }
            catch(Exception exception){
                //System.out.println("Read error");
            }
        }
        try{
            sock.close();
            servSock.close();
        }
        catch(IOException e){
            //failed
        }
    }
}
    

