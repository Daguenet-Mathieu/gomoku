package org.openjfx;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Text;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;

import javafx.scene.text.Font;

import java.util.*;

import org.utils.*;
import org.model.*;
import javafx.application.Platform;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;


/**
 * JavaFX App
 */

public class Gomoku extends Application {
    Pane root = new Pane();                     // Conteneur principal
    int size_square;
    int start_y;
    int start_x;
    int end_x;
    int end_y;
    Game    game = new Game();
    Random random = new Random();
    boolean victory = false;
    int ia_color = 1;
    //int     ia_color = random.nextInt(2);
    boolean    toogle;//savoir si c'est a l'humain de jouer
    float[] dbl = new float[5];
    ArrayList<Candidat.coord> lst;
    Random rand = new Random();
    Text candidatText;
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

    // void printByteMap(byte[][] byteMap) {
    //     for (int i = 0; i < byteMap.length; i++) {
    //         for (int j = 0; j < byteMap[i].length; j++) {
    //             System.out.print(byteMap[i][j]);
    //         }
    //         System.out.println(); // New line after each row
    //     }
    // }

    int getColor(GameMap line[], int square)
    {
        return (line[square].color);
    }

    boolean checkHorizontal(GameMap[] line, int square)
    {
        int color = getColor(line, square);

        if (color == 0 || square >= 15)
            return false;
        //System.out.println("color == " + color);
        for (int i = 1; i < 5; i++)
        {
            if (getColor(line, square + i) != color)
                return false;
        }
        if (color == 1)
            System.out.println("victoire de noir");
        else
            System.out.println("victoire de blanc");
        return true;
    }

    boolean checkVertical(GameMap[][] game, int line, int square)
    {
        int color = getColor(game[line], square);

        if (color == 0 || line >= 15)
            return false;
        for (int i = 1; i < 5; i++)
        {
            if (getColor(game[line + i], square) != color)
            {
                return false;
            }
        }
        if (color == 1)
            System.out.println("victoire de noir");
        else
            System.out.println("victoire de blanc");
        return true;
    }
    
    boolean checkDiagonalLeft(GameMap[][] game, int line, int square)
    {
        int color = getColor(game[line], square);

        if (color == 0 || square < 4 || line >= 15)
            return false;
        for (int i = 1; i < 5; i++)
        {
            if (getColor(game[line + i], square - i) != color)
            {
                return false;
            }
        }
        if (color == 1)
            System.out.println("victoire de noir");
        else
            System.out.println("victoire de blanc");
        return true;
    }
    
    boolean checkDiagonalRight(GameMap[][] game, int line, int square)
    {
        int color = getColor(game[line], square);

        if (color == 0 || square >= 15 || line >= 15)
            return false;
        for (int i = 1; i < 5; i++)
        {
            if (getColor(game[line + i], square + i) != color)
            {
                return false;
            }
        }
        if (color == 1)
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
        if (game.map[point.y][point.x].color != 0)
            return (false);
        return (true);
    }

    void printGame(GameMap[][] map)
    {
        for (int y = 0; y < 19; y++)
        {
            for (int x = 0; x < 19; x++)
            {
                System.out.println(map[y][x].color);
            }
            System.out.printf("\n");
        }
    }

    boolean captureHorizontal(int color, int x, int y)
    {
        System.out.println(" ja passe par ici " + " y " + y + " x " + x);
        System.out.println(" premiere case == " + game.map[y][x].color);
        System.out.println(" deuxieme case == " + game.map[y][x + 1].color);
        System.out.println(" trooisieme case == " + game.map[y][x + 2].color);


        if ((game.map[y][x].color != 0 && game.map[y][x].color == color)
         && (game.map[y][x + 1].color != 0 && game.map[y][x + 1].color != color)
         && (game.map[y][x + 2].color != 0 && game.map[y][x + 2].color != color)
         && game.map[y][x + 3].color == color)
        {
            System.out.println("je return true");
             return (true);
        }
        return (false);
    }

