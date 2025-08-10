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
    int player_color;
    public ArrayList<Point> _last_move = new ArrayList<Point>();
    public ArrayList<Integer> _last_move_color = new ArrayList<Integer>();
    private int _move_time;
    private ArrayList<Point> candidatsList;
    private ArrayList<Point> _prisonners = new ArrayList<Point>();
    private String _comment;

    public void setCandidatsList(ArrayList<Point> list) {
        candidatsList = list;
    }

    public int get_color(){
        return (player_color);
    }

    public void set_color(int color){
        player_color = color;
    }

    public ArrayList<Point> getCandidatsList() {
        return (candidatsList);
    }

    public Map(int size) {
        _comment = new String();
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
        _comment = new String();
        _move_time = 0;
        _map = new int[other.getSize()][other.getSize()];
        for (int i = 0; i < _map.length; i++) {
            System.arraycopy(other._map[i], 0, _map[i], 0, _map[i].length);
        }
        _white_prisonners = other.getWhitePrisonners();
        _black_prisonners = other.getBlackPrisonners();
    }

    public Map(Map other, ArrayList<Map> prisonners) {
        _comment = new String();
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

    public void addWhitePrisonners(int nb){
        _white_prisonners += nb;
    }

    public void addBlackPrisonners(int nb){
        _black_prisonners += nb;
    }

    public int getWhitePrisonners(){//pour  afficher les bonnes infos en cas de replay
        return _white_prisonners;
    }
    public int getBlackPrisonners(){//pour  afficher les bonnes infos en cas de replay
        return _black_prisonners;
    }

    public ArrayList<Point> get_prisonners(){
        return _prisonners;
    }

    public void set_prisonners(ArrayList<Point> p){
        _prisonners = p;
    }

    public boolean tryAddToMap(String cmd, Point coord){
        if (coord.y >= _map.length || coord.y >= _map.length)
            return false;
        int color = 0;
        if ("B".equals(cmd) || "AB".equals(cmd))
            color = 1;
        else if ("W".equals(cmd) || "AW".equals(cmd))
            color = 2;
        else if ("AE".equals(cmd)){
            _map[coord.y][coord.x] = 0;
            _last_move.add(coord);
            _last_move_color.add(0);
            return true;
        }
        else
            return false;
        _map[coord.y][coord.x] = color;
        _last_move.add(coord);
        _last_move_color.add(color);
        return true;
    }

    public void addMove(Point coord, int color){
        _last_move.add(coord);
        _last_move_color.add(color);
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

    static public void printMap(int[][] map ){
        for(int i = 0; i < 19; i++){
            System.out.println(Arrays.toString(map[i]));
    }
    }

    public ArrayList<Point> getLastMove(){
        return _last_move;
    }

    public ArrayList<Integer> getLastMoveColor(){
        return _last_move_color;
    }

    public String getComment(){
        return _comment;
    }

    public void setComment(String comment){
        this._comment += comment;
    }
}