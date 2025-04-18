package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import javafx.scene.control.Button;
import java.util.ArrayList;
// import java.util.Arrays;
import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Orientation;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.Node;

public class FileBox{

    private static ScrollPane scrollPane = new ScrollPane();
    private static TextField dirName = new TextField();
    private static VBox openFileBox = new VBox();
    private static Pane dirNameBox = new HBox();
    private static VBox box = new VBox();
    private static Button dir_validation = new Button("ok");
    
    private static void add_directory_details(File dir){
            File[] files = dir.listFiles();  // Liste tous les fichiers et sous-répertoires
            if (files != null) {
                    Label label = new Label("..");
                    // label.setMinWidth(200);
                    box.getChildren().add(label);
                for (File file : files) {
                    String displayName = file.getName();

                    // Ajouter un "/" si c'est un répertoire
                    if (file.isDirectory()) {
                        displayName += "/";  // Indique que c'est un répertoire
                    }

                    // Ajouter un Label avec le nom du fichier ou du répertoire
                    label = new Label(displayName);
                    // label.setMinWidth(180);
                    // label.setMaxWidth(180);
                    box.getChildren().add(label);
                }
            }

    }

    private static void setOkEvent(){
            dir_validation.setOnAction(e -> {
                box.getChildren().clear();
                String inputPath = dirName.getText();
                dirName.clear();
                boolean shouldExpand = inputPath.equals("~") || inputPath.startsWith("~/");
                String resolvedPath = shouldExpand ? System.getProperty("user.home") + inputPath.substring(1): inputPath;
                File directory = new File(resolvedPath);
                System.out.println(directory);
                if (!directory.exists())
                {
                    System.out.println("le dossier n'exiaste aps");
                    return ;
                }
                System.out.println("le dossier exiaste");
                add_directory_details(directory);
        });
    }

    public static VBox getFileBox(){

            dirNameBox.getChildren().addAll(dirName, dir_validation);
            openFileBox.getChildren().addAll(scrollPane, dirNameBox);
            scrollPane.setPrefViewportHeight(100);//revoir tout ca
            scrollPane.setLayoutX(10);//revoir tout ca
            scrollPane.setLayoutY(10);//revoir tout ca
            scrollPane.setMaxWidth(200);//revoir tout ca
            scrollPane.setMinWidth(200);//revoir tout ca
            scrollPane.setFitToWidth(false);//revoir tout ca
            scrollPane.setContent(box);//revoir tout ca
            File sgf_dir = SGF.openSGFDir();//proteger le null //juste charger fenetre vide!?
            add_directory_details(sgf_dir);
            box.setOnMouseClicked(event -> {
                // Récupérer le nœud sur lequel on a cliqué
                Node clickedNode = event.getPickResult().getIntersectedNode();

                // Remonter jusqu'au Label parent si on a cliqué sur un élément à l'intérieur
                while (clickedNode != null && !(clickedNode instanceof Label)) {
                    clickedNode = clickedNode.getParent();
                }

                // Si on a trouvé un Label, afficher son texte
                if (clickedNode instanceof Label) {
                    Label clickedLabel = (Label) clickedNode;
                    System.out.println("Élément cliqué : " + clickedLabel.getText());
                } else {
                    System.out.println("Aucun Label cliqué");
                }
            });
            setOkEvent();
        return (openFileBox);
    }
}