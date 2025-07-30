package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import javafx.beans.property.StringProperty;


public class Home {
    private int white_time = 0;
    private int black_time = 0;
    private int white_player_type = 0;
    private int black_player_type = 0;
    private String rule = "Gomoku";
    private float komi = -1;
    private int handicap = -1;
    private ArrayList<Map> sgf;
    private int level = 3;
    private HomePage home_page = new HomePage();
    private FileBox filebox = new FileBox(home_page);
    int boardSize = -1;

    public int get_board_size(){
        return boardSize;
    }

    public int getLevel(){
        return level;
    }

    private void changeVisibility(TextField komi, TextField handicap, boolean value){
        komi.setVisible(value);
        handicap.setVisible(value);
        komi.setManaged(value);
        handicap.setManaged(value);
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
        
        String hours = home_page.get_black_hours().getText();
        String min = home_page.get_black_min().getText();
        String sec = home_page.get_black_sec().getText();
        try {
            black_time = Integer.parseInt(hours) * 3600000 + 
                    Integer.parseInt(min) * 60000 + 
                    Integer.parseInt(sec) * 1000;
        }
        catch (NumberFormatException e){
            System.out.println("erreur parsing black time mettre message d'erreur et ne pas valider");
        }
        hours = home_page.get_white_hours().getText();
        min = home_page.get_white_min().getText();
        sec = home_page.get_white_sec().getText();

        try {
            white_time = Integer.parseInt(hours) * 3600000 + 
                    Integer.parseInt(min) * 60000 + 
                    Integer.parseInt(sec) * 1000;
        }
        catch (NumberFormatException e){
            System.out.println("erreur parsing white time mettre message d'erreur et ne pas valider");
        }
        if (black_time == 0)
            black_time = 10 * 60 * 1000;
        if (white_time == 0)
            white_time = 10 * 60 * 1000;
    }

    void resetButtonDifficulty(String deselectedColor){
        home_page.getWhiteEasyButton().setStyle(deselectedColor);
        home_page.getWhiteMediumButton().setStyle(deselectedColor);
        home_page.getWhiteHardButton().setStyle(deselectedColor);
        home_page.getBlackEasyButton().setStyle(deselectedColor);
        home_page.getBlackMediumButton().setStyle(deselectedColor);
        home_page.getBlackHardButton().setStyle(deselectedColor);
    }


