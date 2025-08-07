package org.openjfx;
import org.interfacegui.*;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import org.utils.Point;
// import java.io.InputStream;
// import java.io.FileInputStream;
// import java.io.IOException;
// import javafx.geometry.Pos;
import java.util.ArrayList;

public class App extends Application {
    private Gomoku gomoku;
    private Stage stage;
    private Pane root;
    public Scene scene;
    private Point screen = new Point();
    private int size_y;
    private int size_x;
    Pane goban_root;
    private Scene goban, home;
    private Pane home_body = new Pane();
    private Pane home_root = new Pane();
    private Home home_page = new Home();
    private ImageView background; 
    private ImageView title;
    
    //class settings pour connaitre les preference du joueur couleur des pierres goban etc et donner au gomoku
    //choix temps dans le home en meme temps que le choix des regles et des joueurs


    // private void openBackground(){
    //     String path1 = new File("./img/background.jpg").getAbsolutePath();
    //     String path2 = new File("./img/title.webp").getAbsolutePath();
    //     Image img = new Image(path1.toURI().toString(), false);
    //     img.exceptionProperty().addListener((obs, oldEx, newEx) -> {
    //     System.out.println("Image width: " + img.getWidth() + ", height: " + img.getHeight());
    //         if (newEx != null) {
    //             newEx.printStackTrace();
    //             System.err.println("Erreur chargement image : " + newEx.getMessage());
    //         }
    //     });
    //     background = new ImageView(img);
    //     // background = new ImageView(new Image("file:" + path1));
    //     title = new ImageView(new Image("file:" + path2));//trycatch
    // }

    // private void openBackground(){
    //     System.out.println("open img background !");
    //     System.out.println("Répertoire courant: " + System.getProperty("user.dir"));
    //     File file1 = new File("./img/background.jpg");
    //     File file2 = new File("./img/title.webp");

    //     Image img = new Image(file1.toURI().toString(), false);
    //     img.exceptionProperty().addListener((obs, oldEx, newEx) -> {
    //         if (newEx != null) {
    //             newEx.printStackTrace();
    //             System.err.println("Erreur chargement image : " + newEx.getMessage());
    //         }
    //     });
    //     System.out.println("Image width: " + img.getWidth() + ", height: " + img.getHeight());

    //     background = new ImageView(img);
    //     title = new ImageView(new Image(file2.toURI().toString()));

    // }

    private void openBackground(){
        System.out.println("open img background !");
        System.out.println("Répertoire courant: " + System.getProperty("user.dir"));

        File file1 = new File("./img/background.png");
        File file2 = new File("./img/title.png");

        // Chargement synchrone de background.jpg
        Image img1 = new Image(file1.toURI().toString(), false);
        img1.exceptionProperty().addListener((obs, oldEx, newEx) -> {
            if (newEx != null) {
                System.err.println("Erreur background.jpg : " + newEx.getMessage());
                newEx.printStackTrace();
            }
        });

        if (img1.getWidth() <= 0 || img1.getHeight() <= 0) {
            System.err.println("Erreur : background.jpg non décodé");
        } else {
            System.out.println("✔ background.jpg OK : " + img1.getWidth() + "x" + img1.getHeight());
        }

        background = new ImageView(img1);

        // Chargement synchrone de title.webp
        Image img2 = new Image(file2.toURI().toString(), false);
        img2.exceptionProperty().addListener((obs, oldEx, newEx) -> {
            if (newEx != null) {
                System.err.println("Erreur title.png : " + newEx.getMessage());
                newEx.printStackTrace();
            }
        });

        if (img2.getWidth() <= 0 || img2.getHeight() <= 0) {
            System.err.println("Erreur : title.pgn non décodé");
        } else {
            System.out.println("✔ title.png OK : " + img2.getWidth() + "x" + img2.getHeight());
        }

        title = new ImageView(img2);
        background.setFitWidth(home_root.getWidth());
        background.setFitHeight(home_root.getHeight());
        background.setPreserveRatio(false);
        title.setFitWidth(home_root.getWidth());
        title.setFitHeight(home_root.getHeight());
        title.setPreserveRatio(true);
    }

