package org.interfacegui;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import org.utils.Point;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Goban{
    private Pane _goban;
    private Circle _stones[][];
    private Rectangle _score[][];
    private ArrayList<Circle> _hoshis;
    private ArrayList<Line> _lines;
    private int _size;
    private int _nb_line; 
    private int _square_size;
    private int _heigh_margin_size;
    private int _width_margin_size;

    private void init_margin_size(int heigh, int width){
        int goban_size = _square_size * (_nb_line - 1);
        _heigh_margin_size = (heigh - goban_size) / 2;
        _width_margin_size = (width - goban_size) / 2;
    }
    private void init_square_size(){
        _square_size = _size / _nb_line;
    }

    private void init_hoshi(){}

    private void init_lines(){
        for (int i = 0; i < _nb_line; i++)
        {
            Line line = new Line(
                _width_margin_size + (_square_size * i),
                _heigh_margin_size,
                _width_margin_size + (_square_size * i),
                _heigh_margin_size + (_square_size * (_nb_line - 1))
            );

            _lines.add(line);
        }

        for (int i = 0; i < _nb_line; i++)
        {
            Line line = new Line(
                _width_margin_size,
                _heigh_margin_size + (_square_size * i),
                _width_margin_size + (_square_size * (_nb_line - 1)),
                _heigh_margin_size + (_square_size * i)
            );

            _lines.add(line);
        }
    }

    public Goban(int heigh, int width, int nb_line) {
        _goban = new Pane();
        _goban.setBackground(new Background(new BackgroundFill(Color.web("#DEB887"), null, null)));
        _goban.setPrefSize(width, heigh);

        _size = Math.min(width, heigh);
        _nb_line = nb_line;
        _stones = new Circle[_nb_line][_nb_line];
        _score = new Rectangle[_nb_line][_nb_line];


        _hoshis = new ArrayList<Circle>();
        _lines = new ArrayList<Line>();
        init_square_size();
        init_margin_size(heigh, width);
        init_lines();
        init_hoshi();
        init_stones();
        init_score();
        build_goban();
    }
    
    public Pane get_goban(){
        return (_goban);
    }
    public void update_lines()
    {
        for (int i = 0; i < _lines.size();i++)
        {
            if (i < _nb_line)
            {
                _lines.get(i).setStartX(_width_margin_size + (_square_size * i));
                _lines.get(i).setStartY(_heigh_margin_size);
                _lines.get(i).setEndX(_width_margin_size + (_square_size * i));
                _lines.get(i).setEndY(_heigh_margin_size + (_square_size * (_nb_line - 1)));
            }
            else{
                int n = i - _nb_line;
                _lines.get(i).setStartX(_width_margin_size);
                _lines.get(i).setStartY(_heigh_margin_size + (_square_size * n));
                _lines.get(i).setEndX(_width_margin_size + (_square_size * (_nb_line - 1)));
                _lines.get(i).setEndY(_heigh_margin_size + (_square_size * n));

            }
        }

    }

    public void updateGoban(int new_y, int new_x){
        _goban.setPrefSize(new_x, new_y);
        _size = Math.min(new_x, new_y);
        init_square_size();
        init_margin_size(new_y, new_x);
        update_lines();
        stones_responsivity();
        score_responsivity();
    }

    private void stones_responsivity(){
        for (int i = 0; i < _stones.length; i++){
            for (int j = 0; j < _stones[i].length; j++){
                _stones[i][j].setRadius(_square_size/2);
                _stones[i][j].setCenterX((_square_size * j) + _width_margin_size);
                _stones[i][j].setCenterY((_square_size * i) + _heigh_margin_size);
            }
        }
    }

    private void score_responsivity(){
    double size = _square_size / 5.0;

    for (int i = 0; i < _score.length; i++) {
        for (int j = 0; j < _score[i].length; j++) {
            Rectangle r = _score[i][j];
            
            r.setWidth(size);
            r.setHeight(size);
            double centerX = (_square_size * j) + _width_margin_size;
            double centerY = (_square_size * i) + _heigh_margin_size;

            r.setX(centerX - size / 2.0);
            r.setY(centerY - size / 2.0);
        }
    }
}

    public void init_stones(){

        System.out.println("_stones.length == " + _stones.length + " _stones[0].length == " + _stones[0].length);
        for (int i = 0; i < _stones.length; i++){
            for (int j = 0; j < _stones[i].length; j++){
                _stones[i][j] = new Circle();
                _stones[i][j].setRadius(_square_size/2);
                _stones[i][j].setCenterX((_square_size * j) + _width_margin_size);
                _stones[i][j].setCenterY((_square_size * i) + _heigh_margin_size);
                _stones[i][j].setStroke(Color.TRANSPARENT);
                _stones[i][j].setFill(Color.BLUE); 
                _stones[i][j].setStrokeWidth(1);
                _stones[i][j].setVisible(false);
            }
        }
    }

    public void init_score() {
        double size = _square_size / 5.0;

        for (int i = 0; i < _score.length; i++) {
            for (int j = 0; j < _score[i].length; j++) {
                Rectangle r = new Rectangle();
                r.setWidth(size);
                r.setHeight(size);

                double centerX = (_square_size * j) + _width_margin_size;
                double centerY = (_square_size * i) + _heigh_margin_size;

                r.setX(centerX - size / 2.0);
                r.setY(centerY - size / 2.0);

                r.setStroke(Color.BLACK);
                r.setFill(Color.BLUE);
                r.setStrokeWidth(1);
                r.setVisible(false);

                _score[i][j] = r;
            }
        }
        System.out.println("Rectangles initialisés avec positions corrigées");
    }

    public void remove_score(){
        for (int i = 0; i < _score.length; i++) {
            for (int j = 0; j < _score[i].length; j++) {
                _score[i][j].setVisible(false);
            }
        }
    }


    public void modify_score(Point coordinates, Color color) {
        Rectangle rect = _score[coordinates.y][coordinates.x];
        System.out.println(
            "modify_score on Rectangle at (x=" + rect.getX() +
            ", y=" + rect.getY() +
            "), size (w=" + rect.getWidth() +
            ", h=" + rect.getHeight() + ")"
        );
        
        rect.setVisible(true);
        rect.setFill(color);
        rect.toFront();
    }



    public void remove_stones(ArrayList<Point> stones){
        for (Point coordinates : stones) {
            _stones[coordinates.y][coordinates.x].setVisible(false);
        }
    }
    
    public void remove_stone(Point coordinates) {
        _stones[coordinates.y][coordinates.x].setVisible(false);
    }

    public void modify_stone(Point coordinates, Color color) {
        _stones[coordinates.y][coordinates.x].setVisible(true);
        _stones[coordinates.y][coordinates.x].setFill(color);
    }

    public void navigate_in_game(int sens){}

    private void build_goban() {
        _goban.getChildren().addAll(_lines);
        _goban.getChildren().addAll(_hoshis);
        for (int i = 0; i < _stones.length; i++){
            for (int j = 0; j < _stones[i].length; j++){
                _goban.getChildren().add(_stones[i][j]);
            }
        }
        for (int i = 0; i < _score.length; i++){
            for (int j = 0; j < _score[i].length; j++){
                _goban.getChildren().add(_score[i][j]);
            }
        }

        
    }

    public void set_stone_status(boolean visible, String color, Point coord, String text) {
        Circle stone = _stones[coord.y][coord.x];
        stone.setVisible(visible);
        
        // Get the parent pane to handle text labels
        Pane parent = (Pane) stone.getParent();
        
        // Remove any existing text label associated with this stone
        parent.getChildren().removeIf(node -> 
            node instanceof Text && 
            node.getUserData() != null && 
            node.getUserData().equals("label_" + coord.x + "_" + coord.y));
        
        if (visible) {
            stone.setFill(javafx.scene.paint.Color.web(color));
            
            // Create the text
            Text label = new Text(text);
            label.setFill(javafx.scene.paint.Color.BLACK);
            
            // Set user data to identify this label later
            label.setUserData("label_" + coord.x + "_" + coord.y);
            
            // Set initial font size
            double fontSize = stone.getRadius() * 0.7;
            label.setFont(new Font(fontSize));
            
            // Add the text to the scene
            parent.getChildren().add(label);
            
            // Function to update text position
            Runnable updateTextPosition = () -> {
                double width = label.getBoundsInLocal().getWidth();
                double height = label.getBoundsInLocal().getHeight();
                label.setX(stone.getCenterX() - width / 2);
                label.setY(stone.getCenterY() + height / 4);
            };
            
            // Position initially
            updateTextPosition.run();
            
            // Configure listeners
            stone.radiusProperty().addListener((obs, oldVal, newVal) -> {
                label.setFont(new Font(newVal.doubleValue() * 0.7));
                updateTextPosition.run();
            });
            stone.centerXProperty().addListener((obs, oldVal, newVal) -> updateTextPosition.run());
            stone.centerYProperty().addListener((obs, oldVal, newVal) -> updateTextPosition.run());
        }
    }

    public int get_margin_width(){
        return (_width_margin_size);
    }
    public int get_margin_height(){
        return (_heigh_margin_size);
    }
    public int getSquareSize(){
        return (_square_size);
    }

    public void updateFromMap(Map gameMap) {
        int[][] board = gameMap.get_map();
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Circle stone = _stones[i][j];
                
                if (board[i][j] == 0) {
                    stone.setVisible(false);
                }
                else {
                    stone.setVisible(true);
                    if (board[i][j] == 1) {
                        stone.setFill(Color.BLACK);
                        // _stones[i][j].setStroke(Color.BLACK);
                    } else if (board[i][j] == 2) {
                        stone.setFill(Color.SNOW);
                        // _stones[i][j].setStroke(Color.SNOW);
                    }
                    else if (board[i][j] == 3){
                        stone.setFill(javafx.scene.paint.Color.web("rgba(0,0,0,0.5)"));
                    }
                    else{
                        stone.setFill(javafx.scene.paint.Color.web("rgba(255,255,255,0.5)"));
                    }
                }
            }
        }
    }
}