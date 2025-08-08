package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;
import java.util.Arrays;

public class GoRules implements Rules {
    ArrayList<Point> prisonners;
    ArrayList<Point> forbidden_moves = new ArrayList<Point>();
    ArrayList<Point> deadStones = new ArrayList<Point>();
    int [] prisonners_nbr = new int[2];
    int winner;
    boolean pass = false;
    Rules.GameMode gameStatus = Rules.GameMode.PLAYING;
    int boardSize = 19;
    ArrayList<Point> whitePrisonnerList = new ArrayList<Point>();
    ArrayList<Point> blackPrisonnerList = new ArrayList<Point>();
    int whiteScore = 0;
    int blackScore = 0;
    int tmpBlackPrisonners = 0;
    int tmpWhitePrisonners = 0;

    public int getBlackScore(){
        blackScore = prisonners_nbr[0] + tmpBlackPrisonners + blackPrisonnerList.size();
        return blackScore;
    }

    public int getWhiteScore(){
        whiteScore = prisonners_nbr[1] + tmpWhitePrisonners + whitePrisonnerList.size();
        return whiteScore;
    }


    @Override
    public boolean hasPass(){
        return true;
    }

    public ArrayList<Point> getDeadStones(){
        return deadStones;
    }

    public ArrayList<Point> getWhitePrisonnersList(){
        return whitePrisonnerList;
    }

    public ArrayList<Point> getBlackPrisonnersList(){
        return blackPrisonnerList;
    }

    public void removeMapPrisnners(Map map){
        tmpBlackPrisonners = 0;
        tmpWhitePrisonners = 0;
        for (int i = 0; i < get_board_size(); i++){
            for (int j = 0; j < get_board_size(); j++){
                if (map.get_map()[i][j] == 3){
                    tmpWhitePrisonners += 1;
                    map.get_map()[i][j] = 0;
                }
                else if (map.get_map()[i][j] == 4){
                    tmpBlackPrisonners += 1;
                    map.get_map()[i][j] = 0;
                }

            }
        }
    }

    private void addPrisonnersToList(ArrayList<Point> list, Map map){
        int currentColor = -1;
        int left = -1;
        int right = -1;
        int top = -1;
        int down = -1;
        boolean err = false;
        for (Point p : list){
            if (p.x - 1 >= 0 && p.x - 1 < get_board_size() && p.y >= 0 && p.y < get_board_size())
               left = map.get_map()[p.y][p.x - 1];
            if (p.x + 1 >= 0 && p.x + 1 < get_board_size() && p.y >= 0 && p.y < get_board_size())
               right = map.get_map()[p.y][p.x + 1];
            if (p.x >= 0 && p.x < get_board_size() && p.y - 1 >= 0 && p.y - 1 < get_board_size())
               top = map.get_map()[p.y - 1][p.x];
            if (p.x - 1 >= 0 && p.x < get_board_size() && p.y + 1 >= 0 && p.y + 1 < get_board_size())
               down = map.get_map()[p.y + 1][p.x];
            if (left == 1 || left == 2)
            {
                if (currentColor != -1 && currentColor != left)
                    err = true;
                currentColor = left;
            }
            if (right == 1 || right == 2)
            {
                if (currentColor != -1 && currentColor != right)
                    err = true;
                currentColor = right;
            }
            if (top == 1 || top == 2)
            {
                if (currentColor != -1 && currentColor != top)
                    err = true;
                currentColor = top;
            }
            if (down == 1 || down == 2)
            {
                if (currentColor != -1 && currentColor != down)
                    err = true;
                currentColor = down;
            }
            if (err == true)
            {
                list.clear();
                return ;
            }
        }
        if (currentColor == 2)
            whitePrisonnerList.addAll(list);
        else if (currentColor == 1)
            blackPrisonnerList.addAll(list);
    }

    public void init_prisonners(Map map){
        Map tmp = new Map(map);
        ArrayList<Point> tmpList = new ArrayList<Point>();
        removeMapPrisnners(tmp);
        whitePrisonnerList.clear();
        blackPrisonnerList.clear();
        for (int i = 0; i < get_board_size(); i++){
            for (int j = 0; j < get_board_size(); j++){
                tmpList.clear();
                if (tmp.get_map()[i][j] == 0){
                    floodFill(new Point(j, i), tmp.get_map(), 0, tmpList, 7);
                    addPrisonnersToList(tmpList, tmp);
                    tmpList.clear();
                }
            }
        }
    }

