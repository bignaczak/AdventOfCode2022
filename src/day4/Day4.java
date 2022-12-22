package day4;

import org.apache.commons.lang3.Range;
import util.ReadFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 {

    public static void doPartA(){
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day4.txt";
        List<String> input = ReadFile.streamToArray(inputPath);

        //
//        input.stream()
//                .map(line-> Arrays.asList(line.split(",")))
//                .forEach(System.out::println);
//
//        input.stream()
//                .map(line-> Arrays.asList(line.split(",")))
//                .flatMap(Collection::stream)
//                .forEach(System.out::println);

        long fullyOverlappingRangeCount = input.stream()
                // split the 2 ranges
                .map(line-> Arrays.asList(line.split(",")))
                // construct 2 ranges
                .map(list-> {
                    return list.stream()
                            // split each range to a value and convert to Integer
                            .map(r -> Arrays.stream(r.split("-"))
                                .map(Integer::parseInt)
                                // collect min/max integers to a list for each range (2 ranges per line)
                                .collect(Collectors.toList()))
                            // construct two range objects for each line
                            .map(rangeListInt -> Range.between(rangeListInt.get(0), rangeListInt.get(1)))
                            .collect(Collectors.toList());
                })
                .filter(ranges -> ranges.get(0).containsRange(ranges.get(1)) || ranges.get(1).containsRange(ranges.get(0)))
                .count();

        System.out.println("Fully Overlapping Range Count = " + fullyOverlappingRangeCount);

        Range<Integer> range = Range.between(4,10);
        System.out.println(range);
        System.out.println("Includes 10? " + range.contains(10));

    }

    public static void doPartB() {
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day4.txt";
        List<String> input = ReadFile.streamToArray(inputPath);


        Range<Integer> range = Range.between(4, 10);
        Range<Integer> range2 = Range.between(14,18);
        System.out.println("Do they Overlap? " + range.isOverlappedBy(range2));


        long overlappingRangeCount = input.stream()
                // split the 2 ranges
                .map(line -> Arrays.asList(line.split(",")))
                // construct 2 ranges
                .map(list -> {
                    return list.stream()
                            // split each range to a value and convert to Integer
                            .map(r -> Arrays.stream(r.split("-"))
                                    .map(Integer::parseInt)
                                    // collect min/max integers to a list for each range (2 ranges per line)
                                    .collect(Collectors.toList()))
                            // construct two range objects for each line
                            .map(rangeListInt -> Range.between(rangeListInt.get(0), rangeListInt.get(1)))
                            .collect(Collectors.toList());
                })
                .filter(ranges -> ranges.get(0).isOverlappedBy(ranges.get(1)))
                .count();
        System.out.println("Overlapping Range Count = " + overlappingRangeCount);

    }


    public static void doPartA2(){
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day4.txt";
        List<String> input = ReadFile.streamToArray(inputPath);

        System.out.println("Fully Overlaping Range Count: " +
                input.stream()
                        // split the 2 ranges
                        .map(line-> Arrays.stream(line.split(","))
                                // split each range to a value and convert to Integer
                                .map(r -> Arrays.stream(r.split("-"))
                                        .map(Integer::parseInt)
                                        // collect min/max integers to a list for each range (2 ranges per line)
                                        .collect(Collectors.toList())
                                )
                                // construct two range objects for each line
                                .map(rangeListInt -> Range.between(rangeListInt.get(0), rangeListInt.get(1)))
                                .collect(Collectors.toList())
                        )
                        .filter(ranges -> ranges.get(0).containsRange(ranges.get(1)) || ranges.get(1).containsRange(ranges.get(0)))
                        .count());


    }
    public static void doPartA3(){
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day4.txt";
        List<String> input = ReadFile.streamToArray(inputPath);

        System.out.println("Fully Overlaping Range Count: " +
                input.stream()
                        // split the 2 ranges
                        .map(line-> Arrays.asList(line.split(",|-")).stream()
                                .map(Integer::parseInt)
                                .collect(Collectors.toList())
                        )
                        .map(rangeListInt -> Arrays.asList(
                                Range.between(rangeListInt.get(0), rangeListInt.get(1))
                                , Range.between(rangeListInt.get(2), rangeListInt.get(3))))
                        .filter(ranges -> ranges.get(0).containsRange(ranges.get(1)) || ranges.get(1).containsRange(ranges.get(0)))
                        .count());


    }

}
