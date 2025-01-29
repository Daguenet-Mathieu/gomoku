package org.openjfx;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class Map{
    private int _map[][];
    private int _white_prisonners;
    private int _black_prisonners;

    public Map(int size) {
        _map = new int[size][size];
        _white_prisonners = 0;
        _black_prisonners = 0;
    }

    public Map(Map other) {
        _map = new int[other.getSize()][other.getSize()];
        for (int i = 0; i < _map.length; i++) {
            System.arraycopy(other._map[i], 0, _map[i], 0, _map[i].length);
        }
        _white_prisonners = other.getWhitePrisonners();
        _black_prisonners = other.getBlackPrisonners();
    }

    public int getSize() {
        return _map.length;
    }    

    public void setCase(Point coordinates, int color){
        _map[coordinates.y][coordinates.x] = color;
    }

    public int[][] get_map()
    {
        return _map;
    }
    public void setWhitePrisonners(int nb){
        _white_prisonners += nb;
    }
    public void setBlackPrisonners(int nb){
        _black_prisonners += nb;
    }
    public int getWhitePrisonners(){
        return _white_prisonners;
    }
    public int getBlackPrisonners(){
        return _black_prisonners;
    }
}