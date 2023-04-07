import java.net.*;
import java.io.*;

public class InputHandler extends Thread{

    private String buffer;
    private ClientController client;
    CanvasController cc;
    Socket sock;
    ServerSocket servSock;
    BufferedReader in;

    public InputHandler(ClientController client){
        this.buffer = "";
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
                    buffer = (String.valueOf(in.readLine()));
                    if(buffer == "null"){
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
                System.out.println("input recieved " + buffer.charAt(0));
                //System.out.println(cc.getEventString());
                if (buffer.charAt(0) == 'w'){
                    cc.setEventString(buffer.substring(1));
                    cc.writeToCanvas();
                }
                else if (buffer.charAt(0) == 'c'){
                    System.out.println(buffer);
                    cc.clearCanvas();
                    cc.setEventString(buffer.substring(1));
                    String[] arrOfStrings = cc.getEventString().split("~", -3);
                    for(int i = 0; i < arrOfStrings.length; i++){
                        cc.setEventString(arrOfStrings[i]);
                        cc.setEventString(cc.getEventString() + "~");
                        System.out.println("\nAction no: "+ (i+1) + " of " + arrOfStrings.length);
                        cc.writeToCanvas();
                    }
                }
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
