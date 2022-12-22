package day5;

import util.ReadFile;

import java.util.*;
import java.util.function.Predicate;

public class Day5 {

    public static List<String> rawInput;
    public static Map<Integer, Integer> cargoImportMap;  // key: {cargoId}  value: {line position of value}
    public static int labelLineNum;
    public static int numCargoRows;
    public static List<CrateMove> crateMoves = new ArrayList<>();

    public static Map<Integer, List<String>> crates = new HashMap<>();

    public static void doPartA(){
        String absPath = "/Users/brianignaczak/code/advent2022/src/static/input-day5.txt";
        rawInput = ReadFile.streamToArray(absPath);
        crates = getInitialCrates();

        printCrates("Initial");
        setCrateMoves();

//        for(CrateMove cm: crateMoves){
//            makeMove(cm);
//            printCrates("After move " + cm);
//            break;
//        }

        makeMoves();
        System.out.println("Top of Crates: " + cratesAtTop());

    }

    public static void doPartB(){
        String absPath = "/Users/brianignaczak/code/advent2022/src/static/input-day5.txt";
        rawInput = ReadFile.streamToArray(absPath);
        crates = getInitialCrates();

        printCrates("Initial");
        setCrateMoves();


//        CrateMove cm = new CrateMove(2,2,1);
//        makeMovePartB(cm);
//        printCrates("After move ");


        makeMovesPartB();
        System.out.println("Top of Crates: " + cratesAtTop());

    }

    private static Map<Integer, List<String>> getInitialCrates(){
        HashMap<Integer, List<String>> initialCrates = new HashMap<>();
        List<Integer> keys = getCrateKeys();
        setCargoImportMap();
        initializeHashMap(keys);

        // loop backward through the crate rows
        int firstCrateRow = labelLineNum - 1;
        // loop through each row of crates
        for(int i = firstCrateRow; i>=0; i--){
            String currentCargoLine = rawInput.get(i);
            // loop through each crate position
            for(Integer cargokey: cargoImportMap.keySet()){
                String currentCargo = "";
                int cargoLinePosition = cargoImportMap.get(cargokey);
                // add cargo if present
                if (currentCargoLine.length() > cargoLinePosition) {
                    currentCargo = currentCargoLine.substring(cargoLinePosition, cargoLinePosition + 1);
                    if (! currentCargo.isBlank()) {
                        System.out.printf("About to put %s in stack %d\n", currentCargo, cargokey);
                        CrateStack.get(cargokey).push(currentCargo);
                    }
                }
            }
        }
        for(CrateStack crateStack: CrateStack.stackMap.values()){
            System.out.println(crateStack);
        }
        return initialCrates;
    }

    private static List<Integer> getCrateKeys(){
        Iterator<String> iterator = rawInput.iterator();
        String line, keys = "";
        labelLineNum = -1;
        while(iterator.hasNext() && !(line = iterator.next()).isBlank()){
            System.out.println(line);
            keys = line;
            labelLineNum++;
        }
        System.out.println("******  THE KEYS  *********");
        List<Integer> keyList = Arrays.stream(keys.split(" "))
//                .map(String::trim)
                .filter(Predicate.not(String::isBlank))
                .map(Integer::parseInt)
                .toList();
        System.out.printf("Keylist: %s\n", keyList);
        System.out.println(labelLineNum);
        iterator.remove();
        return keyList;
    }

    /**
     * sets Import map, which gives string position of crate info for a given line
     */
    private static void setCargoImportMap(){
        cargoImportMap = new HashMap<>();
        String line, freight = "";
        String firstCargoRow = rawInput.get(labelLineNum-1);
        System.out.println("*****    FIRST CARGO ROW    ******");
        System.out.println(firstCargoRow);

        int currentCrate = 1;
        for(int i=0; i<firstCargoRow.length(); i++){
            char currentChar = firstCargoRow.charAt(i);
            if(Character.isLetter(currentChar)){
                cargoImportMap.put(currentCrate, i);
                currentCrate++;
            }
        }
        System.out.println(cargoImportMap);
    }

    private static void initializeHashMap(List<Integer> keys){
//        HashMap<Integer, List<String>> initializedMap = new HashMap<>();
        for(Integer k: keys){
//            initializedMap.put(k, new ArrayList<>());
            new CrateStack(k);
        }
        printCrates("Initial");
//        System.out.printf("Initialized map: %s\n", initializedMap);

    }

    private static void setCrateMoves(){
        for(int i=labelLineNum+1; i<rawInput.size(); i++){
            String[] moveRow = rawInput.get(i).split(" ");
            crateMoves.add(new CrateMove(Integer.parseInt(moveRow[1]),
                    Integer.parseInt(moveRow[3]),
                    Integer.parseInt(moveRow[5])));
        }

//        System.out.println("\n\n******   Crate Moves  ********");
//        for(CrateMove cm: crateMoves){
//            System.out.println(cm);
//        }
//        System.out.println("----------------------------\n\n");

    }

    private static void makeMoves(){
        for (CrateMove cm: crateMoves){
            for(int i=0; i<cm.numMoves(); i++){
                CrateStack.get(cm.fromCrate()).moveCrateTo(cm.toCrate());
            }
        }
        printCrates("After Part A");
    }

    private static void makeMove(CrateMove crateMove){
        for(int i=0; i<crateMove.numMoves(); i++) {
            CrateStack.get(crateMove.fromCrate()).moveCrateTo(crateMove.toCrate());
        }
    }

    private static void makeMovePartB(CrateMove crateMove){
        CrateStack.get(crateMove.fromCrate()).moveMultipleCratesTo(crateMove);
    }

    private static void makeMovesPartB(){
        for (CrateMove cm: crateMoves){
            makeMovePartB(cm);
        }
        printCrates("After Part B");
    }

    private static void printCrates(String condition){
        System.out.println("Crates at condition:  " + condition);
        System.out.println("----------------------------");
        for (CrateStack cs: CrateStack.stackMap.values()){
            System.out.println(cs);
        }
        System.out.println("----------------------------");
    }

    private static String cratesAtTop(){
        String topCrates = "";
        for(CrateStack cs: CrateStack.stackMap.values()){
            topCrates += cs.stringStack.lastElement();
        }
        return topCrates;
    }

}
