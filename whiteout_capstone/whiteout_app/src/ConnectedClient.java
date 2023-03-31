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
    private RoomController room;

    public ConnectedClient (CanvasController cc){
        this.cc = cc;
    }

    public void buildClient (int port, ServerSocket servSock, Socket sock, 
    BufferedReader in, BufferedWriter out, int idValue, RoomController room) throws IOException{
        this.port = port;
        this.servSock = servSock;
        this.sock = sock;
        this.in = in;
        this.out = out;
        this.idValue = idValue;
        this.room = room;

        System.out.println("Client Connected.");

    }

    public void adjustId(){
        this.idValue --;
    }
    public int getIdValue(){
        return idValue;
    }

    //send stroke to server
    public void sendString(String eventString) throws IOException{
        //System.out.println("Sending string..");//print test
    
        out.write(eventString);//send eventString
        out.flush();
    
        //System.out.println(cc.getEventString());print test
        //cc.clearEventString();//nuke eventString for next stroke
    }

    public void closeSock() throws IOException{
        System.out.println("closing socket");
        sock.close();
        servSock.close();
        this.cc = null;
        room.removeClient((idValue));
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
                    if(cc.getEventString() == "null"){//happens if the socket is terminated
                        System.out.println("closing socket");
                        sock.close();
                        servSock.close();
                        this.cc = null;
                        room.removeClient((idValue));
                        //System.exit(0);
                        break;
                    }
                }
                catch(IOException e){//in case some eggregious error happens
                    System.out.println("closing socket");
                    sock.close();
                    servSock.close();
                    this.cc = null;
                    room.removeClient((idValue));
                    //System.exit(0);
                    break;
                }
                System.out.println("input recieved");
               // System.out.println(cc.getEventString());

                cc.writeToCanvas();
                cc.actionBackup(cc.getEventString());


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
    

