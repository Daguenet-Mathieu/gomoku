package org.interfacegui;
//import javafx.scene.shape.*;
import java.util.ArrayList;

import org.modelai.Game;

//import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.CornerRadii;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
//import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.utils.Point;


//coder les free 3et reccup list coup interdits + prisonnier et liste prisonniers et fin de parties sur 10 prisioniers //pente et renju faire des emthodes defaut dans rules
//mettre qulques boutons test dans home choix des regles + temps (faire ecoulement du temps)  + choix du type de joueur noir et blanc( humain ou machine?) +  

//mettrre en place fct d'event pour les assigner a chaque creation
//regler le probeleme responsvite et mettre un interval pour que l'ia joue et pour check l'ecoulement du temps //stocker dans map le temps qu'a pris le coup

public class Gomoku
{
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
    private int _game_infos_size_y;
    private  Pane _goban_pane;
    private Pane _game_infos_pane;
    private int _nb_line = 19;
    private int map_index;
    private Rules rule;
    private Pane _end_popin = new Pane();
    private Button _replay;
    private Button _back_home;
    private Home _game_infos = new Home(){};
    private int player_turn = 0;
    private int current_decrement = 0;
    private boolean game_end = false;
    private Timeline gameLoop;
    private Game game;

    public void reset_gomoku(){
        _map.clear();
        _map.add(new Map(_nb_line));
        map_index = 0;
        init_rules(_game_infos.get_rules());
        goban.updateFromMap(_map.get(_map.size() - 1));
        gameInfos.clear();
        gameInfos.reset_infos(_game_infos);
        createDelayedGameLoop();
        _end_popin.setVisible(false);
        player_turn = 0;
        current_decrement = 0;
        game_end = false;
        game = new Game(_game_infos.get_rules());
    }

