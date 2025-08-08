package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;

public class HomePage{
    private Pane page;
    private Pane pageContainer;
    private Button load_sgf = new Button("load sgf");
    private Button gomoku;
    private Button renju;
    private Button pente;
    private Button go;
    private Button white_human;
    private Button white_ia;
    private Button black_human;
    private Button black_ia;
    private Pane black_player;
    private Pane white_player;
    private TextField komi_field;
    private TextField handicap_field;
    private TextField white_hours = new TextField("HH");
    private TextField white_min = new TextField("MM");
    private TextField white_sec = new TextField("SS");
    private TextField black_hours = new TextField("HH");
    private TextField black_min = new TextField("MM");
    private TextField black_sec = new TextField("SS");
    private Button white_five_min = new Button("5 min");
    private Button white_three_min = new Button("10 min");
    private Button black_five_min = new Button("5 min");
    private Button black_three_min = new Button("10 min");
    private Label boardSizeLabel = new Label("board size");
    private Button nineSize = new Button("9");
    private Button thirteenSize = new Button("13");
    private Button nineteenSize = new Button("19");
    private VBox boardSizeBox = new VBox(10);
    private HBox boardSizeButtonBox = new HBox(10);
    private Label fileName = new Label("");
    private HBox fileBox = new HBox(10);
    private Text reset = new Text("reset");
    private Button validation = new Button("Start");
    private Button learnOrView = new Button("Learn");
    private HBox LaunchButtons = new HBox();
    private ArrayList<Map> sgfMap;
    private boolean sgfFile = false;
    private Label error_file = new Label();
    private Label error_message = new Label();
    private StringProperty rule_type = new SimpleStringProperty("");
    private Rules rules_instance = null;
    private HBox blackBox = new HBox();
    private Button blackIaEasy = new Button("easy");
    private Button blackIaMedium = new Button("medium");
    private Button blackIaHard = new Button("hard");
    private HBox whiteBox = new HBox();
    private Button whiteIaEasy = new Button("easy");
    private Button whiteIaMedium = new Button("medium");
    private Button whiteIaHard = new Button("hard");
    private Button blackCustom = new Button("custom");
    private Button blackBackToButton = new Button("back");
    private Button whiteCustom = new Button("custom");
    private Button whiteBackToButton = new Button("back");
    private HBox buttonWhiteTime = new HBox();
    private HBox whiteCustomTime = new HBox();
    private HBox buttonBlackTime = new HBox();
    private HBox blackCustomTime = new HBox();

    public Button getWhiteThreeMin(){
        return white_three_min; 
    }

    public Button getWhiteFiveMin(){
        return white_five_min; 
    }

    public Button getBlackThreeMin(){
        return black_three_min; 
    }

    public Button getBlackFiveMin(){
        return black_five_min; 
    }

    public HBox getBlackCustomTime(){
        return blackCustomTime;
    }

    public HBox getWhiteCustomTime(){
        return whiteCustomTime;
    }

    public HBox getBlackButtonTime(){
        return buttonBlackTime;
    } 

    public HBox getWhiteButtonTime(){
        return buttonWhiteTime;
    } 

    public Button getBlackBackButton(){
        return blackBackToButton;
    }

    public Button getWhiteBackButton(){
        return whiteBackToButton;
    }

    public Button getBlackCustom(){
        return blackCustom;
    }

    public Button getWhiteCustom(){
        return whiteCustom;
    }

    public Button getWhiteEasyButton(){
        return whiteIaEasy;
    }

    public Button getWhiteMediumButton(){
        return whiteIaMedium;
    }
    public Button getWhiteHardButton(){
        return whiteIaHard;
    }
    public Button getBlackEasyButton(){
        return blackIaEasy;
    }
    public Button getBlackMediumButton(){
        return blackIaMedium;
    }
    public Button getBlackHardButton(){
        return blackIaHard;
    }


    public HBox getWhiteBox(){
        return whiteBox;
    }

    public HBox getBlackBox(){
        return blackBox;
    }

