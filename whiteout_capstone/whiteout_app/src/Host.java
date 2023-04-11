import java.io.IOError;
import java.io.IOException;

public class Host extends CanvasController{

    public Host(String command){
        super();
        try{
            super.getSockCon().getClient().sendCommand(command);
        }
        catch(IOException e ){
            System.out.println(e);
        }
    }
    
}
