package day11;

import util.ReadFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 {

    public static void doPartA(){
        var inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day11.txt";
        List<String> lines = ReadFile.streamToArray(inputPath);
        Function<Integer, Integer> worryOperation = (input) -> input * 5;

        int myOutput  = worryOperation.apply(5);
        System.out.println(myOutput);

        createMonkeys();
//        createTestMonkeys();

        int numRounds = 20;
        for(int i=0; i<numRounds; i++){
            playRoundAllMonkeys(Monkey.monkeys);
        }

        System.out.println("\n\n***********************\nAfter " + numRounds + " rounds\n");
        for(var m: Monkey.monkeys){
            System.out.println(m);
        }

        var numObservations = Monkey.monkeys.stream()
                .map((m) -> m.numInspections)
                        .sorted(Integer::compare)
                .sorted(Comparator.reverseOrder())
                                .toList();

        System.out.println(numObservations.subList(0,2));
        System.out.println("Answer is: " + numObservations.get(0) * numObservations.get(1));


        //136524 too high
        //121103 too high
    }

    public static void playRoundAllMonkeys(List<Monkey> monkeys){
        for(var m: monkeys){
            System.out.println("Before Round: " + m);
            m.playRound();
            System.out.println();

        }
        System.out.println("\n+++++++++++++++=\nAfter Round: ");
        Monkey.monkeys.forEach(System.out::println);
    }

    public static void createMonkeys(){

                new Monkey(
                        0
                        , new ArrayList<Integer> (Arrays.asList(92, 73, 86, 83, 65, 51, 55, 93))
                        , (old) -> old * 5
                        , (old) -> (old % 11 == 0) ? 3 : 4
                );

                new Monkey(
                        1
                        , new ArrayList<Integer>(Arrays.asList(99, 67, 62, 61, 59, 98))
                        , (old) -> old * old
                        , (old) -> (old % 2 == 0) ? 6 : 7
                );


                new Monkey(
                        2
                        , new ArrayList<Integer>(Arrays.asList(81, 89, 56, 61, 99))
                        , (old) -> old * 7
                        , (old) -> (old % 5 == 0) ? 1 : 5
                );
                new Monkey(
                        3
                        , new ArrayList<Integer>(Arrays.asList(97, 74, 68))
                        , (old) -> old + 1
                        , (old) -> (old % 17 == 0) ? 2 : 5
                );
                new Monkey(
                        4
                        , new ArrayList<Integer>(Arrays.asList(78, 73))
                        , (old) -> old + 3
                        , (old) -> (old % 19 == 0) ? 2 : 3
                );
                new Monkey(
                        5
                        , new ArrayList<Integer>(Arrays.asList(50))
                        , (old) -> old + 5
                        , (old) -> (old % 7 == 0) ? 1 : 6
                );
                new Monkey(
                        6
                        , new ArrayList<Integer>(Arrays.asList(95, 88, 53, 75))
                        , (old) -> old + 8
                        , (old) -> (old % 3 == 0) ? 0 : 7
                );
                new Monkey(
                        7
                        , new ArrayList<Integer>(Arrays.asList(50, 77, 98, 85, 94, 56, 89))
                        , (old) -> old + 2
                        , (old) -> (old % 13 == 0) ? 4 : 0
                );

    }

    private static void createTestMonkeys(){
        new Monkey(
                0
                , new ArrayList<Integer> (Arrays.asList(79, 98))
                , (old) -> old * 19
                , (old) -> (old % 23 == 0) ? 2 : 3
        );
        new Monkey(
                1
                , new ArrayList<Integer> (Arrays.asList(54, 65, 75, 74))
                , (old) -> old + 6
                , (old) -> (old % 19 == 0) ? 2 : 0
        );

        new Monkey(
                2
                , new ArrayList<Integer> (Arrays.asList(79, 60, 97))
                , (old) -> old * old
                , (old) -> (old % 13 == 0) ? 1 : 3
        );
        new Monkey(
                3
                , new ArrayList<Integer> (Arrays.asList(74))
                , (old) -> old + 3
                , (old) -> (old % 17 == 0) ? 0 : 1
        );

    }



}
