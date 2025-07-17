package org.interfacegui;
// import javafx.scene.shape.*;
// import java.util.ArrayList;
import javafx.scene.paint.Color;
// import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

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
        private VBox _game_infos;

        // private Pane _timers;
        // private Pane _player1_timer;
        // private Pane _player2_timer;
        // private Pane _scores;
        // private Pane _player_1_score;
        // private Pane _player_2_score;
        // private Pane _prev_next_button;//addd 2 bouttons
        //private DoubleBinding fontSizeBinding;
        private int _size_x;
        private int _size_y; 
        private Label _white;
        private Label _black;
        private String _black_prisonners;
        private String _white_prisonners;
        private Label _black_label_prisonners = new Label();
        private Label _white_label_prisonners = new Label();

        private int white_time = 600000;
        private int black_time = 600000;
        private Label white_time_label;
        private Label black_time_label = new Label();
        private Label _last_move_label = new Label("last move time : ");
        private Label _average_white_label = new Label("move time : ");
        private Label _average_black_label = new Label("move time : ");
        private Button _resign;
        private Button _undo = new Button("undo");
        private Button _export = new Button("export");
        private Button _prev;
        private Button _next;
        private Button _pass = new Button("pass");
        private Button _candidats;
        private Button _hint;
        private Button _forbidden = new Button("forbiddens");
        private Label _whiteResults = new Label();
        private Label _blackResults = new Label();
        private VBox _results = new VBox();

        // private int    board_size = 19;

        // public void setBoardSize(int newValue)
        // {
        //     board_size = newValue;
        // }

        // public int getBoardSize()
        // {
        //     return board_size;
        // }

        public void setWhiteResults(String res){
            _whiteResults.setText(res);
        }

        public void setBlackResults(String res){
            _blackResults.setText(res);
        }

        public VBox getResultsBox(){
            return _results;
        }

        private HBox _button_prev_next = new HBox();

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
            if (milliseconds <= 0) {
                return "00:00";
            }
            int remaining_milliseconds = (milliseconds % 1000)/10;
            int total_seconds = milliseconds / 1000;
            int hours = total_seconds / 3600;
            int minutes = (total_seconds % 3600) / 60;
            int seconds = total_seconds % 60;
            // System.out.printlen("milisecnd total == " + milliseconds + " remaining miliseconds == " + remaining_milliseconds + " second == " + seconds + " minute == " + minutes + " hours == " + hours);
            if (hours > 0) {
                return String.format("%02d:%02d", hours, minutes); // HH:MM
            }
            else if (minutes > 0) {
                return String.format("%02d:%02d", minutes, seconds); // MM:SS
            }
            else {
                return String.format("%02d.%02d", seconds, remaining_milliseconds); // SS:MS (centièmes)
            }
        }

        public void set_last_move_time(int val){
            _last_move_label.setText("last time : " + formatTime(val));
        }

        public void set_average_white_time(int val){
            _average_white_label.setText("white avr : " + formatTime(val));
        }

        public void set_average_black_time(int val){
            _average_black_label.setText("black avr : " + formatTime(val));
        }


        public void set_white_prisonners(String str){
            _white_label_prisonners.setText("prisonners : " + str);
        }


        public void set_black_prisonners(String str){
            _black_label_prisonners.setText("prisonners : " + str);
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
            _last_move_label.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));

            _average_white_label.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));

            _average_black_label.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));

            _game_infos.getChildren().add(_white);

            _black_prisonners = "prisonners: 0";
            _white_prisonners = "prisonners: 0";
            _black_label_prisonners.setText(_black_prisonners);
            _white_label_prisonners.setText(_white_prisonners);
            _black = new Label("black");
            _black.setLayoutX(_size_x/2);
            _black.setLayoutY(_size_y/10);
            _black.setFont(new Font("Arial", 8));
            
            _black.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
            white_time_label = new Label(formatTime(white_time));
            black_time_label = new Label(formatTime(black_time));
            _game_infos.getChildren().add(_white_label_prisonners);
            _game_infos.getChildren().add(white_time_label);
            _game_infos.getChildren().add(_black);
            _game_infos.getChildren().add(_black_label_prisonners);
            _game_infos.getChildren().add(black_time_label);
            white_time_label.setLayoutX(_size_x/2);
                        _black.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
            white_time_label.setFont(new Font("Arial", 6));
            black_time_label.setFont(new Font("Arial", 6));
            _white_label_prisonners.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
            _black_label_prisonners.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
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
        // private void update_text() {
        // _white.setLayoutX(_size_x/10);
        // _white.setLayoutY(_size_y/2);
        // _black.setLayoutX(_size_x/10);
        // _black.setLayoutY(_size_y/10);

        // white_time_label.setLayoutX(_size_x/10);
        // white_time_label.layoutYProperty().bind(_white.layoutYProperty().add(
        //     _white.fontProperty().get().getSize() * 1.5)); // 1.5 = 1 + 0.5 marge
        
        // black_time_label.setLayoutX(_size_x/10);
        // black_time_label.layoutYProperty().bind(_black.layoutYProperty().add(
        //     _black.fontProperty().get().getSize() * 1.5)); // 1.5 = 1 + 0.5 marge
        // }

        public GameInfos(int y, int x, Home infos){
            black_time = infos.get_black_time();
            white_time = infos.get_white_time();

            _size_x = x;
            _size_y = y;

            _game_infos = new VBox();
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
            _resign = new Button("resign");
            _prev = new Button("<");
            _next = new Button(">");
            _candidats = new Button("show candidats");
            _hint = new Button("hint");
            _prev.setPadding(Insets.EMPTY);
            _next.setPadding(Insets.EMPTY);

            // Définir la position du bouton dans le Pane
            // _prev.setLayoutX(_size_x/10);
            // _prev.setLayoutY(_size_y - _prev.getHeight() - 10);
            _prev.setPrefWidth(_size_x / 2 - (_size_x/10));
            _prev.setFont(javafx.scene.text.Font.font("Arial", 20));
            
            _next.setLayoutX(_size_x/10 + (_size_x / 2 - (_size_x/10)));
            _next.setLayoutY(_size_y - _next.getHeight() - 10);
            _next.setPrefWidth(_size_x / 2 - (_size_x/10));
            _next.setFont(javafx.scene.text.Font.font("Arial", 20));

            // Ajouter le bouton au Pane
            // _prev.setPadding(new Insets(0,0,0,0));
            _results.getChildren().addAll(_whiteResults, _blackResults);
            _results.setVisible(false);
            _results.setManaged(false);
            _button_prev_next.getChildren().addAll(_prev, _next);
            _game_infos.getChildren().addAll(_last_move_label, _average_white_label, _average_black_label,_candidats, _hint, _forbidden, _resign, _undo);
            _game_infos.getChildren().addAll(_button_prev_next, _pass, _results, _export);

        }

        public void updateGameInfo(int new_y, int new_x){
            _size_x = new_x;
            _size_y = new_y;
            _game_infos.setPrefSize(new_x, new_y);
            // update_text();
            // _prev.setLayoutX(_size_x/10);
            // _prev.setLayoutY(_size_y - _prev.getHeight() - 10);
            // _prev.setPrefWidth(_size_x / 2 - (_size_x/10));

            // _next.setLayoutX(_size_x/10 + (_size_x / 2 - (_size_x/10)));
            // _next.setLayoutY(_size_y - _next.getHeight() - 10);
            // _next.setPrefWidth(_size_x / 2 - (_size_x/10));
            // _next.setFont(javafx.scene.text.Font.font("Arial", 20));

            // _prev.setTextAlignment(javafx.geometry.Pos.CENTER);
            // _prev.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
            // Centrer le texte horizontalement
            // _prev.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            // _prev.setAlignment(Pos.CENTER);

// Centrer le contenu du bouton (texte et image) verticalement et horizontalement
            // _prev.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
            //modifier taille de tout les elements
        }

        public Button getForbiddeButton()
        {
            return _forbidden;
        }

        public VBox getGameInfos()
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
        public Button getCandidatsButton()
        {
            return _candidats;
        }

        public Button getResignButton()
        {
            return _resign;
        }

        public Button getUndoButton()
        {
            return _undo;
        }

        public Button getExportButton()
        {
            return _export;
        }

        public Button getHintButton()
        {
            return _hint;
        }

        public Button getPassButton()
        {
            return _pass;
        }


        public void clear(){
            // reinitialiser le texte du temps et les variables
            //reinitioaliser l'affichage des prisonnier // peut etre a faire au niveau superieur?
        }
}