    public boolean pass(){
        switch (gameStatus) {
            case PLAYING:
                if (pass == false){
                    pass = true;
                }
                else{
                    gameStatus = Rules.GameMode.DEATH_MARKING;
                }
                return true;
            case DEATH_MARKING:
                gameStatus = Rules.GameMode.COUNTING;
                return true;
            case COUNTING:
                gameStatus = Rules.GameMode.ENDGAME;
                return false;
            default:
                return true;
        }
    }

    @Override
    public boolean undo(){
            switch (gameStatus) {
            case PLAYING:
                if (pass == true){
                    pass = false;
                }
                return true;
            case DEATH_MARKING:
                gameStatus = Rules.GameMode.PLAYING;
                return true;
            case COUNTING:
                gameStatus = Rules.GameMode.DEATH_MARKING;
                return true;
            default:
                return false;
        }
    }

    @Override
    public Rules.GameMode getGameMode(){
        return gameStatus;
    }

    private boolean checkForbidden(Point point, ArrayList<Map> map, int index, int color){
        Map newMove = new Map(map.get(index));
        newMove.addMove(point, color);
        ArrayList<Point> prisonners = GetCapturedStones(point, newMove);
        newMove.remove_prisonners(prisonners);
        for (int i = 0; i <= index; i++) {
            Map m = map.get(i);
            if (Arrays.deepEquals(m.get_map(), newMove.get_map()) == true)
                return false;
        }
        return true;
    }

