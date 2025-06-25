package org.interfacegui;
import java.util.ArrayList;
import org.utils.*;

public class PenteRules implements Rules {

    int winner = 0;
    int waitingWinner = 0;
    ArrayList<Point> prisonners;//prisonnier crees par le dernier coup
    ArrayList<Point> forbidden_moves = new ArrayList<Point>();//coups interdit pour la position actuelle
    int [] prisonners_nbr = new int[2];
    public DoubleFree dbfree = new DoubleFree();
    int advWinning = 0;//0 1 2? need check first si 5 sur le plateau ou fqarder en memoire le point?


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
        final int color = map.get_map()[point.y][point.x];
        ArrayList<Point> verticalWin = getVerticalWin();
        ArrayList<Point> horizontalWin = getHorizontalWin();
        ArrayList<Point> diagonalLeftWin = getDiagonalLeftWin();
        ArrayList<Point> diagonalRightWin = getDiagonalRightWin();
        //si waitingWinner != 0 //check si tjs 5 a la suite dans une des arrays si oui set winner return true sinon remettre wating  0 et continuer


        System.out.println("vertical == " + verticalWin.size() + " horizontal == " + horizontalWin.size() + " diagonale left  == " + diagonalLeftWin.size()  + " dagonale right == " + diagonalRightWin.size());
        System.out.println("check end curretn nbr prisonner == " + prisonners_nbr[map.get_map()[point.y][point.x] - 1]);
        
