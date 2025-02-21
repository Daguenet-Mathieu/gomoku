package org.openjfx;
import java.util.ArrayList;


public interface Rules {
    boolean isValidMove(Point point, ArrayList<Map> map);
    boolean endGame(Map map, Point point);
    String getGameType();
    ArrayList<Point> get_forbiden_moves();
    ArrayList<Point> get_prisonners();
    // ArrayList check_captures(Map map, Point point);
    // Méthode par défaut qui vérifie si le coup est valide (si la case est vide)
    default boolean checkEmptySqure(int x, int y, Map map) {
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

//exception 019 out of bond

    default boolean check_horizontal(Map map, Point point){
        boolean right = true;
        boolean left = true;
        int count = 1;
        final int color = map.get_map()[point.y][point.x];
        if (color == 0)
            return false;
        for (int i = 1; count < 5 && (right == true || left == true); i++)
        {
            if (right && point.x - i >= 0 && check_with_dir(map, point, -i, 0, color)){
                count += 1;
            }
            else{
                right = false;
            }
            if (left  && point.x + i < map.getSize() && check_with_dir(map, point, i, 0, color)){
                count += 1;
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
        for (int i = 1; count < 5 && (right == true || left == true); i++)
        {
            if (right && point.y - i >= 0 && check_with_dir(map, point, 0, -i, color)){
                count += 1;
            }
            else{
                right = false;
            }
            if (left  && point.y + i < map.getSize() && check_with_dir(map, point, 0, i, color)){
                count += 1;
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
        for (int i = 1; count < 5 && (right == true || left == true); i++)
        {
            if (right && point.y - i >= 0 && point.x - i >= 0 && check_with_dir(map, point, -i, -i, color)){
                count += 1;
            }
            else{
                right = false;
            }
            if (left  && point.y + i < map.getSize() && point.x + i < map.getSize() && check_with_dir(map, point, i, i, color)){
                count += 1;
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
        for (int i = 1; count < 5 && (right == true || left == true); i++)
        {
            if (right && point.x + i < map.getSize() && point.y - i >= 0 && check_with_dir(map, point, i, -i, color)){
                count += 1;
            }
            else{
                right = false;
            }
            if (left && point.y + i < map.getSize() && point.x - i >= 0 && check_with_dir(map, point, -i, i, color)){
                count += 1;
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


    default boolean check_diagonal(Map map, Point point){
        if (check_diagonal_left(map, point) || check_diagonal_right(map, point))
            return true;
        else
            return false;    
    }

    default boolean check_five(Map map, Point point){
        System.out.println("square state == " + map.get_map()[point.y][point.x]);
        if (map.get_map()[point.y][point.x] == 0)
            return false;
        if (check_horizontal(map, point) || check_vertical(map, point) || check_diagonal(map, point))
            return true;
        return false;
    }
}
