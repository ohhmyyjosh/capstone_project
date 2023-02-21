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
    private static MainMenuController mainMenuController = new MainMenuController();


    @Override
    public void start(Stage primaryStage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("./fxml/MainMenu.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

            primaryStage.setTitle("White-Out");
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            //primaryStage.setOpacity(0.5);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
 public static void main(String[] args) {
        launch(args);
    }
}