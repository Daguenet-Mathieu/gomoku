package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;

public class GomokuRules implements Rules {

    int winner;
    Rules.GameMode gameStatus = Rules.GameMode.PLAYING;
    int boardSize = 19;

    @Override
    public boolean hasPass(){
        return false;
    }


    @Override
    public boolean undo(){
        return true;
    }

    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        // Utilisation de la méthode par défaut pour vérifier si la case est vide
        if (!checkEmptySqure(point.x, point.y, map.get(map.size() - 1)))
        {
            return false;
        }
        
        // Ajouter d'autres vérifications spécifiques au jeu Gomoku, si nécessaire.
        // Par exemple, vérifier si le coup respecte la taille du plateau ou les autres règles du Gomoku.
        // Pour un modèle minimaliste, supposons que tout coup est valide si la case est vide.
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
        Map map = maps.get(index);
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