    boolean captureVertical(int color, int x, int y)
    {
        if ((game.map[y][x].color != 0 && game.map[y][x].color == color)
         && (game.map[y + 1][x].color != 0 && game.map[y + 1][x].color != color)
         && (game.map[y + 2][x].color != 0 && game.map[y + 2][x].color != color)
         && game.map[y + 3][x].color == color)
        {
            System.out.println("je return true");
             return (true);
        }

        return (false);
    }

    boolean captureDiagonalRight(int color, int x, int y)
    {
        if ((game.map[y][x].color != 0 && game.map[y][x].color == color)
         && (game.map[y + 1][x + 1].color != 0 && game.map[y + 1][x + 1].color != color)
         && (game.map[y + 2][x + 2].color != 0 && game.map[y + 2][x + 2].color != color)
         && game.map[y + 3][x + 3].color == color)
        {
            System.out.println("je return true");
             return (true);
        }

        return (false);
    }

    boolean captureDiagonalLeft(int color, int x, int y)
    {
        if ((game.map[y][x].color != 0 && game.map[y][x].color == color)
         && (game.map[y + 1][x - 1].color != 0 && game.map[y + 1][x - 1].color != color)
         && (game.map[y + 2][x - 2].color != 0 && game.map[y + 2][x - 2].color != color)
         && game.map[y + 3][x - 3].color == color)
        {
            System.out.println("je return true");
             return (true);
        }

        return (false);
    }


