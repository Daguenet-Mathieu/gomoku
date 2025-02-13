package org.openjfx;
import java.util.ArrayList;


public interface Rules {
    boolean isValidMove(Point point, ArrayList<Map> map);
    boolean endGame(Map map);
    String getGameType();
    ArrayList<Point> get_forbiden_moves();
    ArrayList<Point> get_prisonners();
    // Méthode par défaut qui vérifie si le coup est valide (si la case est vide)
    default boolean checkEmptySqure(int x, int y, Map map) {
        if (map.get_map()[y][x] != 0) {
            System.out.println("Le coup ne peut pas être joué ici, la case est déjà occupée.");
            return false;
        }
        return true;
    }
}
