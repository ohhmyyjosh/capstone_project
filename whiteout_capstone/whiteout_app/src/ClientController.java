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

    //private String bufferEventString;

    public ClientController(String destIP, int port, CanvasController cc) throws IOException{

        this.destIP = destIP;
        this.port = port;
        this.cc = cc;

        //create socket
        System.out.println("Creating client socket...");
        try{
            sock = new Socket(destIP, port);
            System.out.println("Post socket creation");
        }
        catch(Exception exception){
            System.out.println("Socket creation failed.");
            return;
        }

        //create data stream
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));//recieving
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));//sending

        //outputStream = sock.getOutputStream();
        //objectOutputStream = new ObjectOutputStream(outputStream);

        //pr = new PrintWriter(sock.getOutputStream());
        //pr.println("successful connection");
        //pr.flush();

    }

    //send stroke to server
    public void sendString() throws IOException{
        System.out.println("Sending sting..");//print test

        out.write(cc.getEventString());//send eventString
        out.flush();

        System.out.println(cc.getEventString());
        cc.clearEventString();//nuke eventString for next stroke
    }

    //kill the client
    public void closeSock() throws IOException{
        System.out.println("closing socket");
        sock.close();
        in.close();
        out.close();
    }



    //@Override
    //public void run(){

        // while(true){
        //  if(!sock.isConnected()){
                //System.out.println("Closing socket..");
                //sock.close();
                //in.close();
                //out.close();
        //  }
        //     try{
        //         sleep(5);
                // bufferEventString = in.readLine();
                // cc.writeToCanvas(bufferEventString);
                // }
                // catch(IOExcetpion e){
                    //System.out.println(e);
                // }
                //}
                //objectOutputStream.writeObject(cc.getCanvas());
        //     }
        //     catch(Exception exception){
        //         System.out.println("Failed to write");
        //     }
        // }
        // System.out.println("closing socket");
        //  try{
        //     sock.close();
        //  }
        //  catch(IOException e){
        //     // failed
        //  }
        //}
    }

