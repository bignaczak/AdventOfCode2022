import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Day1 {

    static String filepath = "/src/static/input-day1a.txt";
    static String path = "/Users/brianignaczak/code/advent2022/src/static/input-day1a.txt";
    static FileReader fileReader;

    public static void doPart1(){
        File file = getFile();
        BufferedReader reader = getReader(file);
        List<Integer> allElves = getSortedArrayOfElfCalories(reader);
        System.out.println("From Standard Method");
        allElves.forEach(System.out::println);
        getMaxCalories(allElves, 1);
        getMaxCalories(allElves, 3);
//        int sumTopThree = getTopThreeSum(reader);

        closeReader(reader);
    }

    public static void streamTest(){
        File file = getFile();
        BufferedReader reader = getReader(file);
        List<Integer> rawData = getRawDataList(reader);

        List<Integer> elfTotals = new ArrayList<>();
        rawData.stream()
                .reduce((subTotal, element) -> {
                    if (element == null){
                        elfTotals.add(subTotal);
                        subTotal = 0;
                    } else{
                        subTotal += element;
                    }
                    return subTotal;
                });

        System.out.println("From Stream Method");
        Optional<Integer> maxCals = elfTotals.stream()
                .reduce((subTotal, element) -> {
                    System.out.println(element);
                    return Math.max(subTotal, element);
                });
        System.out.println("\n\nMax Value from Stream method");
        System.out.println(maxCals.orElse(0));
    }

    private static File getFile(){
        try {
            //final String pwd = new File(".").getCanonicalPath();
            //System.out.println(pwd);
            File file = new File(path);
            fileReader = new FileReader(file);
            System.out.println("File read correctly");
            return file;
        } catch (FileNotFoundException e) {
            System.out.println("File not read correctly");
            throw new RuntimeException(e);
        }
    }

    private static BufferedReader getReader(File file){

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            System.out.println("Buffered Reader Created");
            return reader;
        } catch (FileNotFoundException e) {
            System.out.println("Error Creating buffered reader");
            throw new RuntimeException(e);
        }

    }

    private static List<Integer> getChunk(BufferedReader reader){
        String line = "";
        int readLength = 20;
        List<String> myArray = new ArrayList<>();
        List<Integer> calories = new ArrayList<>();

//        for(int i=0; i<readLength; i++) {
        try {
            while(((line = reader.readLine()) != null) && !line.isBlank()) {
                myArray.add(line);
                calories.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return calories;

    }

    public static int getMaxCalories(List<Integer> allElves, int sumTopN){
        int summedCalories = 0;

        for(int i=0; i<sumTopN; i++){
            summedCalories += allElves.get(i);
        }

        System.out.printf("End of file sensed after %d elves.  Max cals for top %d elves is %d\n", allElves.size(), sumTopN, summedCalories);
        return summedCalories;
    }

    public static List<Integer> getSortedArrayOfElfCalories(BufferedReader reader){
        List<Integer> chunk;
        List<Integer> allElves = new ArrayList<>();

        while(!(chunk = getChunk(reader)).isEmpty()){
            int currentCalories = sumChunk(chunk);
            allElves.add(currentCalories);
        }
        Collections.sort(allElves);
        Collections.reverse(allElves);
        return allElves;
    }

    public static void closeReader(BufferedReader reader){
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getTopThreeSum(BufferedReader reader){

    }

    public static int sumChunk(List<Integer> chunk){
        int totalCalories = chunk.stream()
                .reduce(0, (subtotal, element) -> subtotal + element);
        return totalCalories;
    }

    private static List<Integer> getRawDataList(BufferedReader reader){
        String line;
        List<Integer> rawData = new ArrayList<>();
        while(true){
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try{
                rawData.add(Integer.parseInt(line));
            } catch (NumberFormatException e){
                //do nothing
                rawData.add(null);
            }

        }
        return rawData;
    }

}
