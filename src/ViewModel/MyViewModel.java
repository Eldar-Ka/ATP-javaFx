package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
    }
    public Solution getSol(){
        return model.getSol();
    }
    public int getPlayerRow(){
        return model.getPlayerRow();
    }
    public  int getPlayerCol(){
        return model.getPlayerCol();
    }
    public  int[][] getMaze(){
        return model.getMaze();
    }
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
    public void genMaze(int rows, int cols){
        model.genMaze(rows, cols);
    }
    public void solveMaze(){
        model.solveMaze();
    }
    public void movePlayer(KeyCode dir){
        MovementDirection direction;
        switch (dir){
            case NUMPAD8 -> direction=MovementDirection.UP;
            case NUMPAD2 -> direction=MovementDirection.DOWN;
            case NUMPAD6 -> direction=MovementDirection.RIGHT;
            case NUMPAD4 -> direction=MovementDirection.LEFT;
            case NUMPAD9 -> direction=MovementDirection.UPRIGHT;
            case NUMPAD7 -> direction=MovementDirection.UPLEFT;
            case NUMPAD1 -> direction=MovementDirection.DOWNLEFT;
            case NUMPAD3 -> direction=MovementDirection.DOWNRIGHT;
            default -> {
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    /*
    public void movebyMouss(MouseEvent mouseEvent) {
        try {
            model.getMaze();
            MovementDirection direction = null;
            int mouseX = (int) ((mouseEvent.getX())); //  / (mazeDisplayer.getWidth() / getMaze()[0].length)
            int mouseY = (int) ((mouseEvent.getY())); // / (mazeDisplayer.getHeight() / getMaze().length)
            
            if (mouseY < getPlayerRow()) {
                direction=MovementDirection.UP;
            }
            if (mouseY > getPlayerRow()) {
                direction=MovementDirection.DOWN;
            }
            if (mouseX < getPlayerCol()) {
                direction=MovementDirection.LEFT;
            }
            if (mouseX > getPlayerCol()) {
                direction=MovementDirection.RIGHT;
            }
            model.updatePlayerLocation(direction);


        } catch (NullPointerException e) {
            mouseEvent.consume();
        }
    }
    */

    public void close(){
        model.close();
    }
}
