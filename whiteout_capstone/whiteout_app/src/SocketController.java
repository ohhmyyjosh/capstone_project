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
    Scanner in;
    private String init = "To open a socket connection identify this machine:\n1. Client\n2. Server";
    private String configDest = "Please enter your target's IP";
    private String configPort = "Please enter the desired port";
    private String destIP = "localhost";
    private int port = 0;
    private int buffer = 0;
    private Socket sock;
    private ServerSocket servSock;
    
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
    }

    private void ClientController(String destIP, int port, CanvasController cc) throws IOException{
        System.out.println("Creating client socket...");
        try{
            sock = new Socket(destIP, port);
            System.out.println("Post socket creation");
        }
        catch(Exception exception){
            System.out.println("Socket creation failed.");
        }

        OutputStream outputStream = sock.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        PrintWriter pr = new PrintWriter(sock.getOutputStream());
        pr.println("successful connection");
        pr.flush();

        while(true){
            try{
                objectOutputStream.writeObject(cc.getCanvas());
                if(cc.getFlag() == true){
                    break;
                }
            }
            catch(Exception exception){
                System.out.println("Failed to write");
            }
        }
        System.out.println("closing socket");
        sock.close();
    }

    private void ServerController( int port, CanvasController cc) throws IOException{
        System.out.println("Creating server socket...");
        try{
        servSock = new ServerSocket(port);
        sock = servSock.accept();
        System.out.println("Post socket Creation");
        }
        catch(Exception exception){
            System.out.println("Socket creation failed");
        }

        InputStream inputStream = sock.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        System.out.println("Client Connected.");
        
        while(true){
            try{
                Canvas c2 = (Canvas) objectInputStream.readObject();
                cc.setCanvas(c2);
                if(cc.getFlag() == true){
                    break;
                }

            }
            catch(Exception exception){
                System.out.println("Read error");
            }
        }
        sock.close();
        servSock.close();
    }

}
