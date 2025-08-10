package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;

public interface Rules {

    enum GameMode {
        PLAYING,
        DEATH_MARKING,
        COUNTING,
        ENDGAME,
        VIEWING,
        LEARNING
    }

    boolean isValidMove(Point point, ArrayList<Map> map);
    boolean endGame(Map map, Point point);
    String getGameType();
    ArrayList<Point> get_forbiden_moves(ArrayList<Map> map, int index, int color);
    void check_capture(Point point, Map map);
    ArrayList<Point> get_prisonners();
    int get_white_prisonners();
    int get_black_prisonners();
    void set_white_prisonners(int nb);
    void set_black_prisonners(int nb);
    boolean areCapturable(ArrayList<Point> points, Map map, final int color, int dir);
    int getWinner();
    void setWinner(int w);
    boolean hasIa();
    boolean hasPass();
    int get_board_size();
    GameMode getGameMode();
    void setGameMode(Rules.GameMode n);
    ArrayList<Point> verticalWin = new ArrayList<Point>();
    ArrayList<Point> horizontalWin = new ArrayList<Point>();
    ArrayList<Point> diagonalLeftWin = new ArrayList<Point>();
    ArrayList<Point> diagonalRightWin = new ArrayList<Point>();
    void  setBoardSize(int value);
    boolean undo();
 
    default void checkCaptures(Point coord, Map game_map, int inc_x, int inc_y, ArrayList<Point> captured){
        if ((coord.x + (inc_x * 3) > 18 || coord.x + (inc_x * 3) < 0) || (coord.y + (inc_y * 3) > 18 || coord.y + (inc_y * 3) < 0))
            return ;
        final int[][] map = game_map.get_map();
        final int color = map[coord.y][coord.x];
        final int color1 = map[coord.y + inc_y][coord.x + inc_x];
        final int color2 = map[coord.y + (inc_y * 2)][coord.x + (inc_x * 2)];
        final int color3 = map[coord.y + (inc_y * 3)][coord.x + (inc_x * 3)];

        if (color == 0 || color3 != color)
            return ;

        if (color1 != 0 && color1 != color && color2 != 0 && color2 != color){
            captured.add(new Point(coord.x + inc_x, coord.y + inc_y));
            captured.add(new Point(coord.x + (inc_x * 2), coord.y + (inc_y * 2)));
        }        
    }

    default void horizontalCaptures(Point coord, Map map, ArrayList<Point> captured){
        checkCaptures(coord, map, 1, 0, captured);
        checkCaptures(coord, map, -1, 0, captured);
    }

    default void verticalCaptures(Point coord, Map map, ArrayList<Point> captured){
        checkCaptures(coord, map, 0, -1, captured);
        checkCaptures(coord, map, 0, 1, captured);
    }

    default void diagonalLeftCaptures(Point coord, Map map, ArrayList<Point> captured){
        checkCaptures(coord, map, 1, 1, captured);
        checkCaptures(coord, map, -1, -1, captured);
    }

    default void diagonalRightCaptures(Point coord, Map map, ArrayList<Point> captured){
        checkCaptures(coord, map, -1, 1, captured);
        checkCaptures(coord, map, 1, -1, captured);
    }


    default ArrayList<Point> GetCapturedStones(Point coord, Map map){
        ArrayList<Point> captured = new ArrayList<Point>();

        horizontalCaptures(coord, map, captured);
        verticalCaptures(coord, map, captured);
        diagonalLeftCaptures(coord, map, captured);
        diagonalRightCaptures(coord, map, captured);
        return captured;
    }


    default boolean checkEmptySqure(int x, int y, Map map) {
        if ((x < 0 || x >= 19) || (y < 0 || y >= 19)){
            return false;

        }
        if (map.get_map()[y][x] != 0) {
            return false;
        }
        return true;
    }

    default boolean check_with_dir(Map map, Point point, int dir_x, int dir_y, int color){
        if (map.get_map()[point.y + dir_y][point.x + dir_x] == color)
            return true;
        else
            return false;
    }

