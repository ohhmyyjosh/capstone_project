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
    String drawPermission = "";
    String erasePermission = "";
    int numUsers = 1;
    int userIndex = 0;

    public InputHandler(ClientController client){
        try {
            this.buffer = "";
            this.cc = client.getCanvas();
            this.sock = client.getSock();
            this.in = client.getReader();
            this.ready = false;
        } catch (Exception e) {
            return;
        }

    }

    @Override
    public void run(){
        //
        while(true){
            try{
                try{
                    if (in != null){
                        buffer = (String.valueOf(in.readLine()));
                        System.out.println("Waiting on server input..");
                        if(buffer == "null"){
                            System.out.println("closing socket");
                            Platform.runLater(() -> {
                                cc.closeCurrentStageAndOpenMainMenu();
                            });
                            sock.close();
                            return;
                        }
                    }
                    else return;
                    
                }
                catch(IOException e){
                    System.out.println("closing socket");
                    Platform.runLater(() -> {
                        cc.closeCurrentStageAndOpenMainMenu();
                    });
                    sock.close();
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
                    Platform.runLater(() -> cc.setRoomCodeLabel(cc.getRoomCode()));
                }
                else if (buffer.charAt(0) == 'x'){
                    System.out.println("Welcome, " + buffer.substring(1) + "!");
                }
                else if (buffer.charAt(0) == 'z'){
                    System.out.println(buffer);
                    String[] arrOfStrings = buffer.substring(1).split("`", -3);

                    numUsers = arrOfStrings.length;
                    for ( int i = 1; i < arrOfStrings.length; i ++){
                        System.out.println("arrOfStrings[" + i + "]" + arrOfStrings[i]);
                        drawPermission = "";
                        erasePermission = "";
                        userIndex = i;
                        System.out.println("index: " + userIndex);
                        String userInfoBuffer = arrOfStrings[i];
                        String[] userInfo = userInfoBuffer.split("~", -3);
                        String username = userInfo[0];
                        drawPermission += userInfo[1].charAt(0);
                        erasePermission += userInfo[1].charAt(1);
                        

                        System.out.println("user: " + username + "\ndraw: " + drawPermission + "\nerase: " + erasePermission);
                        System.out.println(username + drawPermission + erasePermission);

                        final String finalUsername = username;
                        final String finalDrawPermission = drawPermission;
                        final String finalErasePermission = erasePermission;
                        final int finalUserIndex = userIndex;

                        Platform.runLater(() -> {
                            cc.removeAnchorpanes(numUsers);
                            //cc.removeSeparators(numUsers);
                            cc.updateUserAnchorPane(finalUsername, finalDrawPermission, finalErasePermission, numUsers, finalUserIndex);
                        });
                        // Platform.runLater(() -> cc.removeAnchorpanes(numUsers));
                        // Platform.runLater(() -> cc.updateUserAnchorPane(username, drawPermission, erasePermission, userIndex));
                    }

                    
                }
            }
            catch(Exception e){
                System.out.println(e);
                e.printStackTrace();
                Platform.runLater(() -> {
                    cc.closeCurrentStageAndOpenMainMenu();
                });
                return;
            }
        }
    }
}
