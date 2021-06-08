package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze game");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
        IModel model = new MyModel();
        MyViewModel viewModel=new MyViewModel(model);
        MyViewController view=fxmlLoader.getController();
        view.setViewModel(viewModel);
        MyViewController.playAudio();
        MyViewController.onSetImage();




    }


    public static void main(String[] args) {
        launch(args);
    }
}
