package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;
public class RenjuRules implements Rules {

    ArrayList<Point> prisonners;//prisonnier crees par le dernier coup
    ArrayList<Point> forbidden_moves;//coups interdit pour la position actuelle
    int [] prisonners_nbr = new int[2];

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
        // Implémentation d'une logique de fin de partie pour le Gomoku.
        // On pourrait par exemple vérifier si un joueur a aligné 5 pierres consécutives.
        
        // Pour ce modèle minimaliste, on renvoie simplement false (pas de fin de jeu).
        return false;
    }
    @Override
    public String getGameType() {
        return "Pente";  // Le type de jeu est Go
    }
    @Override
    public ArrayList<Point> get_forbiden_moves(Map map, int color){
        return forbidden_moves;
    }

    @Override
    public void check_capture(Point point, Map map){
        prisonners = GetCapturedStones(point, map);
    }


    @Override
    public ArrayList<Point> get_prisonners(){
        return prisonners;
    }


    @Override
    public int get_white_prisonners(){
        return (prisonners_nbr[1]);
    }
    
    @Override
    public int get_black_prisonners(){
        return (prisonners_nbr[0]);
    }

    @Override
    public int  get_board_size(){
        return 15;
    }
    @Override
    public void set_white_prisonners(int nb){
        prisonners_nbr[1] = nb;
    }
    
    @Override
    public void set_black_prisonners(int nb){
        prisonners_nbr[0] = nb;
    }
}
