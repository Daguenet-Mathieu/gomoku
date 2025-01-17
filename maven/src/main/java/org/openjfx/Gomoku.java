package org.openjfx;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.*;

import org.utils.*;
import org.model.*;

/**
 * JavaFX App
 */

public class Gomoku extends Application {

    int size_square;
    int start_y;
    int start_x;
    int end_x;
    int end_y;
    Game    game = new Game();
    Random random = new Random();
    boolean victory = false;
    //int ia_color = 0;
    int     ia_color = random.nextInt(2);
    boolean    toogle;//savoir si c'est a l'humain de jouer
    // if (ia_color == 0)
    //     Color color = Color.BLACK;
    // else
    //     Color color = Color.BLACK;
    
    // System.out.printf("coucuo");
    public void drawHoshi(Pane root, int x, int y)
    {
        Circle circle = new Circle(x * size_square, y * size_square, size_square / 7);
        root.getChildren().add(circle);
    }

    public void drawGoban(Pane root, int x, int y)
    {
        int x_square = x / 21;
        int y_square = y / 21;
        size_square = x_square < y_square?x_square:y_square; 
        start_x = size_square;
        start_y = size_square;
        end_x = start_x + (18 * size_square);
        end_y = start_y + (18 * size_square);
        for (int i = 1; i < 20; i++)
        {
            Line line = new Line(size_square * i, 1*size_square, size_square * i, 19*size_square);
            line.setStroke(Color.BLACK);  // Définir la couleur de la ligne
            line.setStrokeWidth(2);       // Définir l'épaisseur de la ligne
            root.getChildren().add(line);  // Ajouter la ligne au conteneur

        }
        for (int i = 1; i < 20; i++)
        {
            Line line = new Line(1*size_square, size_square * i, 19*size_square, size_square * i);
            line.setStroke(Color.BLACK);  // Définir la couleur de la ligne
            line.setStrokeWidth(2);       // Définir l'épaisseur de la ligne
            root.getChildren().add(line);  // Ajouter la ligne au conteneur
        }
        drawHoshi(root, 4, 4);
        drawHoshi(root, 4, 10);
        drawHoshi(root, 4, 16);
        drawHoshi(root, 10, 4);
        drawHoshi(root, 10, 10);
        drawHoshi(root, 10, 16);
        drawHoshi(root, 16, 4);
        drawHoshi(root, 16, 10);
        drawHoshi(root, 16, 16);
    }

    void printByteMap(byte[][] byteMap) {
        for (int i = 0; i < byteMap.length; i++) {
            for (int j = 0; j < byteMap[i].length; j++) {
                System.out.print(byteMap[i][j]);
            }
            System.out.println(); // New line after each row
        }
    }

    Game.SquareState getColor(Game.SquareState line[], int square)
    {
        if (line[square] == Game.SquareState.NONE)
            return (Game.SquareState.NONE);
        else if (line[square] == Game.SquareState.BLACK)
            return (Game.SquareState.BLACK);
        return (Game.SquareState.WHITE);
    }

    boolean checkHorizontal(Game.SquareState[] line, int square)
    {
        Game.SquareState color = getColor(line, square);

        if (color == Game.SquareState.NONE || square >= 15)
            return false;
        //System.out.println("color == " + color);
        for (int i = 1; i < 5; i++)
        {
            if (getColor(line, square + i) != color)
                return false;
        }
        if (color == Game.SquareState.BLACK)
            System.out.println("victoire de noir");
        else
            System.out.println("victoire de blanc");
        return true;
    }

    boolean checkVertical(Game.SquareState[][] game, int line, int square)
    {
        Game.SquareState color = getColor(game[line], square);

        if (color == Game.SquareState.NONE || line >= 15)
            return false;
        for (int i = 1; i < 5; i++)
        {
            if (game[line + i][square] != game[line][square])
            {
                return false;
            }
        }
        if (color == Game.SquareState.BLACK)
            System.out.println("victoire de noir");
        else
            System.out.println("victoire de blanc");
        return true;
    }
    
    boolean checkDiagonalLeft(Game.SquareState[][] game, int line, int square)
    {
        Game.SquareState color = getColor(game[line], square);

        if (color == Game.SquareState.NONE || square < 4 || line >= 15)
            return false;
        for (int i = 1; i < 5; i++)
        {
            if (getColor(game[line + i], square - i) != color)
            {
                return false;
            }
        }
        if (color == Game.SquareState.BLACK)
            System.out.println("victoire de noir");
        else
            System.out.println("victoire de blanc");
        return true;
    }
    
    boolean checkDiagonalRight(Game.SquareState[][] game, int line, int square)
    {
        Game.SquareState color = getColor(game[line], square);

        if (color == Game.SquareState.NONE || square >= 15 || line >= 15)
            return false;
        for (int i = 1; i < 5; i++)
        {
            if (getColor(game[line + i], square + i) != color)
            {
                return false;
            }
        }
        if (color == Game.SquareState.BLACK)
            System.out.println("victoire de noir");
        else
            System.out.println("victoire de blanc");
        return true;
    }
    
