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
    private int idValue;
    
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
    }

    @Override
    public void run(){
        while(true){
            try{
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                System.out.println("New buffers established..");

                buffer = String.valueOf(in.readLine());
                if (buffer.charAt(1) == 'h'){//write string
                    out.write(server.buildRoom(sock, in, out, buffer) + "\n");
                    out.flush();
                }
                else if(buffer.charAt(1) == 'j'){
                    if (!server.joinRoom(sock, in, out, buffer.substring(2))){
                        out.write("mFailed to join session. Code is incorrect or room is full.\n");
                        out.flush();
                    }
                }
                cc = new CanvasController();
                System.out.println("New canvas established..");
            }
            catch(IOException e){
                System.out.println(e);
            }    
        }
    }

    
}
