package org.interfacegui;
import org.utils.*;
import org.ast.*;
import java.util.ArrayList;
// import java.io.*;
import java.io.FileWriter;
import java.io.Writer;
import java.io.Reader;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.LocalDate;
import java.io.File;
// import java.util.LinkedHashMap;
// import java.util.Set;
// import java.io.IOException;
import java.text.ParseException;

// public enum NodeType{ DATA, OTHER_DATA_TYPE, ...., BRANCH_NODE }

// public abstract class Union //public interface Union{ 
// IMPOSER DE definir set get value
// 
// }

// public class UnionBranch extends Union{
//   Node node;
// }

// public class UnionData extends Union{
//   String Data;
// }

// public class Node{
//    NodeType typeInfo;
//    Union Data;
//    Node next;
// }

public class SGF{

    private static File     file;
    private static String   rules;
    private static int      ruleType;
    private static int      size;
    private static double      komi;
    private static int      handicap;
    private static String   errorMsg;
    private static boolean  header;
    private static ArrayList<Map> game_moves;
    private static final String[] ignoreSet = new String[] {
        "BM", "DO", "IT", "KO", "MN", "OB", "OW", "TE", "AR", "CR",
        "DD", "DM", "FG", "GB", "GW", "HO", "LB", "LN",
        "MA", "N", "PM", "SL", "SQ", "TR", "UC", "V", "VW", "ST",
        "AN", "BR", "BT", "CP", "DT", "EV", "GC", "GN", "ON", "OT",
        "PB", "PW", "RE", "RO", "SO", "TM", "US", "WR", "WT", "TB",
        "TW", "AS", "IP", "IY", "SE", "SU", "FF", "BL", "WL", "CA", "AP"
    };
    // private static final Set<String> ignoreSet = new String[] {
    //     "BM", "DO", "IT", "KO", "MN", "OB", "OW", "TE", "AB", "AW",
    //     "AR", "CR", "DD", "DM", "FG", "GB", "GW", "HO", "LB", "LN",
    //     "MA", "N", "PM", "SL", "SQ", "TR", "UC", "V", "VW", "ST",
    //     "AN", "BR", "BT", "CP", "DT", "EV", "GC", "GN", "ON", "OT",
    //     "PB", "PW", "RE", "RO", "SO", "TM", "US", "WR", "WT", "TB",
    //     "TW", "AS", "IP", "IY", "SE", "SU", "FF"
    // };
    // private static final Set<String> supportedSet = Set.of("KM", "HA", "AP", "CA", "GM", "RU", "SZ", "C", "AE", "PL", "B", "W", "BL", "WL");

    private static final String[] supportedSet = new String[] {"KM", "HA", "AP", "CA", "GM", "SZ", "C", "AE", "PL", "B", "W", "AB", "AW"};
    private static final String[] listCmdSet = new String[] {"AB", "AW", "AE"};
    private static final String[] rootCmdSet = new String[] {"KM", "HA", "GM", "SZ", "RU", "AP", "CA", "FF", "ST"};
    private static final String[] PointCmdSet = new String[] {"B", "W"};
    private static final String[] NumCmdSet = new String[] {"SZ", "HA", "KM"};

// "KM"//komi
// "HA"//handicap??
// *AP  Application     root	      composed simpletext ':' number // je garde
// *CA  Charset         root	      simpletext//je garde 
// FF   Fileformat      root	      number (range: 1-4)//je garde
// GM   Game            root	      number (range: 1-5,7-17)// je garde

// !SZ  Size            root	      (number | composed number ':' number)//je garde
// C    Comment         -                text  //je garde
// AE   Add Empty       setup            list of point  //je garde
// PL   Player to play  setup            color  //je garde
// B    Black           move             move //je garde
// W    White           move             move //je garde
// BL   Black time left move             real //on verra? utile juste en mode observation
// WL   White time left move             real//on verra? utile juste en mode observation'


