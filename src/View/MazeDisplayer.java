package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    private int playerRow=0;
    private int playerCol=0;
    private Solution sol;
    private Maze m;
    private  int[][] matrix;
    private StringProperty imageFileNameWall = new SimpleStringProperty();
    private StringProperty imageFileNamePlayer = new SimpleStringProperty();
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();
    private Image solutionPathImage;
    private Image wallImage;
    private Image playerImage;

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }
    public int getPlayerRow() {
        return playerRow;
    }
    public int getPlayerCol() {
        return playerCol;
    }
    public void setPlayerPos(int playerRow, int playerCol) {
        this.playerRow = playerRow;
        this.playerCol=playerCol;
        draw();
    }
    public String getImgFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String getImgFileNameWall() {
        return imageFileNameWall.get();
    }
    public void drawMaze(int[][] maze) throws Exception {
        this.matrix =maze;
        m=new Maze(2,2);
        m.setMaze(maze);
        draw();
    }
    public void setSol(Solution sol) {
        this.sol=sol;
        draw();
    }
    private void draw() {
        if(matrix!=null){
            double canvasH=getHeight();
            double canvasW=getWidth();
            int rows= matrix.length;
            int cols=matrix[0].length;
            double cellH=canvasH/rows;
            double cellW=canvasW/cols;
            GraphicsContext graphicsContext=getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasW,canvasH);
            drawMazeWalls(graphicsContext,rows,cols,cellH,cellW);
            if (sol!=null)
                drawSol(graphicsContext,cellH,cellW,sol);
            drawMazePlayer(graphicsContext,cellH,cellW);

        }
    }
    private void drawMazeWalls(GraphicsContext graphicsContext, int rows, int cols, double cellH, double cellW) {
        int i=0,j=0;
        Image wallImg=null;
        try {
            wallImg=new Image(new FileInputStream(getImgFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("file missing");
        }
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(0,0, cellW,cellH);
        for ( i = 0; i < rows; i++) {
            for ( j = 0; j < cols; j++) {
                if (matrix[i][j]==1){
                    double x= j*cellW;
                    double y=i*cellH;
                    if(wallImg==null)
                        graphicsContext.fillRect(x,y, cellW,cellH);
                    else
                        graphicsContext.drawImage(wallImg,x,y,cellW,cellH);
                }
            }
        }
        graphicsContext.fillRect((i-1)*cellW,(j-1)*cellH, cellW,cellH);

    }

    private void drawSol(GraphicsContext graphicsContext, double cellH, double cellW,Solution solution) {
        try {
            double x ;
            double y ;
            graphicsContext.setFill(Color.BLUE);
            for (AState s:solution.getSolutionPath()) {
                x=((MazeState)s).getPos().getRowIndex()*cellH;
                y=((MazeState)s).getPos().getColumnIndex()*cellW;
                graphicsContext.fillRect(x,y, cellW,cellH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sol = null;
    }


    private void drawMazePlayer(GraphicsContext graphicsContext, double cellH, double cellW) {
        Image playerImg=null;
        try {
            playerImg=new Image(new FileInputStream(getImgFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("file missing");
        }
        double x = getPlayerCol()*cellW;
        double y = getPlayerRow()*cellH;
        graphicsContext.setFill(Color.GREEN);
        if(playerImg==null)
            graphicsContext.fillRect(x,y, cellW,cellH);
        else
            graphicsContext.drawImage(playerImg,x,y,cellW,cellH);


    }

}