    int check_captures_start(int color, int last_move_x, int last_move_y)
    {
        int prisoners = 0;

        if (last_move_x < 16)//check la capture horixontale
        {
            if (captureHorizontal(color, last_move_x, last_move_y))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y][last_move_x + 1].node);
                game.map[last_move_y][last_move_x + 1].setColor(0);
                game.m.map[last_move_y][last_move_x + 1] = 0;
                root.getChildren().remove(game.map[last_move_y][last_move_x + 2].node);
                game.map[last_move_y][last_move_x + 2].setColor(0);
                game.m.map[last_move_y][last_move_x + 2] = 0;
                //delete y+1 et y+2
            }
        }
        if (last_move_y < 16)//check la capture verticale
        {
            if (captureVertical(color, last_move_x, last_move_y))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y + 1][last_move_x].node);
                game.map[last_move_y + 1][last_move_x].setColor(0);
                game.m.map[last_move_y + 1][last_move_x] = 0;
                root.getChildren().remove(game.map[last_move_y + 2][last_move_x].node);
                game.map[last_move_y + 2][last_move_x].setColor(0);
                game.m.map[last_move_y + 2][last_move_x] = 0;
                //delete x+1 et x+2
            }

        }
        if (last_move_x < 16 && last_move_y < 16)//check capture diagonale
        {
            if (captureDiagonalRight(color, last_move_x, last_move_y))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y + 1][last_move_x + 1].node);
                game.map[last_move_y + 1][last_move_x + 1].setColor(0);
                game.m.map[last_move_y + 1][last_move_x + 1] = 0;
                root.getChildren().remove(game.map[last_move_y + 2][last_move_x + 2].node);
                game.map[last_move_y + 2][last_move_x + 2].setColor(0);
                game.m.map[last_move_y + 2][last_move_x + 2] = 0;
                //delete x+1 et x+2
            }
        }
        if (last_move_x < 16 && last_move_y > 2)//check capture diagonale
        {
            if (captureDiagonalLeft(color, last_move_x + 3, last_move_y - 3))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y - 1][last_move_x + 1].node);
                game.map[last_move_y - 1][last_move_x + 1].setColor(0);
                game.m.map[last_move_y - 1][last_move_x + 1] = 0;
                root.getChildren().remove(game.map[last_move_y - 2][last_move_x + 2].node);
                game.map[last_move_y - 2][last_move_x + 2].setColor(0);
                game.m.map[last_move_y - 2][last_move_x + 2] = 0;
                //delete x-1 et x-2
            }
        }
        return (prisoners);
    }

    int check_captures_end(int color, int last_move_x, int last_move_y)
    {
        int prisoners = 0;

        if (last_move_x > 2)//check la capture horixontale
        {
            if (captureHorizontal(color, last_move_x - 3, last_move_y))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y][last_move_x - 1].node);
                game.map[last_move_y][last_move_x - 1].setColor(0);
                game.m.map[last_move_y][last_move_x - 1] = 0;
                root.getChildren().remove(game.map[last_move_y][last_move_x - 2].node);
                game.map[last_move_y][last_move_x - 2].setColor(0);
                game.m.map[last_move_y][last_move_x - 2] = 0;

                //delete y-1 et y-2
            }
        }
        if (last_move_y > 2)//check la capture verticale
        {
            if (captureVertical(color, last_move_x, last_move_y - 3))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y - 1][last_move_x].node);
                game.map[last_move_y - 1][last_move_x].setColor(0);
                game.m.map[last_move_y - 1][last_move_x] = 0;
                root.getChildren().remove(game.map[last_move_y - 2][last_move_x].node);
                game.map[last_move_y - 2][last_move_x].setColor(0);
                game.m.map[last_move_y - 2][last_move_x] = 0;

                //delete x-1 et x-2
            }

        }
        if (last_move_x > 2 && last_move_y > 2)//check capture diagonale
        {
            if (captureDiagonalRight(color, last_move_x - 3, last_move_y - 3))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y - 1][last_move_x - 1].node);
                game.map[last_move_y - 1][last_move_x - 1].setColor(0);
                game.m.map[last_move_y - 1][last_move_x - 1] = 0;
                root.getChildren().remove(game.map[last_move_y - 2][last_move_x - 2].node);
                game.map[last_move_y - 2][last_move_x - 2].setColor(0);
                game.m.map[last_move_y - 2][last_move_x - 2] = 0;
                //delete x-1 et x-2
            }
        }
        if (last_move_x < 16 && last_move_y > 2)//check capture diagonale
        {
            if (captureDiagonalLeft(color, last_move_x + 3, last_move_y - 3))
            {
                prisoners += 2;
                root.getChildren().remove(game.map[last_move_y - 1][last_move_x + 1].node);
                game.map[last_move_y - 1][last_move_x + 1].setColor(0);
                game.m.map[last_move_y - 1][last_move_x + 1] = 0;
                root.getChildren().remove(game.map[last_move_y - 2][last_move_x + 2].node);
                game.map[last_move_y - 2][last_move_x + 2].setColor(0);
                game.m.map[last_move_y - 2][last_move_x + 2] = 0;

                //delete x-1 et x-2
            }
        }
        return (prisoners);
    }


    void check_captures(int last_move_x, int last_move_y)
    {
        byte color;
        int prisoners = 0;
        if (game.map[last_move_y][last_move_x].color == 0)
            color = 0;
        else
            color = 1;
        prisoners += check_captures_start(color, last_move_x, last_move_y);
        prisoners += check_captures_end(color, last_move_x, last_move_y);
        if (color == 0)
            game.black_prisoners += prisoners;
        else
            game.white_prisoners += prisoners;
    }

    void do_ia_move(Pane root, int turn)
    {
        Point p;
        if (candidatText!= null)
            root.getChildren().remove(candidatText);
        //System.out.printf("\n\nTurn of IA%d\n\n", turn);
        // try {
        //     Thread.sleep(1000);
        // } catch (InterruptedException e) {
        //     System.out.println("in catch sleep");
        // }
        if (game.first_move())
            p = new Point(10, 10);
        else
            p = game.best_move(turn, ia_color ==0?1:2);

        game.move(p, ia_color == 0?1:2);
        game.nb_move++;
        toogle = true;
        
        Circle circle = new Circle((size_square + p.x * size_square), size_square + (p.y * size_square), size_square / 2, ia_color == 0?Color.BLACK:Color.SNOW);
        root.getChildren().add(circle);
        game.map[p.y][p.x].setColor(ia_color == 0?1:2);
        game.map[p.y][p.x].setNode(circle);
        check_captures(p.x, p.y);
        // for (int i = 0; i < dbl.length; i++) {
        //     dbl[i] = rand.nextFloat() * (float) 100.0;
        // }
        // System.out.println("last move ia : x = " + p.x + " y = " + p.y);
        if (game.val != null)
        {
            String value = String.format("%.2f", game.val);
            candidatText = new Text(size_square * (p.x)  + (size_square/2),
                            size_square * (p.y + 1),
                            value);
            candidatText.setFill(Color.RED); 
            candidatText.setTextOrigin(VPos.CENTER);
            candidatText.setTextAlignment(TextAlignment.CENTER);
            candidatText.setFont(new Font(9));

        
        // lst = generateRandomCoords(5);
        // printCoords(lst);
            root.getChildren().add(candidatText);
        }
        candidate_statistics(game.m.values, game.m.candidat.lst);
        return ;
    }