    HomePage(){
        blackBox.getChildren().addAll(blackIaEasy, blackIaMedium, blackIaHard);
        whiteBox.getChildren().addAll(whiteIaEasy, whiteIaMedium, whiteIaHard);
        blackBox.setManaged(false);
        blackBox.setVisible(false);
        whiteBox.setManaged(false);
        whiteBox.setVisible(false);
        boardSizeBox.getChildren().addAll(boardSizeLabel, boardSizeButtonBox);
        boardSizeButtonBox.getChildren().addAll(nineSize, thirteenSize, nineteenSize);
        LaunchButtons.getChildren().addAll(validation, learnOrView);
        fileBox.getChildren().addAll(fileName, reset);
        fileBox.setManaged(false);
        fileBox.setVisible(false);
        white_hours.setPrefColumnCount(2);
        white_min.setPrefColumnCount(2);
        white_sec.setPrefColumnCount(2);
        black_hours.setPrefColumnCount(2);
        black_min.setPrefColumnCount(2);
        black_sec.setPrefColumnCount(2);
        String selectedColor = "-fx-text-fill: #FFFFFF;";
        String deselectedColor = "-fx-text-fill: #000000;";
        String selectedBackgroundColor = "-fx-background-color: #000000;";
        String deselectedBackgroundColor = "-fx-background-color: #FFFFFF;";
        String deselectedStyle = deselectedBackgroundColor + deselectedColor;
        String selectedStyle = selectedBackgroundColor + selectedColor;
        System.out.println("on passe par ici constructeur home page");
        nineteenSize.setStyle(selectedStyle);
        thirteenSize.setStyle(deselectedStyle);
        nineSize.setStyle(deselectedStyle);
        gomoku = new Button("Gomoku");
        renju = new Button("Renju");
        pente = new Button("Pente");
        go = new Button("Go");
        renju.setManaged(false);
        renju.setVisible(false);
        pageContainer = new StackPane();
        gomoku.setStyle(selectedStyle);
        pente.setStyle(deselectedStyle);
        go.setStyle(deselectedStyle);
        HBox game_button = new HBox(10, gomoku, pente, renju, go);
        white_player = new VBox(5);
        black_player = new VBox(5);
        black_human = new Button("black human");
        black_ia = new Button("black ia");
        black_human.setStyle(selectedStyle);
        black_ia.setStyle(deselectedStyle);
        white_five_min.setStyle(selectedStyle);
        black_five_min.setStyle(selectedStyle);
        Text blackTimeText = new Text("Set Black Info:");
        blackTimeText.setFill(Color.WHITE );
        blackTimeText.setStroke(Color.BLACK);
        blackTimeText.setStrokeWidth(2);
        blackTimeText.setFont(Font.font("System", FontWeight.BOLD, 25));
        buttonBlackTime.getChildren().addAll(black_five_min, black_three_min, blackCustom);
        blackCustomTime.getChildren().addAll(black_hours, black_min, black_sec, blackBackToButton);
        VBox black_info = new VBox(5, blackTimeText, new HBox(5, black_human, black_ia, blackBox));
        VBox black_time_info = new VBox(5, new HBox(5, new Text("time : "),buttonBlackTime , blackCustomTime));
        black_player.getChildren().addAll(black_info, black_time_info);
        white_human = new Button("white human");
        white_ia = new Button("white ia");
        boardSizeBox.setVisible(false);
        boardSizeBox.setManaged(false);
        white_human.setStyle(selectedStyle);
        white_ia.setStyle(deselectedStyle);
        buttonWhiteTime.getChildren().addAll(white_five_min, white_three_min, whiteCustom);
        whiteCustomTime.getChildren().addAll(white_hours, white_min, white_sec, whiteBackToButton);
        VBox white_info = new VBox(5, new Text("Set white Info:"), new HBox(5, white_human, white_ia, whiteBox));
        VBox white_time_info = new VBox(5, new HBox(5, new Text("time : "),buttonWhiteTime , whiteCustomTime));
        white_player.getChildren().addAll(white_info, white_time_info);
        
        komi_field = new TextField("Komi ex : 7.5");
        handicap_field = new TextField("Handicap >= 0 or <= 9");
        komi_field.setVisible(false);
        handicap_field.setVisible(false);
        komi_field.setManaged(false);
        handicap_field.setManaged(false);
        error_file.setVisible(false);
        error_file.setManaged(false);
        error_message.setVisible(false);
        error_message.setManaged(false);
        error_file.setTextFill(Color.RED);
        error_message.setTextFill(Color.RED);
        whiteCustomTime.setManaged(false);
        whiteCustomTime.setVisible(false);
        blackCustomTime.setManaged(false);
        blackCustomTime.setVisible(false);
        page = new VBox(10);
        ((VBox) page).getChildren().addAll(error_message,
            error_file, fileBox, load_sgf, game_button, black_player, white_player, komi_field, boardSizeBox, handicap_field, LaunchButtons);
        pageContainer.getChildren().add(page);
        reset.setOnMouseClicked(e -> {
            deleteFile();
        });
    }

