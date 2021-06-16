package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
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
    public static MyViewController view;

    public static void setViewController(MyViewController viewC) {
        view = viewC;
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
        FXMLLoader propFXML = new FXMLLoader(getClass().getResource("/View/Properties.fxml"));
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
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
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
        //Stage s;
        mediaPlayer.stop();
        /*
        if(viewModel != null) // if in the menu
            viewModel.close();
        else
            viewModel=new MyViewModel(model);
        */
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        ((Stage) closeButton.getScene().getWindow()).setScene(new Scene(root, 1000, 800));

        //IModel model = new MyModel();

        if(viewModel == null) // if in the menu
            viewModel=new MyViewModel(new MyModel());

        //MyViewModel viewModel=new MyViewModel(model);
        MyViewController view=fxmlLoader.getController();
        //MenuController menuView=mainMenufxmlLoader.getController();
        view.setViewModel(viewModel);
        //menuView.setViewModel(viewModel);
//        MyViewController.onSetImage();
//        ((Stage)closeButton.getScene().getWindow()).addEventFilter( ScrollEvent.ANY, view.getOnScrollEventHandler());




        MyViewController.playAudio("./resources/Mp3/StrangerThingsTitleSequence.mp3");
    }

    public void loadGame() throws Exception {
        //mazeDisplayer.loadMaze();
        //textField_mazeRows.setText(mazeDisplayer.getMatrix().length);
        //textField_mazeColumns.setText("2");
        //mazeGen();
    }

}
