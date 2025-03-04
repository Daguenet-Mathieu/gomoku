package org.interfacegui;
// import javafx.scene.shape.*;
// import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
// import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
// import javafx.scene.layout.CornerRadii;
// import javafx.animation.Timeline;
// import javafx.animation.KeyFrame;
// import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
// import javafx.scene.text.TextAlignment;
// import javafx.geometry.Pos;
// import javafx.geometry.Insets;

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

        // private Pane _timers;
        // private Pane _player1_timer;
        // private Pane _player2_timer;
        // private Pane _scores;
        // private Pane _player_1_score;
        // private Pane _player_2_score;
        // private Pane _prev_next_button;//addd 2 bouttons

        private int _size_x;
        private int _size_y; 
        private Label _white;
        private Label _black;
        private int white_time = 600000;
        private int black_time = 600000;
        private Label white_time_label;
        private Label black_time_label = new Label();
        private Button _prev;
        private Button _next;

        public void set_black_time(int time){
            black_time = time;
            black_time_label.setText(formatTime(black_time));
        }
        
        public void set_white_time(int time){
            white_time = time;
            white_time_label.setText(formatTime(white_time));
        }

        public void sub_white_time(int time){
            white_time -= time;
            white_time_label.setText(formatTime(white_time));
        }

        public void sub_black_time(int time){
            black_time -= time;
            black_time_label.setText(formatTime(black_time));
        }

        public int get_black_time(){
            return black_time;
        }

        public int get_white_time(){
            return white_time;
        }
        public void reset_infos(Home infos){
            black_time = infos.get_black_time();
            white_time = infos.get_white_time();
            set_white_time(white_time);
            set_black_time(black_time);

            //nb move
            //player turn
            //prisonners 
            //autres infos? 
        }

        private String formatTime(int milliseconds) {//faire une fct hh mm ss et un autre mm ss micro
            
            if (milliseconds < 0) {
                return "00:00";
            }
            int remaining_milliseconds = (milliseconds % 1000) / 100;
            int total_seconds = milliseconds / 1000;
            int hours = total_seconds / 3600;
            int minutes = (total_seconds % 3600) / 60;
            int seconds = total_seconds % 60;
            //System.out.println("milisecnd total == " + milliseconds + " remaining miliseconds == " + remaining_milliseconds + " second == " + seconds + " minute == " + minutes + " hours == " + hours);
            if (hours > 0) {
                return String.format("%02d:%02d", hours, minutes); // HH:MM
            } else if (minutes > 0) {
                return String.format("%02d:%02d", minutes, seconds); // MM:SS
            } else {
                return String.format("%02d:%02d", seconds, remaining_milliseconds); // SS:MS (centièmes)
            }
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

        public GameInfos(int y, int x, Home infos){
            black_time = infos.get_black_time();
            white_time = infos.get_white_time();

            _size_x = x;
            _size_y = y;

            _game_infos = new Pane();
            // _timers = new Pane();
            // _player1_timer = new Pane();
            // _player2_timer = new Pane();
            // _scores = new Pane();
            // _player_1_score = new Pane();
            // _player_2_score = new Pane();
            // _prev_next_button = new Pane();

            _game_infos.setPrefSize(x, y);
            _game_infos.setBackground(new Background(new BackgroundFill(Color.web("#ADBAC0"), null, null)));
            addText();
            _prev = new Button("<");
            _next = new Button(">");
            _prev.setPadding(Insets.EMPTY);
            _next.setPadding(Insets.EMPTY);

            // Définir la position du bouton dans le Pane
            _prev.setLayoutX(_size_x/10);
            _prev.setLayoutY(_size_y - _prev.getHeight() - 10);
            _prev.setPrefWidth(_size_x / 2 - (_size_x/10));
            _prev.setFont(javafx.scene.text.Font.font("Arial", 20));
            
            _next.setLayoutX(_size_x/10 + (_size_x / 2 - (_size_x/10)));
            _next.setLayoutY(_size_y - _next.getHeight() - 10);
            _next.setPrefWidth(_size_x / 2 - (_size_x/10));
            _next.setFont(javafx.scene.text.Font.font("Arial", 20));

            // Ajouter le bouton au Pane
            // _prev.setPadding(new Insets(0,0,0,0));
            _game_infos.getChildren().add(_prev);
            _game_infos.getChildren().add(_next);

        }

        public void updateGameInfo(int new_y, int new_x){
            _size_x = new_x;
            _size_y = new_y;
            _game_infos.setPrefSize(new_x, new_y);
            update_text();
            _prev.setLayoutX(_size_x/10);
            _prev.setLayoutY(_size_y - _prev.getHeight() - 10);
            _prev.setPrefWidth(_size_x / 2 - (_size_x/10));

            _next.setLayoutX(_size_x/10 + (_size_x / 2 - (_size_x/10)));
            _next.setLayoutY(_size_y - _next.getHeight() - 10);
            _next.setPrefWidth(_size_x / 2 - (_size_x/10));
            _next.setFont(javafx.scene.text.Font.font("Arial", 20));

            // _prev.setTextAlignment(javafx.geometry.Pos.CENTER);
            // _prev.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
            // Centrer le texte horizontalement
            // _prev.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            // _prev.setAlignment(Pos.CENTER);

// Centrer le contenu du bouton (texte et image) verticalement et horizontalement
            // _prev.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
            //modifier taille de tout les elements
        }

        public Pane getGameInfos()
        {
            return _game_infos;
        }
        public Button getPrevButton()
        {
            return _prev;
        }
        public Button getNextButton()
        {
            return _next;
        }
        public void clear(){
            // reinitialiser le texte du temps et les variables
            //reinitioaliser l'affichage des prisonnier // peut etre a faire au niveau superieur?
        }
}
