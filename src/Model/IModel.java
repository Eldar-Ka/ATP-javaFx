package Model;

import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {
    void genMaze(int rows, int cols);
    int[][] getMaze();
    void solveMaze();
    Solution getSol();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
}
