package org.interfacegui;
import java.util.ArrayList;
import org.utils.*;

public class PenteRules implements Rules {

    ArrayList<Point> prisonners;//prisonnier crees par le dernier coup
    ArrayList<Point> forbidden_moves = new ArrayList<Point>();//coups interdit pour la position actuelle
    int [] prisonners_nbr = new int[2];
    public DoubleFree dbfree = new DoubleFree();

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
        System.out.println("check end curretn nbr prisonner == " + prisonners_nbr[map.get_map()[point.y][point.x] - 1]);
        if (check_five(map, point) || prisonners_nbr[0] >= 10 || prisonners_nbr[1] >= 10)
            return true;
        return false;
    }
    
    @Override
    public String getGameType() {
        return "Pente";
    }

    @Override
    public ArrayList<Point> get_forbiden_moves(Map map, int color)
    {
        int[][] m = map.get_map();
        forbidden_moves.clear();
        map.printMap();
        System.out.println("pour x == 8 et y == 10 " + m[10][8] + " et pour x == 10 et y == 8 " + m[8][10]);
        for (Point point : forbidden_moves)
            System.out.println("apres cleat forbidden moves == " + forbidden_moves);
        for (int y = 0; y < get_board_size();y++)
        {
            for (int x = 0; x < get_board_size(); x++){
                if (y == 10 && x == 8)
                    System.out.println("puor la coordonnee qui nous interesse avanr le continue");
                if (m[y][x] != 0)
                    continue ;
                boolean res = this.dbfree.check_double_free(y, x, color, m);
                if (y == 10 && x == 8)
                    System.out.println("puor la coordonnee qui nous interesse res : " + res);
                if (res == false)
                {
                    System.out.println("chew moi x == " + y + " y == " + x);
                }
                //System.out.println("on check la color : " + color + "x  == " + col + " y == " + y + " res fct double == " + this.dbfree.check_double_free(y, col, color,  m) + " res == " + res);
                if (res == false){
                    forbidden_moves.add(new Point (x, y));
                }
            }
        }
        for (Point point : forbidden_moves)
            System.out.println("avant envoi forbidden moves == " + point);
        return forbidden_moves;
    }

    @Override
    public void check_capture(Point point, Map map){
        int capture_color;
        prisonners = GetCapturedStones(point, map);
        if (prisonners.size() != 0)
        {
            capture_color = map.get_map()[point.y][point.x];
            // System.out.printf("color captured %d\n", capture_color);
            prisonners_nbr[capture_color - 1] += prisonners.size(); //to_debug
            // System.out.println("adding prisonner nbr == " + prisonners.size());
            // System.out.println("prisonner nbr == " + prisonners_nbr[capture_color - 1]);
        }

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
        return 19;
    }

    @Override
    public void set_black_prisonners(int nb){
        prisonners_nbr[0] = nb;
    }

    @Override
    public void set_white_prisonners(int nb){
        prisonners_nbr[1] = nb;
    }
    
}
