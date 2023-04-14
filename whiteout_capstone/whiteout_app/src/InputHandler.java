import java.net.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.*;

public class InputHandler extends Thread{

    private String buffer;
    private ClientController client;
    CanvasController cc;
    Socket sock;
    ServerSocket servSock;
    BufferedReader in;
    ActionEvent event;
    private Boolean ready;

    public InputHandler(ClientController client){
        System.out.println("Input Handler made!");
        this.buffer = "";
        this.cc = client.getCanvas();
        this.sock = client.getSock();
        this.in = client.getReader();
        this.ready = false;
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
                        cc.exitCanvasClick(new ActionEvent());
                        return;
                    }
                }
                catch(IOException e){
                    System.out.println("closing socket");
                    sock.close();
                    cc.exitCanvasClick(new ActionEvent());
                    return;
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
                else if (buffer.charAt(0) == 'm'){
                    System.out.println("Room code is: " + buffer.substring(1));
                    this.cc.setRoomCode(buffer.substring(1));
                    System.out.print(this.cc.getRoomCode());
                    Platform.runLater(() -> cc.roomCodeAlert());
                }
            }
            catch(Exception e){
                System.out.println(e);
                cc.exitCanvasClick(new ActionEvent());
                return;
            }
        }
    }
}