    boolean checkEndGame()
    {
        for (int i = 0; i < 19; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                if (checkHorizontal(game.map[i], j))
                    return true;
                if (checkVertical(game.map, i, j))
                    return true;
                if (checkDiagonalLeft(game.map, i, j))
                    return true;
                if (checkDiagonalRight(game.map, i, j))
                    return true;
                // System.out.println("coucou");
            }
        }
        return false;
    }

    boolean validMove(Point point, Game game)
    {
        if (game.map[point.y][point.x] != Game.SquareState.NONE)
            return (false);
        return (true);
    }

    void printGame(Game.SquareState[][] map)
    {
        for (int y = 0; y < 19; y++)
        {
            for (int x = 0; x < 19; x++)
            {
                if (map[y][x] == Game.SquareState.BLACK)
                    System.out.print('1');
                
                else if (map[y][x] == Game.SquareState.WHITE)
                    System.out.print('2');
                else
                    System.out.print('0');
            }
            System.out.printf("\n");
        }
    }

    void do_ia_move(Pane root, int turn)
    {
        Point p;

        //System.out.printf("\n\nTurn of IA%d\n\n", turn);
        if (game.first_move())
            p = new Point(10, 10);
        else
            p = game.best_move(turn, ia_color ==0?1:2);

        game.move(p, ia_color == 0?Game.SquareState.BLACK:Game.SquareState.WHITE);
        toogle = true;
        
        Circle circle = new Circle((size_square + p.x * size_square), size_square + (p.y * size_square), size_square / 2, ia_color == 0?Color.BLACK:Color.SNOW);
        root.getChildren().add(circle);
        return ;
    }

    @Override
    public void start(Stage primaryStage) {
        int size_x = 800;
        int size_y = 800;
        Pane root = new Pane();                     // Conteneur principal
        Scene scene = new Scene(root, size_x, size_y);   // Création de la scène
        toogle = ia_color == 0?false:true;//savoir si c'est a l'humain de jouer
        // Timeline timeline = new Timeline(
        //     new KeyFrame(Duration.seconds(1), e -> {
        //         // Affiche un message dans le terminal à chaque seconde
        //         System.out.println("Message affiché à l'intervalle");
        //     })
        // );


        // // Répète l'animation indéfiniment
        // timeline.setCycleCount(Timeline.INDEFINITE);
        // timeline.play();
        
        // System.out.println(random.nextInt(2));

        scene.setOnMouseClicked(event -> {
            if (toogle == false)
                return ;
            if (victory)
                System.exit(0);
            double x = event.getX();
            double y = event.getY();
            int x_index = (int)((x - size_square / 2) / size_square);
            int y_index = (int)((y - size_square / 2) / size_square);
            //int positon = (x_index + size_square) * (x_index * size_square);
            System.out.println("Clic détecté aux coordonnées : ( x = " + x + ", y = " + y + ")");
            System.out.println("size square = " + size_square);
            System.out.println("index x = " + x_index + " index_y = " + y_index);
            if (x < start_x - (size_square / 2) || y < start_y - (size_square / 2) || x > end_x + (size_square / 2) || y > end_y + (size_square / 2)) {
                System.out.println("click out of goban");
            }
            else
            {
                if (!validMove(new Point(x_index, y_index), game))
                {
                    System.out.println("coup illegal!");
                    return ;
                }
                game.move(new Point(x_index, y_index), ia_color == 0?Game.SquareState.WHITE:Game.SquareState.BLACK);
                System.out.println("circle x = " + (x_index + size_square) * (x_index * size_square) + " circle y = " + (y_index + size_square) * (y_index * size_square));
                Circle circle = new Circle((size_square + x_index * size_square), size_square + (y_index * size_square), size_square / 2, ia_color == 0?Color.SNOW:Color.BLACK);
                root.getChildren().add(circle);
                primaryStage.setScene(scene);  // Associer la scène
                primaryStage.show();           // Afficher la fenêtre
                // if (color == Color.BLACK)
                //     color = Color.SNOW;
                // else
                //     color = Color.BLACK;
            }
            //printGame(game.map);
            //System.out.println("\n\n");
            //printByteMap(game.getMapAsByteArray());
            //System.out.println("\n\n");
            if (checkEndGame())
            {
                victory = true;
                return ;
            }
            do_ia_move(root, ia_color==0?1:2);
            if (checkEndGame())
            {
                victory =true;
                return ;
            }
            //printGame(game.map);
        });

        scene.setFill(Color.web("#DEB887"));//Color.LIGHTBLUE
        primaryStage.setTitle("Gomoku");
        //Line line = new Line(startX, startY, endX, endY);  // Créer la ligne
        // Line line = new Line(10, 0, 10, 300);
        drawGoban(root, size_x, size_y);

        if (toogle == false)
        {
            //game.move(new Point(10, 10), Game.SquareState.BLACK);
            do_ia_move(root, ia_color==0?1:2);
            //printGame(game.map);
        }

        primaryStage.setScene(scene);  // Associer la scène
        primaryStage.show();           // Afficher la fenêtre
        System.out.println("color = "  + ia_color);
        System.out.println("toggle = "  + toogle);

    }

    public static void main(String[] args) {
        launch(args);  // Démarrer JavaFX
    }
}