    Pane getHomePage(){
        return pageContainer;
    }
    Button getLoadSgf(){
        return load_sgf;
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

    Button getLearnOrViewButton(){
        return learnOrView;
    }

    TextField getKomiButton(){
        return komi_field;
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
    public TextField get_black_hours(){
        return black_hours;
    }
    public TextField get_black_min(){
        return black_min;
    }
    public TextField get_black_sec(){
        return black_sec;
    }
    public TextField get_white_hours(){
        return white_hours;
    }
    public TextField get_white_min(){
        return white_min;
    }
    public TextField get_white_sec(){
        return white_sec;
    }

    public VBox getBoardSizeBox(){
        return boardSizeBox;
    }

    public Button get9Button(){
        return nineSize;
    }

    public Button get13Button(){
        return thirteenSize;
    }

    public Button get19Button(){
        return nineteenSize;
    }

    public void addFileBox(VBox scrollPane){
        ((Pane) pageContainer).getChildren().add(scrollPane);
        scrollPane.toFront();
    }

    public void set_error(String msg){
        error_message.setText(msg);
        error_message.setVisible(true);
        error_message.setManaged(true);
    }

    public void removeFileBox(){
        ObservableList<Node> children = pageContainer.getChildren();

        children.remove(children.size() - 1);
        if (SGF.parseFile() == false){
            error_file.setText(SGF.get_file_name() + " is not a valid sgf file");
            error_file.setManaged(true);
            error_file.setVisible(true);
            return ;
        }
        else{
            error_file.setManaged(false);
            error_file.setVisible(false);
        }
        sgfMap = SGF.get_game_moves();
        load_sgf.setManaged(false);
        load_sgf.setVisible(false);
        fileName.setText(SGF.get_file_name());
        fileBox.setManaged(true);
        fileBox.setVisible(true);
        learnOrView.setText("view SGF");
        sgfFile = true;
        rule_type.set(SGF.get_game_rule());
        rules_instance = SGF.getRuleInstance();
    }

    public void closeFileBox(){
        ObservableList<Node> children = pageContainer.getChildren();

        children.remove(children.size() - 1);
        error_file.setManaged(false);
        error_file.setVisible(false);
        load_sgf.setManaged(true);
        load_sgf.setVisible(true);
        fileBox.setManaged(false);
        fileBox.setVisible(false);
        learnOrView.setText("learn");
    }


    private void deleteFile(){
        load_sgf.setManaged(true);
        load_sgf.setVisible(true);
        fileName.setText("");
        fileBox.setManaged(false);
        fileBox.setVisible(false);
        learnOrView.setText("learn");
        sgfFile = false;
        rule_type.set("");
    }

    public StringProperty getStringRule(){
        return rule_type;
    }

    public boolean is_sgf(){
        return sgfFile;
    }

    public ArrayList<Map> getSgfMap(){
        return sgfMap;
    }


    public void setSgfMap(ArrayList<Map> map){
        sgfMap = map;
    }

    public Rules getRuleInstance(){
        return rules_instance;
    }

    public void setRulesInstance(Rules r){
        rules_instance = r;
    }


}
