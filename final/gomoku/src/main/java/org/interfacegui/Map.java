package org.interfacegui;
//import javafx.scene.shape.*;
import java.util.ArrayList;
//import javafx.scene.paint.Color;
//import javafx.scene.layout.Pane;
import java.util.Arrays;
import org.utils.Point;

public class Map{
    private int _map[][];
    private int _white_prisonners;
    private int _black_prisonners;
    public Point _last_move ;
    private int _move_time;


    public Map(int size) {
        _move_time = 0;
        _map = new int[size][size];
        for (int i = 0; i < _map.length; i++) {
            for (int j = 0; j < _map.length; j++){
                _map[i][j] = 0;
            }
        }

        _white_prisonners = 0;
        _black_prisonners = 0;
    }

    public Map(Map other) {
        _move_time = 0;
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

    public int[][] get_map(){
        return _map;
    }
    // public void setWhitePrisonners(int nb){
    //     _white_prisonners += nb;
    // }
    // public void setBlackPrisonners(int nb){
    //     _black_prisonners += nb;
    // }

    public void addWhitePrisonners(){

    }

    public void addBlackPrisonners(){

    }

    public int getWhitePrisonners(){//pour  afficher les bonnes infos en cas de replay
        return _white_prisonners;
    }
    public int getBlackPrisonners(){//pour  afficher les bonnes infos en cas de replay
        return _black_prisonners;
    }

    public void addMove(Point coord, int color){
        _last_move = coord;
        if (coord != null)
            _map[coord.y][coord.x] = color;
    }

    public void remove_prisonners(ArrayList<Point> points){
        for (Point p : points) 
            _map[p.y][p.x] = 0;
    }


    public void set_move_time(int time){
        _move_time = time;
    }

    public int get_move_time(int time){
        return _move_time;
    }


    public void printMap(){
        for(int i = 0; i < _map.length; i++){
            System.out.println(Arrays.toString(_map[i]));
    }
    }
}