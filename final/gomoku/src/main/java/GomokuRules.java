package org.openjfx;
import java.util.ArrayList;

public class GomokuRules implements Rules {
    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        // Utilisation de la méthode par défaut pour vérifier si la case est vide
        if (!checkEmptySqure(point.x, point.y, map.get(map.size() - 1))) {
            return false;
        }
        
        // Ajouter d'autres vérifications spécifiques au jeu Gomoku, si nécessaire.
        // Par exemple, vérifier si le coup respecte la taille du plateau ou les autres règles du Gomoku.
        // Pour un modèle minimaliste, supposons que tout coup est valide si la case est vide.
        return true;
    }

    @Override
    public boolean endGame(Map map, Point point) {
        if (check_five(map, point))
            return true;
        return false;
    }
    
    @Override
    public String getGameType() {
        return "Gomoku";
    }
    @Override
    public ArrayList<Point> get_forbiden_moves(){
        return new ArrayList<Point>();
    }

    @Override
    public void check_capture(Point point, Map map){
        // nothing to do
        return ;
    }


    @Override
    public ArrayList<Point> get_prisonners(){
        return new ArrayList<Point>();
    }
}

