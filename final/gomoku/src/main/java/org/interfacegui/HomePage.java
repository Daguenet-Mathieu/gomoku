package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;



public class HomePage{
    private Pane page;
    private Button gomoku;
    private Button renju;
    private Button pente;
    private Button go;
    private Button validation;
    private Button white_human;
    private Button white_ia;
    private Button black_human;
    private Button black_ia;
    private Pane black_player;
    private Pane white_player;
    private TextField komi_field;
    // komiFeield.setText("7.5"); // Valeur par défaut
    private TextField handicap_field;
    // handiapfield.setText("0"); // Valeur par défaut
    private TextField white_time;
    private TextField black_time;

    HomePage(){
        // Définir la couleur pour les boutons désélectionnés
        String deselectedColor = "-fx-background-color: #ADD8E6;"; // Bleu clair pour désélectionné
        
        System.out.println("on passe par ici constructeur home page");
        
        // Création des boutons de jeu
        gomoku = new Button("Gomoku");
        renju = new Button("Renju");
        pente = new Button("Pente");
        go = new Button("Go");
        validation = new Button("Validation");
        
        // Appliquer le style désélectionné à tous les boutons de jeu
        gomoku.setStyle(deselectedColor);
        renju.setStyle(deselectedColor);
        pente.setStyle(deselectedColor);
        go.setStyle(deselectedColor);
        
        HBox game_button = new HBox(10, gomoku, pente, renju, go);
        
        // Configuration joueur noir
        white_player = new VBox(5);
        black_player = new VBox(5);
        black_human = new Button("black human");
        black_ia = new Button("black ia");
        
        // Appliquer le style désélectionné aux boutons du joueur noir
        black_human.setStyle(deselectedColor);
        black_ia.setStyle(deselectedColor);
        
        black_time = new TextField("10:00");
        VBox black_info = new VBox(5, new Text("Set Black Info:"), new HBox(5, black_human, black_ia));
        VBox black_time_info = new VBox(5, new HBox(5, new Text("time : "), black_time));
        black_player.getChildren().addAll(black_info, black_time_info);
        
        // Configuration joueur blanc
        white_human = new Button("white human");
        white_ia = new Button("white ia");
        
        // Appliquer le style désélectionné aux boutons du joueur blanc
        white_human.setStyle(deselectedColor);
        white_ia.setStyle(deselectedColor);
        
        white_time = new TextField("10:00");
        VBox white_info = new VBox(5, new Text("Set white Info:"), new HBox(5, white_human, white_ia));
        VBox white_time_info = new VBox(5, new HBox(5, new Text("time : "), white_time));
        white_player.getChildren().addAll(white_info, white_time_info);
        
        komi_field = new TextField("Komi ex : 7.5");
        handicap_field = new TextField("Handicap >= 0 or <= 9");
        komi_field.setVisible(false);
        handicap_field.setVisible(false);
        
        page = new VBox(10);
        ((VBox) page).getChildren().addAll(
            game_button, black_player, white_player, validation, komi_field, handicap_field);
    }

    Pane getHomePage(){
        return page;
    }

    Button getGomokuButton(){
        return gomoku;
    }
    Button getPenteButton(){
        return pente;
    }

    Button getGoButton(){
        return go;
    }

    Button getRenjuButton(){
        return renju;
    }

    Button getValidationButton(){
        return validation;
    }
    TextField getKomiButton(){
        return komi_field;
    }

    TextField get_black_time(){
        return black_time;
    }

    TextField get_white_time(){
        return white_time;
    }


    TextField getHandicap(){
        return handicap_field;
    }
    Button getWhiteIaTypeButton(){
        return white_ia;
    }
    Button getWhiteHumanTypeButton(){
        return white_human;
    }
    Button getBlackIaTypeButton(){
        return black_ia;
    }
    Button getBlackHumanTypeButton(){
        return black_human;
    }

}