    public void set_home_event(){
        home.widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("redimensionnement de home ! largeur = " + newValue.doubleValue());
        });
        home.heightProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("redimensionnement de home ! hauteur = " + newValue.doubleValue());
        });
        home_page.getValidationButton().setOnMouseClicked(event -> {
            double scenex = stage.getWidth();
            double sceney = stage.getHeight();
            if (home_page.getErrorMsg() != null && home_page.getErrorMsg().isEmpty() == false){
                home_page.displayErrorMsg();
                return ;
            }
            gomoku = new Gomoku((int)sceney, (int)scenex, home_page);
            goban_root = new Pane();
            goban = new Scene(goban_root, 800, 525);
            set_goban_event();
            goban_root.getChildren().add(gomoku.getGameDisplay());
            switchScene(goban);
            stage.setResizable(true);
        });

        home_page.getLearnOrViewButton().setOnMouseClicked(event -> {
            final String[] path = {"tuto/Go.sgf", "tuto/Pente.sgf", "tuto/Gomoku.sgf"};
            final String[] names = {"Go", "Pente", "Gomoku"};
            int i = 0;
            for (; i < 3; i++)
            {
                if (names[i].equals(home_page.get_rules()))
                    break;
            }
            SGF.setFile("./", path[i]);
            if (SGF.parseFile() == false){
                home_page.setErrorMsg("missing file please check app install");
                home_page.displayErrorMsg();
                return ;
            }
            ArrayList<Map> sgfMap;
            try {
                sgfMap = SGF.get_game_moves();
            }
            catch (Exception e) {
                home_page.setErrorMsg("invalid config file please check app install");
                home_page.displayErrorMsg();
                return ;
            }
            home_page.setRulesInstance(SGF.getRuleInstance());
            double scenex = stage.getWidth();
            double sceney = stage.getHeight();
            System.out.println(" goban width == " + scenex + " heigh " + sceney);
            home_page.setGameMode(Rules.GameMode.LEARNING);
            home_page.setSgfMap(sgfMap);
            gomoku = new Gomoku((int)sceney, (int)scenex, home_page);
            goban_root = new Pane();
            goban = new Scene(goban_root, 850, 525);
            set_goban_event();
            goban_root.getChildren().add(gomoku.getGameDisplay());
            switchScene(goban);
            stage.setResizable(true);
        });
    }

    private void set_goban_event(){
        gomoku.get_home_button().setOnMouseClicked(event -> {
            double scenex = stage.getWidth();
            double sceney = stage.getHeight();
            System.out.println("hone width == " + scenex + " heigh " + sceney);
            home_root = new Pane();
            home = new Scene(home_root, 606, 450);
            home_page = new Home();
            openBackground();
            background.setMouseTransparent(true);
            title.setMouseTransparent(true);
            home_root.getChildren().add(background);
            home_root.getChildren().add(title);
            set_home_event();
            home_root.getChildren().add(home_page.getHomePage()); // Ajout du texte à home_root
            switchScene(home);
            stage.setResizable(false);
            home_root.getChildren().add(home_body);
            home_page.getHomePage().setTranslateY(90);
        });

        gomoku.get_replay_button().setOnMouseClicked(event -> {
            gomoku.reset_gomoku();
        });

        goban.widthProperty().addListener((observable, oldValue, newValue) -> {
            double sceneWidth = goban.getWidth();
            double sceneHeight = goban.getHeight();
            System.out.println("size x == " + size_x + " size y == " + size_y);
            screen.x = (int)newValue.doubleValue();
            gomoku.updateGameDisplay((int)sceneHeight, (int)sceneWidth);
        });
        goban.heightProperty().addListener((observable, oldValue, newValue) -> {
            double sceneWidth = goban.getWidth();
            double sceneHeight = goban.getHeight();
            size_y = (int)goban.getHeight();
            System.out.println("size x == " + size_x + " size y == " + size_y);
            screen.y = (int)newValue.doubleValue();
            gomoku.updateGameDisplay((int)sceneHeight, (int)sceneWidth);
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
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        stage = primaryStage;
        stage.sizeToScene();
        stage.setTitle("Gomoku");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        int size = get_size((int)screenBounds.getWidth(), (int)screenBounds.getHeight());
        size /= 4;
        size_x = size;
        size_y = size;
        System.out.println("size x == " + size_x + " size y == " + size_y);
        root = new Pane();
        scene = new Scene(root, 606, 450);
        home = new Scene(home_root, 606, 450);
        openBackground();
        background.setMouseTransparent(true);
        title.setMouseTransparent(true);
        home_root.getChildren().add(background);
        home_root.getChildren().add(title);
        set_home_event();
        stage.setResizable(false);
        home_root.getChildren().add(home_page.getHomePage());
        home_page.getHomePage().setTranslateY(90);
        home_root.setStyle("-fx-background-color: #FF6347;");
        root.setOnMouseClicked(e -> System.out.println("Pane cliqué !"));
        stage.centerOnScreen();
        stage.setScene(home);
        stage.show();
        if (gomoku != null)
            stage.setOnCloseRequest(we -> gomoku.print_history_of_move());  
    }

    public void switchScene(Scene newScene) {
        stage.setScene(newScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
