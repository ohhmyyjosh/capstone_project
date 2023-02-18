import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
 
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
  
  
        Parent root;
        try{
            root = FXMLLoader.load(getClass().getResource("./MainMenu.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            //primaryStage.setOpacity(0.5);

            primaryStage.setTitle("White-Out");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } 
        catch (IOException e){

        }
    }
  
 
 public static void main(String[] args) {
        launch(args);
    }
}