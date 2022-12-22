package day7;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import util.ReadFile;

import java.util.*;
import java.util.stream.Collectors;

public class Day7 {

    public static ElfDrive elfDrive = new ElfDrive();
    public static boolean receivingInputMode = false;

    private static Map<String, Integer> sizeMap = new HashMap<>();

    public static void doPartA(){
//        testElfDir();
//        testElfDrive();
        String s = "7878";
//        Integer i = Integer.parseInt(s);
        System.out.println("Is " + s + " a number: " + NumberUtils.isDigits(s));


        String absPath = "/Users/brianignaczak/code/advent2022/src/static/input-day7.txt";
        List<String> lines = ReadFile.streamToArray(absPath);
//
        Set<String> commands = new HashSet<>();
//
//
        int i = 0;
        for(String line: lines) {
            parseLine(line, commands);
            i++;
//            if (i>14) break;
        }

//        System.out.println(commands);
        elfDrive.toString();

//        TreeNode treeNode = ElfDrive.getNodeGlobal("gbhcnts");
//        TreeNode treeNode = ElfDrive.getNodeGlobal("/root/qcznqph/mznnlph/gbhcnts");
//        TreeNode treeNode = ElfDrive.getNodeGlobal("/root/qcznqph/mznnlph/gbhcnts/gbhcnts");
//        TreeNode treeNode = ElfDrive.getNodeGlobal("/root/qcznqph/mznnlph/gbhcnts/gbhcnts/lwshph");
        TreeNode treeNode = ElfDrive.getNodeGlobal("/root/qcznqph/mznnlph/gbhcnts/gbhcnts/lwshph/gpqgrw");
//        TreeNode treeNode = ElfDrive.getNodeGlobal("/root/qcznqph/mznnlph/gbhcnts/gbhcnts/lwshph/gpqgrw/mbsgrlld");
//        TreeNode treeNode = ElfDrive.getNodeGlobal("/root/qcznqph/mznnlph/gbhcnts/gbhcnts/lwshph/gpqgrw/mbsgrlld/twdhlmp");
        System.out.printf("\n\nTesting dir retrieval, retrieved: %s with contents\n", treeNode);
        if (treeNode instanceof ElfDir elfDir){
            elfDir.getContents();
        }
        System.out.println("\nDirectory size = " + ElfDir.getSize(treeNode));


        for(TreeNode n: ElfDrive.nodes){
            if(n instanceof ElfDir elfDir){
                Integer dirSize = ElfDir.getSize(elfDir);
                String absPath2 = elfDir.getAbsPath();
                System.out.printf("About to put %s --> %d\n", absPath2, dirSize );
                sizeMap.put(absPath2, dirSize);
            }
        }
        System.out.println("\n\nNow for the whole size map\n______________________\n");
        System.out.println(sizeMap);

        Integer deleteSum = sizeMap.keySet().stream()
                .filter(key -> sizeMap.get(key) <= 100000)
                .map(sizeMap::get)
                .reduce(Integer::sum)
                .orElse(0);

        System.out.println("\nTotal amount of deletable files: " + deleteSum);


    }

    public static void doPartB() {
        Integer rootSize = sizeMap.get("/");
        Integer totalDiskSize = 70000000;
        Integer requiredSpace = 30000000;
        Integer maxSize = totalDiskSize - requiredSpace;
        Integer targetDeleteSize = rootSize - (maxSize);
        System.out.println("Root Size: " + rootSize);
        System.out.println("Target Delete Size: " + targetDeleteSize);
        System.out.println("For free space equal to: " + (rootSize - targetDeleteSize));
        System.out.println("____________________________");
        System.out.println(
                sizeMap.values().stream()
                .filter(val -> val >= targetDeleteSize)
                        .sorted().toList()
        );
        System.out.println("\n\n");

        Integer sizeToDelete = sizeMap.keySet().stream()
                .filter(key -> sizeMap.get(key) >= targetDeleteSize)
                .map(key -> sizeMap.get(key))
                .min((Integer::compare))
                .orElse(0);
        System.out.println("Must delete directory of size " + sizeToDelete);

        List<String> deleteDir = sizeMap.keySet().stream()
                .filter(key -> sizeMap.get(key) == sizeToDelete).toList();
        System.out.println(deleteDir);


    }

    public static void parseLine(String line, Set<String> commands){

        List<String> splitLine = Arrays.asList(line.split(" "));
        boolean isCommand = splitLine.get(0).equalsIgnoreCase("$");
//        System.out.println(line + " -- " + isCommand + " -- " + splitLine.get(0));
        if (isCommand){
            receivingInputMode = false;
            String commandString = splitLine.get(1);
            commands.add(commandString);
//            System.out.println(splitLine.subList(2,splitLine.size()));
            handleCommand(commandString, splitLine.subList(2,splitLine.size()));
        } else {
            parseListDir(line);
        }
    }

    public static void handleCommand(String commandString, List<String> args){
        System.out.printf("Command %s with args %s\n", commandString, args);
//        String commandString = lin
        switch (commandString){
            case "cd" -> elfDrive.changeDirFromPwd(args.get(0));
            case "ls" -> receivingInputMode = true;
            default -> throw new RuntimeException("No command matched in switch statemet");
        }
        System.out.println(ElfDrive.pwd.toString());
    }

    public static void parseListDir(String line){
        List<String> input = Arrays.asList(line.split(" "));
        boolean isFile = StringUtils.isNumeric(input.get(0));
        System.out.println("for '" + line + "' first input " + input.get(0) + " is numeric: " + isFile);
        if (isFile){
            // Create a file in the pwd
            int fileSize = Integer.parseInt(input.get(0));
            ElfDrive.getNodeInPwd(input.get(1), fileSize);
        }else{
            // create a dir in the pwd if not existing
            ElfDrive.getNodeInPwd(input.get(1), 0);
        }

    }


    public static void testElfDir(){
        ElfDir elfDir = new ElfDir();
        System.out.println(elfDir);
    }

    public static void testElfDrive(){
        System.out.println(elfDrive.toString());
        elfDrive.changeDirFromPwd("testDir");
        System.out.println(elfDrive);
        elfDrive.changeDirFromPwd("numTwo");
        System.out.println(elfDrive);



    }

}
