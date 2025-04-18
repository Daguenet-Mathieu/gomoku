package org.interfacegui;
import java.util.ArrayList;
import java.io.File;

public class SGF{

    public static File openSGFDir(){
        try {
            File directory = new File("sgf");
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Le dossier 'sgf' a été créé.");
                }
                else {
                    System.out.println("Impossible de créer le dossier 'sgf'.");
                    return null;
                }
            }
            return directory;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;

    }

    public static void createSgf(ArrayList<Map> map, String rules){
        //creer le filename
            File file = new File("sgf/fileTest.txt");
            System.out.println("File == " + file);
            try{
                if (!file.exists()){
                   file.createNewFile(); // crée le fichier vide
                   System.out.println("il a ete creee File " + file);
                }
                else
                   System.out.println("il existe le File " + file);
            }
            catch(Exception e){
                System.out.println(e);
            }

        //ouvrir le dossier sgf si succes remplir le fichier //sinon message d'erreur?
    }
    public static ArrayList<Map> readSgf(File file){
        return (null);
    }
}
