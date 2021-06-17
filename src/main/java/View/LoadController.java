package View;

import IO.MyDecompressorInputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadController implements Initializable {
    public Stage stage;
    public TextField textField;
    private MyViewController view;
    MenuController menuController;
    private final Logger logGen= LogManager.getLogger();


    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    public void setView(MyViewController view) {
        this.view = view;
    }

    public void load(ActionEvent actionEvent) throws IOException {
        byte savedMazeBytes[] = new byte[0];
        try {

            InputStream in = new MyDecompressorInputStream(new
                    FileInputStream(textField.getText()));
            savedMazeBytes = new byte[50000];
            in.read(savedMazeBytes);
            in.close();
            menuController.newGame();
            menuController.getView().loadMaze(textField.getText());
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("couldn't fined the file");
            alert.setContentText("couldn't fined the file named: "+ textField.getText());
            alert.show();
            logGen.error("couldn't fined the file named: "+ textField.getText());
        }



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
