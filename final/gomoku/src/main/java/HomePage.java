package org.openjfx;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


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
        gomoku = new Button("Gomoku");
        renju = new Button("Renju");
        pente = new Button("Pente");
        go = new Button("Go");
        validation = new Button("Validation");
        white_player = new Pane();
        black_player = new Pane();
        black_human = new Button("black human");
        black_ia = new Button("back ia");
        black_player.getChildren().addAll(black_human, black_ia);
        white_human = new Button("white human");
        white_ia = new Button("white ia");
        white_time = new TextField("10:00");
        black_time = new TextField("10:00");
        komi_field = new TextField();
        handicap_field = new TextField();
        white_player.getChildren().addAll(white_human, white_ia);
        black_ia.translateXProperty().bind(black_human.widthProperty());

        white_ia.translateXProperty().bind(white_human.widthProperty());

        // Initialisation des champs de texte
        komi_field.setText("7.5");  // Valeur par défaut
        handicap_field.setText("0");  // Valeur par défaut

        komi_field.setVisible(false);
        komi_field.setVisible(false);

        // Crée un conteneur (ici un VBox) pour organiser les éléments
        page = new VBox(10);  // Espacement entre les éléments
        ((VBox) page).getChildren().addAll(
            gomoku, renju, pente, go, black_player, white_player, validation, white_time, black_time
        );
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

    Button get_PenteButton(){
        return pente;
    }

    Button getValidationButton(){
        return validation;
    }
    TextField getKomiButton(){
        return komi_field;
    }
    TextField getHandicap(){
        return handicap_field;
    }

}