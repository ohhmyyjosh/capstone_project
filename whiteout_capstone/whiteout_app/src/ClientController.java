import java.net.*;
import java.io.*;
import java.util.Scanner;
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

    public ClientController(String destIP, int port, CanvasController cc) throws IOException{

        this.destIP = destIP;
        this.port = port;
        this.cc = cc;

        System.out.println("Creating client socket...");
        try{
            sock = new Socket(destIP, port);
            System.out.println("Post socket creation");
        }
        catch(Exception exception){
            System.out.println("Socket creation failed.");
            return;
        }

        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

        outputStream = sock.getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);

        pr = new PrintWriter(sock.getOutputStream());
        pr.println("successful connection");
        pr.flush();
    }

    @Override
    public void run(){

        while(true){
            try{
                if(cc.getFlag() == true){
                    System.out.println("closing socket");
                    sock.close();
                    System.exit(0);
                    break;
                }
                if(cc.getSend() == true){
                    System.out.println("send = true");
                    out.write(cc.getEventString(), 0, cc.getEventString().length());
                    System.out.println(cc.getEventString());
                    cc.setSend(false);
                    out.flush();
                }
                //objectOutputStream.writeObject(cc.getCanvas());
            }
            catch(Exception exception){
                //System.out.println("Failed to write");
            }
        }
        System.out.println("closing socket");
         try{
            sock.close();
         }
         catch(IOException e){
            // failed
         }
    }


}
