package org.interfacegui;
import org.utils.*;
import org.ast.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.Writer;
import java.io.Reader;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.LocalDate;
import java.io.File;
import java.text.ParseException;

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
    private static Rules ruleInstance;
    // private static final String[] ignoreSet = new String[] {//purquoi c'est pas actif ca???
    //     "BM", "DO", "IT", "KO", "MN", "OB", "OW", "TE", "AR", "CR",
    //     "DD", "DM", "FG", "GB", "GW", "HO", "LB", "LN",
    //     "MA", "N", "PM", "SL", "SQ", "TR", "UC", "V", "VW", "ST",
    //     "AN", "BR", "BT", "CP", "DT", "EV", "GC", "GN", "ON", "OT",
    //     "PB", "PW", "RE", "RO", "SO", "TM", "US", "WR", "WT", "TB",
    //     "TW", "AS", "IP", "IY", "SE", "SU", "FF", "BL", "WL", "CA", "AP"
    // };//a utiliser???
    // private static final String[] supportedSet = new String[] {"KM", "HA", "AP", "CA", "GM", "SZ", "C", "AE", "PL", "B", "W", "AB", "AW"};
    private static final String[] listCmdSet = new String[] {"AB", "AW", "AE"};
    private static final String[] rootCmdSet = new String[] {"KM", "HA", "GM", "SZ", "RU", "AP", "CA", "FF", "ST"};
    private static final String[] PointCmdSet = new String[] {"B", "W"};
    private static final String[] NumCmdSet = new String[] {"SZ", "HA", "KM", "GM"};

    private static void init_rules(){
        rules = rules.toLowerCase();
        switch (rules){
            case "pente" :
                ruleInstance = new PenteRules();
                break;
            case "renju" :
                ruleInstance = new RenjuRules();
                break;
            case "go":
                ruleInstance = new GoRules();
                break;
            default:
                ruleInstance = new GomokuRules();
        }
        ruleInstance.setBoardSize(size);
    }

    public static File openSGFDir(){
        try {
            File directory = new File("sgf");
            if (!directory.exists()) {
                if (directory.mkdirs() == false) {
                    return null;
                }
            }
            return directory;
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return null;

    }

    private static String add_moves(String fileContent, ArrayList<Map> map){
            final String alpha = "abcdefghijklmnopqrstuvwxyz";
            // final String[] move = new String[] { "B", "W" };
            final String[] move = new String[] {"AE", "AB", "AW"};
            //int i = 0;
            for (Map m : map){
                ArrayList<Point> lastMove = m.getLastMove();
                ArrayList<Integer> lastMoveColor = m.getLastMoveColor();
                fileContent += ";";
                for (int j = 0; j < lastMove.size(); j++){
                    if (m.getLastMove() != null){
                        fileContent += move[lastMoveColor.get(j)];
                        fileContent += "[" + alpha.charAt(lastMove.get(j).x) + "" + alpha.charAt(lastMove.get(j).y)  + "]";
                        if (m.get_prisonners() != null && m.get_prisonners().size() > 0)
                            fileContent += " AE";
                        for (Point p : m.get_prisonners()){
                            fileContent += "[" + alpha.charAt(p.x) + "" + alpha.charAt(p.y) + "]";
                        }
                        fileContent += "\n";
                        //i^=1;//changer pour get_color de Map
                    }
                }
            }
            fileContent = fileContent.substring(0, fileContent.length() - 1);
            fileContent += ")";
            return fileContent;
    }


    public static void createSgf(ArrayList<Map> map, String rule){
        final int rule_type = "go".equals(rule) || "Go".equals(rule)? 1 : 4;
        String fileContent = "(;FF[4] " + "GM[" + rule_type + "] RU[" + rule + "] SZ[" + map.get(0).getSize() + "] CA[UTF-8] AP[Gomoku:1]\n";
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
        int index = file.indexOf("[");
        if (index == -1)
            return null;
        String command = file.substring(0, index);
        file.delete(0, index);
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
            value.x = index;
        }
        else
            throw new ParseException("invalid syntaxe " + val, 0);
        if (Character.isAlphabetic(val.charAt(1))) {
            int index = Character.toLowerCase(val.charAt(1)) - 'a';
            value.y = index;
        }
        else
            throw new ParseException("invalid syntaxe " + val, 0);
        return value;
    }

    private  static ArrayList<Point> getValueArray(String val, StringBuilder file) throws ParseException{
        ArrayList<Point> value = new ArrayList<Point>();
        value.add(getValueCoord(val));
        while (file.charAt(0) == '['){
            val = getValueString(file);
            value.add(getValueCoord(val));
        }
        return value;
    }

    private  static String getValueString(StringBuilder file) throws ParseException{
        trimSpace(file);
        if (file.charAt(0) != '[')
            throw new ParseException("invalid syntaxe ", 0);
        int index = file.indexOf("]");
        String value = file.substring(1, index);
        file.delete(0, 2 + value.length());
        trimSpace(file);
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

    private static Node parseMove(StringBuilder file) throws ParseException{
        Node commandList = null;
        Node currentNode = null;
        while (file.toString().isEmpty() == false && file.charAt(0) != '(' && file.charAt(0) != ')' && file.charAt(0) != ';')
        {
            String commandName = getCommandName(file);
            if (commandName == null )
                throw new ParseException("invalid syntaxe", 0);
            String val = getValueString(file);
            if (val == null )
                throw new ParseException("invalid syntaxe", 0);
            Node newInstruct = new Node();
            newInstruct.setType(CommandType.INSTRUCTION);
            newInstruct.DataType = getNode(getTypeCmd(commandName), commandName);
            if (indexOf(commandName, NumCmdSet) != -1){
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
        }
        return commandList;
    }

    private static Node buildTree(StringBuilder file, int deepth) throws ParseException{ 
        Node tree = new Node();
        tree.setType(CommandType.BRANCH);
        Node currentNode = tree;
        Node currentMove = null;

        boolean branchDone = false;
        if (deepth > 361)
            throw new ParseException("too many branch", 0);
        while (file.length() != 0){
            trimSpace(file);
            if (file.length() == 0)
                return tree;
            char next_char = file.charAt(0);
            file.deleteCharAt(0);
            if (next_char == ')'){
                if (deepth == 0){
                    throw new ParseException("invalid syntaxe", 0);
                }
                else
                    return tree;
            }
            else if (next_char == '('){
                Node branch = buildTree(file, deepth + 1);
                branch.setType(CommandType.BRANCH);
                if (branch.DataType == null && branch.next == null)
                    throw new ParseException("invalid syntaxe", 0);
                currentNode.next = branch;
                currentNode = branch;
                branchDone = true;
            }
            else if (next_char == ';'){
                if (branchDone == true)
                    throw new ParseException("invalid file format", 0);
                Node newMove = new Node();
                newMove.setType(CommandType.MOVE);
                newMove.DataType = parseMove(file);
                if (currentMove == null)
                {
                    currentNode.DataType = newMove;
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

    private static void handleKomi(Union node) throws ParseException{
        if (komi != -1)
            throw new ParseException("error, unexpected KM : multiples definition" + ((NumValue)node).getVal(), 0);
        komi = ((NumValue)node).getVal().doubleValue();
    }

    private static void handleHandicap(Union node) throws ParseException{
        if (handicap != -1)
            throw new ParseException("error, unexpected HA : multiples definition" + ((NumValue)node).getVal(), 0);
        handicap = ((NumValue)node).getVal().intValue();
    }

    private static void handleGameType(Union node) throws ParseException{
        if (ruleType != 0) 
            throw new ParseException("error, unexpected GM : multiples definition" + ((NumValue)node).getVal(), 0);
        ruleType = ((NumValue)node).getVal().intValue();
    }

    private static void handleBoardSize(Union node) throws ParseException{
        if (size != 0) 
            throw new ParseException("error, unexpected SZ : multiples definition" + ((NumValue)node).getVal(), 0);
        size = ((NumValue)node).getVal().intValue();
    }

    private static void handleRuleset(Union node) throws ParseException{
        if (rules != null)
            throw new ParseException("error, unexpected RU : multiples definition" + ((StringValue)node).getVal(), 0);
        rules = ((StringValue)node).getVal();
    }

    private static void    setheader(Union node) throws ParseException{
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
        if (node instanceof CoordValue)
        {
            if (PlayMove(((CoordValue)node).getVal(), game_moves, map, node.getValue()) == false)
                throw new ParseException("error, unexpected " + node.getValue() + " invalid coordinate : + " + ((CoordValue)node).getVal() + " invalid coordinate", 0);
        }
        else if (node instanceof StringValue){
            map.setComment(((StringValue)node).getVal());
        }
        else if (node instanceof ArrayValue){
            for (Point p : ((ArrayValue)node).getVal()){
                if (PlayMove(p, game_moves, map, node.getValue()) == false)
                    throw new ParseException("error, unexpected " + node.getValue() + " invalid coordinate : + " + p + " at move " + game_moves.size(), 0);
            }
        }
    }

    private static void checkHeader(){
        if (size == 0)
            size = 19;
        if (rules == null)
            rules = "gomoku";
        if (komi == -1)
            komi = 0;
        if (handicap == -1)
            handicap = 0;
        if (ruleType == 1)
            rules = "go";
        init_rules();    
    }

    private static void executeTree(Node tree, int depth, int index) throws ParseException{
        if (tree == null || index != 1)
            return;
        if (tree.getType() == CommandType.MOVE) {
            Node list = (Node)tree.DataType;
            Map map = null;
            if (game_moves.size() != 0) 
                map = new Map(game_moves.get(game_moves.size()-1));
            int isHeader = 0;
            while (list != null){
                if (header == true)
                    isHeader = indexOf(list.DataType.getCommand(), rootCmdSet);
                if (isHeader == -1 && header == true){
                    header = false;
                    isHeader = 0;
                    checkHeader();
                    map = new Map(size);
                    if (game_moves.size() == 0){
                        game_moves.add(map);
                        map = new Map(size);
                    }
                }
                if (header == true)
                    setheader(list.DataType);
                else
                    setCommand(map, list.DataType);
                list = list.next;
            }
            if (map != null){
                game_moves.add(map);
            }

        }
        if (tree.getType() == CommandType.BRANCH && tree.DataType instanceof Node) {
            executeTree((Node) tree.DataType, depth + 1, index);
        }
        if (tree.next != null) {
            if (tree.next.getType() == CommandType.BRANCH && tree.getType() == CommandType.BRANCH) {
                executeTree(tree.next, depth, index + 1);
            }
            else{
                executeTree(tree.next, depth, index);
            }
        }
    }

    public static void printTree(Node tree, int depth, int index) {
        if (tree == null)
            return;

        String indent = "  ".repeat(depth);
        System.out.println(indent + "Dans printTree, index = " + index);

        if (tree.getType() == CommandType.MOVE) {
            Node list = (Node) tree.DataType;
            while (list != null) {
                Union str = list.DataType;
                if (str.getType() == CommandType.NUM_VALUE)
                    System.out.println(indent + "NUM: " + ((NumValue) str).getCommand() + " -> " + ((NumValue) str).getVal());
                else if (str.getType() == CommandType.COORD_VALUE)
                    System.out.println(indent + "COORD: " + ((CoordValue) str).getCommand() + " -> y=" + ((CoordValue) str).getVal().y + ", x=" + ((CoordValue) str).getVal().x);
                else if (str.getType() == CommandType.STRING_VALUE)
                    System.out.println(indent + "STR: " + ((StringValue) str).getCommand() + " -> " + ((StringValue) str).getVal());
                list = list.next;
            }
        }
        if (tree.getType() == CommandType.BRANCH && tree.DataType instanceof Node) {
            printTree((Node) tree.DataType, depth + 1, index);
        }
        if (tree.next != null) {
            if (tree.next.getType() == CommandType.BRANCH && tree.getType() == CommandType.BRANCH) {
                printTree(tree.next, depth, index + 1);
            }
            else{
                printTree(tree.next, depth, index);
            }
        }
    }

    public static String incrementLast(String input) {
        String[] parts = input.split("\\.");
        int lastIndex = parts.length - 1;
        int lastNumber = Integer.parseInt(parts[lastIndex]);
        parts[lastIndex] = String.valueOf(lastNumber + 1);
        return String.join(".", parts);
    }

    private static void checkRules()
    {
        if (ruleType == 1)
            rules = "go";
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
        try {
            Reader reader = new FileReader( file);
            while ((charsRead = reader.read(buffer, 0, bufferSize)) > 0){
                file_content.append(buffer, 0, charsRead);
            }
            reader.close();
        }
        catch(Exception e){
            errorMsg = "error while reading file";
            return false;
        }
        Node tree;
        try{
            tree = buildTree(file_content, 0);
            game_moves = new ArrayList<Map>();
            rules = null;
            ruleType = 0;
            size = 0;
            komi = -1;
            handicap = -1;
            header = true;
            executeTree(tree.next, 0, 1);
            checkRules();
        }
        catch (ParseException e){
            errorMsg = e.getMessage();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String get_game_rule(){
        return rules;
    }

    public static int getSize(){
        return size;
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

    public static Rules getRuleInstance(){
        return ruleInstance;
    }

    private static boolean PlayMove(Point p, ArrayList<Map> mapList, Map map,String color){
        mapList.add(map);
        if ("AE".equals(color) == false && ruleInstance.isValidMove(p, mapList) == false)
        {
            return false;
        }

        if (map.tryAddToMap(color, p) == false) 
            return false;
        mapList.remove(mapList.size() - 1);
        if ("AE".equals(color))
            return true;
        int colorVal = color.contains("B") ? 1 : 2;
        if ((ruleInstance instanceof GomokuRules) == false)
        {
            ArrayList<Point> points = ruleInstance.GetCapturedStones(p, map);
            map.remove_prisonners(points);
            if (colorVal == 1)
                map.addBlackPrisonners(points.size());
            else
                map.addWhitePrisonners(points.size());
            map.set_prisonners(points);
        }
        return true;
    }

}
