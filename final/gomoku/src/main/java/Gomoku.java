package org.openjfx;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

public class Gomoku
{
    private Pane game_display;
    //plateau de jeu
    //regles de jeu
    //settings choisi
    //info sur les joueurs la partie , temps tour de jeu actuel etc ...
    //stocker arraylist de map
    //minmax avec constructeur approprie
    private Goban goban;
    private GameInfos gameInfos;
    private int _game_infos_size_x;
    private  Pane _goban_pane;
    private Pane _game_infos_pane;
    public Gomoku(int heigh, int width){
        game_display = new Pane();
        _game_infos_size_x = width / 4;
        gameInfos = new GameInfos(heigh, _game_infos_size_x);
        //faire le new gamedisplay donner 1/3 largeur
        goban = new Goban(heigh, width = _game_infos_size_x, 19);//nb ligne a definir plus tRD //donner 2/3 largeur
        _goban_pane = goban.get_goban();
        _game_infos_pane = gameInfos.getGameInfos();
        _goban_pane.setLayoutX(_game_infos_size_x);
        game_display.getChildren().addAll(_game_infos_pane, _goban_pane);
    }

    public void updateGameDisplay(int new_y, int new_x){
        _game_infos_size_x = new_x / 4;
        System.out.println("\t\t\tgame info size " + _game_infos_size_x);
        gameInfos.updateGameInfo(new_y, _game_infos_size_x);
        goban.updateGoban(new_y, new_x - _game_infos_size_x);
        _goban_pane.setLayoutX(_game_infos_size_x);
    }

    public Pane getGameDisplay(){
        System.out.println("\t\t\tgame info size " + _game_infos_size_x);
        _goban_pane.setLayoutX(_game_infos_size_x);
        return game_display;
    }

    public boolean play_move(Point coord){
        return true;//check les regles add dans la liste de map + mettr a jour l'affichage et gerer les timmers mettre a jour le last time play changer celui du compteur 
    }

    //get setting display
    //get game display
    //get accueil display
}