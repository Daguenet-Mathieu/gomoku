package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;

public class PenteRules implements Rules {

    ArrayList<Point> prisonners;//prisonnier crees par le dernier coup
    ArrayList<Point> forbidden_moves;//coups interdit pour la position actuelle
    int [] prisonners_nbr = new int[2];

    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        // Utilisation de la méthode par défaut pour vérifier si la case est vide
        if (!checkEmptySqure(point.x, point.y, map.get(map.size() - 1))) {
            return false;
        }
        //check les doubles free three
        // prisonners = GetCapturedStones(point, map.get(map.size() - 1));
        // Ajouter d'autres vérifications spécifiques au jeu Gomoku, si nécessaire.
        // Par exemple, vérifier si le coup respecte la taille du plateau ou les autres règles du Gomoku.
        // Pour un modèle minimaliste, supposons que tout coup est valide si la case est vide.
        return true;
    }

    @Override
    public boolean endGame(Map map, Point point) {
        System.out.println("check end curretn nbr prisonner == " + prisonners_nbr);
        if (check_five(map, point) || prisonners_nbr[0] >= 10 || prisonners_nbr[1] >= 10)
            return true;
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
    public void check_capture(Point point, Map map){
        int capture_color;
        prisonners = GetCapturedStones(point, map);
        if (prisonners.size() != 0)
        {
            capture_color = map.get_map()[point.x][point.y];
            prisonners_nbr[capture_color] += prisonners.size();
            System.out.println("adding prisonner nbr == " + prisonners.size());
            System.out.println("prisonner nbr == " + prisonners_nbr);
        }

    }

    @Override
    public ArrayList<Point> get_prisonners(){
        return prisonners;
    }


}