    public Home() {
        String selectedColor = "-fx-background-color: #FF0000;";
        String deselectedColor = "-fx-background-color: #ADD8E6;";
        home_page.getLoadSgf().setOnAction(e -> {
            home_page.addFileBox(filebox.getFileBox());
        });


        home_page.getWhiteEasyButton().setStyle(selectedColor);
        home_page.getBlackEasyButton().setStyle(selectedColor);
        home_page.getBlackIaTypeButton().setOnAction(e -> {
            System.out.println("black is bot");
            black_player_type = 1;
            home_page.getBlackBox().setManaged(true);
            home_page.getBlackBox().setVisible(true);
            home_page.getBlackIaTypeButton().setStyle(selectedColor);
            home_page.getBlackHumanTypeButton().setStyle(deselectedColor);
        });

        home_page.getBlackHumanTypeButton().setOnAction(e -> {
            System.out.println("black is human");
            black_player_type = 0;
            home_page.getBlackBox().setManaged(false);
            home_page.getBlackBox().setVisible(false);

            home_page.getBlackHumanTypeButton().setStyle(selectedColor);
            home_page.getBlackIaTypeButton().setStyle(deselectedColor);
        });

        home_page.getWhiteIaTypeButton().setOnAction(e -> {
            System.out.println("white is bot");
            white_player_type = 1;
            home_page.getWhiteBox().setManaged(true);
            home_page.getWhiteBox().setVisible(true);

            home_page.getWhiteIaTypeButton().setStyle(selectedColor);
            home_page.getWhiteHumanTypeButton().setStyle(deselectedColor);
        });

        home_page.getWhiteHumanTypeButton().setOnAction(e -> {
            System.out.println("white is human");
            white_player_type = 0;
            home_page.getWhiteBox().setManaged(false);
            home_page.getWhiteBox().setVisible(false);

            // Mettre à jour les couleurs
            home_page.getWhiteHumanTypeButton().setStyle(selectedColor);
            home_page.getWhiteIaTypeButton().setStyle(deselectedColor);
        });
        home_page.getWhiteEasyButton().setOnAction(e -> {
            level = 3;
            resetButtonDifficulty(deselectedColor);
            home_page.getBlackEasyButton().setStyle(selectedColor);
            home_page.getWhiteEasyButton().setStyle(selectedColor);
        });
        home_page.getWhiteMediumButton().setOnAction(e -> {
            level = 2;
            resetButtonDifficulty(deselectedColor);
            home_page.getBlackMediumButton().setStyle(selectedColor);
            home_page.getWhiteMediumButton().setStyle(selectedColor);
        });
        home_page.getWhiteHardButton().setOnAction(e -> {
            level = 1;
            resetButtonDifficulty(deselectedColor);
            home_page.getBlackHardButton().setStyle(selectedColor);
            home_page.getWhiteHardButton().setStyle(selectedColor);
        });
        home_page.getBlackEasyButton().setOnAction(e -> {
            level = 3;
            resetButtonDifficulty(deselectedColor);
            home_page.getBlackEasyButton().setStyle(selectedColor);
            home_page.getWhiteEasyButton().setStyle(selectedColor);
        });
        home_page.getBlackMediumButton().setOnAction(e -> {
            level = 2;
            resetButtonDifficulty(deselectedColor);
            home_page.getBlackMediumButton().setStyle(selectedColor);
            home_page.getWhiteMediumButton().setStyle(selectedColor);
        });
        home_page.getBlackHardButton().setOnAction(e -> {
            level = 1;
            resetButtonDifficulty(deselectedColor);
            home_page.getBlackHardButton().setStyle(selectedColor);
            home_page.getWhiteHardButton().setStyle(selectedColor);
        });
        // Groupe 3: Règles du jeu
        home_page.getGomokuButton().setOnAction(e -> {
            System.out.println("gomoku");
            changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
            home_page.getBoardSizeBox().setVisible(false);
            home_page.getBoardSizeBox().setManaged(false);
            rule = "Gomoku";
            
            // Mettre à jour les couleurs
            home_page.getGomokuButton().setStyle(selectedColor);
            home_page.getPenteButton().setStyle(deselectedColor);
            home_page.getRenjuButton().setStyle(deselectedColor);
            home_page.getGoButton().setStyle(deselectedColor);
        });

        home_page.getStringRule().addListener((observable, oldValue, newValue) -> {
            boardSize = SGF.getSize();
            String[] rules_type = new String[] {"gomoku", "pente", "renju", "go"};
            Button[] rules_button = new Button[] {
                home_page.getGomokuButton(),
                home_page.getPenteButton(),
                home_page.getRenjuButton(),
                home_page.getGoButton()
            };
            newValue = newValue.toLowerCase();
            int i = 0;
            boolean matched = false;
            while (i < rules_type.length){
                System.out.println("newValue == " + newValue + " tested rules " + rules_type[i]);
                if (rules_type[i].equals(newValue)){
                    rules_button[i].setStyle(selectedColor);
                    matched = true;
                    break;
                }
                else
                    rules_button[i].setStyle(deselectedColor);
                i++;
            }
            System.out.println("i == " + i + "matchd == " + matched);
            if (matched == false){
                System.out.println("default rules reset sur string event");
                rule = "Gomoku";
                return ;
            }
            System.out.println("new rules set sur string event");
            // System.out.println("gomoku");
            if ("go".equals(rules_type[i])){
                changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), true);
            }
            else{
                changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
            }
            rule = rules_type[i];
        });

        home_page.getPenteButton().setOnAction(e -> {
            System.out.println("Pente");
            changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
            home_page.getBoardSizeBox().setVisible(false);
            home_page.getBoardSizeBox().setManaged(false);
            rule = "Pente";
            
            // Mettre à jour les couleurs
            home_page.getPenteButton().setStyle(selectedColor);
            home_page.getGomokuButton().setStyle(deselectedColor);
            home_page.getRenjuButton().setStyle(deselectedColor);
            home_page.getGoButton().setStyle(deselectedColor);
        });

        home_page.get9Button().setOnAction(e -> {
            System.out.println("9");
            boardSize = 9;
            // changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
        });

        home_page.get13Button().setOnAction(e -> {
            System.out.println("13");
            boardSize = 13;
            // changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
        });

        home_page.get19Button().setOnAction(e -> {
            System.out.println("19");
            boardSize = 19;
            // changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
        });
        // Vous avez un doublon pour Pente que j'ai supprimé

        home_page.getRenjuButton().setOnAction(e -> {
            System.out.println("Renju");
            changeVisibility(home_page.getKomiButton(), home_page.getHandicap(), false);
            home_page.getBoardSizeBox().setVisible(false);
            home_page.getBoardSizeBox().setManaged(false);

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
            home_page.getBoardSizeBox().setVisible(true);
            home_page.getBoardSizeBox().setManaged(true);
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
    public void set_file(){

    }
    
    public void remove_file(){
        
    }

    public ArrayList<Map> getSgfMap(){
        return home_page.getSgfMap();
    }

    public Rules getRuleInstance(){
        return home_page.getRuleInstance();
    }



}
