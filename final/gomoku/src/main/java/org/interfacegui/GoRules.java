package org.interfacegui;
import java.util.ArrayList;
import org.utils.Point;
import java.util.Arrays;

public class GoRules implements Rules {
    ArrayList<Point> prisonners;//prisonnier crees par le dernier coup
    ArrayList<Point> forbidden_moves = new ArrayList<Point>();//coups interdit pour la position actuelle
    int [] prisonners_nbr = new int[2];
    int winner;
    boolean pass = false;
    Rules.GameMode gameStatus = Rules.GameMode.PLAYING;

    public boolean pass(){
        switch (gameStatus) {
            case PLAYING:
                if (pass == false)
                    pass = true;
                else
                    gameStatus = Rules.GameMode.DEATH_MARKING;
                System.out.println("Mode : en cours de jeu");
                return true;
            case DEATH_MARKING:
                gameStatus = Rules.GameMode.COUNTING;
                System.out.println("Mode : sélection des pierres mortes");
                return false;
            case COUNTING:
                System.out.println("Mode : décompte des points");
                return false;
            default:
                return true;
        }
        //si pass == 
        // false le mettre a true si a true changer le mode de jeu is valid changera sa facon de verifier si le coup est valid
    }

    public void undo(){
        pass = false;
        if (gameStatus == Rules.GameMode.DEATH_MARKING)
            gameStatus = Rules.GameMode.PLAYING;
        else if (gameStatus == Rules.GameMode.COUNTING)
            gameStatus = Rules.GameMode.DEATH_MARKING;
        // else if (Rules.game)
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
                // System.out.println("ci dessous nouvelle map a test");
            // newMove.printMap();
            // System.out.println("ci dessous map a test");
            // m.printMap();
            System.out.println("deep equals == " + Arrays.deepEquals(m.get_map(), newMove.get_map()));
            if (Arrays.deepEquals(m.get_map(), newMove.get_map()) == true)
                return false;
        }
        return true;

    }

    @Override
    public boolean isValidMove(Point point, ArrayList<Map> map) {
        System.out.println(gameStatus.name());
        Map tmp = new Map(map.get(map.size() - 1));
        final int color = (map.size() - 1) % 2 + 1;
        tmp.addMove(point, color);
        check_capture(point, tmp);
        tmp.remove_prisonners(prisonners);
        //ArrayList<Point> tmpPrissners = getCapturableList();
        // si tmp est pas vide le coup est invalide
        // lancer le floodfill depuis point? ou plutot appeler getCapturableList (private ArrayList<Point> getCapturableList(Point coord, Map map, int color, int advColor){)
        // if (isCapturable())
            // return false; //faut simuler les captures autour aussi T.T
        if (gameStatus == Rules.GameMode.COUNTING)
            return false;
        boolean valid = checkEmptySqure(point.x, point.y, map.get(map.size() - 1));
        // Utilisation de la méthode par défaut pour vérifier si la case est vide
        System.out.println("coucou par ici equals in go");
        if (gameStatus == Rules.GameMode.PLAYING && checkForbidden(point, map, map.size() - 1, ((map.size() - 1) % 2) + 1)) {
            if (valid)
                pass = false;
            return valid;
        }
        else if (gameStatus == Rules.GameMode.DEATH_MARKING){
            return (valid == false);
        }
        //check si le coup est ans la lsite des interdits
        //prisonners = getNewPrisonners();
        //generer les coups interdit danscette position forbidden_moves = get_forbiden_moves()
        // Ajouter d'autres vérifications spécifiques au jeu Gomoku, si nécessaire.
        // Par exemple, vérifier si le coup respecte la taille du plateau ou les autres règles du Gomoku.
        // Pour un modèle minimaliste, supposons que tout coup est valide si la case est vide.
        //generer la nouvelle liste de coups interdits
        return false;
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
    public ArrayList<Point> get_forbiden_moves(ArrayList<Map> map, int index, int color){//besoin de connaitre tout l'historique arraylist <Map>
        forbidden_moves.clear();
        for (int i = 0; i < get_board_size(); i++){
            for (int j = 0; j < get_board_size(); j++){
                if (map.get(index).get_map()[i][j] == 0 && checkForbidden(new Point(j, i), map, index, color) == false)
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
        final int color = map.get_map()[point.y][point.x];
        // final int advColor = color == 1? 2: 1;
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
        //check si pierre au contact d'un 0 vider la liste et quitter
    }

    private void printMap(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }


    private void floodFill(Point p, int[][] map, int color, ArrayList<Point> list, int value){//tableau de couleur pour le comptage?
        // printMap(map);
        // System.out.println("dans floodfill color == " + color + " case checked color == " + map[p.y][p.x]);
        if (p.x < 0 || p.y < 0 || p.x >= get_board_size() || p.y >= get_board_size() || map[p.y][p.x] != color)
        {
            return ;
        }
        if (map[p.y][p.x] == color)
        {
            list.add(new Point(p.x, p.y));
            map[p.y][p.x] = value;
        }
        floodFill(new Point(p.x + 1, p.y), map, color, list, value);
        floodFill(new Point(p.x - 1, p.y), map, color, list, value);
        floodFill(new Point(p.x, p.y + 1), map, color, list, value);
        floodFill(new Point(p.x, p.y - 1), map, color, list, value);
    }

    private ArrayList<Point> getCapturableList(Point coord, Map map, int color, int advColor){
        ArrayList<Point> p = new ArrayList<Point>();
        if (coord.x < 0 || coord.y < 0 || coord.x >= get_board_size() || coord.y >= get_board_size())
            return p;
        // System.out.println("color == " + color + " adv color == " + advColor + " cehcked case color == " + map.get_map()[coord.y][coord.x]);
        if (map.get_map()[coord.y][coord.x] == advColor)
            floodFill(coord, copyMap(map), advColor, p, 7);
        // System.out.println("nb prisonners == " + p.size());
        checkCapturedStones(p, map);
        // System.out.println("nb prisonners == " + p.size());
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
        // System.out.println("nb prisonners == " + p.size() + " tmp == " + tmp.size());
        // if (map.get_map()[coord.y][coord.x + 1] == advColor) //horizontal
        // if (map.get_map()[coord.y + 1][coord.x] == advColor) //vertical
        // if (map.get_map()[coord.y + 1][coord.x + 1] == advColor) //diag droite
        // if (map.get_map()[coord.y][coord.x] == advColor) //diag gauche

        return p;
    }


    @Override
    public void check_capture(Point point, Map map){
        prisonners = GetCapturedStones(point, map);
        // prisonners = custom captures check floodfill? si rencontre un 0 pas de caprure sinon toutes pierres de couleur != coord du point capturees
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