public void candidate_statistics(float[] val, ArrayList<Candidat.coord> lst){
    game.candidate.clear();
    // printGame(game.map);
    for (int i = 0; i < lst.size(); i++) {
        if (validMove(new Point(lst.get(i).y, lst.get(i).x), game))
        {
            Color couleur = Color.GREEN;
            // Color couleur = Color.GREEN;
            if (val[i] < 0)
                couleur = Color.web("0xa4c8eb",1.0);
            Candidat.coord coord = lst.get(i);
            // System.out.println("x == " + coord.x + " y == " + coord.y);
            
            Circle circle = new Circle(
                size_square * (coord.y + 1),
                size_square * (coord.x + 1),
                size_square / 2,
                couleur
            );
            
            String value = String.format("%.0f", val[i]);
            Text text = new Text(
                size_square * (coord.y) + (size_square/2),
                size_square * (coord.x + 1),
                value
            );
            text.setTextOrigin(VPos.CENTER);
            text.setTextAlignment(TextAlignment.CENTER);
            text.setFont(new Font(9));
            
            // Créer un groupe pour chaque cercle et son texte
            Group group = new Group();
            group.getChildren().addAll(circle, text);
            
            // Ajouter le groupe à la liste des candidats
            game.candidate.add(group);
        }
    }
    root.getChildren().addAll(game.candidate);
}


// public void candidate_statistics(float[] val, ArrayList<Candidat.coord> lst) {
//     game.candidate.clear();
//     for (int i = 0; i < lst.size(); i++) {
//         Candidat.coord coord = lst.get(i);
//         System.out.println("x == " + coord.x + " y == " + coord.y);
//         Circle circle = new Circle(
//             size_square * (coord.x + 1),
//             size_square * (coord.y + 1),
//             size_square / 2,
//             Color.GREEN
//         );

//         String value = String.format("%.2f", val[i]);
        
//       Text text = new Text(size_square * (coord.x)  + (size_square/2), 
//                             size_square * (coord.y + 1), 
//                             value);
        
//         text.setTextOrigin(VPos.CENTER);
//         text.setTextAlignment(TextAlignment.CENTER);
        
//         // Créer un groupe pour chaque cercle et son texte
//         Group group = new Group();
//         group.getChildren().addAll(circle, text);
        
//         // Ajouter le groupe au root
//         root.getChildren().add(group);
        
//         // Ajouter le cercle à la liste des candidats
//         game.candidate.add(group);
//     }
// }


    // public static ArrayList<Candidat.coord> generateRandomCoords(int n) {
    //     Random rand = new Random();
    //     ArrayList<Candidat.coord> lst = new ArrayList<>();
        
    //     for (int i = 0; i < n; i++) {
    //         int x = rand.nextInt(19);
    //         int y = rand.nextInt(19);
    //         lst.add(new Candidat.coord(x, y));
    //     }
        
    //     return lst; 
    // }

    // public static void printCoords(ArrayList<Candidat.coord> lst) {
    //     for (Candidat.coord c : lst) {
    //         System.out.println("Coordonnée: (" + c.x + ", " + c.y + ")");
    //     }
    // }

    // public void removeCandidate() {
    //     // Utilisation d'un iterator pour éviter la ConcurrentModificationException
    //     Iterator<Node> iterator = root.getChildren().iterator();
    //     while (iterator.hasNext()) {
    //         Node child = iterator.next();
    //         if (child instanceof Group) {
    //             Group group = (Group) child;
    //             // Vérifie si le groupe contient un cercle
    //             for (Object groupChild : group.getChildren()) {
    //                 if (groupChild instanceof Circle) {
    //                     iterator.remove();  // Supprime le groupe contenant le cercle
    //                     break;
    //                 }
    //             }
    //         }
    //     }
    // }