    boolean checkSuicide(Map map, Point point, int color, int advColor){
        map.addMove(point, color);
        check_capture(point, map);
        map.remove_prisonners(prisonners);
        ArrayList<Point> listPrisonners = getCapturableList(point, map, advColor, color);
        if (listPrisonners.size() == 0)
            return false;

        return true;
    }

    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        if (gameStatus == Rules.GameMode.COUNTING)
            return false;
        if (map.size() < 2)
            return true;
        int squareColor = map.get(map.size() - 1).get_map()[point.y][point.x];
        boolean valid = (map.get(map.size() - 1).get_map()[point.y][point.x] == 0);
        if (gameStatus == Rules.GameMode.PLAYING && checkForbidden(point, map, map.size() - 1, ((map.size() - 1) % 2) + 1)) {
            Map tmp = new Map(map.get(map.size() - 1));
            final int color = (map.size() - 1) % 2 + 1;
            final int advColor = color == 1 ? 2 : 1;
            if (checkSuicide(tmp, point, color, advColor) == true)
                return false;
            if (valid)
                pass = false;
            return valid;
        }
        else if (valid == false && gameStatus == Rules.GameMode.DEATH_MARKING){
            ArrayList<Point> tmpArr = new ArrayList<Point>();
            Map tmpMap = new Map(map.get(map.size() - 1));
            floodFill(point, tmpMap.get_map(), squareColor, tmpArr, 7);
            deadStones = tmpArr;
            return (true);
        }
        return false;
    }

    @Override
    public boolean endGame(Map map, Point point) {
        return false;
    }

    @Override
    public String getGameType() {
        return "Go";
    }

    @Override
    public ArrayList<Point> get_forbiden_moves(ArrayList<Map> map, int index, int color){
        if (index == 0)
            return (new ArrayList<Point>());
        forbidden_moves.clear();
        final int advColor = color == 1 ? 2 : 1;
        for (int i = 0; i < get_board_size(); i++){
            for (int j = 0; j < get_board_size(); j++){
                Map tmp = new Map(map.get(map.size() - 1));

                if (map.get(index).get_map()[i][j] == 0 && checkForbidden(new Point(j, i), map, index, color) == false)
                    forbidden_moves.add(new Point(j, i));
                else if (map.get(index).get_map()[i][j] == 0 && checkSuicide(tmp, new Point(j, i), color, advColor))
                    forbidden_moves.add(new Point(j, i));
            }
        }
        return forbidden_moves;
    }

    private int[][] copyMap(Map map){
        int[][] cpy = new int[get_board_size()][get_board_size()];
        for (int i = 0; i < get_board_size(); i++){
            for (int j = 0; j < get_board_size(); j++){
                cpy[i][j] = map.get_map()[i][j];
            }
        }
        return cpy;
    }

    private void removeDuplicates(ArrayList<Point> list) {
        for (int i = 0; i < list.size(); i++) {
            Point p1 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                Point p2 = list.get(j);
                if (p1.x == p2.x && p1.y == p2.y) {
                    list.remove(j);
                    j--;
                }
            }
        }
    }

    private boolean isCapturable(Point point, Map map){
        if (point.x + 1 < get_board_size() && map.get_map()[point.y][point.x+1] == 0)
            return false;
        if (point.x - 1 >= 0 && map.get_map()[point.y][point.x-1] == 0)
            return false;
        if (point.y - 1 >= 0 && map.get_map()[point.y-1][point.x] == 0)
            return false;
        if (point.y + 1 < get_board_size() && map.get_map()[point.y+1][point.x] == 0)
            return false;
        return true;
    }

    private void checkCapturedStones(ArrayList<Point> list, Map map){

        removeDuplicates(list);
        for (Point l : list){
            System.out.println(l + " is capturable ? " + isCapturable(l, map));
            if (isCapturable(l, map) == false)
            {
                list.clear();
                return ;
            }
        }
    }

    private void floodFill(Point p, int[][] map, int color, ArrayList<Point> list, int value){
        if (p.x < 0 || p.y < 0 || p.x >= get_board_size() || p.y >= get_board_size() || map[p.y][p.x] != color)
        {
            return ;
        }
        if (map[p.y][p.x] == color)
        {
            list.add(new Point(p.x, p.y));
            map[p.y][p.x] = value;
        }
        else
            return;
        floodFill(new Point(p.x + 1, p.y), map, color, list, value);
        floodFill(new Point(p.x - 1, p.y), map, color, list, value);
        floodFill(new Point(p.x, p.y + 1), map, color, list, value);
        floodFill(new Point(p.x, p.y - 1), map, color, list, value);
    }

    private ArrayList<Point> getCapturableList(Point coord, Map map, int color, int advColor){
        ArrayList<Point> p = new ArrayList<Point>();
        if (coord.x < 0 || coord.y < 0 || coord.x >= get_board_size() || coord.y >= get_board_size())
            return p;
        if (map.get_map()[coord.y][coord.x] == advColor)
            floodFill(coord, copyMap(map), advColor, p, 7);
        checkCapturedStones(p, map);
        return p;
    }

    private void mergePrisonnersList(ArrayList<Point> p, ArrayList<Point> tmp){
        for (Point t : tmp){
            int i = 0;
            while (i < p.size()){
                if (p.get(i).x == t.x && p.get(i).y == t.y)
                    break;
                i++;
            }
            if (i == p.size())
                p.add(t);
        }
    }

    @Override
    public ArrayList<Point> GetCapturedStones(Point coord, Map map){

        ArrayList<Point> p = new ArrayList<Point>();
        ArrayList<Point> tmp;
        final int color = map.get_map()[coord.y][coord.x];
        final int advColor = (color == 1) ? 2 : 1;

        tmp = getCapturableList(new Point(coord.x + 1, coord.y), map, color, advColor);
        mergePrisonnersList(p, tmp);
        tmp = getCapturableList(new Point(coord.x - 1, coord.y), map, color, advColor);
        mergePrisonnersList(p, tmp);
        tmp = getCapturableList(new Point(coord.x, coord.y + 1), map, color, advColor);
        mergePrisonnersList(p, tmp);
        tmp = getCapturableList(new Point(coord.x, coord.y - 1), map, color, advColor);
        mergePrisonnersList(p, tmp);
        return p;
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
        return boardSize;
    }
    
    @Override
    public void  setBoardSize(int value){
        if (value != -1)
            boardSize = value;
        else
            boardSize = 19;
    }

    @Override
    public void set_white_prisonners(int nb){
        prisonners_nbr[1] = nb;
    }
    
    @Override
    public void set_black_prisonners(int nb){
        prisonners_nbr[0] = nb;
    }

    @Override
    public boolean areCapturable(ArrayList<Point> point, Map map, final int color, int dir){
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
    public boolean hasIa(){
        return false;
    }
}
