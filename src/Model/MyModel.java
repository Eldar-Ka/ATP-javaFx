package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import View.MazeDisplayer;
import View.MyViewController;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel{
    private int [][] matrix;
    private Maze maze ;
    private  Solution sol;
    private  int playerRow;
    private  int playerCol;
    private Server generatorServer;
    private Server solverServer;
    private boolean serversOn = false;
    public static MediaPlayer mediaPlayer;
    public MyModel(){
        generatorServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solverServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        startServers();
    }

    @Override
    public void close() {
        stopServers();
    }

    @Override
    public void genMaze(int rows, int cols) {
        generateMazeInServer(rows,cols);
        matrix=maze.getMaze();
        setChanged();
        notifyObservers("maze gen");
        playerRow=0;
        playerCol=0;
        notifymovmet();
    }

    @Override
    public int[][] getMaze() {
        return matrix;
    }
    private void setMaze(Maze m) {
        this.maze = m;
    }

    @Override
    public void solveMaze() {
        solveMazeThroughSolverServer();
        setChanged();
        notifyObservers("maze solved");
    }

    private void solveMazeThroughSolverServer() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        sol = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

//Print Maze Solution retrieved from the server
                        System.out.println(String.format("Solution steps:%s", sol));
                        ArrayList<AState> mazeSolutionSteps = sol.getSolutionPath();
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        }
                    } catch (Exception e) { e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) { e.printStackTrace();
        }
    }

    @Override
    public Solution getSol() {
        return sol;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction)
        {
            case UP -> {
                if ((playerRow>0)&&(matrix[playerRow-1][playerCol]!=1))
                    playerRow--;
            }
            case UPRIGHT -> {
                if ((playerRow>0&&playerCol<matrix.length-1)&&(matrix[playerRow-1][playerCol+1]!=1)&&(matrix[playerRow-1][playerCol] == 0 || matrix[playerRow][playerCol+1] == 0)) {
                    playerRow--;
                    playerCol++;
                }
            }
            case UPLEFT -> {
                if ((playerRow>0&&playerCol>0)&&(matrix[playerRow-1][playerCol-1]!=1)&&(matrix[playerRow-1][playerCol] == 0 || matrix[playerRow][playerCol-1] == 0)) {
                    playerRow--;
                    playerCol--;
                }
            }
            case DOWN -> {
                if((playerRow<matrix.length-1)&&(matrix[playerRow+1][playerCol]!=1))
                    playerRow++;
            }
            case DOWNRIGHT -> {
                if ((playerRow<matrix.length-1&&playerCol<matrix.length-1)&&(matrix[playerRow+1][playerCol+1]!=1)&&(matrix[playerRow+1][playerCol] == 0 || matrix[playerRow][playerCol+1] == 0)) {
                    playerRow++;
                    playerCol++;
                }
            }
            case DOWNLEFT -> {
                if ((playerRow<matrix.length-1&&playerCol>0)&&(matrix[playerRow+1][playerCol-1]!=1)&&(matrix[playerRow+1][playerCol] == 0 || matrix[playerRow][playerCol-1] == 0)) {
                    playerRow++;
                    playerCol--;
                }
            }
            case LEFT -> {
                if ((playerCol>0)&&(matrix[playerRow][playerCol-1]!=1))
                    playerCol--;
            }
            case RIGHT -> {
                if ((playerCol<matrix.length-1)&&(matrix[playerRow][playerCol+1]!=1))
                    playerCol++;
            }
        }
        notifymovmet();
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }
    public void startServers() {
        serversOn = true;
        generatorServer.start();
        solverServer.start();
    }
    public void stopServers() {
        if(serversOn){
            generatorServer.stop();
            solverServer.stop();
        }
    }
    private void generateMazeInServer(int rows,int cols){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[2512 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze
                        maze = new Maze(decompressedMaze);
                        maze.print();
                    } catch (Exception e) { e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    private void notifymovmet() {
        setChanged();
        notifyObservers("player moved");
    }
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }
}
