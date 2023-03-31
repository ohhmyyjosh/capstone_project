import java.net.*;
import java.io.*;

public class InputHandler extends Thread{

    private ClientController client;
    CanvasController cc;
    Socket sock;
    ServerSocket servSock;
    BufferedReader in;

    public InputHandler(ClientController client){
        this.cc = client.getCanvas();
        this.sock = client.getSock();
        this.in = client.getReader();
    }

    @Override
    public void run(){
        //
        while(true){
            try{
                System.out.println("Waiting on server input..");
                try{
                    cc.setEventString(String.valueOf(in.readLine()));
                    if(cc.getEventString() == "null"){
                        System.out.println("closing socket");
                        sock.close();
                        System.exit(0);
                        break;
                    }
                }
                catch(IOException e){
                    System.out.println("closing socket");
                    sock.close();
                    System.exit(0);
                    break;
                }
                System.out.println("input recieved");
                //System.out.println(cc.getEventString());

                cc.writeToCanvas();
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        try{
            sock.close();
        }
        catch(IOException e){
            //failed
        }
    }
    
}
