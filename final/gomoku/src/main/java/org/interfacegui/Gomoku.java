package org.interfacegui;
//import javafx.scene.shape.*;
import java.util.ArrayList;

import org.modelai.Game;
import org.modelai.MinMax;

//import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//import javafx.scene.paint.Color;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.CornerRadii;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
//import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.utils.Point;
import javafx.scene.text.Font;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import org.modelai.Candidat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.scene.paint.Color;
import java.util.Comparator;

//import java.util.concurrent.ExecutionException;

//coder les free 3et reccup list coup interdits + prisonnier et liste prisonniers et fin de parties sur 10 prisioniers //pente et renju faire des emthodes defaut dans rules
//mettre qulques boutons test dans home choix des regles + temps (faire ecoulement du temps)  + choix du type de joueur noir et blanc( humain ou machine?) +  

//mettrre en place fct d'event pour les assigner a chaque creation
//regler le probeleme responsvite et mettre un interval pour que l'ia joue et pour check l'ecoulement du temps //stocker dans map le temps qu'a pris le coup

public class Gomoku
{
    // private SGF sgf = new SGF();
    private Pane game_display;
    //plateau de jeu
    //regles de jeu
    //settings choisi
    //info sur les joueurs la partie , temps tour de jeu actuel etc ...
    //stocker arraylist de map
    //minmax avec constructeur approprie
    private ArrayList<Map> _map;
    private Goban goban;
    private GameInfos gameInfos;
    private int _game_infos_size_x;
    public int _game_infos_size_y;
    private  Pane _goban_pane;
    private VBox _game_infos_pane;
    private int _nb_line;
    private int map_index;
    private Rules rule;
    private VBox _end_popin = new VBox();
    private Button _replay;
    private Button _back_home;
    private Home _game_infos = new Home(){};
    private int player_turn = 0;
    private int current_decrement = 0;
    private boolean game_end = false;
    private Timeline gameLoop;
    private Label _end_text = new Label();
    private int _winner = 0;
    private Label game_name;
    private int start_move_time;
    private int end_move_time;
    private ArrayList<Point> candidatsList;
    private float bestMoveScore;
    private ArrayList<Point> hintList;
    private ArrayList<Integer> whiteTimeList = new ArrayList<Integer>();
    private ArrayList<Integer> blackTimeList = new ArrayList<Integer>(); 
    // private DoubleBinding fontSizeBinding;

    private Game game;
    private ArrayList<Point> saved;
    private boolean toggleCandidat = false;
    private boolean toggleHint = false;
    private boolean ia_playing = false;
    private ExecutorService executor = null;
    private Future<Point> future = null;
    private boolean forbiddenVisibility = true;
    Rules.GameMode playingMode = Rules.GameMode.PLAYING;
    
    // void changeCandidatVisibility(boolean visible){
    //     int count = 0;
    //     for (Float score : candidatsMap.keySet()) {
    //         if (count == 0) { 
    //             count++; 
    //             continue; // Ignore le premier coup
    //         }
    //         goban.set_stone_status(visible, "#00FF00", candidatsMap.get(score), String.format("%.2f", score));
    //         count++;
    //     }
    // }

    // void setCandidats(ArrayList<Candidat.coord> candidats, float[] values){
    //     System.out.println("candidats = " + candidats.size());
    //     System.out.println("values = " + values.length);
    //     System.out.println("le coup choisi == " + game.val);
    //     if (candidats == null || values == null)
    //         return ;
    //     candidatsMap = new HashMap();
    //     System.out.println("llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
    //     for (int i = 0; i < candidats.size(); i++) {
    //         float score = values[i];
    //         while (candidatsMap.containsKey(score)) {
    //             System.out.println("cououc");
    //            score += 0.00001f;  // Suffisamment petit pour ne pas affecter l'ordre réel
    //         }
    //         candidatsMap.put(score, new Point(candidats.get(i).y, candidats.get(i).x));
    //     }
    // }


    private void eraseForbiddens(){
        ArrayList<Point> points = rule.get_forbiden_moves(_map, map_index, player_turn + 1);
        for (Point point : points){
            changeForbiddenVisibility(forbiddenVisibility, point);
        }
        forbiddenVisibility = false;
    }

