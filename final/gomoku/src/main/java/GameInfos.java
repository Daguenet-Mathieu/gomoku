package org.openjfx;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class GameInfos{
        private Pane _game_infos;
        private Pane _timers;
        private Pane _player1_timer;
        private Pane _player2_timer;
        private Pane _scores;
        private Pane _player_1_score;
        private Pane _player_2_score;
        private Pane _prev_next_button;//addd 2 bouttons
        GameInfo(int board_size){
            game_infos;
            timers = new Pane();
            player1_timer = new Pane();
            player2_timer = new Pane();
            scores = new Pane();
            player_1_score = new Pane();
            player_2_score = new Pane();
            prev_next_button = new Pane();
        };

}
