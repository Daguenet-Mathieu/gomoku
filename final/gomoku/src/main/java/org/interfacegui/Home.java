package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import javafx.scene.control.Button;
//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Home {
    private int white_time = -1;
    private int black_time = -1;
    private int white_player_type = 0;
    private int black_player_type = 0;
    private String rule = "Gomoku";
    private float komi = -1;
    private int handicap = -1;
    
    private HomePage home_page = new HomePage();

    private void changeVisibility(TextField komi, TextField handicap, boolean value){
        komi.setVisible(value);
        handicap.setVisible(value);
    }


    int getTimeIndex(int size){
        if (size == 3)
            return 0;
        else if (size == 1)
            return 2;
        else if (size == 2)
            return 1;
        else
            return -1;
    }

    private void getTimes(){
        int[] time_size = {3600000, 60000, 1};
        String str = home_page.get_black_time().getText();
        List<String> time_parts = Arrays.asList(str.split(":"));
        System.out.println(time_parts.size());
        int size = time_parts.size();
        size = getTimeIndex(size);
        if (size == -1)
            return ;
        System.out.println("split == " + time_parts); // [12, 34, 56, 789]
        System.out.println("black time : " + str);
        for (String part : time_parts) {
            try {
                int nombre = Integer.parseInt(part);
                if (white_time == -1)
                    white_time = 0;
                white_time += nombre * time_size[size];
                System.out.println("Conversion réussie : " + nombre);
            }
            catch (NumberFormatException error) {
                white_time = -1;
                System.out.println("Erreur : la chaîne n'est pas un nombre valide.");
            }
            size +=1 ;
        }

        str = home_page.get_white_time().getText();
        time_parts = Arrays.asList(str.split(":"));
        size = time_parts.size();
        size = getTimeIndex(size);
        if (size == -1)
            return ;
        System.out.println("split == " + time_parts); // [12, 34, 56, 789]

        System.out.println("white time : " + str);
        for (String part : time_parts) {
            try {
                int nombre = Integer.parseInt(part);
                System.out.println("Conversion réussie : " + nombre);
                if (black_time == -1)
                    black_time = 0;
                black_time += nombre * time_size[size];
            }
            catch (NumberFormatException error) {
                System.out.println("Erreur : la chaîne n'est pas un nombre valide.");
                black_time = -1;
            }
            size += 1;
        }
    }

    public Home() {
        // Couleurs pour les boutons
        String selectedColor = "-fx-background-color: #FF0000;"; // Rouge pour sélectionné
        String deselectedColor = "-fx-background-color: #ADD8E6;"; // Bleu clair pour désélectionné

        // Groupe 1: Type de joueur noir
        home_page.getBlackIaTypeButton().setOnAction(e -> {
            System.out.println("black is bot");
            black_player_type = 1;
            
            // Mettre à jour les couleurs
            home_page.getBlackIaTypeButton().setStyle(selectedColor);
            home_page.getBlackHumanTypeButton().setStyle(deselectedColor);
        });

        home_page.getBlackHumanTypeButton().setOnAction(e -> {
            System.out.println("black is human");
            black_player_type = 0;
            
            // Mettre à jour les couleurs
            home_page.getBlackHumanTypeButton().setStyle(selectedColor);
            home_page.getBlackIaTypeButton().setStyle(deselectedColor);
        });

        // Groupe 2: Type de joueur blanc
        home_page.getWhiteIaTypeButton().setOnAction(e -> {
            System.out.println("white is bot");
            white_player_type = 1;
            
            // Mettre à jour les couleurs
            home_page.getWhiteIaTypeButton().setStyle(selectedColor);
            home_page.getWhiteHumanTypeButton().setStyle(deselectedColor);
        });

        home_page.getWhiteHumanTypeButton().setOnAction(e -> {
            System.out.println("white is human");
            white_player_type = 0;
            
            // Mettre à jour les couleurs
            home_page.getWhiteHumanTypeButton().setStyle(selectedColor);
            home_page.getWhiteIaTypeButton().setStyle(deselectedColor);
        });

        // Groupe 3: Règles du jeu
        home_page.getGomokuButton().setOnAction(e -> {
            System.out.println("gomoku");
            changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
            rule = "Gomoku";
            
            // Mettre à jour les couleurs
            home_page.getGomokuButton().setStyle(selectedColor);
            home_page.getPenteButton().setStyle(deselectedColor);
            home_page.getRenjuButton().setStyle(deselectedColor);
            home_page.getGoButton().setStyle(deselectedColor);
        });

        home_page.getPenteButton().setOnAction(e -> {
            System.out.println("Pente");
            changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
            rule = "Pente";
            
            // Mettre à jour les couleurs
            home_page.getPenteButton().setStyle(selectedColor);
            home_page.getGomokuButton().setStyle(deselectedColor);
            home_page.getRenjuButton().setStyle(deselectedColor);
            home_page.getGoButton().setStyle(deselectedColor);
        });

        // Vous avez un doublon pour Pente que j'ai supprimé

        home_page.getRenjuButton().setOnAction(e -> {
            System.out.println("Renju");
            changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
            rule = "Renju";
            
            // Mettre à jour les couleurs
            home_page.getRenjuButton().setStyle(selectedColor);
            home_page.getGomokuButton().setStyle(deselectedColor);
            home_page.getPenteButton().setStyle(deselectedColor);
            home_page.getGoButton().setStyle(deselectedColor);
        });

        home_page.getGoButton().setOnAction(e -> {
            System.out.println("Go");
            changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), true);
            rule = "Go";
            
            // Mettre à jour les couleurs
            home_page.getGoButton().setStyle(selectedColor);
            home_page.getGomokuButton().setStyle(deselectedColor);
            home_page.getPenteButton().setStyle(deselectedColor);
            home_page.getRenjuButton().setStyle(deselectedColor);
        });
        home_page.getValidationButton().setOnAction(e -> {
            System.out.println("Validation clicked");
            if (home_page.getKomiButton().isVisible()) {
                try {
                    komi = Float.parseFloat(home_page.getKomiButton().getText());
                    System.out.println("Komi: " + komi);
                } 
                catch (NumberFormatException ex) {
                    System.out.println("Invalid Komi value");
                    komi = -1;
                }
            }
            
            if (home_page.getHandicap().isVisible()) {
                try {
                    handicap = Integer.parseInt(home_page.getHandicap().getText());
                    if (handicap < 0 || handicap > 9) {
                        throw new NumberFormatException("Handicap out of range");
                    }
                    System.out.println("Handicap: " + handicap);
                } 
                catch (NumberFormatException ex) {
                    System.out.println("Invalid Handicap value");
                    handicap = -1;
                }
            }
            getTimes();
            System.out.println("black time == " + black_time + " white time ==  " + white_time);
        });

    }

    public int get_white_time() {
        return white_time;
    }
    public int get_black_time() {
        return black_time;
    }
    public String get_rules() {
        return rule;
    }
    public int get_white_player_type() {
        return white_player_type;
    }
    public int get_black_player_type() {
        return black_player_type;
    }
    public int get_handicap() {
        return handicap;
    }
    public float get_komi() {
        return komi;
    }
    public Pane getHomePage() {
        return home_page.getHomePage();
    }
    public Button getValidationButton() {
        return home_page.getValidationButton();
    }
    public boolean checkInfoValidity(){
        if (rule == "Go" && komi < 0 || (handicap < 0 && handicap > 9))
            return false;
        return true;
    }
}
