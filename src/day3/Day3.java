package day3;

import day3.ElfGroup;
import org.w3c.dom.ls.LSOutput;
import util.ReadFile;

import javax.xml.stream.events.Characters;
import java.util.*;

public class Day3 {

    public static void doPartA(){
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day3.txt";
        List<String> inputArray = ReadFile.toArray(inputPath);
        int totalValue = 0;
        for (String line: inputArray){
            int currentValue = 0;
            // split in half as array of chars and then create set
            int splitAt = (int) line.length() / 2;
            String firstHalf = line.substring(0, splitAt);
            String secondHalf = line.substring(splitAt);
            String sharedLetter = "";
            for (int i=0; i<splitAt; i++){
                String currentLetter = line.substring(i, i+1);
                if (secondHalf.contains(currentLetter)){
                    sharedLetter = currentLetter;
                    currentValue = getCharValue(sharedLetter.charAt(0));
                    totalValue += currentValue;
                    break;
                }
            }
//            List<Character> firstHalfList = new ArrayList<>();
//            List<Character> secondHalfList = new ArrayList<>();
//            for(int i=0; i<line.length(); i++){
//                if(i < splitAt) {
//                    firstHalfList.add(line.charAt(i));
//                } else{
//                    secondHalfList.add(line.charAt(i));
//                }
//            }
//            System.out.println(firstHalfList + "\n" + secondHalfList);
//
//            Set<Character> sharedChar = new HashSet<Character>(firstHalfList.stream().toList(),secondHalfList.stream().toList());
            System.out.printf("firstHalf: %s    secondHalf: %s      shared: %s  with value = %d\n",
                    firstHalf, secondHalf, sharedLetter, currentValue);
//            char[] secondHalf = line.substring(splitAt).toCharArray();
        }
        System.out.printf("Total Value = %d\n", totalValue);
    }

    public static void doPartB(){
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day3.txt";
//        List<String> inputArray = ReadFile.toArray(inputPath);
        List<String> inputArray = ReadFile.streamToArray(inputPath);
        // make sure list is divisible by two
        if (inputArray.size() % 3 != 0) throw new RuntimeException("The list length is not divisible by 3!");

        // Instantiate ElfGroups
        List<ElfGroup> elfGroups = new ArrayList<>();
        Iterator<String> iterator = inputArray.iterator();
        while(iterator.hasNext()){
            ElfGroup elfGroup = new ElfGroup(iterator.next(), iterator.next(), iterator.next());
            System.out.println(elfGroup);
            elfGroups.add(elfGroup);
        }

        int totalValue = elfGroups.stream()
                .map((element) -> element.getBadgeValue())
                .reduce((subtotal, badgeValue) -> subtotal + badgeValue)
                .orElse(0);
        System.out.println("Total Value = " + totalValue);
    }


    public static int getCharValue(char c){
        int value = Character.getNumericValue(c) - 9;
        if (Character.isUpperCase(c)) value += 26;
        return value;
    }

}
