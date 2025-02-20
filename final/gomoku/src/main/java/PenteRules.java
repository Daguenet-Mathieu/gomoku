package org.openjfx;
import java.util.ArrayList;


public class PenteRules implements Rules {

    ArrayList<Point> prisonners;//prisonnier crees par le dernier coup
    ArrayList<Point> forbidden_moves;//coups interdit pour la position actuelle

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
        //5 ou plus allignes 5 paires captures 
    return false;
    }

    @Override
    public String getGameType() {
        return "Pente";
    }
    @Override
    public ArrayList<Point> get_forbiden_moves(){
        return forbidden_moves;
    }

    @Override
    public ArrayList<Point> get_prisonners(){
        return prisonners;
    }


}
