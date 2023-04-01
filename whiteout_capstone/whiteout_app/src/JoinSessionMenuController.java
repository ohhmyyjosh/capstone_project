// import java.io.IOException;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.stage.Stage;
// import javafx.scene.Node;

// public class JoinSessionMenuController {

//     @FXML
//     private Button backButton;

//     @FXML
//     private Button joinSessionButton;

//     @FXML
//     private TextField sessionCodeInputField;

//     @FXML
//     void backButtonClick(ActionEvent event) {
//         Parent root;
//         try {
//             root = FXMLLoader.load(getClass().getResource("./fxml/MainMenu.fxml"));
//             Scene s = new Scene(root);
//             s.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
//             Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//             window.setMaximized(false);
//             window.setScene(s);
//             window.centerOnScreen();
//             window.show();

//         } catch (IOException e) {
//             e.printStackTrace();
//         } 
//     }

//     @FXML
//     void joinSessionButtonClick(ActionEvent event) {

//     }

// }
