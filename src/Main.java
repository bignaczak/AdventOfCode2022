import day10.Day10;
import day11.Day11;
import day12.Day12;
import day14.Day14;
import day8.Day8;
import day9.Day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //      DAY14
        //***********************************************************
        Day14.doPartA();
//        Day14.doPartB();

        //      DAY12
        //***********************************************************
//        Day12.doPartA_V2();
//        Day12.doPartB();

        // test reduce
//        ArrayList<Integer> myInts = new ArrayList<>(Arrays.asList(6,3,6));
//        System.out.println(myInts.stream()
//                .reduce(1, (accum, val) -> val * accum)
//        );


        //      DAY11
        //***********************************************************
//        Day11.doPartA();

        //      DAY10
        //***********************************************************
//        Day10.doPartA();


        //      DAY9
        //***********************************************************
//        Day9.doPartA();
//        Day8.doPartB();

        //      DAY8
        //***********************************************************
//        Day8.doPartA();
//        Day8.doPartB();

        //504 too low

        //      DAY7
        //***********************************************************
//        Day7.doPartA();
//        Day7.doPartB();

        //      DAY6
        //***********************************************************
//        Day6.doPartA();
//        Day6.doPartB();

        //      DAY5
        //***********************************************************
//        Day5.doPartA();
//        Day5.doPartB();



        //      DAY4
        //***********************************************************
//        Day4.doPartA();
//        Day4.doPartB();
//        Day4.doPartA2();
//        Day4.doPartA3();


        //      DAY3
        //***********************************************************
//        day3.Day3.doPartA();
//        Day3.doPartB();




        //      DAY2
        //***********************************************************
        /*
        Day2.Match myMatch = new Day2.Match(Day2.Play.ROCK, Day2.Play.PAPER);
        System.out.printf(myMatch + " with winner: %s and myScore: %d\n",
                myMatch.determineWinner(), myMatch.getMyScore());

        Day2.Match myMatch2 = new Day2.Match(Day2.Play.PAPER, Day2.Play.ROCK);
        System.out.printf(myMatch2 + " with winner: %s and myScore: %d\n",
                myMatch2.determineWinner(), myMatch2.getMyScore());

        Day2.Match myMatch3 = new Day2.Match(Day2.Play.SCISSORS, Day2.Play.SCISSORS);
        System.out.printf(myMatch3 + " with winner: %s and myScore: %d\n",
                myMatch3.determineWinner(), myMatch3.getMyScore());

        System.out.println("Part A");
        System.out.println("-----------------------------");
        Day2.readAllMatches(Day2.StrategyInterpretation.A);
        Day2.calculateTotalScore();

        System.out.println();
        System.out.println("Part B");
        System.out.println("-----------------------------");
        Day2.readAllMatches(Day2.StrategyInterpretation.B);
        Day2.calculateTotalScore();

        System.out.println();
        System.out.println("Unique combinations in list:");
        System.out.println("-----------------------------");
        for (Triplet<Day2.Play, Day2.Play, Day2.Outcome> t: Day2.getMatchSet()){
            System.out.println(t);
        }
        Day2.Play them = Day2.Play.ROCK;
        Day2.Outcome outcome = Day2.Outcome.WIN;
        System.out.printf("Them %s, outcome %s, myPlay: %s\n", them, outcome, Day2.determinePlay(them, outcome));
         */

        //      DAY1
        //***********************************************************

//        Day1.doPart1();
//        Day1.streamTest();


    }

    private static void testParallelStream() {
        List<Integer> myList = new ArrayList<>();
        myList.add(10);
        myList.add(55);
        myList.add(43);
        myList.add(23);
        myList.add(22);
        myList.add(11);
        myList.add(14);
        myList.add(43);
        myList.add(2);
        myList.add(5);
        for(int i = -1_000_000; i<1_000_000; i++){
            myList.add(i);
        }
        long startTime = System.currentTimeMillis();
//        System.out.println(myList);
        List<Integer> accum = new ArrayList<>();
        for(int i: myList){
            if(i <= 10) accum.add(i);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("For loop duration (ms): " + duration);

        startTime = System.currentTimeMillis();
        List<Integer> accum2 = myList.stream()
                .filter(listItem -> listItem <= 10)
                .toList();
        duration = System.currentTimeMillis() - startTime;
        System.out.println("For Stream (ms): " + duration);

        startTime = System.currentTimeMillis();
        List<Integer> accum3 = myList.parallelStream()
                .filter(listItem -> listItem <= 10)
                .toList();
        duration = System.currentTimeMillis() - startTime;
        System.out.println("For Parallel Stream (ms): " + duration);
    }
}