    void changeForbiddenVisibility(boolean visible , Point p) {

        goban.set_stone_status(visible, "#FF0000", p, null);
    }


    void changeHintVisibility(boolean visible) {
        if (hintList == null || hintList.isEmpty()) return;
        // hintList.sort(null);
        int val = 0;
        for (int i = 0; i < hintList.size(); i++) {
            
            Point p = hintList.get(i);
            goban.set_stone_status(visible, "#00F0FF", p, String.format("%d", (int)p.val));
            // if ()
        }
    }

    void setHint(ArrayList<Candidat.coord> hint, float[] values) {
        if (rule.hasIa() == true && (hint == null || values == null)) return;

        hintList = new ArrayList<>();
        // bestMoveScore = game.val;
        
        // System.out.println("hint = " + hint.size());
        // System.out.println("values = " + values.length);
        // System.out.println("le coup choisi == " + game.val);
        for (int i = 0; i < hint.size(); i++) {
            hintList.add(new Point(hint.get(i).y, hint.get(i).x));
            hintList.get(hintList.size() - 1).set_val(values[i]);
            // System.out.println("val == " + hintList.get(hintList.size() - 1).get_val());
        }
        System.out.println("avant sort list == " + hintList);
        // hintList.sort((a, b) -> Integer.compare((int)a.get_val(), (int)b.get_val()));
        // hintList.sort(Comparator.comparingDouble(Point::get_val));
        hintList.sort(Comparator.comparingDouble(Point::get_val));//.reversed());

        System.out.println("aapres sort list == " + hintList);
        int nb = -1000000000;
        int nb2 = 0;
        int val = 1;
        for (int i = hintList.size() - 1; i >= 0; i--){
            if (nb != -1)
                nb2 = nb;
            nb = (int)hintList.get(i).get_val();
            System.out.println(nb + " et " + nb2);
            if ((int)nb < (int)nb2)
                val = hintList.size() - i;
            System.out.println("val == " + val);
            hintList.get(i).set_val((float)val);
        }
        // nb = 1;
        // for (int i = 0; i < hintList.size(); i++){
        //     val = (int)hintList.get(i).get_val();
        //     hintList.get(i).set_val((float)nb);
        //     for (int j = i + 1; j < hintList.size(); j++){
        //         if ((int)hintList.get(j).get_val() == val)
        //             hintList.get(j).set_val((float)nb);
        //         else{
        //             i = j;
        //             break;
        //         }
        //     }
        //     nb = i + 1;

        // }
    }


    void changeCandidatVisibility(boolean visible) {
        ArrayList<Point> currentCandidats = _map.get(map_index).getCandidatsList();
        System.out.println("map inde == " + map_index + " curent candidats == " + currentCandidats);
        if (currentCandidats == null || currentCandidats.isEmpty()) return;

        for (int i = 0; i < currentCandidats.size(); i++) {
            Point p = currentCandidats.get(i);
            if (p.val < 0)
                goban.set_stone_status(visible, "#FF0000", p, String.format("%.2f", p.val));
            // if (p.val != bestMoveScore){
            //     goban.set_stone_status(visible, "#00FF00", p, String.format("%.2f", p.val));
            // }
            else{
                goban.set_stone_status(visible, "#00FF00", p, String.format("%.2f", p.val));
            }
        }
        if (visible == false){
            goban.updateFromMap(_map.get(map_index));
        }
    }

    void setCandidats(ArrayList<Candidat.coord> candidats, float[] values) {
        // System.err.println("ici on set les candidats!!!!!!!!????????????!!!!!!!!!!!!!");
        if (rule.hasIa() == false || candidats == null || values == null || game.val == null) return;
        candidatsList = new ArrayList<>();
        bestMoveScore = game.val;
        changeCandidatVisibility(false);
        // System.err.println("candidats = " + candidats.size());
        // System.err.println("values = " + values.length);
        // System.err.println("best == " + game.m.best.y + " " + game.m.best.y );
        // System.err.println("move == " + game.m.move.y + " " + game.m.move.y );
        for (int i = 0; i < candidats.size(); i++){
            System.err.println("candidat[" + i + "] = " + candidats.get(i).y + " " + candidats.get(i).x);
        }
        for (int i = 0; i < values.length; i++){
            System.err.println("values[" + i + "] = " + values[i] + " " +  values[i]);
        }
        System.err.println("le coup choisi == " + game.val);
        for (int i = 0; i < values.length; i++) {
            candidatsList.add(new Point(candidats.get(i).y, candidats.get(i).x));
            candidatsList.get(candidatsList.size() - 1).set_val(values[i]);
        }
        _map.get(map_index).setCandidatsList(candidatsList);
    }

