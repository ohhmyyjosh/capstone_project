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

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("./fxml/MainMenu.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setTitle("White-Out");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
  
 
    public static void main(String[] args) {
        String trustFileName = "C:\\Program Files\\Java\\jre1.8.0_341\\lib\\security" + "/" + "cacerts";
        System.setProperty("javax.net.ssl.trustStore", trustFileName);
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        System.setProperty("javax.net.debug", "all");

        launch(args);
    }
} 