        if (prisonners_nbr[0] >= 10 || prisonners_nbr[1] >= 10){
            return true;
        }
        if (check_five(map, point))
        {
            if (verticalWin.size() >= 5 && areCapturable(verticalWin, map, color, 0) == true)
                return false;
            if (horizontalWin.size() >= 5 && areCapturable(horizontalWin, map, color, 1) == true)
                return false;
            if (diagonalLeftWin.size() >= 5 && areCapturable(diagonalLeftWin, map, color, 2) == true)
                return false;
            if (diagonalRightWin.size() >= 5 && areCapturable(diagonalRightWin, map, color, 3) == true)
                return false;
            this.winner = color;
            return true;
        }
        return false;
    }

    @Override
    public int getWinner(){
        return this.winner;
    }

    @Override
    public void setWinner(int w){
        this.winner = w;
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

    private boolean inMap(int y, int x){
        if (x < 0 || x >= get_board_size() || y < 0 || y >= get_board_size())
            return false;
        return true;
    }

    private int getColor(Point point, int incrementX, int incrementY, Map map){
        return map.get_map()[point.y + incrementY][point.x + incrementX];
    }

    private boolean checkDir(Point point, Map map, int incrementX, int incrementY, final int color){
        if (point.x + incrementX < 0 || point.y + incrementY < 0 || point.x - incrementX < 0 || point.y - incrementY < 0)
            return false;
        System.out.println("tjs en course 1");
        if (point.x + incrementX >= get_board_size() || point.y + incrementY >= get_board_size() || point.x - incrementX >= get_board_size() || point.y - incrementY >= get_board_size())
            return false;
        System.out.println("tjs en course 2");
        if (color == getColor(point, incrementX, incrementY, map) && color == getColor(point, -incrementX, -incrementY, map))
            return false;
        System.out.println("tjs en course 3");
        if (color != getColor(point, incrementX, incrementY, map) && color != getColor(point, -incrementX, -incrementY, map))
            return false;
        System.out.println("tjs en course 4");
        boolean before = false;
        Point square2;
        if (color == map.get_map()[point.y + incrementY][point.x + incrementX])
            square2 = new Point(point.x + incrementX, point.y + incrementY);
        else{
            square2 = new Point(point.x - incrementX, point.y - incrementY);
            before = true;
        }
        System.out.println("case check : x1 == " + point.x + " y1 == " + point.y + " x2 == " + square2.x + " y2 == " + square2.y);
        return checkNeighborhood(point, square2, map, incrementY, incrementX, before, color);
    }

    private boolean checkNeighborhood(Point p1, Point p2, Map map, int incrY, int incrX, boolean before, int color){
        int advColor = color == 1? 2 : 1;
        if (before == false){
            System.out.println("j'ai trouve un before");
            System.out.println("je test : y == " + (p1.y - incrY) + " x == " + (p1.x - incrX) + " et y == " + (p2.y + incrY) + " et x == " + (p2.x + incrX));
            if (map.get_map()[p1.y - incrY][p1.x - incrX] == 0 && map.get_map()[p2.y + incrY][p2.x + incrX] == advColor)
                return true;
            System.out.println("pas le premier test");
            if (map.get_map()[p1.y - incrY][p1.x - incrX] == advColor && map.get_map()[p2.y + incrY][p2.x + incrX] == 0)
                return true;
            System.out.println("pas le 2em test");
        }
        else{
            System.out.println("j'ai trouve un after");
            System.out.println("je test : y == " + (p2.y - incrY) + " x == " + (p2.x - incrX) + " et y == " + (p1.y + incrY) + " et x == " + (p1.x + incrX));
            if (map.get_map()[p2.y - incrY][p2.x - incrX] == 0 && map.get_map()[p1.y + incrY][p1.x + incrX] == advColor)
                return true;
            System.out.println("pas le premier test");
            if (map.get_map()[p2.y - incrY][p2.x - incrX] == advColor && map.get_map()[p1.y + incrY][p1.x + incrX] == 0)
                return true;
            System.out.println("pas le 2em test");
        }
        return false;
    }


    private boolean isCapturable(Point point, Map map, final int color){
        System.out.println("les coords sont : x == " + point.x + " y == " + point.y);
        System.out.println("color == " + map.get_map()[point.y][point.x]);
        System.out.println("\n\nHORIZONTAL\n\n");
        if (checkDir(point, map, 1, 0, color) == true)//horixontal
            return true;
        System.out.println("\n\nVERTICAL\n\n");
        if (checkDir(point, map, 0, 1, color) == true)//vertical
            return true;
        System.out.println("\n\nDIAGONAL TOP\n\n");
        if (checkDir(point, map, 1, 1, color) == true)//diagtop
            return true;
        System.out.println("\n\nDIAGONAL LOW\n\n");
        if (checkDir(point, map, 1, -1, color) == true)//diaglow
            return true;
        return false;
    }

    private void sortList(ArrayList<Point> list)
    {
        for (int i = 0; i < list.size() - 1; i++) {
            Point p1 = list.get(i);
            Point p2 = list.get(i + 1);
            if (p2.y < p1.y || (p2.y == p1.y && p2.x < p1.x)) {
                list.set(i, p2);
                list.set(i + 1, p1);
                i = -1;
            }
        }
    }

    private boolean checkFiveAfterCapture(ArrayList<Point> list, ArrayList<Point> capturable, int dir){
        ArrayList<Point> newList = new ArrayList<Point>();
        boolean rm;
        for (Point l : list){
            rm = false;
            for (Point p : capturable){
                if (l == p){
                    rm = true;
                }
            }
            if (rm == false)
                newList.add(l);
        }
        sortList(newList);
        for (int i = 0; i < newList.size() - 1; i++){
            Point point1 = newList.get(i);
            Point point2 = newList.get(i + 1);
            System.out.println("point 1 == " + point1 + " point2 " + point2);
            System.out.println("1er == " + Math.abs(point1.x - point2.x) + " 2em " + Math.abs(point1.y - point2.y));
            if (Math.abs(point1.x - point2.x) >= 2 || Math.abs(point1.y - point2.y) >= 2)
                return true;
        }
        return false;
    }

    @Override
    public boolean areCapturable(ArrayList<Point> points, Map map, final int color, int dir){ 
        System.out.println("coucou!!!!!!!!!!!!!!!!!!!!!!!!");
        ArrayList<Point> capturable = new ArrayList<Point>();
        for (Point p : points){
            if (isCapturable(p, map, color) == true){
                capturable.add(p);
            }
        }
        return checkFiveAfterCapture(points, capturable, dir);//enleverl es capturablessort et check si 
    }
}
