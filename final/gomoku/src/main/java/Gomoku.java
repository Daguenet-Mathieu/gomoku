package org.openjfx;

public class Gomoku
{
    //plateau de jeu
    //regles de jeu
    //settings choisi
    //info sur les joueurs la partie , temps tour de jeu actuel etc ...
    //minmax avec constructeur approprie
    public Goban goban;
    public Gomoku(int size){
        goban = new Goban(size);
    };
}