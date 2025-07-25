package org.interfacegui;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import org.utils.Point;
// import javafx.beans.binding.Bindings;
// import javafx.beans.binding.DoubleBinding;
// import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//import javafx.scene.layout.CornerRadii;
//import javafx.scene.input.MouseEvent;

public class Goban{
    private Pane _goban;
    private Circle _stones[][];
    private Rectangle _score[][];
    private ArrayList<Circle> _hoshis;
    private ArrayList<Line> _lines;
    private int _size;
    private int _nb_line; 
    //private int _nb_move;
    //private int _line_size;
    private int _square_size;
    private int _heigh_margin_size;
    private int _width_margin_size;
    //private int _current_move; 

    private void init_margin_size(int heigh, int width){
        int goban_size = _square_size * (_nb_line - 1);
        _heigh_margin_size = (heigh - goban_size) / 2;
        _width_margin_size = (width - goban_size) / 2;
        // System.out.println("dif == " + (heigh - ((heigh / _nb_line) * (_nb_line - 1))));
        // System.out.println("margin size == " + _heigh_margin_size);
        // System.out.println("boban size == " + heigh + " nbline ==  " + _nb_line);
        // System.out.println("heigh : " + heigh + " width : " + width);
        // System.out.println("heigh margin = " + _heigh_margin_size + " width marginn = " + _width_margin_size);
        // System.out.println("square size == " + _square_size + " size gobsn = " + (_square_size * _nb_line));
    }
    private void init_square_size(){
        _square_size = _size / _nb_line;
        // System.out.println("boban size == " + _size + " nbline ==  " + _nb_line);
    }

    private void init_hoshi(){}

    private void init_lines(){
        for (int i = 0; i < _nb_line; i++)
        {
            // Line line = new Line(_heigh_margin_size + (_square_size * i), _heigh_margin_size, 100, _heigh_margin_size + (_square_size * _nb_line));
            // Line(double startX, double startY, double endX, double endY) 
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
            // Line line = new Line(_heigh_margin_size + (_square_size * i), _heigh_margin_size, 100, _heigh_margin_size + (_square_size * _nb_line));
            // Line(double startX, double startY, double endX, double endY) 
            Line line = new Line(
                _width_margin_size,
                _heigh_margin_size + (_square_size * i), // x1
                _width_margin_size + (_square_size * (_nb_line - 1)),
                _heigh_margin_size + (_square_size * i)  // y2 (calculer en fonction de la position de la ligne)
            );

            _lines.add(line);
        }
    }
    //private void update_stones(){}

