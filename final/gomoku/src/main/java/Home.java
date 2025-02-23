package org.openjfx;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class Home{
    private int white_time;
    private int black_time;
    private int white_player_type;
    private int black_player_type;
    private int rule;
    private int komi;
    private int handicap;
    
    //private Pane pane
    private HomePage home_page = new HomePage();

    Home(){}

//build le Pane et set les event ou le prendre via le constructeur?

    public int get_white_time(){
        return white_time;
    }
    public int get_black_time(){
        return black_time;
    }
    public int get_rules(){
        return rule;
    }
    public int get_white_player(){
        return white_player_type;
    }

    public int get_black_player(){
        return black_player_type;
    }

    public int get_handicap(){
        return handicap;
    }
    public int get_komi(){
        return komi;
    }

    // public Pane getPage(){
    //     return home_page.page;
    // }

    public Pane getHomePage(){
        return home_page.getHomePage();
    }
    // public Pane get_home_pane(){
    //     return pane;
    // }
    public Button getValidationButton(){
        return home_page.getValidationButton();
    }
}
