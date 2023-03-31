import java.net.*;
import java.io.*;
import java.util.Scanner;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ConnectedClient extends Thread {

    private int port;
    private int idValue;

    private Socket sock;
    private ServerSocket servSock;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    private BufferedReader in = null;
    private BufferedWriter out = null;

    private CanvasController cc;

    public ConnectedClient (CanvasController cc){
        this.cc = cc;
    }

    public void buildClient (int port, ServerSocket servSock, Socket sock, 
    BufferedReader in, BufferedWriter out, int idValue) throws IOException{
        this.port = port;
        this.servSock = servSock;
        this.sock = sock;
        this.in = in;
        this.out = out;
        this.idValue = idValue;

        System.out.println("Client Connected.");

    }

    @Override
    public void run(){
        //
        while(true){
            try{

            //socket regeneration
                // if(sock.isClosed()){
                //     System.out.println("Regenerating socket..");
                //     try{
                //         servSock = new ServerSocket(this.port);
                //         sock = servSock.accept();
                //         System.out.println("Post socket Creation");
                //         }
                //         catch(Exception exception){
                //             System.out.println("Socket creation failed");
                //         }
                
                //         in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                //         out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                // }

                System.out.println("Waiting on client input..");
                try{
                    cc.setEventString(String.valueOf(in.readLine()));
                    if(cc.getEventString() == "null"){
                        System.out.println("closing socket");
                        sock.close();
                        servSock.close();
                        
                        //System.exit(0);
                        break;
                    }
                }
                catch(IOException e){
                    System.out.println("closing socket");
                    sock.close();
                    servSock.close();

                    //System.exit(0);
                    break;
                }
                System.out.println("input recieved");
                System.out.println(cc.getEventString());

                cc.writeToCanvas();


            //naive implementation
                //Canvas c2 = new Canvas();
                //c2 = (Canvas)objectInputStream.readObject();
                //cc.setCanvas(c2);
            }

            catch(Exception e){
                System.out.println(e);
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
    

