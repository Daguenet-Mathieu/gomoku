package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;

public class GomokuRules implements Rules {

    int winner;
    Rules.GameMode gameStatus = Rules.GameMode.PLAYING;
    int boardSize = 19;
    int nbMove = 0;

    @Override
    public boolean hasPass(){
        return false;
    }


    @Override
    public boolean undo(){
        if (nbMove > 0)
            nbMove--;
        return true;
    }

    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        map.get(map.size()-1).printMap();
        System.out.println(map.get(map.size() - 1).get_map()[point.y][point.x]);
        if (!checkEmptySqure(point.x, point.y, map.get(map.size() - 1)))
        {
            return false;
        }
        nbMove++;
        return true;
    }

    @Override
    public void  setBoardSize(int value){
        if (value != -1)
            boardSize = value;
        else
            boardSize = 19;
    }

    @Override
    public boolean endGame(Map map, Point point) {
        if (check_five(map, point)){
            gameStatus = Rules.GameMode.ENDGAME;
            winner = getColor(map, point);
            return true;
        }
        if (nbMove == boardSize * boardSize)
        {
            winner = 0;
            return true;
        }
        return false;
    }
    
    @Override
    public String getGameType() {
        return "Gomoku";
    }

    @Override
    public Rules.GameMode getGameMode(){
        return gameStatus;
    }

    @Override
    public ArrayList<Point> get_forbiden_moves(ArrayList<Map> maps, int index, int color)
    {
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
        return boardSize;
    }

    @Override
    public boolean areCapturable(ArrayList<Point> points, Map map, final int color, int dir){
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
        return true;
    }
    
}
