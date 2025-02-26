package org.openjfx;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.text.Text;

public class App extends Application {
    private Gomoku gomoku;
    private Stage stage;
    private Pane root;
    private Scene scene;
    private Point screen = new Point();
    private int size_y;
    private int size_x;
    Pane goban_root;
    private Scene goban, home, settings;//les mettre dans les classes correspondantes
    private Pane home_body = new Pane();
    private Pane home_root = new Pane();
    private Home home_page = new Home();
    //class settings pour connaitre les preference du joueur couleur des pierres goban etc et donner au gomoku
    //choix temps dans le home en meme temps que le choix des regles et des joueurs

    public void set_home_event(){
        home.widthProperty().addListener((observable, oldValue, newValue) -> {
            // home.setPrefWidth(newValue.doubleValue());
            System.out.println("redimentionnement de home!!!!!!!!!!!!!!!!!1");
            // home_body.setPrefHeight((int)newValue);
        });
        home.heightProperty().addListener((observable, oldValue, newValue) -> {
            // home.setPrefHeight(newValue.doubleValue());
            // home_body.setPrefHeight((int)newValue);
            System.out.println("redimentionnement de home!!!!!!!!!!!!!!!!!1");

        });
        home_page.getValidationButton().setOnMouseClicked(event -> {
            double scenex = stage.getWidth();
            double sceney = stage.getHeight();
            System.out.println(" goban width == " + scenex + " heigh " + sceney);
            //recuperer les datas si invalide les mettres en valeurs et laisserl a page telle qu'elle
            gomoku= new Gomoku((int)sceney, (int)scenex, home_page); //appeler fct qui set toutes les infos
            goban_root = new Pane();
            goban = new Scene(goban_root, scenex, sceney);
            set_goban_event();
            goban_root.getChildren().add(gomoku.getGameDisplay()); // Ajout du texte à home_root
            switchScene(goban);
        });
    }

    private void set_goban_event(){
        gomoku.get_home_button().setOnMouseClicked(event -> {
            System.out.println("promis un jour ce bouton sera fonctionnel");
            double scenex = stage.getWidth();
            double sceney = stage.getHeight();
            System.out.println("hone width == " + scenex + " heigh " + sceney);
            home_root = new Pane();
            home = new Scene(home_root, scenex, sceney);
            home_page = new Home();
            home_root.setStyle("-fx-background-color:rgb(71, 157, 255);");
            home_root.getChildren().add(home_page.getHomePage());
            set_home_event();
            switchScene(home);
            // Text homeText2 = new Text(100, 100, "Bienvenue sur la scène Home!");
            // homeText2.setFill(Color.BLACK);
            // home_root.getChildren().add(homeText2); // Ajout du texte à home_root
            // home_root.getChildren().add(home_body);
            // home.setFill(Color.web("#FF6347"));

            home_root.getChildren().add(home_body);

        });

        gomoku.get_replay_button().setOnMouseClicked(event -> {
            gomoku.reset_gomoku();
        });

        goban.widthProperty().addListener((observable, oldValue, newValue) -> {
            double sceneWidth = goban.getWidth();
            double sceneHeight = goban.getHeight();
            // goban_root.getChildren().add(gomoku.getGameDisplay());

            System.out.println("size x == " + size_x + " size y == " + size_y);
            screen.x = (int)newValue.doubleValue();
            //int new_size = get_size(screen.x, screen.y);
            gomoku.updateGameDisplay((int)sceneHeight, (int)sceneWidth);
            // System.out.println("New width: " + newValue);
        });
        goban.heightProperty().addListener((observable, oldValue, newValue) -> {
            double sceneWidth = goban.getWidth();
            double sceneHeight = goban.getHeight();
            size_y = (int)goban.getHeight();
            System.out.println("size x == " + size_x + " size y == " + size_y);
            //double newHeight = newValue.doubleValue(); // newValue est un Number, donc on le convertit en double
            screen.y = (int)newValue.doubleValue();
            // int new_size = get_size(screen.x, screen.y);
            gomoku.updateGameDisplay((int)sceneHeight, (int)sceneWidth);
            // System.out.println("New height: " + newValue);
        });


    }
    private int get_size(int width, int heigh)
    {
        int size = Math.min(width, heigh);
        screen.x = width;
        screen.y = heigh;
        System.out.println("width : " + width + " height : " + heigh + " size : " + size);
        return ((int)size);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Gomoku");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        int size = get_size((int)screenBounds.getWidth(), (int)screenBounds.getHeight());
        size /= 4;
        size_x = size;
        size_y = size;
        System.out.println("size x == " + size_x + " size y == " + size_y);
        // stage.setWidth(size);
        // stage.setHeight(size);
        // gomoku = new Gomoku(size, size);
        root = new Pane();
        // goban_root = new Pane();
        scene = new Scene(root, size, size);
        // goban = new Scene(goban_root, size, size);
        // set_goban_event();
        home = new Scene(home_root, size, size);
        set_home_event();
        // Text homeText = new Text(100, 100, "Bienvenue sur la scène Home!");
        // homeText.setFill(Color.BLACK);
        home_root.getChildren().add(home_page.getHomePage()); // Ajout du texte à home_root
        // home_root.getChildren().add(home_body);
        // home.setFill(Color.web("#FF6347"));
        home_root.setStyle("-fx-background-color: #FF6347;");
        // goban_root.getChildren().add(gomoku.getGameDisplay());
        //set les ecouteurss sur le stage? ou stur chaques scene?

        root.setOnMouseClicked(e -> System.out.println("Pane cliqué !"));
        // scene.setFill(Color.web("#FF6347"));
        
        stage.centerOnScreen();
        stage.setScene(home);
        // stage.setScene(home);
        stage.show();
        // System.out.println("heihgt " + goban.getHeight() + " size " + size);
    }

    // public void switchScene(Scene newScene) {
    //     stage.setScene(newScene);
    // }

    // public void switchScene(Scene newScene) {
    //     // Conserver la taille de la fenêtre avant de changer la scène
    //     double currentWidth = stage.getWidth();
    //     double currentHeight = stage.getHeight();

    //     // Changer la scène
    //     stage.setScene(newScene);

    //     // Redéfinir la taille de la fenêtre après avoir changé de scène
    //     // stage.setWidth(currentWidth);
    //     // stage.setHeight(currentHeight);
    // }

    public void switchScene(Scene newScene) {
    stage.setScene(newScene);
}

    public static void main(String[] args) {
        launch(args);
    }
}
