package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SaveController implements Initializable {
    public Stage stage;
    public TextField textField;
    public MazeDisplayer mazeDisplayer;

    public void setMazeDisplayer(MazeDisplayer mazeDisplayer) {
        this.mazeDisplayer = mazeDisplayer;
    }

    public void save(ActionEvent actionEvent) {
        mazeDisplayer.saveMaze(textField.getText());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Maze Saved");
        alert.setHeaderText("Maze Saved");
        alert.setContentText("Saved");
        alert.show();
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
