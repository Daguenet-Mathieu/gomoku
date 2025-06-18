package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;

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
    public ArrayList<Point> get_forbiden_moves(Map map, int color){//degager color
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

    @Override
    public int get_white_prisonners(){
        return (0);
    }
    
    @Override
    public int get_black_prisonners(){
        return (0);
    }
    
    @Override
    public void set_black_prisonners(int nb){
        return ;
    }

    @Override
    public void set_white_prisonners(int nb){
        return ;
    }
    

    @Override
    public int  get_board_size(){
        return 19;
    }

    @Override
    public boolean areCapturable(ArrayList<Point> points, Map map){
        return false;
    }
}
