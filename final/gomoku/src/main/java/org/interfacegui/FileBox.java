package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
//import javafx.scene.control.Button;
//import java.util.ArrayList;
// import java.util.Arrays;
import java.io.File;
import javafx.scene.control.ScrollPane;
//import javafx.geometry.Orientation;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
//simport javafx.scene.text.Text;
import javafx.scene.Node;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBox{

    private ScrollPane scrollPane;
    private TextField dirName;
    private VBox openFileBox;
    private Pane dirNameBox;
    private VBox box;
    private Button dir_validation;
    private HomePage homePage;
    private String absolutePath;

    FileBox(HomePage h){
        homePage = h;
    }

    private String getParent(String pathString){
        Path path = Paths.get(pathString);
        System.out.println("path == "  + path);
        Path parent = path.getParent();
        System.out.println("parent == "  + parent);
        String parentString = parent != null ? parent.toString() : null;
        return parentString;
    }

    private void add_directory_details(File dir){
        File[] files = dir.listFiles();  // Liste tous les fichiers et sous-répertoires
        Pane pane = new Pane();
        Label label = new Label("..");
        if (getParent(dir.getAbsolutePath()) != null)
        {
            box.getChildren().add(pane);
            // pane.setMinWidth(pane.getParent().getLayoutBounds().getWidth());
            // pane.setMinHeight(pane.getParent().getLayoutBounds().getHeight());
            // pane.setPrefWidth(Double.MAX_VALUE);  // Définit la largeur préférée au maximum
            // pane.setMaxWidth(Double.MAX_VALUE);   // Définit la largeur maximale au maximum
            pane.getChildren().add(label);
            file_event(pane);
            // pane.setMinWidth(200);
        }
        if (files != null) {
            for (File file : files) {
                String displayName = file.getName();
                // Ajouter un "/" si c'est un répertoire
                if (file.isDirectory()) {
                    displayName += File.separator ;  // Indique que c'est un répertoire
                }
                // Ajouter un pane avec le nom du fichier ou du répertoire
                pane = new Pane();
                box.getChildren().add(pane);
                // pane.setMinWidth(pane.getParent().getLayoutBounds().getWidth());
                // pane.setMinHeight(pane.getParent().getLayoutBounds().getHeight());

                // pane.setPrefWidth(Double.MAX_VALUE);  // Définit la largeur préférée au maximum
                // pane.setMaxWidth(Double.MAX_VALUE);   // Définit la largeur maximale au maximum
                label = new Label(displayName);
                file_event(pane);
                pane.getChildren().add(label);
                // pane.setMinWidth(180);
                // pane.setMaxWidth(180);
            }
        }
    }

    private void file_event(Pane file){
        String style = "-fx-background-color: white;";
        file.setStyle("-fx-background-color: white;");
        file.setOnMouseClicked(event -> {
            System.out.println("style == " + file.getStyle());
            if (style.equals(file.getStyle()) == false){
                String value = ((Label) file.getChildren().get(0)).getText();
                System.out.println("value == " + (absolutePath + value));
                if (("..".equals(value) || new File(absolutePath + File.separator + value).isDirectory()) == false){
                    homePage.removeFileBox(value);
                    return ;
                }
                System.out.println("c'estun dossier!");
                box.getChildren().clear();
                System.out.println("absolute avant : " + absolutePath);
                if (("..".equals(value)))
                    absolutePath = getParent(absolutePath);
                else
                    absolutePath = absolutePath + File.separator + value;
                System.out.println("absolute apres : " + absolutePath);
                add_directory_details(new File(absolutePath));
            }
            for (Node node : box.getChildren()) {
                ((Pane) node).setStyle("-fx-background-color: white;");
            }
            file.setStyle("-fx-background-color: red;");
        });
    }

    private void setOkEvent(){
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
            absolutePath = directory.getAbsolutePath();
            System.out.println("le dossier exiaste");
            add_directory_details(directory);
        });
    }

    public VBox getFileBox(){
            openFileBox = new VBox();
            scrollPane = new ScrollPane();
            dirName = new TextField();
            dirNameBox = new HBox();
            box = new VBox();
            dir_validation = new Button("ok");
            dirNameBox.getChildren().addAll(dirName, dir_validation);
            openFileBox.getChildren().addAll(scrollPane, dirNameBox);
            scrollPane.setPrefViewportHeight(100);//revoir tout ca
            scrollPane.setLayoutX(10);//revoir tout ca
            scrollPane.setLayoutY(10);//revoir tout ca
            scrollPane.setMaxWidth(200);//revoir tout ca
            // scrollPane.setMinWidth(200);//revoir tout ca
            scrollPane.setContent(box);//revoir tout ca

            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Ou .ALWAYS pour toujours afficher
            scrollPane.setFitToWidth(true);  // Ajuste la largeur du contenu à la taille du parent
    // scrollPane.setMaxWidth(Double.MAX_VALUE);  // Pas de largeur fixe
            scrollPane.setFitToWidth(false);
            File sgf_dir = SGF.openSGFDir();//proteger le null //juste charger fenetre vide!?
            absolutePath = sgf_dir.getAbsolutePath();
            add_directory_details(sgf_dir);
            // // box.setOnMouseClicked(event -> {
            // //     // Récupérer le nœud sur lequel on a cliqué
            // //     Node clickedNode = event.getPickResult().getIntersectedNode();

            // //     // Remonter jusqu'au Label parent si on a cliqué sur un élément à l'intérieur
            // //     while (clickedNode != null && !(clickedNode instanceof Pane)) {
            // //         clickedNode = clickedNode.getParent();
            // //     }

            // //     // Si on a trouvé un Label, afficher son texte
            // //     if (clickedNode instanceof Pane) {
            // //         Pane clickedPane = (Pane) clickedNode;
            // //         // System.out.println("Élément cliqué : " + clickedPane.getText());
            // //     } else {
            // //         System.out.println("Aucun Pane cliqué");
            // //     }
            // // });
            // box.setOnMouseClicked(event -> {
            // // Récupérer le nœud sur lequel on a cliqué
            // Node clickedNode = event.getPickResult().getIntersectedNode();

            // // Remonter l'arbre des nœuds jusqu'à ce qu'on trouve un Pane
            // while (clickedNode != null && !(clickedNode instanceof Pane)) {
            //     clickedNode = clickedNode.getParent();
            // }

            // // Si on a trouvé un Pane, récupérer son fichier ou répertoire
            // if (clickedNode instanceof Pane) {
            //     Pane clickedPane = (Pane) clickedNode;
            //     File clickedFile = (File) clickedPane.getUserData();
            //     System.out.println("Fichier ou répertoire cliqué : " + clickedFile.getPath());
            // } else {
            //     System.out.println("Aucun Pane cliqué");
            // }
            // });
            setOkEvent();
        return (openFileBox);
    }
}