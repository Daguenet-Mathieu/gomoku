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
        // Implémentation d'une logique de fin de partie pour le Gomoku.
        // On pourrait par exemple vérifier si un joueur a aligné 5 pierres consécutives.
        
        // Pour ce modèle minimaliste, on renvoie simplement false (pas de fin de jeu).
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
    public ArrayList<Point> get_prisonners(){
        return new ArrayList<Point>();
    }
}

