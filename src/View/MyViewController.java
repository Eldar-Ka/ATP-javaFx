package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
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
    public Pane paneMaze;
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
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);



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

    public void generateMaze() {
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
            mediaPlayer.stop();
            Media media = new Media(Paths.get("./resources/Mp3/Victory.mp3").toUri().toString());
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
        Media media = new Media(Paths.get("./resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
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
            Media media = new Media(Paths.get("./resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
            mute = true;
            btn_mute.textProperty().setValue("UnMute");
            mediaPlayer.setVolume(0.0);
        }else{
            Media media = new Media(Paths.get("./resources/Mp3/MouseClickSoundEffect.mp3").toUri().toString());
            click = new MediaPlayer(media);
            click.play();
            mediaPlayer.setVolume(1.0);
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
    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    public void newGame() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        ((Stage) closeButton.getScene().getWindow()).setScene(new Scene(root, 1000, 800));
    }

    public void saveGame() {
        mazeDisplayer.saveMaze();
    }

    public void loadGame() throws Exception {
        mazeDisplayer.loadMaze();
        //textField_mazeRows.setText(mazeDisplayer.getMatrix().length);
        //textField_mazeColumns.setText("2");
        //mazeGen();
    }

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
    EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent event) {
            if (event.isControlDown()) {
                boolean zOut = false;
                double scale = mazeDisplayer.getScale();
                double oldScale = scale;

                if (event.getDeltaY() < 0) {
                    scale /= Math.pow(1.2, -event.getDeltaY() / 20);
                    //scale /= 1.3;
                    zOut = true;
                }
                else
                    scale *= Math.pow(1.2, event.getDeltaY() / 20);
                    //scale *= 1.3;

                scale = clamp(scale, 1.0d, 50.0d);

                double f = (scale / oldScale) - 1;

                double dx = (event.getSceneX() - (paneMaze.getBoundsInParent().getWidth() / 2 + paneMaze.getBoundsInParent().getMinX()));
                double dy = (event.getSceneY() - (paneMaze.getBoundsInParent().getHeight() / 2 + paneMaze.getBoundsInParent().getMinY()));
                myScale.set(scale);
                if (!zOut){
                    setPivot(f*dx,f*dy );
                }
                else{
                    paneMaze.setTranslateX(paneMaze.getTranslateX()/50);
                    paneMaze.setTranslateY(paneMaze.getTranslateY()/50);
                }


                event.consume();
            }

        }

    };
    public void setPivot( double x, double y) {
        paneMaze.setTranslateX(paneMaze.getTranslateX()-x);
        paneMaze.setTranslateY(paneMaze.getTranslateY()-y);
    }

    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }

    public void openConfigurations(ActionEvent actionEvent) throws IOException {
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

    public void About(ActionEvent actionEvent) {
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
}
