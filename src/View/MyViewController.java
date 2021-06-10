package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer, Initializable {
    public Button closeButton;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Button btn_generateMaze;
    public Button btn_solveMaze;
    public Button btn_mute;
    private MyViewModel viewModel;
    public Label lbl_playerRow;
    public Label lbl_playerCol;
    StringProperty updatePlayerRow=new SimpleStringProperty();
    StringProperty updatePlayerCol=new SimpleStringProperty();
    public static boolean mute=false;
    public static MediaPlayer mediaPlayer;
    public static MediaPlayer click;
    public static MediaPlayer vic;
    public static ImageView imgView;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
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

    public void generateMaze(ActionEvent actionEvent) {
        if(!mute) {
            Media media = new Media(Paths.get("./resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
        }
        int rows=Integer.parseInt(textField_mazeRows.getText());
        int cols=Integer.parseInt(textField_mazeColumns.getText());
        btn_generateMaze.setDisable(true);
        btn_solveMaze.setDisable(false);
        viewModel.genMaze(rows,cols);


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
            if(!mute) {
                mediaPlayer.stop();
                Media media = new Media(Paths.get("./resources/Mp3/Victory.mp3").toUri().toString());
                vic = new MediaPlayer(media);
                vic.play();
            }
            btn_solveMaze.setDisable(true);
        }
    }
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
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
        if(!mute) {
            Media media = new Media(Paths.get("./resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
        }
        mazeDisplayer.setSol(viewModel.getSol());
    }

    private void playerMoved() {
        setPlayerPos(viewModel.getPlayerRow(),viewModel.getPlayerCol());
    }

    private void mazeGen() throws Exception {
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_playerRow.textProperty().bind(updatePlayerRow);
        lbl_playerCol.textProperty().bind(updatePlayerCol);
    }

    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }
    public Solution getSolution() {
        return viewModel.getSol();
    }
    public static void onSetImage(){
        try {
            FileInputStream input = new FileInputStream("resources/images/winner.jpg");
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
            mute = true;
            btn_mute.textProperty().setValue("UnMute");
            mediaPlayer.stop();
        }else{
            Media media = new Media(Paths.get("./resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
            mediaPlayer.play();
            mute = false;
            btn_mute.textProperty().setValue("Mute");
        }
    }
    public static void playAudio() {
        Media media=new Media(Paths.get("./resources/Mp3/StrangerThingsTitleSequence.mp3").toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void handleCloseButtonAction(ActionEvent event) {
        System.out.println("stop");
        viewModel.close();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /*
    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            double zoom_fac = 1.05;
            if (scroll.getDeltaY() < 0) {
                zoom_fac = 2.0 - zoom_fac;
            }
            Scale newScale = new Scale();
            newScale.setPivotX(scroll.getX());
            newScale.setPivotY(scroll.getY());
            newScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
            mazeDisplayer.getTransforms().add(newScale);
            scroll.consume();
        }
    }

     */
}
