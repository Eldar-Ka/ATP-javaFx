package View;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MazeDisplayer extends Canvas {
    private int playerRow=0;
    private int playerCol=0;
    private int prevPlayerCol=0;
    private String prevPlayerDirection=null;
    private Solution sol=null;
    private Maze m;
    private  int[][] matrix;
    private StringProperty imageFileNameWall = new SimpleStringProperty();
    private StringProperty imageFileNamePlayer = new SimpleStringProperty();
    private StringProperty imageFileNamePlayerL = new SimpleStringProperty();
    private StringProperty imageFileNameWin = new SimpleStringProperty();


    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String getImageFileNameWin() {
        return imageFileNameWin.get();
    }
    public void setImageFileNameWin(String imageFileNameWin) {
        this.imageFileNameWin.set(imageFileNameWin);
    }

    public String getImageFileNamePlayerL() {
        return imageFileNamePlayerL.get();
    }

    public void setImageFileNamePlayerL(String imageFileNamePlayerL) {
        this.imageFileNamePlayerL.set(imageFileNamePlayerL);
    }

    public int[][] getMatrix() {
        return matrix;
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
        m = new Maze(2,2);
        m.setMaze(maze);
        m.setColumns(maze[0].length);
        m.setRows(maze.length);
        Position p = new Position(maze.length, maze[0].length);
        m.setGoalPosition(p);
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

            if (sol!=null)
                drawSol(graphicsContext, cellH, cellW);
            else
                drawMazeWalls(graphicsContext,rows,cols,cellH,cellW);

            if (getPlayerRow()==matrix.length-1&&getPlayerCol()==matrix[0].length-1) {
                drawMazeWin(graphicsContext);
                setDisable(true);
            }


            if (prevPlayerDirection == null || prevPlayerCol<getPlayerCol()) {
                prevPlayerCol=getPlayerCol();
                prevPlayerDirection = getImageFileNamePlayer();
                //drawMazePlayer(graphicsContext, cellH, cellW, );
            }
            if (prevPlayerCol>getPlayerCol()) {
                prevPlayerCol = getPlayerCol();
                prevPlayerDirection = getImageFileNamePlayerL();
                //drawMazePlayer(graphicsContext, cellH, cellW, getImageFileNamePlayerL());
            }
            drawMazePlayer(graphicsContext, cellH, cellW, prevPlayerDirection);

        }
    }
    private void drawMazeWalls(GraphicsContext graphicsContext, int rows, int cols, double cellH, double cellW) {
        int i,j=0;
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

    private void drawSol(GraphicsContext graphicsContext, double cellH, double cellW) {
        int i,j=0;
        int[][] mat = m.getMaze();
        ArrayList<AState> path = sol.getSolutionPath();
        Image wallImg=null;
        try {
            wallImg=new Image(new FileInputStream(getImgFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("file missing");
        }
        graphicsContext.setFill(Color.BLUE);

        for ( i = 0; i < mat.length; i++) {
            for ( j = 0; j < mat[i].length; j++) {
                //AState p = new MazeState(null, m, new Position(i, j));
                double x= j*cellW;
                double y=i*cellH;
                if (matrix[i][j]==1){
                    if(wallImg==null)
                        graphicsContext.fillRect(x,y, cellW,cellH);
                    else
                        graphicsContext.drawImage(wallImg,x,y,cellW,cellH);
                }
                for (AState aState : path) {
                    if (aState.toString().equals("{" + i + "," + j + "}"))
                        graphicsContext.fillRect(x, y, cellW, cellH);
                }
            }
        }
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(0,0, cellW,cellH);
        graphicsContext.fillRect((i-1)*cellW,(j-1)*cellH, cellW,cellH);
        sol = null;
    }
    private void drawMazeWin(GraphicsContext graphicsContext) {
        Image winImg=null;
        try {
            winImg=new Image(new FileInputStream(getImageFileNameWin()));
        } catch (FileNotFoundException e) {
            System.out.println("file missing");
        }
        graphicsContext.drawImage(winImg,getHeight()/4,getWidth()/4,(getHeight()/4)*3,(getWidth()/4)*3);
    }

    private void drawMazePlayer(GraphicsContext graphicsContext, double cellH, double cellW, String direction) {
        Image playerImg=null;
        try {
            playerImg=new Image(new FileInputStream(direction));
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

    public void saveMaze(){
        String mazeFileName = "savedMaze.maze";
        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(m.toByteArray());
            System.out.println(m);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMaze() throws Exception {

        String mazeFileName = "savedMaze.maze"; // delete

        byte savedMazeBytes[] = new byte[0];
        try {
            InputStream in = new MyDecompressorInputStream(new
                    FileInputStream(mazeFileName));
            savedMazeBytes = new byte[50000];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m = new Maze(savedMazeBytes);
        //drawMaze(m.getMaze());
        draw();
    }

}
