package org.interfacegui;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.Node;
import java.nio.file.Path;
import java.nio.file.Paths;
// import javafx.geometry.Pos;

public class FileBox{

    private ScrollPane scrollPane;
    private TextField dirName;
    private VBox openFileBox;
    private Pane dirNameBox;
    private VBox box;
    private Button cross = new Button("Close Box");
    private HBox crossContainer = new HBox(cross);
    private Button dir_validation;
    private HomePage homePage;
    private String absolutePath;
    private SGF sgf = new SGF();


    public Button getCross(){
        return cross;
    }

    FileBox(HomePage h){
        homePage = h;
        cross.setStyle("-fx-background-color: #000000; -fx-text-fill: #FF0000;");
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
                    SGF.setFile(absolutePath, ((Label) file.getChildren().get(0)).getText());
                    homePage.removeFileBox();
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
            // crossContainer.setAlignment(Pos.BASELINE_RIGHT);
            dirNameBox.getChildren().addAll(dirName, dir_validation);
            openFileBox.getChildren().addAll(crossContainer, scrollPane, dirNameBox);
            scrollPane.setPrefViewportHeight(100);
            scrollPane.setLayoutX(10);
            scrollPane.setLayoutY(10);
            scrollPane.setMaxWidth(200);
            scrollPane.setContent(box);

            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToWidth(false);
            File sgf_dir = sgf.openSGFDir();
            absolutePath = sgf_dir.getAbsolutePath();
            add_directory_details(sgf_dir);
            setOkEvent();
        return (openFileBox);
    }
}