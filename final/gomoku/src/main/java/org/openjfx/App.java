package org.openjfx;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private Gomoku gomoku;
    private Stage stage;
    private Pane root;
    private Scene scene;
    private Point screen = new Point();
    private Pane accueil, settings;//les mettre dans les classes correspondantes

    private int get_size(int width, int height)
    {
        int size = Math.min(width, height);
        screen.x = width;
        screen.y = height;
        System.out.println("width : " + width + " height : " + height + " size : " + size);
        return ((int)size);
    }
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Gomoku");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        int size = get_size((int)screenBounds.getWidth(), (int)screenBounds.getHeight());
        size /= 4;
        gomoku = new Gomoku(size);
        root = new Pane();
        scene = new Scene(root, size, size);
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            screen.x = (int)newValue.doubleValue();
            int new_size = get_size(screen.x, screen.y);
            this.gomoku.goban.updateGoban(new_size);
            System.out.println("New width: " + newValue);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            //double newHeight = newValue.doubleValue(); // newValue est un Number, donc on le convertit en double
            screen.y = (int)newValue.doubleValue();
            int new_size = get_size(screen.x, screen.y);
            this.gomoku.goban.updateGoban(new_size);
            System.out.println("New height: " + newValue);
        });
        stage.setWidth(size);
        stage.setHeight(size);
        scene.setFill(Color.web("#FF6347"));
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }
    
    public void switchScene(Scene newScene) {
        stage.setScene(newScene);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
