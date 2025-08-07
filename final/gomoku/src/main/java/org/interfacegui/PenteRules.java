package org.interfacegui;
import java.util.ArrayList;
import org.utils.*;

public class PenteRules implements Rules {

    int winner = 0;
    int waitingWinner = 0;
    ArrayList<Point> prisonners;
    ArrayList<Point> forbidden_moves = new ArrayList<Point>();
    int [] prisonners_nbr = new int[2];
    public DoubleFree dbfree = new DoubleFree();
    Rules.GameMode gameStatus = Rules.GameMode.PLAYING;
    int boardSize = 19;
    int nbMove = 0;

    @Override
    public void  setBoardSize(int value){
        if (value != -1)
            boardSize = value;
        else
            boardSize = 19;
    }


    @Override
    public boolean hasPass(){
        return false;
    }

    private boolean isValidMove2(Point point, ArrayList<Map> map, int color){
        System.out.println("color == " + color + " x == " + point.x + " y == " + point.y);
        if (!checkEmptySqure(point.x, point.y, map.get(map.size() - 1))) {
            return false;
        }
        if (this.dbfree.check_double_free(point.y, point.x, color, map.get(map.size() - 1).get_map()) == false)
            return false;
        return true;
    }

    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        if (!checkEmptySqure(point.x, point.y, map.get(map.size() - 1))) {
            return false;
        }
        if (this.dbfree.check_double_free(point.y, point.x, ((map.size() + 1)%2)+1, map.get(map.size() - 1).get_map()) == false)
            return false;
        nbMove++;
        return true;
    }


    @Override
    public boolean undo(){
        waitingWinner = 0;
        if (nbMove > 0)
            nbMove--;
        return true;
    }

    private void removeCaptured(ArrayList<Point> list, Map map){
        list.removeIf(p -> map.get_map()[p.y][p.x] == 0);
    }

    @Override
    public boolean endGame(Map map, Point point) {
        final int color = map.get_map()[point.y][point.x];
        ArrayList<Point> verticalWin = getVerticalWin();
        ArrayList<Point> horizontalWin = getHorizontalWin();
        ArrayList<Point> diagonalLeftWin = getDiagonalLeftWin();
        ArrayList<Point> diagonalRightWin = getDiagonalRightWin();
        removeCaptured(verticalWin, map);
        removeCaptured(horizontalWin, map);
        removeCaptured(diagonalLeftWin, map);
        removeCaptured(diagonalRightWin, map);
        if (waitingWinner != 0)
        {
            boolean winnerFound = false;
            if (checkFiveInRow(verticalWin))
                winnerFound = true;
            else if (checkFiveInRow(horizontalWin))
                winnerFound = true;
            else if (checkFiveInRow(diagonalLeftWin))
                winnerFound = true;
            else if (checkFiveInRow(diagonalRightWin))
                winnerFound = true;
            if (winnerFound)
            {
                winner = (color == 1) ? 2 : 1;
                gameStatus = Rules.GameMode.ENDGAME;
                return true;
            }
            else
                waitingWinner = 0;
        }
        if (prisonners_nbr[0] >= 10 || prisonners_nbr[1] >= 10){
            winner = (prisonners_nbr[0] >= 10) ? 1 : 2;
            gameStatus = Rules.GameMode.ENDGAME;
            return true;
        }
        if (check_five(map, point))
        {
            if (verticalWin.size() >= 5 && areCapturable(verticalWin, map, color, 0) == true)
                waitingWinner = color;
            if (horizontalWin.size() >= 5 && areCapturable(horizontalWin, map, color, 1) == true)
                waitingWinner = color;
            if (diagonalLeftWin.size() >= 5 && areCapturable(diagonalLeftWin, map, color, 2) == true)
                waitingWinner = color;
            if (diagonalRightWin.size() >= 5 && areCapturable(diagonalRightWin, map, color, 3) == true)
                waitingWinner = color;
            if (waitingWinner == 0)
            {
                this.winner = color;
                return true;
            }
        }
        if (nbMove == boardSize * boardSize)
        {
            winner = 0;
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
    public ArrayList<Point> get_forbiden_moves(ArrayList<Map> maps, int index, int color)
    {
        Map map = maps.get(index);
        int[][] m = map.get_map();
        forbidden_moves.clear();
        map.printMap();
        System.out.println("color == " + color);
        for (int y = 0; y < get_board_size();y++)
        {
            for (int x = 0; x < get_board_size(); x++){
                if (y == 10 && x == 8)
                if (m[y][x] != 0)
                    continue ;
                boolean res = this.dbfree.check_double_free(y, x, color, m);
                if (res == false){
                    forbidden_moves.add(new Point (x, y));
                }
            }
        }
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
        return boardSize;
    }

    @Override
    public void set_black_prisonners(int nb){
        prisonners_nbr[0] = nb;
    }

    @Override
    public void set_white_prisonners(int nb){
        prisonners_nbr[1] = nb;
    }

    // private boolean inMap(int y, int x){
    //     if (x < 0 || x >= get_board_size() || y < 0 || y >= get_board_size())
    //         return false;
    //     return true;
    // }

    private int getColor(Point point, int incrementX, int incrementY, Map map){
        return map.get_map()[point.y + incrementY][point.x + incrementX];
    }

    private boolean checkDir(Point point, Map map, int incrementX, int incrementY, final int color){
        if (point.x + incrementX < 0 || point.y + incrementY < 0 || point.x - incrementX < 0 || point.y - incrementY < 0)
            return false;
        if (point.x + incrementX >= get_board_size() || point.y + incrementY >= get_board_size() || point.x - incrementX >= get_board_size() || point.y - incrementY >= get_board_size())
            return false;
        if (color == getColor(point, incrementX, incrementY, map) && color == getColor(point, -incrementX, -incrementY, map))
            return false;
        if (color != getColor(point, incrementX, incrementY, map) && color != getColor(point, -incrementX, -incrementY, map))
            return false;
        boolean before = false;
        Point square2;
        if (color == map.get_map()[point.y + incrementY][point.x + incrementX])
            square2 = new Point(point.x + incrementX, point.y + incrementY);
        else{
            square2 = new Point(point.x - incrementX, point.y - incrementY);
            before = true;
        }
        return checkNeighborhood(point, square2, map, incrementY, incrementX, before, color);
    }

    private boolean checkNeighborhood(Point p1, Point p2, Map map, int incrY, int incrX, boolean before, int color){
        int advColor = color == 1? 2 : 1;
        ArrayList<Map> mapList = new ArrayList();
        mapList.add(map);
        if (before == false){
            if (p1.y - incrY < 0 || p1.y - incrY >= get_board_size()|| p2.y + incrY < 0 || p2.y + incrY >= get_board_size())
                return false;
            if (p1.x - incrX < 0 || p1.x - incrX >= get_board_size()|| p2.x + incrX < 0 || p2.x + incrX >= get_board_size())
                return false;
            if (map.get_map()[p1.y - incrY][p1.x - incrX] == 0 && isValidMove2(new Point(p1.x - incrX, p1.y - incrY), mapList, advColor) && map.get_map()[p2.y + incrY][p2.x + incrX] == advColor)
                return true;
            if (map.get_map()[p1.y - incrY][p1.x - incrX] == advColor && map.get_map()[p2.y + incrY][p2.x + incrX] == 0 && isValidMove2(new Point(p2.x + incrX, p2.y + incrY), mapList, advColor))
                return true;
        }
        else{
            if (p2.y - incrY < 0 || p2.y - incrY >= get_board_size()|| p1.y + incrY < 0 || p1.y + incrY >= get_board_size())
                return false;
            if (p2.x - incrX < 0 || p2.x - incrX >= get_board_size()|| p1.x + incrX < 0 || p1.x + incrX >= get_board_size())
                return false;

            if (map.get_map()[p2.y - incrY][p2.x - incrX] == 0 && isValidMove2(new Point(p2.x - incrX, p2.y - incrY), mapList, advColor) && map.get_map()[p1.y + incrY][p1.x + incrX] == advColor)
                return true;
            if (map.get_map()[p2.y - incrY][p2.x - incrX] == advColor && map.get_map()[p1.y + incrY][p1.x + incrX] == 0 && isValidMove2(new Point(p1.x + incrX, p1.y + incrY), mapList, advColor))
                return true;
        }
        return false;
    }


    private boolean isCapturable(Point point, Map map, final int color){
        if (checkDir(point, map, 1, 0, color) == true)//horixontal
            return true;
        if (checkDir(point, map, 0, 1, color) == true)//vertical
            return true;
        if (checkDir(point, map, 1, 1, color) == true)//diagtop
            return true;
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

    private boolean checkFiveInRow(ArrayList<Point> list)
    {
        sortList(list);
        int five = 1;
        for (int i = 0; i < list.size() - 1; i++)
        {
            Point point1 = list.get(i);
            Point point2 = list.get(i + 1);
            if (Math.abs(point1.x - point2.x) > 1 || Math.abs(point1.y - point2.y) > 1)
                five = 1;
            else
                five++;
        }
        if (five >= 5)
            return true;
        return false;
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
        // System.out.println("list : " + list);
        // System.out.println("new list : " + newList);
        return checkFiveInRow(newList);
    }

    @Override
    public boolean areCapturable(ArrayList<Point> points, Map map, final int color, int dir){ 
        // System.out.println("coucou!!!!!!!!!!!!!!!!!!!!!!!!");
        ArrayList<Point> capturable = new ArrayList<Point>();
        for (Point p : points){
            if (isCapturable(p, map, color) == true){
                capturable.add(p);
            }
        }
        if (checkFiveAfterCapture(points, capturable, dir))//enleverl es capturablessort et check si
            return false;
        return true; 
    }

    @Override
    public boolean hasIa(){
        return true;
    }

    @Override
    public Rules.GameMode getGameMode(){
        return gameStatus;
    }

}
