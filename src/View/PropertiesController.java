package View;


import Server.Configurations;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    public Stage stage;
    public ChoiceBox<String> searchingAlgorithm;
    public ChoiceBox<String> generator;
    Configurations conf = Configurations.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generator.getItems().addAll("EmptyMazeGenerator","SimpleMazeGenerator","MyMazeGenerator");
        searchingAlgorithm.getItems().addAll("BreadthFirstSearch","DepthFirstSearch", "BestFirstSearch");
        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("resources/config.properties"));

            String a1= properties.getProperty("searchingAlgorithm");
            String a2= properties.getProperty("generator");
            if(a1.equals("BestFirstSearch")){
                searchingAlgorithm.setValue("BestFirstSearch");}
            else if(a1.equals("DepthFirstSearch")){
                searchingAlgorithm.setValue("DepthFirstSearch");}
            else if(a1.equals("BreadthFirstSearch")){
                searchingAlgorithm.setValue("BreadthFirstSearch");}
            if(a2.equals("MyMazeGenerator")){
                generator.setValue("MyMazeGenerator");}
            else if(a2.equals("SimpleMazeGenerator")){
                generator.setValue("SimpleMazeGenerator");}
            else if(a2.equals("EmptyMazeGenerator")){
                generator.setValue("EmptyMazeGenerator");}


        }
        catch (Exception e){}
    }
    public void UpdateClicked(){
        conf.setMazeGeneratingAlgorithm(generator.getValue());
        conf.setMazeSearchingAlgorithm(searchingAlgorithm.getValue());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Properties Updated");
        alert.setHeaderText("Properties Updated");
        alert.setContentText("Updated");
        alert.show();

    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
