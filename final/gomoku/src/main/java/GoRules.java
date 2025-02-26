package org.openjfx;
import java.util.ArrayList;


public class GoRules implements Rules {
    ArrayList<Point> prisonners;//prisonnier crees par le dernier coup
    ArrayList<Point> forbidden_moves;//coups interdit pour la position actuelle

    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        // Utilisation de la méthode par défaut pour vérifier si la case est vide
        if (!checkEmptySqure(point.x, point.y, map.get(map.size() - 1))) {
            return false;
        }
        //check si le coup est ans la lsite des interdits
        //prisonners = getNewPrisonners();
        //generer les coups interdit danscette position forbidden_moves = get_forbiden_moves()
        // Ajouter d'autres vérifications spécifiques au jeu Gomoku, si nécessaire.
        // Par exemple, vérifier si le coup respecte la taille du plateau ou les autres règles du Gomoku.
        // Pour un modèle minimaliste, supposons que tout coup est valide si la case est vide.
        //generer la nouvelle liste de coups interdits
        return true;
    }

    @Override
    public boolean endGame(Map map, Point point) {
        // Implémentation d'une logique de fin de partie pour le Gomoku.
        // On pourrait par exemple vérifier si un joueur a aligné 5 pierres consécutives.
        
        // Pour ce modèle minimaliste, on renvoie simplement false (pas de fin de jeu).
        return false;
    }

    @Override
    public String getGameType() {
        return "Go";
    }

    @Override
    public ArrayList<Point> get_forbiden_moves(){
        return forbidden_moves;
    }

    @Override
    public void check_capture(Point point, Map map){
        // prisonners = custom captures check floodfill? si rencontre un 0 pas de caprure sinon toutes pierres de couleur != coord du point capturees
    }


    @Override
    public ArrayList<Point> get_prisonners(){
        return prisonners;
    }
}
