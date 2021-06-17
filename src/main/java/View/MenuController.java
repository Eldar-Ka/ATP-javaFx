package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class MenuController {
    public Button closeButton;
    public static MyViewModel viewModel;
    public static MediaPlayer mediaPlayer;
    public static boolean mute;
    private MyViewController view;

    public MyViewController getView() {
        return view;
    }

    public MyViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void openConfigurations() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Properties");
        FXMLLoader propFXML = new FXMLLoader(getClass().getResource("/Properties.fxml"));
        Parent root = propFXML.load();
        PropertiesController propController = propFXML.getController();
        propController.setStage(stage);
        Scene scene = new Scene(root, 500, 250);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void About() {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/About.fxml").openStream());
            Scene scene = new Scene(root, 748, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }

    public static void playAudio(String path) {

        Media media=new Media(Paths.get(path).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        mute=false;

    }

    public void handleCloseButtonAction() {
        System.out.println("stop");
        if(viewModel != null) // if in the menu
            viewModel.close();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void newGame() throws IOException {
        mediaPlayer.stop();

        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/MyView.fxml"));
        Parent root = fxmlLoader.load();
        ((Stage) closeButton.getScene().getWindow()).setScene(new Scene(root, 1000, 800));


        if(viewModel == null) // if in the menu
            viewModel=new MyViewModel(new MyModel());

        view=fxmlLoader.getController();
        view.setViewModel(viewModel);
        MyViewController.playAudio("src/main/resources/Mp3/StrangerThingsTitleSequence.mp3");
    }

    public void loadGame() throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Load");
        FXMLLoader loadFXML = new FXMLLoader(getClass().getResource("/LoadView.fxml"));
        Parent root = loadFXML.load();
        LoadController loadController = loadFXML.getController();
        loadController.setStage(stage);
        loadController.setView(view);
        Scene scene = new Scene(root, 700, 450);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        loadController.setMenuController(this);
    }
    public void openInstructions(ActionEvent actionEvent) throws IOException {
        try {
            Stage stage = new Stage();
            stage.setTitle("Instructions");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/HelpView.fxml").openStream());
            Scene scene = new Scene(root, 600, 1025);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }

}
