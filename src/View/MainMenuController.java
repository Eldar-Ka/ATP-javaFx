package View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class MainMenuController {
    public static MediaPlayer mediaPlayer;

    public static void playAudio() {
        Media media=new Media(Paths.get("./resources/Mp3/IveanPolkkaBass.mp3").toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
}
