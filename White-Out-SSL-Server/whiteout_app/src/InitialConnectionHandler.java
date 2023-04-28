import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class InitialConnectionHandler extends Thread{
    private String buffer = "";
    private String sendBuffer = "";
    private int idValue;
    private String key;
    
    private int port;
    private Socket sock;
    private ServerController server;
    private RoomController room;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    private BufferedReader in = null;
    private BufferedWriter out = null;

    private CanvasController cc;

    private ConnectedClient connection;

    public InitialConnectionHandler(Socket sock, ServerController server){
        this.sock = sock;
        this.server = server;
        this.start();
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            System.out.println("New buffers established..");

            buffer = String.valueOf(in.readLine());
            System.out.println(buffer);
            if (buffer.charAt(0) == 'h'){//write string
                System.out.println("Host found.");
                this.key = server.buildRoom(sock, in, out, buffer);
                sendBuffer = "m" + key + "\n";
                out.write(sendBuffer);
                out.flush();
                try{
                    Thread.sleep(100);
                    server.getRoom(key).refresh();
                }
                catch(InterruptedException e){
                    e.printStackTrace();;
                }
            }
            else if(buffer.charAt(0) == 'j'){
                if (!server.joinRoom(sock, in, out, buffer.substring(1))){
                    out.write("iFailed to join session. Code is incorrect or room is full.\n");
                    out.flush();
                }
                else{
                    try{
                        Thread.sleep(100);
                        server.getRoom(server.parseString(buffer.substring(1))).refresh();
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            return;
        }
        catch(IOException e){
            System.out.println(e);
        }    
        return;
    }
}
