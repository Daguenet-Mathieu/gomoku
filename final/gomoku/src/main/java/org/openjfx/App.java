package org.openjfx;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;


public class App extends Application {
    private Gomoku gomoku;
    private Stage stage;
    private Pane root;
    private Scene scene;
    private Point screen = new Point();
    private Scene goban, accueil, settings;//les mettre dans les classes correspondantes

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
        // stage.setWidth(size);
        // stage.setHeight(size);
        gomoku = new Gomoku(size, size);
        root = new Pane();
        Pane goban_root = new Pane();

        scene = new Scene(root, size, size);
        goban = new Scene(goban_root, size, size);
        goban_root.getChildren().add(gomoku.getGameDisplay());
        //set les ecouteurss sur le stage? ou stur chaques scene?
        goban.widthProperty().addListener((observable, oldValue, newValue) -> {
            double sceneWidth = goban.getWidth();
            double sceneHeight = goban.getHeight();
            screen.x = (int)newValue.doubleValue();
            //int new_size = get_size(screen.x, screen.y);
            gomoku.updateGameDisplay((int)sceneHeight, (int)sceneWidth);
            // System.out.println("New width: " + newValue);
        });

        goban.heightProperty().addListener((observable, oldValue, newValue) -> {
            double sceneWidth = goban.getWidth();
            double sceneHeight = goban.getHeight();
            //double newHeight = newValue.doubleValue(); // newValue est un Number, donc on le convertit en double
            screen.y = (int)newValue.doubleValue();
            // int new_size = get_size(screen.x, screen.y);
            gomoku.updateGameDisplay((int)sceneHeight, (int)sceneWidth);
            // System.out.println("New height: " + newValue);
        });
        scene.setFill(Color.web("#FF6347"));
        stage.centerOnScreen();
        stage.setScene(goban);
        stage.show();
        // System.out.println("heihgt " + goban.getHeight() + " size " + size);
    }
    
    public void switchScene(Scene newScene) {
        stage.setScene(newScene);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
