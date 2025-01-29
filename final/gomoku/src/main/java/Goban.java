package org.openjfx;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class Goban{
    private Pane _goban;
    private Circle _stones[][];
    private ArrayList<Circle> _hoshis;
    private ArrayList<Line> _lines;
    private int _size;
    private int _nb_line; 
    private int _nb_move;
    private int _line_size;
    private int _square_size;
    private int _margin_size;
    private int _current_move; 
    private ArrayList<Map> _game_states;

    private get_margin_size(){
        _margin_size = _size % _nb_line;
        System.out.println("margin size == " + _margin_size);
    };
    private get_square_size(){
        _square_size = _size / _nb_line;
        System.out.println("square size == " + _square_size);

    };
    private init_hoshi(){};
    private init_line(){};
    private update_stones(){};
    private build_goban(){};

    public Goban(int goban_size, int nb_line) {
        _goban = new Pane();
        _size = goban_size;
        _nb_line = nb_line;
        _nb_move = 0;
        _current_move = 0;
        _game_states = new ArrayList<Map>();
        _game_states.add(new Map(_size));
        _stones = new Circle[_size][_size];

        _hoshis = new ArrayList<Circle>();
        _lines = new ArrayList<Line>();
        _margin_size = get_margin_size();
        _square_size = get_square_size();
        init_stones();
        init_lines();
        init_hoshi();
        build_goban();
    }
    
    public Pane get_goban(){
        return (_goban);
    }
    void updateGoban(int new size){

    };
    public void init_stones(){

    };
    
    public void remove_stones(ArrayList<Point> stones){};
    
    public void remove_stone(Point coordinates) {
        _stones[coordinates.y][coordinates.x].setVisible(false);
    }

    public void modify_stone(Point coordinates, Color color) {
        _stones[coordinates.y][coordinates.x].setVisible(true);
        _stones[coordinates.y][coordinates.x].setFill(color);
    }

    public void navigate_in_game(int sens){};

    private void build_goban() {
        _goban.getChildren().addAll(_lines);
        _goban.getChildren().addAll(_hoshis);
        ArrayList<Circle> allStones = new ArrayList<Circle>();
        for (Circle[] row : _stones) {
            allStones.addAll(allStones);
        }
    }
}