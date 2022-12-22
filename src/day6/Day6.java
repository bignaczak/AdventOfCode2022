package day6;

import util.ReadFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 {

    public static void doPartA(){
        String absPath = "/Users/brianignaczak/code/advent2022/src/static/input-day6.txt";
        List<String> lines = ReadFile.streamToArray(absPath);
        String line = lines.get(0);
        int bufferSize = 4;

        System.out.println("Answer is " + getStartPosition(line, bufferSize));

    }
    public static void doPartB(){
        String absPath = "/Users/brianignaczak/code/advent2022/src/static/input-day6.txt";
        List<String> lines = ReadFile.streamToArray(absPath);
        String line = lines.get(0);
        int bufferSize = 14;

        System.out.println("Answer is " + getStartPosition(line, bufferSize));

    }


    private static int getStartPosition(String line, int bufferSize){

        for(int i=0; i<line.length()-bufferSize; i++){
            String buffer = line.substring(i, i+bufferSize);
            System.out.println(buffer);
            Set<String> uniqueLetters = new HashSet<String>(Arrays.asList(buffer.split("")));
//            System.out.println(uniqueLetters.size());
            if(uniqueLetters.size() == bufferSize){
                return (i+bufferSize);
            }
//            if (i>60) break;
        }

        return 0;

    }
}
