package org.interfacegui;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import org.utils.Point;

public class GameScene{
    private Pane scene;


    public GameScene(int size){};
    public void remove_stone(int x, int y){};
    public void remove_stones(ArrayList<Point> stones){};
    public void addStones(int x, int y, Color color){};
    public void updateScene(int new_size){
    };
    public Pane getScene()
    {
        return scene;
    }
}