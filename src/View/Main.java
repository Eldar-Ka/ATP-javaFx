package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("MyView.fxml"));
        FXMLLoader mainMenufxmlLoader=new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        Parent root = fxmlLoader.load();
        Parent menu = mainMenufxmlLoader.load();
        primaryStage.setTitle("Maze game");
        primaryStage.setScene(new Scene(menu, 1000, 800));
        primaryStage.show();
        MenuController.playAudio("./resources/Mp3/IveanPolkkaBass.mp3");
        MenuController menuView=mainMenufxmlLoader.getController();

        //view.setViewModel(viewModel);
        //menuView.setScene(new Scene(root, 1000, 800));


        primaryStage.setOnCloseRequest(we -> {
            System.out.println("Stage is closing");
            MyViewModel Vmodel= menuView.getViewModel();
            if(Vmodel != null)
                Vmodel.close();
        });


    }


    public static void main(String[] args) {
        launch(args);
    }
}
