package org.interfacegui;
import org.utils.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalTime;
import java.time.LocalDate;
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

    private static String add_moves(String fileContent, ArrayList<Map> map){
            final String alpha = "abcdefghijklmnopqrstuvwxyz";
            final String[] move = new String[] { "B", "W" };
            int i = 0;
            for (Map m : map){
                if (m.getLastMove() != null){
                    fileContent += ";" + move[i];
                    fileContent += "[" + alpha.charAt(m.getLastMove().x) + "" + alpha.charAt(m.getLastMove().y)  + "]";
                    if (m.get_prisonners() != null && m.get_prisonners().size() > 0)
                        fileContent += "\n;AE";
                    for (Point p : m.get_prisonners()){
                        fileContent += "[" + alpha.charAt(p.x) + "" + alpha.charAt(p.y) + "]";
                    }
                    fileContent += "\n";
                    i^=1;
                }
            }
            fileContent = fileContent.substring(0, fileContent.length() - 1);
            fileContent += ")";
            return fileContent;
    }

    public static void createSgf(ArrayList<Map> map, String rule){
        //creer le filename
            String fileContent = "(;FF[4] GM[4] SZ[19]\n";
            // if ("go".equals(rule))
            //     ff = "[1]"; GM!
            System.out.println("je passe dans create sgf");
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();
            System.out.println("localDate == " + localDate);
            System.out.println("localDate to strng  == " + localDate.toString());

            System.out.println("localtime  to string == " + localTime.toString());
            System.out.println("localtime == " + localTime);
            String fileName = localDate.toString() + "_" + localTime.toString();
            System.out.println("FileName == " + fileName);
            if (fileName.indexOf(".") != -1)
                fileName = "./sgf/" + rule + "_" + fileName.substring(0, fileName.indexOf("."));
            System.out.println("FileName == " + fileName);
            File file = new File(fileName + ".sgf");
            System.out.println("File == " + file);
            int i = 1;
            while (file.exists()){
                file = new File(fileName + "(" + i + ")" + ".sgf");
                i++;
            }
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
            file.setWritable(true);
            file.setReadable(true);
            // write(new Path(file.getPath()), ff.getBytes());
            try {
                Writer writer = new FileWriter(file, true);
                fileContent = add_moves(fileContent, map);
                writer.write(fileContent, 0, fileContent.length());
                writer.close();
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