    // private static ArrayList<Map> build_sgf(){
    //     return null;
    // }

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
            e.printStackTrace();
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
                    i^=1;//changer pour get_color de Map
                }
            }
            fileContent = fileContent.substring(0, fileContent.length() - 1);
            fileContent += ")";
            return fileContent;
    }

    public static void createSgf(ArrayList<Map> map, String rule){
        final int rule_type = "go".equals(rule) ? 1 : 4;
        String fileContent = "(;FF[4] " + "GM[" + rule_type + "4]" + " RU[" + rule + "] SZ[19] CA[UTF-8] AP[Gomoku:1]\n";
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        String fileName = localDate.toString() + "_" + localTime.toString();
        if (fileName.indexOf(".") != -1)
            fileName = "./sgf/" + rule + "_" + fileName.substring(0, fileName.indexOf("."));
        File file = new File(fileName + ".sgf");
        int i = 1;
        while (file.exists()){
            file = new File(fileName + "(" + i + ")" + ".sgf");
            i++;
        }
        file.setWritable(true);
        file.setReadable(true);
        try {
            Writer writer = new FileWriter(file, true);
            fileContent = add_moves(fileContent, map);
            writer.write(fileContent, 0, fileContent.length());
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String getExtension(File file){
        if (file != null){
            String filename = file.getName();
            int dotIndex = filename.lastIndexOf(".");
            if (dotIndex >= 0) {
                return filename.substring(dotIndex + 1);
            }
        }
        return null;
    }

    // public static boolean getGameContent(String file_content){
    //     Integer board_size;//changer
    //     int gm;
    //     String ru;
        
    //     //str.charAt(i)
    //     for (Integer i = 0; i < file_content.length(); i++){
    //         String word = "test";//getCurrentWord(String, i);
    //         // if (supportedSet.contains(word)){//file_content.charAT(i);//supportedSet.contains(possibleNullString);
    //         //     //faire action\
    //         //     //si ru //sz //km //... ff// ha// soit remplacer soit rejeter
    //         // }
    //         // else if (ignoreSet.contains(word)){
    //         //     //jump a la fin des values de l'instruciton []...[]
    //         // }
    //         // else
    //         //     ;//erreur et quitter false
    //     }
    //     // String ?;
    //     //ha km
    //     //skip white space Charachter.whitespace ? stringbuilder at i?
    //     //check ( ; puis get les infos header si pas info skip jusqua [ puis tout les [] sans char entre
    //     return true;
    // }

    // public static boolean getGameContent(){
    //     return true;
    // }
    // public static boolean parseFile()
    // {
        // return true ;}

    private static void trimSpace(StringBuilder sb) {
        int i = 0;
        while (i < sb.length() && Character.isWhitespace(sb.charAt(i))) {
            i++;
        }
        if (i > 0) {
            sb.delete(0, i);
    }
}

    private static CommandType getTypeCmd(String name){
        if (name == null)
            return CommandType.BRANCH;
        if (indexOf(name, NumCmdSet) != -1)
            return CommandType.NUM_VALUE;
        if (indexOf(name, PointCmdSet) != -1)
            return CommandType.COORD_VALUE;
        if (indexOf(name, listCmdSet) != -1)
            return CommandType.ARRAY_VALUE;
        else
            return CommandType.STRING_VALUE;
    }

    private  static Union getNode (CommandType type, String name){
        switch (type)
        {
            case BRANCH:
                return new Node();
            case MOVE:
                return new Node();
            case ARRAY_VALUE:
                return new ArrayValue(name, type);
            case STRING_VALUE:
                return new StringValue(name, type);
            case COORD_VALUE:
                return new CoordValue(name, type);
            case NUM_VALUE:
                return new NumValue(name, type);
            default:
                return null;
        }       
    }

    private  static String getCommandName(StringBuilder file){

        // // Effacer les 3 premiers caractères
        // file.delete(0, 3);

        // // Effacer le premier caractère
        // file.deleteCharAt(0);commandName
        int index = file.indexOf("[");//proteger pas trouve
        if (index == -1)
            return null;
        String command = file.substring(0, index);
        //verifier si dans supposrted ou unsupported set
        // System.out.println("command dans get command == " + command);
        file.delete(0, index);
        // System.out.println("file == " + file);
        return command;
    }

    private  static Number getValueNum(String val, String cmdName) throws ParseException{
        if ("KM".equals(cmdName))
        {
            try {
                return Double.parseDouble(val);
            }
            catch (NumberFormatException e) {
                throw new ParseException("invalid syntaxe " + val, 0);
            }
        }
        else{
            try {
                return Integer.parseInt(val);
            }
            catch (NumberFormatException e) {
                throw new ParseException("invalid syntaxe " + val, 0);
            }
        }
    }

    private  static Point getValueCoord(String val) throws ParseException{
        Point value = new Point();
        if (val.length() != 2)
            throw new ParseException("invalid syntaxe " + val, 0);
        if (Character.isAlphabetic(val.charAt(0))) {
            int index = Character.toLowerCase(val.charAt(0)) - 'a';
            System.out.println("index y == " + index);
            value.y = index;
        }
        else
            throw new ParseException("invalid syntaxe " + val, 0);
        if (Character.isAlphabetic(val.charAt(1))) {
            int index = Character.toLowerCase(val.charAt(1)) - 'a';
            System.out.println("index x == " + index);
            value.x = index;
        }
        else
            throw new ParseException("invalid syntaxe " + val, 0);
        return value;
    }

    private  static ArrayList<Point> getValueArray(String val, StringBuilder file) throws ParseException{
        ArrayList<Point> value = new ArrayList<Point>();
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("val ==  " + val);
        System.out.println("file ==  " + file);
        System.out.println("----------------------------------------------------------------------------------------");
//tant que getValueString retourne pas null ajouter a value
        return value;
    }

    private  static String getValueString(StringBuilder file) throws ParseException{
        trimSpace(file);
        // System.out.println("in strin val 2");
        //check bien [
        if (file.charAt(0) != '[')
            throw new ParseException("invalid syntaxe ", 0);
        int index = file.indexOf("]");
        // System.out.println("in strin val 3 inde == " + index);
        String value = file.substring(1, index);
        // System.out.println("val == " + value);
        file.delete(0, 2 + value.length());
        trimSpace(file);
        // System.out.println("file == " + file);
        return value;
    }


    private static int indexOf(String value, String[] array){
        for (int i = 0; i < array.length; i++)
        {
            if (array[i].equals(value))
                return i;
        }
        return -1;
    }

    // private static void eraseCmd(StringBuilder file){
    //     trimSpace(file);
    //     while (file.charAt(0) == '[')
    //     {
    //         getCommandName(file);
    //     }
    // }

    private static Node parseMove(StringBuilder file) throws ParseException{
        Node commandList = null;
        Node currentNode = null;
        while (file.toString().isEmpty() == false && file.charAt(0) != '(' && file.charAt(0) != ')' && file.charAt(0) != ';')
        {
            String commandName = getCommandName(file);
            // System.out.println("coucou");
            // System.out.println("cmd == " + commandName);
            if (commandName == null )//check si dans une des listes
                throw new ParseException("invalid syntaxe", 0);
            // System.out.println("coucou2");
            String val = getValueString(file);
            if (val == null )//check si dans une des listes
                throw new ParseException("invalid syntaxe", 0);
            Node newInstruct = new Node();
            newInstruct.setType(CommandType.INSTRUCTION);
            newInstruct.DataType = getNode(getTypeCmd(commandName), commandName);
            if (indexOf(commandName, NumCmdSet) != -1){
                System.out.println( "NUm val pars ==  " + getValueNum(val, commandName));
                newInstruct.DataType.setValue(getValueNum(val, commandName));
            }
            else if (indexOf(commandName, PointCmdSet) != -1)
                newInstruct.DataType.setValue(getValueCoord(val));
            else if (indexOf(commandName, listCmdSet) != -1)
                newInstruct.DataType.setValue(getValueArray(val, file));
            else
                newInstruct.DataType.setValue(val);
            if (commandList == null)
                commandList = newInstruct;
            else
                currentNode.next = newInstruct;
            currentNode = newInstruct;
            // System.out.println("coucou3");
            // System.out.println("value == " + val);
            // System.out.println("file == " + file);

           // if (indexOf(commandName, ignoreSet) != -1)
           // {
           //     String val = getValueString(file);
           //     System.out.println("value == " + val);
           //     System.out.println("file == " + file);
           //     // Node currentCommand = getNode(getCmdType(), commandName);
           // }
           // else
           // {
           //     eraseCmd(file);
           // }
        }
        return commandList;
    }

    private static Node buildTree(StringBuilder file, int deepth) throws ParseException{ 
        Node tree = new Node();
        tree.setType(CommandType.BRANCH);
        Node currentNode = tree;
        Node currentMove = null;

        // System.out.println("deepth == " + deepth);
        // Node currentMove = null;
        boolean branchDone = false;
        System.out.println("deepth == " + deepth + " file == " + file);
        if (deepth > 361)
            throw new ParseException("too many branch", 0);
        while (file.length() != 0){
            trimSpace(file);
            if (file.length() == 0)
                return tree;
            char next_char = file.charAt(0);
            file.deleteCharAt(0);
            System.out.println("deepth == " + deepth + " next char == " + next_char + " file == " + file);
            if (next_char == ')'){
                if (deepth == 0){
                    System.out.println("je passe par )");
                    System.out.println("file quand ) et 0 == " + file);
                    throw new ParseException("invalid syntaxe", 0);
                }
                else
                    return tree;
            }
            else if (next_char == '('){
                System.out.println("je passe par (");
                Node branch = buildTree(file, deepth + 1);
                branch.setType(CommandType.BRANCH);
                if (branch.DataType == null && branch.next == null)
                    throw new ParseException("invalid syntaxe", 0);
                currentNode.next = branch;
                currentNode = branch;
                branchDone = true;
                System.out.println("next == " + tree.next);
                System.out.println("next == " + currentNode);
            }
            else if (next_char == ';'){
                System.out.println("je passe par ;");
                if (branchDone == true)
                    throw new ParseException("invalid file format", 0);
                // System.out.println("je passe par (")
                Node newMove = new Node();
                newMove.setType(CommandType.MOVE);
                newMove.DataType = parseMove(file);
                if (currentMove == null)
                {
                    currentNode.DataType = newMove;
                    // currentMove = newMove;
                }
                else
                    currentMove.next = newMove;
                currentMove = newMove;
                currentNode = newMove;
            }
            else
                throw new ParseException("invalid file format", 0);
        }
        return tree;
    } 


    private static void printTree(Node tree, int depth) {
        if (tree == null)
            return;

        String indent = "  ".repeat(depth);
        System.out.println(indent + "Node type: " + tree.getType());
        if (tree.getType() == CommandType.MOVE) {
            Node list = (Node)tree.DataType;
            while (list != null){
                Union str = list.DataType;
                if (str.getType() == CommandType.NUM_VALUE)
                    System.out.println(indent + " NUM Command: " + ((NumValue)str).getCommand() + " -> " + ((NumValue)str).getVal());
                else if (str.getType() == CommandType.ARRAY_VALUE)
                    ;
                    // System.out.println(indent + " list Command: " + ((ArrayValue)str).getCommand() + " -> " + str.getVal());
                else if (str.getType() == CommandType.COORD_VALUE)
                    System.out.println(indent + " coord Command: " + ((CoordValue)str).getCommand() + " -> " + "y == " + ((CoordValue)str).getVal().y + " x " + ((CoordValue)str).getVal().x);
                else
                    System.out.println(indent + " str Command: " + ((StringValue)str).getCommand() + " -> " + ((StringValue)str).getVal());

                list = list.next;
            }
        }

        if (tree.getType() == CommandType.BRANCH && tree.DataType instanceof Node) {
            System.out.println(indent + "Entering branch:");
            printTree((Node) tree.DataType, depth + 1);
        }
        printTree(tree.next, depth);
    }

    //boolean set header()  si false throw dire multiple definition of name si != de val deja set? ou tjs quitter? 

    private static void handleKomi(Union node) throws ParseException{
        if (komi != -1)
            throw new ParseException("error, unexpected KM : multiples definition" + ((NumValue)node).getVal(), 0);
        komi = ((NumValue)node).getVal().doubleValue();
    }

    private static void handleHandicap(Union node) throws ParseException{
        // NumValue handicap =  (NumValue) node;
        if (handicap != -1)
            throw new ParseException("error, unexpected HA : multiples definition" + ((NumValue)node).getVal(), 0);
        handicap = ((NumValue)node).getVal().intValue();
    }

    private static void handleGameType(Union node) throws ParseException{
        // NumValue gameType =  (NumValue) node;
        if (ruleType != 0) 
            throw new ParseException("error, unexpected GM : multiples definition" + ((NumValue)node).getVal(), 0);
        ruleType = ((NumValue)node).getVal().intValue();
    }

    private static void handleBoardSize(Union node) throws ParseException{
        // NumValue size =  (NumValue) node;
        if (size != 0) 
            throw new ParseException("error, unexpected SZ : multiples definition" + ((NumValue)node).getVal(), 0);
        size = ((NumValue)node).getVal().intValue();
    }

    private static void handleRuleset(Union node) throws ParseException{
        // StringValue rule =  (StringValue) node;
        if (rules != null)
            throw new ParseException("error, unexpected RU : multiples definition" + ((StringValue)node).getVal(), 0);
        rules = ((StringValue)node).getVal();
    }

    private static void    setheader(Union node) throws ParseException{
        // System.out.println(node.getCommand());
        String command = node.getCommand();
        switch (command) {
            case "KM":
                handleKomi(node);
                break;
            case "HA":
                handleHandicap(node);
                break;
            case "GM":
                handleGameType(node);
                break;
            case "SZ":
                handleBoardSize(node);
                break;
            case "RU":
                handleRuleset(node);
                break;
        }
    }

    private static void    setCommand(Map map, Union node) throws ParseException{
        System.out.println("set command : " + node.getCommand());
    }

    private static void checkHeader(){
        if (size == 0)
            size = 19;
        if (rules == null)
            rules = "Gomoku";
        if (komi == -1)
            komi = 0;
        if (handicap == -1)
            handicap = 0;
    }

    private static void executeTree(Node tree, int depth) throws ParseException{
        if (tree == null)
            return;
        // String indent = "  ".repeat(depth);
        // System.out.println(indent + "Node type: " + tree.getType());
        if (tree.getType() == CommandType.MOVE) {
            Node list = (Node)tree.DataType;
            Map map = null;
            if (header == false)
                map = new Map(size);
            while (list != null){
                int isHeader = indexOf(list.DataType.getCommand(), rootCmdSet);
                System.out.println("header " + header + " is header == " + isHeader + " cmd == " + list.DataType.getCommand());
                // if (header == false && isHeader != -1)
                //     throw new ParseException("error, unexpected : " + list.DataType.getCommand(), 0);
                if (header == true && isHeader != -1)
                    setheader(list.DataType);
                else if (isHeader != -1){
                    header = false;
                    checkHeader();
                }
                if (header == true)
                    setCommand(map, list.DataType);
                //si cmd header add a la bonne var
                //
                // if ()
                //else()
                //si c'est dans list point try add
                //si c'est dans list
                // StringValue str = (StringValue) list.DataType;
                // System.out.println(indent + "  Command: " + str.getCommand() + " -> " + str.getVal());
                list = list.next;
            }
            game_moves.add(map);
        }

        if (tree.getType() == CommandType.BRANCH && tree.DataType instanceof Node) {
            // System.out.println(indent + "Entering branch:");
            executeTree((Node) tree.DataType, depth + 1);
        }
        executeTree(tree.next, depth);
    }

    public static boolean parseFile(){
        if ("sgf".equals(getExtension(file)) == false)
        {
            errorMsg = "invalid file ext";
            return false;
        }
        int bufferSize = 100;
        char[] buffer = new char[bufferSize];
        StringBuilder file_content = new StringBuilder();
        int charsRead;
        try {//try with ressource ?!??
            Reader reader = new FileReader( file);
            while ((charsRead = reader.read(buffer, 0, bufferSize)) > 0){
                // file_content += new String(buffer);
                file_content.append(buffer, 0, charsRead);
            }
            reader.close();
        }
        catch(Exception e){
            errorMsg = "error while reading file";
            e.printStackTrace();
        }

        System.out.println("file content == " + file_content);
        Node tree;
        try{
            tree = buildTree(file_content, 0);
            System.out.println("tree == " + tree);
            printTree(tree, 0);
            game_moves = new ArrayList<Map>();
            rules = null;
            ruleType = 0;
            size = 0;
            komi = -1;
            handicap = -1;
            header = true;
            executeTree(tree, 0);
        }
        catch (ParseException e){
            System.out.println("Parse error: " + e.getMessage());
            errorMsg = e.getMessage();
            e.printStackTrace();
            System.out.println("coucou");
            return false;
        }
        return true;
    }

    public static String get_game_rule(){
        return rules;
    }

    public static ArrayList<Map> get_game_moves(){
        return game_moves;
    }

    public static String get_file_name(){
        return file.getName();
    }

    public static void setFile(String absolute_path, String filename){
        file = new File(absolute_path, filename);
    }
    public static String getErrorMsg(){
        return errorMsg;
    }
}
