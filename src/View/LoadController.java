package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadController implements Initializable {
    public Stage stage;
    public TextField textField;
    private MyViewController view;
    MenuController menuController;

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    public void setView(MyViewController view) {
        this.view = view;
    }

    public void load(ActionEvent actionEvent) throws IOException {
        menuController.newGame();
        menuController.getView().loadMaze(textField.getText());

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