    default boolean check_horizontal(Map map, Point point){
        boolean right = true;
        boolean left = true;
        int count = 1;
        final int color = map.get_map()[point.y][point.x];
        if (color == 0)
            return false;
        horizontalWin.add(point);
        for (int i = 1; (right == true || left == true); i++)
        {
            if (right && point.x - i >= 0 && check_with_dir(map, point, -i, 0, color)){
                count += 1;
                horizontalWin.add(new Point(point.x - i, point.y));
            }
            else{
                right = false;
            }
            if (left  && point.x + i < map.getSize() && check_with_dir(map, point, i, 0, color)){
                count += 1;
                horizontalWin.add(new Point(point.x + i, point.y));
            }
            else{
                left = false;
            }
        }
        if (count >= 5)
            return true;
        else 
            return false;
    }

    default boolean check_vertical(Map map, Point point){
        boolean right = true;
        boolean left = true;
        int count = 1;
        final int color = map.get_map()[point.y][point.x];
        if (color == 0)
            return false;
        verticalWin.add(point);
        for (int i = 1; (right == true || left == true); i++)
        {
            if (right && point.y - i >= 0 && check_with_dir(map, point, 0, -i, color)){
                count += 1;
                verticalWin.add(new Point(point.x, point.y - i));
            }
            else{
                right = false;
            }
            if (left  && point.y + i < map.getSize() && check_with_dir(map, point, 0, i, color)){
                count += 1;
                verticalWin.add(new Point(point.x, point.y + i));
            }
            else{
                left = false;
            }
        }
        if (count >= 5)
            return true;
        else
            return false;
    }

    default boolean check_diagonal_left(Map map, Point point){
        boolean right = true;
        boolean left = true;
        int count = 1;
        final int color = map.get_map()[point.y][point.x];
        if (color == 0)
            return false;
        diagonalLeftWin.add(point);
        for (int i = 1; (right == true || left == true); i++)
        {
            if (right && point.y - i >= 0 && point.x - i >= 0 && check_with_dir(map, point, -i, -i, color)){
                count += 1;
                diagonalLeftWin.add(new Point(point.x - i, point.y - i));
            }
            else{
                right = false;
            }
            if (left  && point.y + i < map.getSize() && point.x + i < map.getSize() && check_with_dir(map, point, i, i, color)){
                count += 1;
                diagonalLeftWin.add(new Point(point.x + i, point.y + i));
            }
            else{
                left = false;
            }
        }
        if (count >= 5)
            return true;
        else 
            return false;    
    }

    default boolean check_diagonal_right(Map map, Point point){
        boolean right = true;
        boolean left = true;
        int count = 1;
        final int color = map.get_map()[point.y][point.x];
        if (color == 0)
            return false;
        diagonalRightWin.add(point);
        for (int i = 1; (right == true || left == true); i++)
        {
            if (right && point.x + i < map.getSize() && point.y - i >= 0 && check_with_dir(map, point, i, -i, color)){
                count += 1;
                diagonalRightWin.add(new Point(point.x + i, point.y - i));
            }
            else{
                right = false;
            }
            if (left && point.y + i < map.getSize() && point.x - i >= 0 && check_with_dir(map, point, -i, i, color)){
                count += 1;
                diagonalRightWin.add(new Point(point.x - i, point.y + i));
            }
            else{
                left = false;
            }
        }
        if (count >= 5)
            return true;
        else 
            return false;    
    }


    default int getColor(Map map, Point point){
        return map.get_map()[point.y][point.x];
    }

    default void check_diagonal(Map map, Point point){
        check_diagonal_left(map, point);
        check_diagonal_right(map, point);    
    }

    default boolean check_five(Map map, Point point){
        if (map.get_map()[point.y][point.x] == 0)
            return false;
        verticalWin.clear();
        horizontalWin.clear();
        diagonalLeftWin.clear();
        diagonalRightWin.clear();
        check_horizontal(map, point);
        check_vertical(map, point);
        check_diagonal(map, point);
        if (verticalWin.size() >= 5 || horizontalWin.size() >= 5 || diagonalLeftWin.size() >= 5 || diagonalRightWin.size() >= 5)
            return true;
        return false;
    }

    default ArrayList<Point> getVerticalWin(){
        return verticalWin;
    }

    default ArrayList<Point> getHorizontalWin(){
        return horizontalWin;
    }

    default ArrayList<Point> getDiagonalLeftWin(){
        return diagonalLeftWin;
    }
    
    default ArrayList<Point> getDiagonalRightWin(){
        return diagonalRightWin;
    }

}