    void showCandidats() {
        if (candidatsList == null)
            return ;
        for (Point p : candidatsList) {
            System.err.println("Score: " + p.val + " -> Point: " + p);
        }
    }

    public void reset_gomoku(){
        _map.clear();
        _map.add(new Map(_nb_line));
        saved.clear();
        map_index = 0;
        // init_rules(_game_infos.get_rules());
        init_rules(_game_infos.get_rules(), _game_infos.get_board_size());
        if (rule.hasPass() == false){
            gameInfos.getPassButton().setVisible(false);
            gameInfos.getPassButton().setManaged(false);
        }
        goban.updateFromMap(_map.get(_map.size() - 1));
        gameInfos.clear();
        gameInfos.reset_infos(_game_infos);
        createDelayedGameLoop();
        goban.remove_score();
        _end_popin.setVisible(false);
        _end_popin.setManaged(false);
        player_turn = 0;
        current_decrement = 0;
        game_end = false;
        game = new Game(_game_infos.get_rules(), rule.get_board_size());
        _winner = 0;
        gameInfos.set_black_prisonners("0");
        gameInfos.set_white_prisonners("0");
        ia_playing = false;
    }


    private int get_average(ArrayList<Integer> list){
        if (list.isEmpty()) return 0;
        int sum = 0;
        for (int val : list) sum += val;
        return sum / list.size();
    }

