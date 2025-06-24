package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;

public interface Rules {
    boolean isValidMove(Point point, ArrayList<Map> map);
    boolean endGame(Map map, Point point);
    String getGameType();
    // ArrayList<Point> get_forbiden_moves();
    ArrayList<Point> get_forbiden_moves(Map map, int color);
    void check_capture(Point point, Map map);
    ArrayList<Point> get_prisonners();
    int get_white_prisonners();
    int get_black_prisonners();
    void set_white_prisonners(int nb);
    void set_black_prisonners(int nb);
    boolean areCapturable(ArrayList<Point> points, Map map, final int color);
    int getWinner();
    void setWinner(int w);

    int get_board_size();

    ArrayList<Point> verticalWin = new ArrayList<Point>();
    ArrayList<Point> horizontalWin = new ArrayList<Point>();
    ArrayList<Point> diagonalLeftWin = new ArrayList<Point>();
    ArrayList<Point> diagonalRightWin = new ArrayList<Point>();
 
    // ArrayList check_captures(Map map, Point point);
    // Méthode par défaut qui vérifie si le coup est valide (si la case est vide)

    // default int get_block_size(Point point Map map, int x_dir, int y_dir, int color){
    //     int size = 0;
    //     int current_color;
    //     final int[][] map = map.get_map();
    //     int i = 0;
    //     while (1) {
    //         if (point.y + (i * y_dir) < 0 || point.x + (i * x_dir) < 0 || point.x + (i * x_dir) > 19 || point.x + (i * x_dir) > 19)//remplacer par sizzemap
    //             break;
    //         current_color = map[(point.y + (i * y_dir))][(point.x + (i * x_dir))]
    //         if current_color == color
    //             size += 1;
    //         else
    //             break ;
    //     }
    //     return size;
    // }

    // default int checkFreeThree(Point point, Map map, int x_dir, int y_dir, int color){
    //     int leftBlock = get_block_size(point, map, x_dir, y_dir, color);
    //     int rightBlock = get_block_size(point, map, -x_dir, -y_dir, color);
    //     int blockSize = 1 + leftBlock + rightBlock;
    //     final int[][] map = map.get_map();
    //     System.out.println("right block == " + rightBlock + " left block == " + leftBlock);
    //     if (map[point.y + (rightBlock * -y_dir)][point.x + (rightBlock * -x_dir)] != 0 || map[point.y + (rightBlock * -y_dir)][point.x + (rightBlock * -x_dir)] != 0)
    //         return 0;

    //     if (blockSize > 3)
    //         return 0;
    //     if (blockSize == 3)
    //         return 1;
    //     int nextRightBlock = get_block_size(new Point(point.x + ((rightBlock + 1) * -x_dir), ((rightBlock + 1) * -y_dir)), map, x_dir, y_dir, color);
    //     int nextLeftBlock = get_block_size(new Point(point.x + ((leftBlock + 1) * x_dir), ((leftBlock + 1) * y_dir)), map, x_dir, y_dir, color);
    //     System.out.println("next right block == " + nextRightBlock + " nezt left block == " + nextLeftBlock);
    //     int free_nbr = 0;
    //     if (rightBlock + nextRightBlock > 3 )

    // }

    // default int verticalThreeCheck(Point point, Map map, int color){
    //     int free_nbr = 0;

    //     free_3_nbr += checkFreeThree();
    //     return free_nbr;
    // }

    // default int freeThreeCreation(Point coord, Map map, int color){
    //     int free_3_nbr = 0;
    //     //check current size block all driection si ce block <= 3 check au dela 1 espace si block qui cree cumules cres un 3 et aucune pierre au contact ni du point ni des 2 blocks fct generaliste et call avec les dirs? //4 fct distonctes?  //taille 1er block commence a 1 puisque point futur coup
        
    //     return free_3_nbr;
    // }

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
        //check avant
        checkCaptures(coord, map, 1, 0, captured);
        checkCaptures(coord, map, -1, 0, captured);
        //chack apres
    }

    default void verticalCaptures(Point coord, Map map, ArrayList<Point> captured){
        //check avant
        checkCaptures(coord, map, 0, -1, captured);
        checkCaptures(coord, map, 0, 1, captured);

        //chack apres
    }
    default void diagonalLeftCaptures(Point coord, Map map, ArrayList<Point> captured){
        //check avant
        checkCaptures(coord, map, 1, 1, captured);
        checkCaptures(coord, map, -1, -1, captured);

        //chack apres
    }
    default void diagonalRightCaptures(Point coord, Map map, ArrayList<Point> captured){
        //check avant
        checkCaptures(coord, map, -1, 1, captured);
        checkCaptures(coord, map, 1, -1, captured);

        //chack apres
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
            System.out.println("out of range.");
            return false;

        }
        if (map.get_map()[y][x] != 0) {
            System.out.println("Le coup ne peut pas être joué ici, la case est déjà occupée.");
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
        for (int i = 1; count < 5 && (right == true || left == true); i++)
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

//part de la gauche et va vers la droite    
    default boolean check_diagonal_left(Map map, Point point){
        boolean right = true;
        boolean left = true;
        int count = 1;
        final int color = map.get_map()[point.y][point.x];
        if (color == 0)
            return false;
        diagonalLeftWin.add(point);
        for (int i = 1; count < 5 && (right == true || left == true); i++)
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

    //part de la droite et va vers la gauche
    default boolean check_diagonal_right(Map map, Point point){
        boolean right = true;
        boolean left = true;
        int count = 1;
        final int color = map.get_map()[point.y][point.x];
        if (color == 0)
            return false;
        diagonalRightWin.add(point);
        for (int i = 1; count < 5 && (right == true || left == true); i++)
        {
            if (right && point.x + i < map.getSize() && point.y - i >= 0 && check_with_dir(map, point, i, -i, color)){
                count += 1;
                System.out.println("J'add a diag r");
                diagonalRightWin.add(new Point(point.x + i, point.y - i));
            }
            else{
                right = false;
            }
            if (left && point.y + i < map.getSize() && point.x - i >= 0 && check_with_dir(map, point, -i, i, color)){
                count += 1;
                System.out.println("J'add a diag r");
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


    default void check_diagonal(Map map, Point point){
        check_diagonal_left(map, point);
        check_diagonal_right(map, point);    
    }

    default boolean check_five(Map map, Point point){
        System.out.println("square state == " + map.get_map()[point.y][point.x]);
        if (map.get_map()[point.y][point.x] == 0)
            return false;
        verticalWin.clear();
        horizontalWin.clear();
        diagonalLeftWin.clear();
        diagonalRightWin.clear();
        check_horizontal(map, point);
        check_vertical(map, point);
        check_diagonal(map, point);
        System.out.println("dans rules vertical == " + verticalWin.size() + " horizontal == " + horizontalWin.size() + " diagonale left  == " + diagonalLeftWin.size()  + " dagonale right == " + diagonalRightWin.size());
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
