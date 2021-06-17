package View;

import java.io.IOException;

public interface IView  {
    void loadMaze(String path);
    void saveGame()throws IOException;
}
