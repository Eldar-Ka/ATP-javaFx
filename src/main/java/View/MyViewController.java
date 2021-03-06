package View;

import ViewModel.MyViewModel;
import algorithms.search.Solution;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController extends MenuController implements IView, Observer, Initializable  {

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Pane paneMaze;
    public Button btn_generateMaze;
    public Button btn_solveMaze;
    public Button btn_mute;
    public Label lbl_playerRow;
    public Label lbl_playerCol;
    StringProperty updatePlayerRow=new SimpleStringProperty();
    StringProperty updatePlayerCol=new SimpleStringProperty();
    public static MediaPlayer click;
    public static MediaPlayer vic;
    public static ImageView imgView;
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    double focus=1;


    public void setViewModel(MyViewModel viewModel) {
        super.setViewModel(viewModel);
        this.viewModel.addObserver(this);
    }

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }
    public void setUpdatePlayerRow(int row) {
        this.updatePlayerRow.set(""+row);
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int col) {
        this.updatePlayerCol.set(""+col);
    }

    public void generateMaze() {
        if(!mute) {
            Media media = new Media(Paths.get("src/main/resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
        }
        if (textField_mazeRows.getText().trim().isEmpty()||textField_mazeColumns.getText().trim().isEmpty())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("dimensions are missing");
            alert.setContentText("set maze rows and columns");
            alert.show();
        }
        int rows=Integer.parseInt(textField_mazeRows.getText());
        int cols=Integer.parseInt(textField_mazeColumns.getText());
        if (rows<2&&cols<2){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("maze cant be smaller then 2X2");
            alert.setContentText("make the maze bigger");
            alert.show();
        }
        else {
            btn_generateMaze.setDisable(true);
            btn_solveMaze.setDisable(false);
            viewModel.genMaze(rows, cols);
        }
    }

    public void loadMaze(String name) {

        if(!mute){
            Media media = new Media(Paths.get("src/main/resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
        }
        btn_generateMaze.setDisable(true);
        btn_solveMaze.setDisable(false);
        viewModel.loadMaze(name);
        int[][] m = viewModel.getMaze();
        textField_mazeRows.setText(String.valueOf(m.length));
        textField_mazeColumns.setText(String.valueOf(m.length));


    }
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
    private void setPlayerPos(int row, int col) {
        mazeDisplayer.setPlayerPos(row,col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
        if (mazeDisplayer.getMatrix().length-1==row&&mazeDisplayer.getMatrix()[0].length-1==col)
        {
            mediaPlayer.stop();
            Media media = new Media(Paths.get("src/main/resources/Mp3/Victory.mp3").toUri().toString());
            vic = new MediaPlayer(media);
            vic.play();
            btn_solveMaze.setDisable(true);
        }
    }
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent.getCode());
        keyEvent.consume();
    }

    @Override
    public void update(Observable o, Object arg) {
        String change=(String) arg;
        switch (change){
            case "maze gen"-> {
                try {
                    mazeGen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "player moved"->playerMoved();
            case "maze solved"->mazeSolved();
        }
    }
    private void mazeSolved() {
        Media media = new Media(Paths.get("src/main/resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
        click = new MediaPlayer(media);
        click.play();
        mazeDisplayer.setSol(viewModel.getSol());
    }

    private void playerMoved() {
        setPlayerPos(viewModel.getPlayerRow(),viewModel.getPlayerCol());
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if(viewModel.getMaze() != null) {
            int maximumSize = Math.max(viewModel.getMaze()[0].length, viewModel.getMaze().length);
            double mousePosX=helperMouseDragged(maximumSize,mazeDisplayer.getHeight(),
                    viewModel.getMaze().length,mouseEvent.getX(),mazeDisplayer.getWidth() / maximumSize);
            double mousePosY=helperMouseDragged(maximumSize,mazeDisplayer.getWidth(),
                    viewModel.getMaze()[0].length,mouseEvent.getY(),mazeDisplayer.getHeight() / maximumSize);
            if ( mousePosX == viewModel.getPlayerCol() && mousePosY < viewModel.getPlayerRow() )
                viewModel.movePlayer(KeyCode.NUMPAD8);
            else if (mousePosY == viewModel.getPlayerRow() && mousePosX > viewModel.getPlayerCol() )
                viewModel.movePlayer(KeyCode.NUMPAD6);
            else if ( mousePosY == viewModel.getPlayerRow() && mousePosX < viewModel.getPlayerCol() )
                viewModel.movePlayer(KeyCode.NUMPAD4);
            else if (mousePosX == viewModel.getPlayerCol() && mousePosY > viewModel.getPlayerRow()  )
                viewModel.movePlayer(KeyCode.NUMPAD2);
        }
    }

    private  double helperMouseDragged(int maxsize, double canvasSize, int mazeSize,double mouseEvent,double temp){
        double cellSize=canvasSize/maxsize;
        double start = (canvasSize / 2 - (cellSize * mazeSize / 2)) / cellSize;
        double mouse = (int) ((mouseEvent) / (temp) - start);
        return mouse;
    }

    private void mazeGen() throws Exception {
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_playerRow.textProperty().bind(updatePlayerRow);
        lbl_playerCol.textProperty().bind(updatePlayerCol);
        paneMaze.scaleXProperty().bind(myScale);
        paneMaze.scaleYProperty().bind(myScale);
    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }
    public Solution getSolution() {
        return viewModel.getSol();
    }
    public static void onSetImage(){
        try {
            FileInputStream input = new FileInputStream("images/winner.jpg");
            Image image = new Image(input);
            imgView = new ImageView(image);
            //SET THE WHOLE IMAGE INTO IMAGEVIEW
            Rectangle2D imagePart = new Rectangle2D(0, 0, 50, 80);
            imgView.setViewport(imagePart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void MuteMusic(ActionEvent actionEvent) {
        if(!mute) {
            Media media = new Media(Paths.get("src/main/resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
            mute = true;
            btn_mute.textProperty().setValue("UnMute");
            mediaPlayer.setVolume(0.0);
        }else{
            Media media = new Media(Paths.get("src/main/resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
            mediaPlayer.setVolume(1.0);
            mute = false;
            btn_mute.textProperty().setValue("Mute");
        }
    }


    public void saveGame() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("SaveAs");
        FXMLLoader saveFXML = new FXMLLoader(getClass().getResource("/SaveView.fxml"));
        Parent root = saveFXML.load();
        SaveController saveController = saveFXML.getController();
        saveController.setStage(stage);
        saveController.setMazeDisplayer(mazeDisplayer);
        Scene scene = new Scene(root, 700, 450);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            focus*=1.5;
            double scale = mazeDisplayer.getScale();
            if (scroll.getDeltaY() > 0) {
                mazeDisplayer.setScaleX(focus);
                mazeDisplayer.setScaleY(focus);
            }
            else if (scroll.getDeltaY() < 0) {
                focus*=0.5;
                mazeDisplayer.setScaleX(focus);
                mazeDisplayer.setScaleY(focus);
            }
            if (mazeDisplayer.getScaleX()*focus<0.9)
            {
                focus = 0.8;
                mazeDisplayer.setScaleX(1);
                mazeDisplayer.setScaleY(1);
            }
            scroll.consume();
        }
    }


}