    public void createDelayedGameLoop() {//se lance au bout de 5s ? check si tour joueur ia si oui appelle fct pou jouer son coup puis ecoule le temps
        gameLoop = new Timeline();
        System.out.println("-1 je passe la");
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
        if (rule.hasIa() == true)
        {
            try {
                if (player_turn == 0 && _game_infos.get_black_player_type() == 1){//faire une fct
                    if (ia_playing == false){
                            executor = Executors.newSingleThreadExecutor();
                            future = executor.submit(() -> {
                                return game.best_move(player_turn+1, player_turn+1);
                            });
                        ia_playing = true;
                    }
                    else if (future.isDone()){
                        playMove(future.get());
                        executor.shutdown();
                        setCandidats(game.m.candidat.lst, game.m.values);
                        System.err.println("candidats pour le coup : " + (_map.size() - 1));
                        showCandidats();
                        System.err.println("\n\n\n");
                        ia_playing = false;
                    }
                }
                else if (player_turn == 1 && _game_infos.get_white_player_type() == 1){//faire une fct
                    if (ia_playing == false){
                        executor = Executors.newSingleThreadExecutor();
                        future = executor.submit(() -> {
                            return game.best_move(player_turn+1, player_turn+1);
                        });
                        ia_playing = true;
                    }
                    else if (future.isDone()){
                        playMove(future.get());
                        executor.shutdown();
                        setCandidats(game.m.candidat.lst, game.m.values);
                        System.err.println("candidats pour le coup : " + (_map.size() - 1));
                        showCandidats();
                        System.err.println("\n\n\n");
                        ia_playing = false;
                    }

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
        }
        if (player_turn != current_decrement){
            int t = end_move_time - start_move_time;
            if (current_decrement == 0)
                blackTimeList.add(t);
            else
                whiteTimeList.add(t);
            current_decrement = current_decrement == 0?1:0;
            gameInfos.set_last_move_time(t);
            start_move_time = 0;
            end_move_time = 0;
            return ;
        }
        gameInfos.set_average_white_time(get_average(whiteTimeList));
        gameInfos.set_average_black_time(get_average(blackTimeList));
        if (player_turn == 0)
            gameInfos.sub_black_time(10);
        else
            gameInfos.sub_white_time(10);
        end_move_time += 10;
        if (gameInfos.get_black_time() <= 0 || gameInfos.get_white_time() <= 0){
            gameLoop.stop();
            game_end = true;
            ia_playing = false;
            _winner = (gameInfos.get_black_time() <= 0) ? 2 : 1;
            String res = _winner == 1? "black" : "white";
            _end_text.setText(res + " win");
            _end_popin.setVisible(true);
            _end_popin.setManaged(true);
        }
        //check si 1 joueur a 0 ou < geme fini set le vainqueur arreter la loop
        // if (ia_turn()) {
            // do_ia_move();
        // }
        // count_time();
        });
        gameLoop.getKeyFrames().add(keyFrame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        // PauseTransition delay = new PauseTransition(Duration.seconds(5));
        // delay.setOnFinished(e -> gameLoop.play());
        // delay.play();
        gameLoop.play();
        //return gameLoop;
    }

    private void init_rules(String rules_type, int boardSize){
        if (rules_type == "Gomoku")
            rule = new GomokuRules();
        if (rules_type == "Pente")
            rule = new PenteRules();
        if (rules_type == "Renju")
            rule = new RenjuRules();
        if (rules_type == "Go")
            rule = new GoRules();
        rule.setBoardSize(boardSize);
    }

    private void playMove(Point point){
        System.out.println("dans print move coord : x == " + point.x + " y == " + point.y);
        if (map_index < (_map.size() - 1) || !rule.isValidMove(point, _map) || rule.getGameMode() == Rules.GameMode.ENDGAME)
            return ;
        // if ()//!PLAYING
        if (rule.getGameMode() == Rules.GameMode.DEATH_MARKING){
            ArrayList<Point> deadStones = ((GoRules)rule).getDeadStones();
            // String color;
            Map currentMap = _map.get(_map.size() - 1);
            int color = currentMap.get_map()[point.y][point.x];
            int newColor = (color == 1 || color == 2) ? color + 2 : color - 2;
            for (Point p : deadStones){
                currentMap.get_map()[p.y][p.x] = newColor;
            }
            goban.updateFromMap(currentMap);
            //update dans la Map a 2 == 4 1 == 3? et juste update from map et inverser 
            // if (_map.get(_map.size() - 1).get_map()[point.y][point.x] == 1) {//ajouter dans l' array list chaque etape ?
            //     deadStones = ((GoRules)rule).getBlackDeadStones();
            //     color = "rgba(0,0,0,0.5)";
            // }
            // else {
            //     deadStones = ((GoRules)rule).getWhiteDeadStones();
            //     color = "rgba(255,255,255,0.5)";
            // }
            // System.out.println("color == " + color);
            // for (Point p : deadStones){
            //     goban.set_stone_status(true, color, p, null);
            // }
            return ;
        }
        System.out.println("map index == " + map_index );
        changeCandidatVisibility(false);
        toggleCandidat = false;
        changeHintVisibility(false);
        hintList = null;
        toggleHint = false;
        // System.out.println("goban cliqué aux coordonnées : (" + (x - margin_w) / square + ", " + (y - margin_h) / square + ")");
        //check legalite du coup avec les regles si ok ajouter le coup et enlever les prisonniers
        // int i = ((int)x - margin_w) / square;
        // int j = ((int)y - margin_h) / square;
        System.out.println("size map == " + _map.size());
        _map.get(_map.size() -1);
        System.out.println(); //no printmap
        _map.add(new Map(_map.get(_map.size() - 1)));
        _map.get(_map.size() - 1).addMove(point, _map.size() % 2 + 1);
        System.out.println("size map == " + _map.size());
        map_index = _map.size() - 1;
        System.out.println("map_index apres update dans play move" + map_index);

        if (rule.hasIa() == true)
            game.move(point, player_turn+1); // To update MinMax.map
        //MinMax.display_Map();
        
        //add 0 si y a des prisonniers
        _map.get(_map.size() -1); //no printmap
            rule.check_capture(point, _map.get(_map.size() - 1));

            //for point in rules.capturedPoint
            //    game.remove(point);
        if ((rule instanceof Gomoku) == false)
        {
            ArrayList<Point> points = rule.GetCapturedStones(point, _map.get(_map.size() - 1));
            for (Point p : points) {
                System.out.println("capture 1 er affichage : " + p);  // Appel automatique à toString()
                if (rule.hasIa() == true)
                    game.remove(p, _map.get(_map.size() - 1).get_prisonners(), false); // To uppdate Minmax.map
            }
            points = rule.get_prisonners();
            System.out.println("nbr prisonners : " + points.size());
            for (Point p : points) {
                System.out.println("capture 2 em affichage : " + p);  // Appel automatique à toString()
            }
            _map.get((_map.size()-1)).remove_prisonners(points);
            if (player_turn == 0)
                _map.get((_map.size()-1)).addBlackPrisonners(points.size());
            else
                _map.get((_map.size()-1)).addWhitePrisonners(points.size());
            if (points != null && points.size() > 0){
                display_nb_prisonners();
            }
            _map.get((_map.size()-1)).set_prisonners(points);
        }
        _map.get((_map.size()-1)).set_color(player_turn);

        if (rule.endGame(_map.get(_map.size() - 1), point)){
            System.out.println("partie finie!");
            int winner = rule.getWinner();
            if (winner == 1)
                System.out.println("noir gagne!");
            else
                System.out.println("blanc gagne!");
            _end_popin.setVisible(true);
            _end_popin.setManaged(true);
            game_end = true;
            ia_playing = false;
            gameLoop.stop();
            System.out.println("winner == " + winner);
            _winner = winner - 1;
            String res = _winner == 0 ? "black" : "white";
            _end_text.setText(res + " win");
        }
        else
            System.out.println("non!");
        goban.updateFromMap(_map.get(_map.size() -1));
        updatePlayerTurn();//le get de la regle
    }

    // private void undoMove(){
    //     if (map_index < _map.size() - 1)
    //         return ;
    //     Point coord = _map.get(_map.size() - 1).getLastMove();
    //     map_index -= 1;
    //     goban.set_stone_status(false, null, coord, null);//faire un autre fct avec un boucle
    //     game.remove(coord);
    //     _map.remove(_map.size() - 1);
    //     player_turn ^= 1;      
    // }

    private void updatePlayerTurn(){
        System.out.println("update player turn fct : " + player_turn);
        player_turn ^= 1;
        System.out.println("update player turn fct : " + player_turn);
    }

    private void display_nb_prisonners(){
        gameInfos.set_black_prisonners(Integer.toString( _map.get((_map.size()-1)).getBlackPrisonners()));
        gameInfos.set_white_prisonners(Integer.toString( _map.get((_map.size()-1)).getWhitePrisonners()));

    }

    private void undoMove(){
        System.out.println("map sie == " + _map.size() + " map index == " + map_index);
        if (_map.size() < 2 || map_index < _map.size() - 1)
        {
            System.out.println("undo move aucune action");
            return ;
        }
        if (_map.get(_map.size() - 1).get_prisonners() != null){
            for (Point p : _map.get(_map.size() - 1).get_prisonners()){
                System.out.println("les prisonniers qui vont etre anules sont : ");
                    System.out.print(p);
                }
                System.out.println();
        }
        Point coord = _map.get(_map.size() - 1).getLastMove();
        map_index -= 1;
        if (rule.hasIa() == true)
            game.remove(coord, _map.get(_map.size() - 1).get_prisonners(), true);
        _map.remove(_map.size() - 1);
        goban.updateFromMap(_map.get(_map.size() - 1));
        // if (player_turn)//CHANGER LE NB PROSONNIERS pour la bonne couleur
        player_turn ^= 1;// le get de la regle 
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("update player turn" + player_turn);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        
        rule.set_black_prisonners(_map.get((_map.size()-1)).getBlackPrisonners());
        rule.set_white_prisonners(_map.get((_map.size()-1)).getWhitePrisonners());
        display_nb_prisonners();
    }

    public Gomoku(int heigh, int width, Home game_infos)/*prendra les regles en paramettre vu que connu au clic*/{
        // fontSizeBinding = (DoubleBinding) Bindings.min(
        //         _game_infos	at org.interfacegui.PenteRules.endGame(PenteRules.java:27)
// .widthProperty().multiply(0.1),
        //         _game_infos.heightProperty().multiply(0.1)
        //     );
//         fontSizeBinding = Bindings.createDoubleBinding(
//     () -> Math.min(heigh, width) * 0.025
// );
        // fontSizeBinding = Bindings.createDoubleBinding(
        //     () -> Math.min(
        //         _game_infos.widthProperty().multiply(0.1).get(),
        //         _game_infos.heightProperty().multiply(0.1).get()
        //     ),
        //     _game_infos.widthProperty(),
        //     _game_infos.heightProperty()
        // );
        // SGF.createSgf(null, "gomoku");
        _game_infos = game_infos;
        init_rules(_game_infos.get_rules(), _game_infos.get_board_size());
        _nb_line = rule.get_board_size();
        game = new Game(game_infos.get_rules(), rule.get_board_size());
        System.out.println("constructeur gomoku rule type == " + rule.getGameType());
        map_index = 0;
        System.out.println("height == " + heigh + " width == " + width);
        _map = new ArrayList<Map>();
        saved = new ArrayList<Point>();
        _map.add(new Map(_nb_line));
        game_display = new Pane();
        _end_popin.setVisible(false);
        _end_popin.setManaged(false);
        //_end_popin.setStyle("-fx-background-color: orange;");
        _replay = new Button("Replay");
        _back_home = new Button("Back Home");

        // _end_popin.setPrefSize(200, 40);
        _end_popin.setLayoutX(10);
        _end_popin.setLayoutY(10);
        // _replay.setPrefWidth(_end_popin.getPrefWidth() / 2);
        // _replay.setLayoutY(heigh * 0.8);
        // _back_home.setPrefWidth(_end_popin.getPrefWidth() / 2);
        // _back_home.setLayoutX(_end_popin.getPrefWidth() / 2);
        _back_home.setLayoutY(heigh * 0.8);
        _end_popin.setFillWidth(true);
        _end_popin.getChildren().addAll(_end_text, _replay, _back_home);
        _game_infos_size_x = width / 4;
        gameInfos = new GameInfos(heigh, _game_infos_size_x, game_infos);
        game_name = new Label(game_infos.get_rules());
        //faire le new gamedisplay donner 1/3 largeur
        _game_infos_size_y = heigh;
        goban = new Goban(heigh, width - _game_infos_size_x, rule.get_board_size());//nb ligne a definir plus tRD //donner 2/3 largeur
        _goban_pane = goban.get_goban();
        _game_infos_pane = gameInfos.getGameInfos();//donner les temps en parametres//donnerl e temps en parametre et des getteur pour cehck la fin del a game //ajouter les temps dans la map aussi
        _game_infos_pane.getChildren().add(0, game_name);
        _game_infos_pane.getChildren().add(0, _end_popin);

            DoubleBinding fontSizeBinding = (DoubleBinding) Bindings.min(
                _game_infos_pane.widthProperty().multiply(0.1),
                _game_infos_pane.heightProperty().multiply(0.1)
            );

        game_name.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
        _goban_pane.setLayoutX(_game_infos_size_x);
        game_display.getChildren().addAll(_game_infos_pane, _goban_pane);
        createDelayedGameLoop();

        gameInfos.getResignButton().setOnAction(event -> {
                gameLoop.stop();
                game_end = true;
                ia_playing = false;
                System.out.println("player turn == " + player_turn);
                _winner = player_turn;
                String res = _winner == 1? "black" : "white";
                _end_text.setText(res + " win");
                _end_popin.setVisible(true);
                _end_popin.setManaged(true);
            
        });

        gameInfos.getUndoButton().setOnAction(event -> {
            if (rule instanceof GoRules){
                if (((GoRules)rule).undo() == false)
                    return ;
                goban.remove_score();
            }
            undoMove();
            if ((_game_infos.get_black_player_type() == 1 || _game_infos.get_white_player_type() == 1) && _map.size() > 1)
                undoMove();
        });
        gameInfos.getExportButton().setOnAction(event -> {
            SGF.createSgf(_map, rule.getGameType());
            //export sgf action
        });

        gameInfos.getPrevButton().setOnAction(event -> {
            if (map_index > 0){
                changeCandidatVisibility(false);
                changeHintVisibility(false);
                eraseForbiddens();
                // changeForbiddenVisibility(false, null);
                forbiddenVisibility = false;
                toggleCandidat = false;
                toggleHint = false;
                map_index--;
                goban.updateFromMap(_map.get(map_index));
            }
            // Action à réaliser lors du clic
            // stones[i][j].setVisible(true/false) et setFill(Color.BLACK/WHITE) parcourrir map et pierres
            System.out.println("Le bouton prev a été cliqué !");
        });
        gameInfos.getNextButton().setOnAction(event -> {
            if (map_index < _map.size() - 1){
                changeCandidatVisibility(false);
                changeHintVisibility(false);
                eraseForbiddens();
                // changeForbiddenVisibility(false, null);
                forbiddenVisibility = false;
                toggleCandidat = false;
                toggleHint = false;
                map_index++;
                goban.updateFromMap(_map.get(map_index));
            }
            // Action à réaliser lors du clic
            System.out.println("Le bouton next a été cliqué !");
        });
        gameInfos.getCandidatsButton().setOnAction(event -> {
            if (ia_playing == true)
                return ;
            toggleCandidat = toggleCandidat == true? false : true;
            changeCandidatVisibility(toggleCandidat);
            changeHintVisibility(false);
            eraseForbiddens();
            // changeForbiddenVisibility(false, null);
            forbiddenVisibility = false;
            toggleHint = false;
        });

        gameInfos.getHintButton().setOnAction(event -> {
            if (rule.hasIa() == false || ia_playing || game_end)
                return ;
            toggleHint = toggleHint == true? false : true;
            if (hintList == null){
                game.best_move(player_turn+1, player_turn+1);
                setHint(game.m.candidat.lst, game.m.values);
            }
            changeCandidatVisibility(false);
            changeHintVisibility(toggleHint);
            eraseForbiddens();
            // changeForbiddenVisibility(false, null);
            forbiddenVisibility = false;
            toggleCandidat = false;
        });
        gameInfos.getForbiddeButton().setOnAction(event -> {
            if (ia_playing /*|| game_end || map_index < _map.size() - 1*/)
                return ;
            changeCandidatVisibility(false);
            changeHintVisibility(false);
            toggleCandidat = false;
            toggleHint = false;
            // int color = _map.get(map_index).get_color() ^ 1;
            System.out.println("map index == " + map_index + " player turn == " + player_turn + " player turn d'apres map == " + _map.get(map_index).get_color());
            ArrayList<Point> points = rule.get_forbiden_moves(_map, map_index, player_turn + 1);
            for (Point point : points){
                changeForbiddenVisibility(forbiddenVisibility, point);
            }
            forbiddenVisibility = forbiddenVisibility == false;
        });
        
            // _end_popin.setVisible(true);
            // _end_popin.setManaged(true);
            // game_end = true;
            // ia_playing = false;
            // gameLoop.stop();
            // System.out.println("winner == " + winner);
            // _winner = winner - 1;
            // String res = _winner == 0 ? "black" : "white";
            // _end_text.setText(res + " win");


        gameInfos.getPassButton().setOnAction(event -> {
            //System.out.println("player tur == " + player_turn);
            GoRules r = (GoRules)rule;
            String res;
            if (r.getGameMode() == Rules.GameMode.ENDGAME){
                int blackScore = r.getBlackScore();
                int whiteScore = r.getWhiteScore();
                if (whiteScore > blackScore)
                    res = "white win";
                else if (whiteScore < blackScore)
                    res = "black win";
                else
                    res = "jigo";
                _end_text.setText(res);
                game_end = true;
                gameLoop.stop();
                _end_popin.setVisible(true);
                _end_popin.setManaged(true);
                return ;
            }
            if (r.pass())
                updatePlayerTurn();
            Map newMap = new Map(_map.get(_map.size() - 1));
            _map.add(newMap);
            map_index++;
            System.out.println("player tur == " + player_turn);
            if (rule.getGameMode() == Rules.GameMode.COUNTING){
                ((GoRules)rule).init_prisonners(_map.get(_map.size() - 1));
                ArrayList<Point> tmp = ((GoRules)rule).getWhitePrisonnersList();
                //System.out//.println("white prisonners == " + tmp);
                for (Point p : tmp)
                    goban.modify_score(p, Color.WHITE);
                tmp = ((GoRules)rule).getBlackPrisonnersList();
                // System.out.println("black prisonners == " + tmp);
                for (Point p : tmp)
                    goban.modify_score(p, Color.BLACK);
                gameInfos.setBlackResults("black res: " + r.getBlackScore());
                gameInfos.setWhiteResults("white res: " + r.getWhiteScore());
                gameInfos.getResultsBox().setVisible(true);
                gameInfos.getResultsBox().setManaged(true);
                return ;
            }
        });


            goban.get_goban().setOnMouseClicked(event -> {
            if (game_end)
                return ;
            int margin_w = goban.get_margin_width();
            int margin_h = goban.get_margin_height();
            int square = goban.getSquareSize();
            int goban_size = square * (_nb_line);
            int width_allowed_margin;
            int height_allowed_margin;
            System.out.println("size squre == " + square + " goba size == " + goban_size);
            if (margin_w < square / 2)
                width_allowed_margin = margin_w;
            else
                width_allowed_margin = square/2;
            if (margin_h < square / 2)
                height_allowed_margin = margin_h;
            else
                height_allowed_margin = square/2;
            // if ()//calculer la marge 1/2 square si marge plus grande toute la marge sinon
            double x = event.getX();
            double y = event.getY();
            System.out.println("Pane cliqué aux coordonnées : (" + x + ", " + y + ")");
            System.out.println("width allowed margon == " + width_allowed_margin + " height allowed margin == " + height_allowed_margin);
            if (x - (margin_w) > goban_size + width_allowed_margin || x < margin_w - width_allowed_margin || y < margin_h - height_allowed_margin || y - (margin_h) > goban_size + height_allowed_margin)
            {    
                System.out.println("coup illegal");
                return ;
            }
            x -= margin_w;
            y -= margin_h;
            if (x < 0)
                x += width_allowed_margin;
            if (y < 0)
                y += height_allowed_margin;
            if (x > goban_size)
                x -= width_allowed_margin;
            if (y > goban_size)
                y -= height_allowed_margin;
            System.out.println("apres ajustemeent marge : (" + x + ", " + y + ")");
            
            x/= square;
            y/= square;

            // if (x < margin_w / 2 || x > goban_size + margin_w + (margin_w / 2) || y < margin_h / 2 || y > goban_size + margin_h + (margin_h / 2))
            // {    
            //     System.out.println("coup illegal");
            //     return ;
            // }
            System.out.println("x- margin == " + (x) + " y - margin == " +  (y));

            x = Math.round(x);
            y = Math.round(y);
            Point new_move = new Point((int)x, (int)y);
            System.out.println("x- margin == " + (x) + " y - margin == " +  (y));
            saved.add(new_move);
            playMove(new_move);
        });

    }

    public void print_history_of_move ()
    {
        System.out.println("History of moves");
        saved.forEach((point) -> System.out.printf("%s ", point.colormove()));
        System.out.println("\nWindow closed\nSystem exited normally");
    }

    public void updateGameDisplay(int new_y, int new_x){
        _game_infos_size_x = new_x / 4;
        _game_infos_size_y = new_y;
        System.out.println("\t\t\tgame info size " + _game_infos_size_x);
        gameInfos.updateGameInfo(new_y, _game_infos_size_x);
        goban.updateGoban(new_y, new_x - _game_infos_size_x);
        _goban_pane.setLayoutX(_game_infos_size_x);
        // _end_popin.setLayoutX(new_x / 2);
        // _end_popin.setLayoutY(_game_infos_size_y / 2);

    }

    public Pane getGameDisplay(){
        System.out.println("\t\t\tgame info size " + _game_infos_size_x);
        _goban_pane.setLayoutX(_game_infos_size_x);
        //  _end_popin.setLayoutX(_game_infos_size_x / 2);
        // _end_popin.setLayoutY(_game_infos_size_y / 2);
        return game_display;
    }

    // public boolean play_move(Point coord){
    //     return true;//check les regles add dans la liste de map + mettr a jour l'affichage et gerer les timmers mettre a jour le last time play changer celui du compteur 
    // }

    public Button get_home_button(){
        return _back_home;
    }

    public Button get_replay_button(){
        return _replay;
    }

    //get setting display
    //get game display
    //get accueil display
}