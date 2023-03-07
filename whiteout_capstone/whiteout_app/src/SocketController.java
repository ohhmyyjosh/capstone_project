import java.net.*;
import java.io.*;
import java.util.Scanner;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


public class SocketController {

    private String init = "To open a socket connection identify this machine:\n1. Client\n2. Server";
    private String configSource = "Please enter this machine's Ip";
    private String configDest = "Please enter your target's IP";
    private String configPort = "Please enter the desired port";
    private String sourceIP = "LocalHost";
    private String destIP = "LocalHost";
    private int port = 0;
    private int buffer = 0;
    
    public SocketController(CanvasController cc) throws IOException{
        System.out.println(init);
        Scanner in = new Scanner(System.in);
        try{
            while(true){
                buffer = in.nextInt();
                if(buffer == 1 || buffer == 2){
                    break;
                }
                else{
                    System.out.println(init);
                }
            }
        }catch(Exception exception){
            System.out.println(init);
        }
        
        System.out.println(configSource);
        sourceIP = in.next();
        System.out.println(configDest);
        destIP = in.next();
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

        if(buffer == 1){
            ClientController(sourceIP,destIP,port,cc);
        }
        else{
            ServerController(sourceIP,destIP,port,cc);
        }

    }

    private void ClientController(String sourceIP, String destIP, int port, CanvasController cc) throws IOException{
        System.out.println("Creating client socket...");
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        
        try{
            Socket sock = new Socket(destIP, port);
            OutputStream outputStream = sock.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            for(int i =0; i < 3000; i ++){
                WritableImage image = cc.getCanvas().snapshot(params, null);

                objectOutputStream.writeObject(image);

            }
            sock.close();
        }
        catch(Exception exception){
            System.out.println("Socket creation failed.");
        }

    }

    private void ServerController(String sourceIP, String destIP, int port, CanvasController cc) throws IOException{
        System.out.println("Creating client socket...");
        try{
        ServerSocket servSock = new ServerSocket(port);
        Socket sock = servSock.accept();

        InputStream inputStream = sock.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        System.out.println("Client Connected.");

        for(int i =0; i < 3000; i ++){
            WritableImage image = (WritableImage) objectInputStream.readObject();

            cc.setCanvas(image);

        }

        sock.close();
        servSock.close();
        }
        catch(Exception exception){
            System.out.println("Socket creation failed.");
        }
    }
    



    
}
