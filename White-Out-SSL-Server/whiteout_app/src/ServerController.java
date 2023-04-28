import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.*;

public class ServerController extends Thread{

    private SSLServerSocketFactory sslsf;
    private SSLSocket sock;
    private SSLServerSocket servSock;

    //private Socket sock;
    //private ServerSocket servSock;
    private int port;
    private RoomController room;
    private String key;
    private Map rooms;
    private String buffer;
    private Object obj;

    public ServerController() throws IOException{
        Map<String, RoomController> map = new HashMap<String, RoomController>();
        rooms = map;
        rooms = new ConcurrentHashMap();
        this.port = 5001;
        //servSock = new ServerSocket(port);
        sslsf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        servSock = (SSLServerSocket)sslsf.createServerSocket(port);

        this.start();
    }

    public void removeRoom(String key){
        this.rooms.remove(key);
        System.out.println("Room closed successfully");
    }

    public String buildRoom(Socket sock, BufferedReader in, BufferedWriter out, String hostInit){
        room = new RoomController(sock, in, out, hostInit, this);
        while(true){
            key = "";
            Random r = new Random(System.currentTimeMillis());
            key += Integer.toString(r.nextInt(99999));
            
            if(rooms.put(key, room) != null){
                System.out.println("Key found: " + key);
                room.setKey(key);
                break;
            }
        }
        return key;
    }
    public Boolean joinRoom(Socket sock, BufferedReader in, BufferedWriter out, String guestInit){
        room = (RoomController)rooms.get(parseString(guestInit));
        if (room != null){
            if(room.addClient(sock, in, out, guestInit)){
                return true;
            }
        }
        return false;
    }
    public RoomController getRoom(String key){
        return (RoomController)rooms.get(key);
    }

    public String parseString(String rawInput){//returns the room code 
        buffer = "";
        for ( int i = 0; i < rawInput.length(); i ++){
            if (rawInput.charAt(i) != '~'){
                buffer += rawInput.charAt(i);
            }
            else{
                break;
            }
        }
        return buffer;
    }

    @Override
    public void run(){
        while(true){
            try{
                System.out.println("Waiting for connection...");
                //sock = servSock.accept();
                sock = (SSLSocket)servSock.accept();
                InitialConnectionHandler initCon = new InitialConnectionHandler(sock, this);
            }
            catch(IOException e){
                System.out.println(e);
            }    
        }
    }


}
