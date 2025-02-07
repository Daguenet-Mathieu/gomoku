package org.openjfx;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
// private double tempsRestant = 60.0;  // Temps en secondes
// private double dernierAffichage = 0.0; // Temps du dernier affichage (en secondes)

// Timeline timer = new Timeline(new KeyFrame(Duration.millis(10), e -> {
//     tempsRestant -= 0.01; // Décrémenter de 10ms (0.01s)

//     // Vérifier si 0.01s se sont écoulées depuis la dernière mise à jour
//     if (tempsRestant - dernierAffichage >= 0.01) {
//         dernierAffichage = tempsRestant;  // Mettre à jour la dernière valeur affichée
//         mettreAJourAffichage();  // Mettre à jour l'affichage de l'UI
//     }
    
//     // Si le temps est écoulé, arrêter le timer
//     if (tempsRestant <= 0) {
//         finDuTemps();  // Effectuer l'action quand le temps est écoulé
//         timer.stop();   // Arrêter le timer
//     }
// }));

// timer.setCycleCount(Timeline.INDEFINITE);  // Le timer continue indéfiniment
// timer.play();  // Démarrer le timer


public class GameInfos{
        private Pane _game_infos;
        private Pane _timers;
        private Pane _player1_timer;
        private Pane _player2_timer;
        private Pane _scores;
        private Pane _player_1_score;
        private Pane _player_2_score;
        private Pane _prev_next_button;//addd 2 bouttons
        private int _size_x;
        private int _size_y; 
        private Label _white;
        private Label _black;
        private int white_time = 600000;
        private int black_time = 600000;
        private Label white_time_label;
        private Label black_time_label = new Label();


        private String formatTime(int milliseconds) {//faire une fct hh mm ss et un autre mm ss micro
            int seconds = milliseconds / 1000;
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int remainingSeconds = seconds % 60;
            
            return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
        }
        
        private void addText() {
            _white = new Label("white");
            _white.setLayoutX(_size_x/10);
            _white.setLayoutY(_size_y/10);
            _white.setFont(new Font("Arial", 8));
            
            // Conversion explicite en DoubleBinding
            DoubleBinding fontSizeBinding = (DoubleBinding) Bindings.min(
                _game_infos.widthProperty().multiply(0.1),
                _game_infos.heightProperty().multiply(0.1)
            );
            
            // Lier la propriété de la police à la taille calculée
            _white.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
            
            _game_infos.getChildren().add(_white);


            _black = new Label("black");
            _black.setLayoutX(_size_x/2);
            _black.setLayoutY(_size_y/10);
            _black.setFont(new Font("Arial", 8));
            
            _black.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
            _game_infos.getChildren().add(_black);
            white_time_label = new Label(formatTime(white_time));
            black_time_label = new Label(formatTime(black_time));
            _game_infos.getChildren().add(white_time_label);
            _game_infos.getChildren().add(black_time_label);
            white_time_label.setLayoutX(_size_x/2);
                        _black.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
            white_time_label.setFont(new Font("Arial", 6));
            black_time_label.setFont(new Font("Arial", 6));
            white_time_label.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
            black_time_label.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
        }

    // private void update_text() {
    //     _white.setLayoutX(_size_x/10);
    //     _white.setLayoutY(_size_y/2);
    //     _black.setLayoutX(_size_x/10);
    //     _black.setLayoutY(_size_y/10);
    //     white_time_label.setLayoutX(_size_x/10);
    //     white_time_label.setLayoutY(_size_y/10 + 20);
    //     black_time_label.setLayoutX(_size_x/10);
    //     black_time_label.setLayoutY(_size_y/2 + 20);
   // }
private void update_text() {
   _white.setLayoutX(_size_x/10);
   _white.setLayoutY(_size_y/2);
   _black.setLayoutX(_size_x/10);
   _black.setLayoutY(_size_y/10);

   white_time_label.setLayoutX(_size_x/10);
   white_time_label.layoutYProperty().bind(_white.layoutYProperty().add(
       _white.fontProperty().get().getSize() * 1.5)); // 1.5 = 1 + 0.5 marge
   
   black_time_label.setLayoutX(_size_x/10);
   black_time_label.layoutYProperty().bind(_black.layoutYProperty().add(
       _black.fontProperty().get().getSize() * 1.5)); // 1.5 = 1 + 0.5 marge
}
        public GameInfos(int y, int x){
            _size_x = x;
            _size_y = y;
            _game_infos = new Pane();
            _timers = new Pane();
            _player1_timer = new Pane();
            _player2_timer = new Pane();
            _scores = new Pane();
            _player_1_score = new Pane();
            _player_2_score = new Pane();
            _prev_next_button = new Pane();
            _game_infos.setPrefSize(x, y);
            _game_infos.setBackground(new Background(new BackgroundFill(Color.web("#ADBAC0"), null, null)));
            addText();
        }

        public void updateGameInfo(int new_y, int new_x){
            _size_x = new_x;
            _size_y = new_y;
            _game_infos.setPrefSize(new_x, new_y);
            update_text();
            //modifier taille de tout les elements
        }

        public Pane getGameInfos()
        {
            return _game_infos;
        }

}
