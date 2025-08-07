package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import java.util.ArrayList;

public class Home {
    private int white_time = 300000;
    private int black_time = 300000;
    private int white_player_type = 0;
    private int black_player_type = 0;
    private String rule = "Gomoku";
    private float komi = -1;
    private int handicap = -1;
    private int level = 3;
    private HomePage home_page = new HomePage();
    private FileBox filebox = new FileBox(home_page);
    int boardSize = -1;
    private Rules.GameMode _gameMode = Rules.GameMode.PLAYING;
    private String errorMsg;

    public void setGameMode(Rules.GameMode gameMode){
        _gameMode = gameMode;
    }

    public Rules.GameMode getGameMode(){
        return _gameMode;
    }

    public int get_board_size(){
        return boardSize;
    }

    public int getLevel(){
        return level;
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

    private void checkTimeVal(StringBuilder hours, StringBuilder min, StringBuilder sec){
        if ("HH".equals(hours.toString())){
            hours.setLength(0);
            hours.append("0");
        }
        if ("MM".equals(min.toString()))
        {
            min.setLength(0);
            min.append("0");
        }
        if ("SS".equals(sec.toString()))
        {
            sec.setLength(0);
            sec.append("0");
        }
            
    }

    public void displayErrorMsg(){
        home_page.set_error(errorMsg);
        errorMsg = "";
    }

    public void setErrorMsg(String msg){
        if (errorMsg != null && errorMsg.isEmpty() == false)
            errorMsg = errorMsg + "\n" + msg;
        else
            errorMsg = msg;
    }

    public String getErrorMsg(){
        return errorMsg;
    }

    private void getTimes(){
        StringBuilder hours;
        StringBuilder min;
        StringBuilder sec;
        System.out.println("white time : " + white_time + " black time : " + black_time);
        if (black_time == 0){
            hours = new StringBuilder(home_page.get_black_hours().getText());
            min = new StringBuilder(home_page.get_black_min().getText());
            sec = new StringBuilder(home_page.get_black_sec().getText());
            checkTimeVal(hours, min, sec);
            try {
                black_time = Integer.parseInt(hours.toString()) * 3600000 + 
                        Integer.parseInt(min.toString()) * 60000 + 
                        Integer.parseInt(sec.toString()) * 1000;
            }
            catch (NumberFormatException e){
                setErrorMsg("invalid black time.");
            }
            if (black_time == 0)
                setErrorMsg("invalid black time.");

        }
        if (white_time == 0){
            hours = new StringBuilder(home_page.get_white_hours().getText());
            min = new StringBuilder(home_page.get_white_min().getText());
            sec = new StringBuilder(home_page.get_white_sec().getText());
            checkTimeVal(hours, min, sec);
            try {
                white_time = Integer.parseInt(hours.toString()) * 3600000 + 
                        Integer.parseInt(min.toString()) * 60000 + 
                        Integer.parseInt(sec.toString()) * 1000;
            }
            catch (NumberFormatException e){
                setErrorMsg("invalid black time.");
            }
            if (white_time == 0)
                setErrorMsg("invalid white time.");
        }
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
        filebox.getCross().setOnAction(e -> {
            home_page.closeFileBox();
        });
        home_page.getBlackFiveMin().setOnAction(e -> {
            black_time = 300000;
            home_page.getBlackFiveMin().setStyle(selectedColor);
            home_page.getBlackThreeMin().setStyle(deselectedColor);
        });
        home_page.getBlackThreeMin().setOnAction(e -> {
            black_time = 600000;
            home_page.getBlackFiveMin().setStyle(deselectedColor);
            home_page.getBlackThreeMin().setStyle(selectedColor);
        });
        home_page.getWhiteFiveMin().setOnAction(e -> {
            white_time = 300000;
            home_page.getWhiteFiveMin().setStyle(selectedColor);
            home_page.getWhiteThreeMin().setStyle(deselectedColor);
        });
        home_page.getWhiteThreeMin().setOnAction(e -> {
            white_time = 600000;
            home_page.getWhiteFiveMin().setStyle(deselectedColor);
            home_page.getWhiteThreeMin().setStyle(selectedColor);
        });

        home_page.getBlackCustom().setOnAction(e -> {
            black_time = 0;
            home_page.getBlackCustomTime().setManaged(true);
            home_page.getBlackCustomTime().setVisible(true);
            home_page.getBlackButtonTime().setManaged(false);
            home_page.getBlackButtonTime().setVisible(false);
            home_page.getBlackThreeMin().setStyle(deselectedColor);
            home_page.getBlackFiveMin().setStyle(selectedColor);
        });
        home_page.getWhiteCustom().setOnAction(e -> {
            white_time = 0;
            home_page.getWhiteCustomTime().setManaged(true);
            home_page.getWhiteCustomTime().setVisible(true);
            home_page.getWhiteButtonTime().setManaged(false);
            home_page.getWhiteButtonTime().setVisible(false);
            home_page.getWhiteThreeMin().setStyle(deselectedColor);
            home_page.getWhiteFiveMin().setStyle(selectedColor);
        });
        home_page.getBlackBackButton().setOnAction(e -> {
            black_time = 300000;
            home_page.getBlackCustomTime().setManaged(false);
            home_page.getBlackCustomTime().setVisible(false);
            home_page.getBlackButtonTime().setManaged(true);
            home_page.getBlackButtonTime().setVisible(true);
        });
        home_page.getWhiteBackButton().setOnAction(e -> {
            white_time = 300000;
            home_page.getWhiteCustomTime().setManaged(false);
            home_page.getWhiteCustomTime().setVisible(false);
            home_page.getWhiteButtonTime().setManaged(true);
            home_page.getWhiteButtonTime().setVisible(true);
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
        home_page.getGomokuButton().setOnAction(e -> {
            home_page.getBoardSizeBox().setVisible(false);
            home_page.getBoardSizeBox().setManaged(false);
            home_page.getBlackIaTypeButton().setManaged(true);
            home_page.getBlackIaTypeButton().setVisible(true);
            home_page.getWhiteIaTypeButton().setManaged(true);
            home_page.getWhiteIaTypeButton().setVisible(true);
            rule = "Gomoku";
            home_page.getGomokuButton().setStyle(selectedColor);
            home_page.getPenteButton().setStyle(deselectedColor);
            home_page.getGoButton().setStyle(deselectedColor);
        });

        home_page.getStringRule().addListener((observable, oldValue, newValue) -> {
            boardSize = SGF.getSize();
            String[] rules_type = new String[] {"gomoku", "pente", "go"};
            Button[] rules_button = new Button[] {
                home_page.getGomokuButton(),
                home_page.getPenteButton(),
                home_page.getGoButton()
            };
            newValue = newValue.toLowerCase();
            int i = 0;
            boolean matched = false;
            while (i < rules_type.length){
                if (rules_type[i].equals(newValue)){
                    rules_button[i].setStyle(selectedColor);
                    matched = true;
                    break;
                }
                else
                    rules_button[i].setStyle(deselectedColor);
                i++;
            }
            if (matched == false){
                rule = "Gomoku";
                return ;
            }
            rule = rules_type[i];
        });

        home_page.getPenteButton().setOnAction(e -> {
            home_page.getBoardSizeBox().setVisible(false);
            home_page.getBoardSizeBox().setManaged(false);
            rule = "Pente";
            home_page.getPenteButton().setStyle(selectedColor);
            home_page.getGomokuButton().setStyle(deselectedColor);
            home_page.getRenjuButton().setStyle(deselectedColor);
            home_page.getGoButton().setStyle(deselectedColor);
            home_page.getBlackIaTypeButton().setManaged(true);
            home_page.getBlackIaTypeButton().setVisible(true);
            home_page.getWhiteIaTypeButton().setManaged(true);
            home_page.getWhiteIaTypeButton().setVisible(true);
        });

        home_page.get9Button().setOnAction(e -> {
            boardSize = 9;
        });

        home_page.get13Button().setOnAction(e -> {
            boardSize = 13;
        });

        home_page.get19Button().setOnAction(e -> {
            boardSize = 19;
        });
        home_page.getGoButton().setOnAction(e -> {
            home_page.getBoardSizeBox().setVisible(true);
            home_page.getBoardSizeBox().setManaged(true);
            rule = "Go";
            home_page.getGoButton().setStyle(selectedColor);
            home_page.getGomokuButton().setStyle(deselectedColor);
            home_page.getPenteButton().setStyle(deselectedColor);
            home_page.getBlackIaTypeButton().setManaged(false);
            home_page.getBlackIaTypeButton().setVisible(false);
            home_page.getWhiteIaTypeButton().setManaged(false);
            home_page.getWhiteIaTypeButton().setVisible(false);
        });

        home_page.getValidationButton().setOnAction(e -> {
            getTimes();
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

    public Button getLearnOrViewButton(){
        return home_page.getLearnOrViewButton();
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

    public void setSgfMap(ArrayList<Map> map){
        home_page.setSgfMap(map);
    }

    public void setRulesInstance(Rules r){
        home_page.setRulesInstance(r);
    }
}
