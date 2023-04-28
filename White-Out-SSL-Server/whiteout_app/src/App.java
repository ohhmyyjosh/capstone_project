import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
 
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        try{
            ServerController server = new ServerController();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
  
 
 public static void main(String[] args) {
    Boolean debug = false;
    String trustfilename = "C:\\Program Files\\Java\\\\jdk-19\\bin" + "/" + "selfsigned.jks";

    System.setProperty("javax.net.ssl.keyStore", trustfilename);
    System.setProperty("javax.net.ssl.keyStorePassword", "whiteout");
    if(debug){
        System.setProperty("javax.ent.debug", "all");
    }
        launch(args);
    }
}