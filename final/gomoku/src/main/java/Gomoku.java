package org.openjfx;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.control.Button;

//coder les free 3et reccup list coup interdits + prisonnier et liste prisonniers et fin de parties sur 10 prisioniers //pente et renju faire des emthodes defaut dans rules
//mettre qulques boutons test dans home choix des regles + temps (faire ecoulement du temps)  + choix du type de joueur noir et blanc( humain ou machine?) +  

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
    private Rules rule = new GomokuRules();
    private Pane _end_popin = new Pane();
    private Button _replay;
    private Button _back_home;
    private Home home_object = new Home(){};

    public void reset_gomoku(){
        _map.clear();
        _map.add(new Map(_nb_line));
        map_index = 0;
        rule = new GomokuRules();
        goban.updateFromMap(_map.get(_map.size() - 1));
        gameInfos.clear();
        _end_popin.setVisible(false);
    }


    public Gomoku(int heigh, int width)/*prendra les regles en paramettre vu que connu au clic*/{
        map_index = 1;
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
        gameInfos = new GameInfos(heigh, _game_infos_size_x);
        //faire le new gamedisplay donner 1/3 largeur
        _game_infos_size_y = heigh;
        goban = new Goban(heigh, _game_infos_size_x, 19);//nb ligne a definir plus tRD //donner 2/3 largeur
        _goban_pane = goban.get_goban();
        _game_infos_pane = gameInfos.getGameInfos();//donner les temps en parametres//donnerl e temps en parametre et des getteur pour cehck la fin del a game //ajouter les temps dans la map aussi
        _goban_pane.setLayoutX(_game_infos_size_x);
        game_display.getChildren().addAll(_game_infos_pane, _goban_pane, _end_popin);
        _end_popin.setLayoutX(_game_infos_size_x / 2);
        _end_popin.setLayoutY(_game_infos_size_y / 2);

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

            System.out.println("x- margin == " + (x) + " y - margin == " +  (y));
            if (!rule.isValidMove(new Point((int)x, (int)y) , _map))
                return ;
            // System.out.println("goban cliqué aux coordonnées : (" + (x - margin_w) / square + ", " + (y - margin_h) / square + ")");
            //check legalite du coup avec les regles si ok ajouter le coup et enlever les prisonniers
            // int i = ((int)x - margin_w) / square;
            // int j = ((int)y - margin_h) / square;
            System.out.println("size map == " + _map.size());
            _map.get(_map.size() -1).printMap();
            System.out.println();
            _map.add(new Map(_map.get(_map.size() - 1)));
            _map.get(_map.size() - 1).addMove(new Point((int)x, (int)y), _map.size() % 2 + 1);
            
            //add 0 si y a des prisonniers
            _map.get(_map.size() -1).printMap();
            System.out.println("size map == " + _map.size());
            goban.updateFromMap(_map.get(_map.size() -1));
            map_index = _map.size() - 1;
            if (rule.endGame(_map.get(_map.size() - 1), new Point((int)x, (int)y))){
                System.out.println("partie finie!");
                _end_popin.setVisible(true);
            }
            else
                System.out.println("non!");
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

    public boolean play_move(Point coord){
        return true;//check les regles add dans la liste de map + mettr a jour l'affichage et gerer les timmers mettre a jour le last time play changer celui du compteur 
    }

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