    public Goban(int heigh, int width, int nb_line) {
        _goban = new Pane();
        // _goban.setStyle("-fx-background-color: #556B2F;");
        _goban.setBackground(new Background(new BackgroundFill(Color.web("#DEB887"), null, null)));
        _goban.setPrefSize(width, heigh);

        _size = Math.min(width, heigh);
        _nb_line = nb_line;
        //_nb_move = 0;
        //_current_move = 0;
        // _game_states.add(new Map(_size));
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
        //System.out.println("new ize x == " + new_x + " y == " + new_y);
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

            // Position sur l'intersection
            double centerX = (_square_size * j) + _width_margin_size;
            double centerY = (_square_size * i) + _heigh_margin_size;

            r.setX(centerX - size / 2.0);
            r.setY(centerY - size / 2.0);
        }
    }
}


    // private void score_responsivity(){
    //     double size = _square_size;

    //     for (int i = 0; i < _score.length; i++) {
    //         for (int j = 0; j < _score[i].length; j++) {
    //             Rectangle r = new Rectangle();

    //             r.setWidth(size);
    //             r.setHeight(size);

    //             double centerX = (_square_size * j) + _width_margin_size + (_square_size / 2.0);
    //             double centerY = (_square_size * i) + _heigh_margin_size + (_square_size / 2.0);

    //             r.setX(centerX - size / 2.0);
    //             r.setY(centerY - size / 2.0);

    //             r.setStroke(Color.TRANSPARENT);
    //             r.setFill(Color.BLUE);
    //             r.setStrokeWidth(1);
    //             r.setVisible(false);

    //             _score[i][j] = r;
    //         }
    //     }
    //         Rectangle rect = _score[0][0];
    //         System.out.println(
    //         "score respnsivity on Rectangle at (x=" + rect.getX() +
    //         ", y=" + rect.getY() +
    //         "), size (w=" + rect.getWidth() +
    //         ", h=" + rect.getHeight() + ")");


    // }

    public void init_stones(){

        System.out.println("_stones.length == " + _stones.length + " _stones[0].length == " + _stones[0].length);
        for (int i = 0; i < _stones.length; i++){
            for (int j = 0; j < _stones[i].length; j++){
                // System.out.println("set stone i == " + i + " j == " + j + " width == " + ((_square_size * j) + _width_margin_size + (_square_size/2)) + " heght == " + ((_square_size * j) + _heigh_margin_size + (_square_size/2)));
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

                // Position centrée sur l'intersection des lignes (pas décalée)
                double centerX = (_square_size * j) + _width_margin_size;
                double centerY = (_square_size * i) + _heigh_margin_size;

                r.setX(centerX - size / 2.0);
                r.setY(centerY - size / 2.0);

                r.setStroke(Color.BLACK); // Contour pour visibilité
                r.setFill(Color.BLUE);
                r.setStrokeWidth(1);
                r.setVisible(false);

                _score[i][j] = r;
            }
        }
        System.out.println("Rectangles initialisés avec positions corrigées");
    }

    // public void init_score() {
    //     double size = _square_size / 5.0;

    //     for (int i = 0; i < _score.length; i++) {
    //         for (int j = 0; j < _score[i].length; j++) {
    //             Rectangle r = new Rectangle();

    //             r.setWidth(size);
    //             r.setHeight(size);

    //             double centerX = (_square_size * j) + _width_margin_size + (_square_size / 2.0);
    //             double centerY = (_square_size * i) + _heigh_margin_size + (_square_size / 2.0);

    //             r.setX(centerX - size / 2.0);
    //             r.setY(centerY - size / 2.0);

    //             r.setStroke(Color.TRANSPARENT);
    //             r.setFill(Color.BLUE);
    //             r.setStrokeWidth(1);
    //             r.setVisible(false);

    //             _score[i][j] = r;
    //         }
    //     }

        // Affichage debug
    //     Rectangle rect = _score[0][0];
    //     System.out.println(
    //         "score init on Rectangle at (x=" + rect.getX() +
    //         ", y=" + rect.getY() +
    //         "), size (w=" + rect.getWidth() +
    //         ", h=" + rect.getHeight() + ")"
    //     );
    // }



    // public void init_score(){

    //     // System.out.println("_stones.length == " + _stones.length + " _stones[0].length == " + _stones[0].length);
    //     for (int i = 0; i < _score.length; i++){
    //         for (int j = 0; j < _score[i].length; j++){
    //             // System.out.println("set stone i == " + i + " j == " + j + " width == " + ((_square_size * j) + _width_margin_size + (_square_size/2)) + " heght == " + ((_square_size * j) + _heigh_margin_size + (_square_size/2)));
    //             _score[i][j] = new Rectangle();
    //             _score[i][j].setX(_square_size/5);
    //             _score[i][j].setY(_square_size/5);
    //             // _score[i][j].setRadius(_square_size/2);
    //             _score[i][j].setX((_square_size * j) + _width_margin_size / 5);
    //             _score[i][j].setY((_square_size * i) + _heigh_margin_size / 5);
    //             _score[i][j].setStroke(Color.TRANSPARENT);
    //             _score[i][j].setFill(Color.BLUE); 
    //             _score[i][j].setStrokeWidth(1);
    //             _score[i][j].setVisible(false);
    //         }
    //         Rectangle rect = _score[0][0];
    //         System.out.println(
    //         "score init on Rectangle at (x=" + rect.getX() +
    //         ", y=" + rect.getY() +
    //         "), size (w=" + rect.getWidth() +
    //         ", h=" + rect.getHeight() + ")"
    //     );

    //     }
    // }

    public void remove_score(){
        for (int i = 0; i < _score.length; i++) {
            for (int j = 0; j < _score[i].length; j++) {
                _score[i][j].setVisible(false);
            }
        }
    }

    // public void modify_score(Point coordinates, Color color) {
    //     _score[coordinates.y][coordinates.x].setVisible(true);
    //     _score[coordinates.y][coordinates.x].setFill(color);
    // }

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
        // System.out.println("score len " + _score.length + " score len 2 " + _score[0].length);
        for (int i = 0; i < _score.length; i++){
            for (int j = 0; j < _score[i].length; j++){
                _goban.getChildren().add(_score[i][j]);
            }
        }

        
    }
    // private void handleMouseClick(MouseEvent event) {
    //     double x = event.getX();
    //     double y = event.getY();
    //     System.out.println("Pane cliqué aux coordonnées : (" + x + ", " + y + ")");
    // }
    // _goban.setOnMouseClicked(this::handleMouseClick);

    // public void set_stone_status(boolean visible, String color, Point coord, String text) {
    //     Circle stone = _stones[coord.y][coord.x];
    //     stone.setVisible(visible);
        
    //     if (visible) {
    //         stone.setFill(javafx.scene.paint.Color.web(color));
            
    //         // Créer le texte
    //         Text label = new Text(text);
    //         label.setFill(javafx.scene.paint.Color.RED);
            
    //         // Lier la taille de la police au rayon du cercle
    //         DoubleBinding fontSizeBinding = stone.radiusProperty().multiply(0.7);
    //         label.fontProperty().bind(Bindings.createObjectBinding(() -> 
    //             new Font(fontSizeBinding.get()), fontSizeBinding));
            
    //         // Lier la position du texte au centre du cercle
    //         label.xProperty().bind(stone.centerXProperty().subtract(
    //             Bindings.createDoubleBinding(() -> 
    //                 label.getBoundsInLocal().getWidth() / 2, 
    //                 label.boundsInLocalProperty())));
                    
    //         label.yProperty().bind(stone.centerYProperty().add(
    //             Bindings.createDoubleBinding(() -> 
    //                 label.getBoundsInLocal().getHeight() / 4, 
    //                 label.boundsInLocalProperty())));
            
    //         // Ajouter le texte à la scène
    //         Pane parent = (Pane) stone.getParent();
    //         parent.getChildren().add(label);
    //     }
    // }


    // public void set_stone_status(boolean visible, String color, Point coord, String text) {
    //     Circle stone = _stones[coord.y][coord.x];
    //     stone.setVisible(visible);
        
    //     if (visible) {
    //         stone.setFill(javafx.scene.paint.Color.web(color));
            
    //         // Créer le texte
    //         Text label = new Text(text);
    //         label.setFill(javafx.scene.paint.Color.RED);
            
    //         // Définir la taille de la police initiale
    //         double fontSize = stone.getRadius() * 0.7;
    //         label.setFont(new Font(fontSize));
            
    //         // Ajouter le texte à la scène d'abord
    //         Pane parent = (Pane) stone.getParent();
    //         parent.getChildren().add(label);
            
    //         // Fonction pour mettre à jour la position du texte
    //         Runnable updateTextPosition = () -> {
    //             double width = label.getBoundsInLocal().getWidth();
    //             double height = label.getBoundsInLocal().getHeight();
    //             label.setX(stone.getCenterX() - width / 2);
    //             label.setY(stone.getCenterY() + height / 4);
    //         };
            
    //         // Positionner initialement
    //         updateTextPosition.run();
            
    //         // Configurer les listeners
    //         stone.radiusProperty().addListener((obs, oldVal, newVal) -> {
    //             label.setFont(new Font(newVal.doubleValue() * 0.7));
    //             updateTextPosition.run();
    //         });
            
    //         stone.centerXProperty().addListener((obs, oldVal, newVal) -> updateTextPosition.run());
    //         stone.centerYProperty().addListener((obs, oldVal, newVal) -> updateTextPosition.run());
    //     }
    // }


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