//     public void removeCandidate() {
//     // On parcourt tous les enfants du root et on supprime uniquement ceux qui sont des groupes
//     for (Object child : root.getChildren()) {
//         if (child instanceof Group) {
//             root.getChildren().remove(child);
//         }
//     }
// }
    public void removeCandidate() {
    // Enlever chaque Group de root
    for (Group group : game.candidate) {
        root.getChildren().remove(group);
    }
    // Vider la liste candidate
    game.candidate.clear();
    }
    // public void removeCandidate()
    // {
    //     // root.getChildren().removeAll(root.getChildren(game.candidate.toArray(new Circle[0])));
    //     root.getChildren().removeAll(game.candidate);
    // }

    @Override
    public void start(Stage primaryStage) {
        int size_x = 800;
        int size_y = 800;
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
            removeCandidate();
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
                game.move(new Point(x_index, y_index), ia_color == 0?2:1);
                game.nb_move++;
                Circle circle = new Circle((size_square + x_index * size_square), size_square + (y_index * size_square), size_square / 2, ia_color == 0?Color.SNOW:Color.BLACK);
                root.getChildren().add(circle);
                game.map[y_index][x_index].setColor(ia_color == 0?2:1);
                game.map[y_index][x_index].setNode(circle);
                check_captures(x_index, y_index);
                // primaryStage.setScene(scene);  // Associer la scène
                // primaryStage.show();           // Afficher la fenêtre
                // if (color == Color.BLACK)
                //     color = Color.SNOW;
                // else
                //     color = Color.BLACK;
            }
            //printGame(game.map);
            //System.out.println("\n\n");
            //printByteMap(game.getMapAsByteArray());
            //System.out.println("\n\n");
            if (checkEndGame() || game.black_prisoners == 10 || game.white_prisoners == 10)
            {
                victory = true;
                return ;
            }
            Platform.runLater(() -> {
            // On attend un court instant avant d'exécuter le coup de l'IA
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
                    do_ia_move(root, ia_color == 0 ? 1 : 2);
                    if (checkEndGame() || game.black_prisoners == 10 || game.white_prisoners == 10) {
                        victory = true;
                    }
                }));
                timeline.setCycleCount(1);
                timeline.play();
            });
            // Platform.runLater(() -> {
            //     do_ia_move(root, ia_color==0?1:2);
            //     if (checkEndGame()) {
            //         victory = true;
            //     }
            // });
            // do_ia_move(root, ia_color==0?1:2);
            // if (checkEndGame())
            // {
            //     victory =true;
            //     return ;
            // }
            //printGame(game.map);
        });

        scene.setFill(Color.web("#DEB887"));//Color.LIGHTBLUE
        primaryStage.setTitle("Gomoku");
        //Line line = new Line(startX, startY, endX, endY);  // Créer la ligne
        // Line line = new Line(10, 0, 10, 300);
        drawGoban(root, size_x, size_y);

        if (toogle == false)
        {
            do_ia_move(root, ia_color==0?1:2);
            //printGame(game.map);
        }

        primaryStage.setScene(scene);  // Associer la scène
        primaryStage.show();           // Afficher la fenêtre
        System.out.println("color = "  + ia_color);
        System.out.println("toggle = "  + toogle);

    }

    public static void main(String[] args) {
        // MinMax test = new MinMax();
        // test.map[10][10]=1;
        // test.map[10][9]=1;
        // test.map[10][8]=1;
        // test.map[9][10]=1;
        // test.map[8][11] = 1;
        // test.map[7][12] = 2;
        // test.map[8][10]= 2;
        // test.map[7][9] = 2;
        // test.map[9][9] = 2;
        // test.map[10][7]= 2;

        // test.display_map();

        // test.eval(1);
        // test.ev.display();
        // System.exit(0);
        launch(args);  // Démarrer JavaFX
    }
}