    public void createDelayedGameLoop() {//se lance au bout de 5s ? check si tour joueur ia si oui appelle fct pou jouer son coup puis ecoule le temps
        gameLoop = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
            //System.out.println("coucou curent turn == " + player_turn + " current decrement == " + current_decrement );
            if (player_turn == 0 && _game_infos.get_black_player_type() == 1)
                playMove(game.best_move(player_turn+1, player_turn+1));
            else if (player_turn == 1 && _game_infos.get_white_player_type() == 1)
                playMove(game.best_move(player_turn, player_turn));
            if (player_turn != current_decrement){
                current_decrement = current_decrement == 0?1:0;
                return ;
            }
            if (player_turn == 0)
                gameInfos.sub_black_time(10);
            else
                gameInfos.sub_white_time(10);
            if (gameInfos.get_black_time() <= 0 || gameInfos.get_white_time() <= 0){
                gameLoop.stop();
                game_end = true;
                //set le vainqueur mettre la pipin de fin de partie
                _end_popin.setVisible(true);
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

    private void init_rules(String rules_type){
        if (rules_type == "Gomoku")
            rule = new GomokuRules();
        if (rules_type == "Pente")
            rule = new PenteRules();
        if (rules_type == "Renju")
            rule = new RenjuRules();
        if (rules_type == "Go")
            rule = new GoRules();
    }

    private void playMove(Point point){
        if (!rule.isValidMove(point , _map))
            return ;
        // System.out.println("goban cliqué aux coordonnées : (" + (x - margin_w) / square + ", " + (y - margin_h) / square + ")");
        //check legalite du coup avec les regles si ok ajouter le coup et enlever les prisonniers
        // int i = ((int)x - margin_w) / square;
        // int j = ((int)y - margin_h) / square;
        System.out.println("size map == " + _map.size());
        _map.get(_map.size() -1).printMap();
        System.out.println();
        _map.add(new Map(_map.get(_map.size() - 1)));
        _map.get(_map.size() - 1).addMove(point, _map.size() % 2 + 1);

        game.move(point, player_turn+1); // To update MinMax.map
        
        //add 0 si y a des prisonniers
        _map.get(_map.size() -1).printMap();
        System.out.println("size map == " + _map.size());
        map_index = _map.size() - 1;
        rule.check_capture(point, _map.get(_map.size() - 1));
        if (rule.endGame(_map.get(_map.size() - 1), point)){
            System.out.println("partie finie!");
            if (_map.size() % 2 == 0)
                System.out.println("noir gagne!");
            else
                System.out.println("blamc gagne!");
            _end_popin.setVisible(true);
            game_end = true;
            gameLoop.stop();
        }
        else
            System.out.println("non!");
        ArrayList<Point> points = rule.GetCapturedStones(point, _map.get(_map.size() - 1));
        for (Point p : points) {
            System.out.println("capture 1 er affichage : " + p);  // Appel automatique à toString()
        }
        points = rule.get_prisonners();
        System.out.println("nbr prisonners : " + points.size());
        for (Point p : points) {
            System.out.println("capture 2 em affichage : " + p);  // Appel automatique à toString()
        }
        _map.get((_map.size()-1)).remove_prisonners(points);
        goban.updateFromMap(_map.get(_map.size() -1));
        player_turn = player_turn == 0?1:0;

    }

    public Gomoku(int heigh, int width, Home game_infos)/*prendra les regles en paramettre vu que connu au clic*/{
        init_rules(game_infos.get_rules());
        game = new Game(game_infos.get_rules());
        _game_infos = game_infos;
        System.out.println("constructeur gomoku rule type == " + rule.getGameType());
        map_index = 1;
        System.out.println("height == " + heigh + " width == " + width);
        _map = new ArrayList<Map>();
        _map.add(new Map(_nb_line));
        game_display = new Pane();
        _end_popin.setVisible(false);
        _end_popin.setStyle("-fx-background-color: orange;");
        _replay = new Button("Replay");
        _back_home = new Button("Back Home");
        _end_popin.getChildren().addAll(_replay, _back_home);
        _end_popin.setPrefSize(100, 40);
        _replay.setPrefWidth(_end_popin.getPrefWidth() / 2);
        _back_home.setPrefWidth(_end_popin.getPrefWidth() / 2);
        _back_home.setLayoutX(_end_popin.getPrefWidth() / 2);
        _game_infos_size_x = width / 4;
        gameInfos = new GameInfos(heigh, _game_infos_size_x, game_infos);
        //faire le new gamedisplay donner 1/3 largeur
        _game_infos_size_y = heigh;
        goban = new Goban(heigh, width - _game_infos_size_x, 19);//nb ligne a definir plus tRD //donner 2/3 largeur
        _goban_pane = goban.get_goban();
        _game_infos_pane = gameInfos.getGameInfos();//donner les temps en parametres//donnerl e temps en parametre et des getteur pour cehck la fin del a game //ajouter les temps dans la map aussi
        _goban_pane.setLayoutX(_game_infos_size_x);
        game_display.getChildren().addAll(_game_infos_pane, _goban_pane, _end_popin);
        _end_popin.setLayoutX(_game_infos_size_x / 2);
        _end_popin.setLayoutY(_game_infos_size_y / 2);
        createDelayedGameLoop();
        gameInfos.getPrevButton().setOnAction(event -> {
            if (map_index > 0){
                map_index--;
                goban.updateFromMap(_map.get(map_index));
            }
            // Action à réaliser lors du clic
            // stones[i][j].setVisible(true/false) et setFill(Color.BLACK/WHITE) parcourrir map et pierres
            System.out.println("Le bouton prev a été cliqué !");
        });
        gameInfos.getNextButton().setOnAction(event -> {
            if (map_index < _map.size() - 1){
                map_index++;
                goban.updateFromMap(_map.get(map_index));
            }
            // Action à réaliser lors du clic
            System.out.println("Le bouton next a été cliqué !");
        });
            goban.get_goban().setOnMouseClicked(event -> {
            if (game_end == true)
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
            playMove(new_move);
        });

    }

    public void updateGameDisplay(int new_y, int new_x){
        _game_infos_size_x = new_x / 4;
        _game_infos_size_y = new_y;
        System.out.println("\t\t\tgame info size " + _game_infos_size_x);
        gameInfos.updateGameInfo(new_y, _game_infos_size_x);
        goban.updateGoban(new_y, new_x - _game_infos_size_x);
        _goban_pane.setLayoutX(_game_infos_size_x);
        _end_popin.setLayoutX(new_x / 2);
        _end_popin.setLayoutY(_game_infos_size_y / 2);

    }

    public Pane getGameDisplay(){
        System.out.println("\t\t\tgame info size " + _game_infos_size_x);
        _goban_pane.setLayoutX(_game_infos_size_x);
         _end_popin.setLayoutX(_game_infos_size_x / 2);
        _end_popin.setLayoutY(_game_infos_size_y / 2);
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