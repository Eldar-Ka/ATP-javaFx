package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class MainMenuController {
    public static MediaPlayer mediaPlayer;
    public Button closeButton;
    private MyViewModel viewModel;
    public Scene gamescene;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setScene(Scene scene) {
        this.gamescene = scene;
    }

    public void playGame() {
        //mediaPlayer.stop();
        ((Stage) closeButton.getScene().getWindow()).setScene(gamescene);
    }

    public static void playAudio() {
        /* duplicate music maze and menu
        Media media=new Media(Paths.get("./resources/Mp3/IveanPolkkaBass.mp3").toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
         */
    }

    public void handleCloseButtonAction(ActionEvent event) {
        System.out.println("stop");
        viewModel.close();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
