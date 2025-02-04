package org.openjfx;
import javafx.scene.layout.Pane;


public class Gomoku
{
    Pane game_display;
    //plateau de jeu
    //regles de jeu
    //settings choisi
    //info sur les joueurs la partie , temps tour de jeu actuel etc ...
    //stocker arraylist de map
    //minmax avec constructeur approprie
    public Goban goban;
    public Gomoku(int heigh, int width){
        game_display = new Pane();
        //faire le new gamedisplay donner 1/3 largeur
        goban = new Goban(heigh, width, 19);//nb ligne a definir plus tRD //donner 2/3 largeur
    };
    public Pane get_game_display(){

    } 
    //get setting display
    //get game display
    //get accueil display
}