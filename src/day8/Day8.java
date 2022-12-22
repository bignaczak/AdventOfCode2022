package day8;

import util.ReadFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 {

    public enum ViewFrom{
        LEFT, RIGHT, TOP, BOTTOM
    }

    public static void doPartA(){
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day8.txt";
        List<String> lines = ReadFile.streamToArray(inputPath);
        int colIndex = 28;
        int rowIndex = 5;

//        List<Integer> row = Stream.of(lines.get(0).split(""))
//                .map(Integer::parseInt)
//                        .toList();
//        Integer[] firstRow = row.toArray(new Integer[0]);
//        System.out.println(Arrays.toString(firstRow));

        List<List<Integer>> forest = lines.stream()
                .map(line -> line.split(""))
                .map(stringArray-> {
                    return Arrays.stream(stringArray)
                            .map(Integer::parseInt)
                            .toList();
                })
                .collect(Collectors.toList());


//        System.out.println("\nForm streamed list...");
//        System.out.println(forest.get(forest.size()-1));

        int visibleTrees = gerPerimeter(forest);

        // From these methods, getNumVisibleForRowIntStream was most consistent
        int numVisibleForRow = getNumVisibleForRow(rowIndex, forest);
        int numVisibleIntStream = getNumVisibleForRowIntStream(rowIndex, forest);
        int numVisibleForRowParallel = getNumVisibleForRowParallel(rowIndex, forest);

        long startTime = System.currentTimeMillis();
        int numVisibleInterior = IntStream.range(1, forest.size()-1)
//                .parallel()
                .map(rowNum -> getNumVisibleForRowIntStream(rowNum, forest))
                .reduce(Integer::sum)
                .orElse(0);
        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("\n\nNum Visible Interior: %d IntStream calc took %d ms\n", numVisibleInterior, duration);
        System.out.printf("num Visible Perimeter: %d\n\n", visibleTrees);


        System.out.println("\n****************\nNum Visible for Row " + rowIndex
                + ": " + numVisibleForRow + "\n-----------\n");
        visibleTrees += numVisibleInterior;
        System.out.println("\n****************\nTotal Visible: " + visibleTrees + "\n****************\n");

        isIndexVisibleStream(rowIndex, colIndex, forest);
//
//        List<Boolean> bools = new ArrayList<>();
//        bools.add(false);
//        bools.add(false);
//        bools.add(false);
//
//        System.out.println("Result of boolean stream operation: " + bools.parallelStream().anyMatch(e -> e));
    }

    public static void doPartB(){
        String inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day8.txt";
        List<String> lines = ReadFile.streamToArray(inputPath);


        List<List<Integer>> forest = lines.stream()
                .map(line -> line.split(""))
                .map(stringArray -> {
                    return Arrays.stream(stringArray)
                            .map(Integer::parseInt)
                            .toList();
                }).toList();

        testPartB(forest);

        long startTime = System.currentTimeMillis();
        Integer maxViewIndex =IntStream.range(1, forest.size()-1)
                .mapToObj(rowIndex -> getViewIndexForRow(rowIndex, forest))
                .map(list -> list.stream().reduce(Math::max).orElse(0))
                .reduce(Math::max)
                .orElse(0);

//                .reduce(Math::max)
//                .max(Math::max)
//                .orElse(0);
        long duration = System.currentTimeMillis() - startTime;

        System.out.println();
        System.out.println("Max View index is: " + maxViewIndex);
        System.out.println("Duration (ms) = " + duration);

    }

    public static void testPartB(List<List<Integer>> forest){
        int rowIndex = 55;
        int colIndex = 80;
        List<Integer> row = forest.get(rowIndex);
        List<Integer> col = getColumn(colIndex, forest);
        int baselineHeight = row.get(colIndex);
        System.out.println("row: " + row);
        System.out.println("col: " + col);
        System.out.println("Baseline H: " + baselineHeight);

//        List<Integer> leftView = getDirectionTreeLine(ViewFrom.LEFT, rowIndex, colIndex, forest);
//        int leftTrees = calculateNumTreesVisible(baselineHeight, leftView);
//
//
//        Map<ViewFrom, List<Integer>> dirMap = new HashMap<>();
//        for(ViewFrom dir: ViewFrom.values()){
//            if(dir == ViewFrom.LEFT) dirMap.put(dir, row.subList(0, colIndex));
//            if(dir == ViewFrom.RIGHT) dirMap.put(dir, row.subList(colIndex+1, row.size()));
//            if(dir == ViewFrom.TOP) dirMap.put(dir, col.subList(0, rowIndex));
//            else dirMap.put(dir, col.subList(rowIndex+1, col.size()));
//        }
//        System.out.println("Left View: " + leftView);
//        System.out.println("Left visible trees: " + leftTrees);
        int viewIndex = 1;
        for(ViewFrom dir: ViewFrom.values()){
            List<Integer> treeLine = getDirectionTreeLine(dir, rowIndex, colIndex, forest);
            int numTrees = calculateNumTreesVisible(baselineHeight, treeLine);
            System.out.println("For dir: " + dir + "\nTreeLine: " + treeLine + "\nNumTrees: " + numTrees);
            viewIndex *= numTrees;
        }
        System.out.printf("*****  View Index:  %d *********\n", viewIndex);

        System.out.println("\n");
        System.out.println("Using Stream method: " + calculateViewIndex(rowIndex, colIndex, forest));

        System.out.println("\n");

        List<Integer> viRow = getViewIndexForRow(rowIndex, forest);
        System.out.println("View index for row " + rowIndex + ": " + viRow);

        System.out.println("Row Max: " + viRow.stream().reduce(Math::max).orElse(0));

    }
    public static List<Integer> getViewIndexForRow(int rowNum, List<List<Integer>> forest){
        List<Integer> currentRow = forest.get(rowNum);
        return IntStream.range(1, currentRow.size()-1)
                .mapToObj(colNum -> calculateViewIndex(rowNum, colNum, forest))
                .toList();
    }

    private static Integer calculateViewIndex(int rowNum, int colNum, List<List<Integer>> forest){
        List<Integer> row = forest.get(rowNum);
        List<Integer> column = getColumn(colNum, forest);
        Integer baselineHeight = row.get(colNum);
        int rowEnd = row.size();
        int colEnd = column.size();
        Integer viewIndex =  Stream.of(ViewFrom.values())
                .map(dir -> getDirectionTreeLine(dir, rowNum, colNum, forest))
                .map(list -> calculateNumTreesVisible(baselineHeight, list))
                .reduce(1, Math::multiplyExact);

        return viewIndex;
//                .map(list -> calculateNumTreesVisible(baselineHeight, list));
    }

    private static List<Integer> getDirectionTreeLine(ViewFrom dir, int rowNum, int colNum, List<List<Integer>> forest){
        List<Integer> row = forest.get(rowNum);
        List<Integer> column = getColumn(colNum, forest);
        int rowEnd = row.size();
        int colEnd = column.size();
        List<Integer> myList;
        if(dir == ViewFrom.LEFT) {
            myList = row.subList(0, colNum);
        } else if(dir == ViewFrom.RIGHT) {
            myList = row.subList(colNum+1, rowEnd);
        } else if(dir == ViewFrom.TOP) {
            myList = column.subList(0, rowNum);
        } else {
            myList = column.subList(rowNum+1, colEnd);
        }

        if(dir == ViewFrom.LEFT || dir == ViewFrom.TOP) {
            List<Integer> reversedList = new ArrayList<>();
            myList.stream().forEach(item -> reversedList.add(item));
//            Collections.copy(reversedList, myList);
            Collections.reverse(reversedList);
            return reversedList;
        }

        return myList;
    }

    private static Integer calculateNumTreesVisible(int baselineHeight, List<Integer> treeLine){
        return IntStream.range(0, treeLine.size())
                .filter(i -> treeLine.get(i) >= baselineHeight)
                .map(i -> i+1)      // offset for count of trees
                .findFirst()
                .orElse(treeLine.size());
    }

    private static int gerPerimeter(List<List<Integer>> forest){
        int height = forest.size();
        int width = forest.get(0).size();
        return 2 * (height + width - 2);
    }

    private static List<Integer> getColumn(int colIndex, List<List<Integer>> forest){
        return forest.stream()
                .map(row -> row.get(colIndex))
                .toList();
    }

    private static int getNumVisibleForRow(int rowIndex, List<List<Integer>> forest){
        long startTime = System.currentTimeMillis();
        int numVisible = 0;
        int numCalculations = 0;
        List<Integer> row = forest.get(rowIndex);
        // Sequential loop through each column index
        for(int colIndex = 1; colIndex < (row.size()-1); colIndex++){
            if(isIndexVisible(rowIndex, colIndex, forest)) numVisible++;
            numCalculations++;
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Sequential Row Calculation Time (ms): " + duration + " visible: " + numVisible +
                " for " + numCalculations + " calculations");

        return numVisible;
    }

    private static int getNumVisibleForRowIntStream(int rowIndex, List<List<Integer>> forest){
        long startTime = System.currentTimeMillis();
        int numCalculations = 0;
        AtomicInteger numCalculationsAtomic = new AtomicInteger();
        List<Integer> row = forest.get(rowIndex);

        long numVisibleIndexedStream = IntStream.range(1, row.size()-1)
                .peek(i -> numCalculationsAtomic.incrementAndGet())
                .filter(i -> isIndexVisibleStream(rowIndex, i, forest))
                .count();
        long duration = System.currentTimeMillis() - startTime;

        System.out.println("IntStream Row Calculation Time (ms): " + duration + " visible: " + numVisibleIndexedStream +
                " for " + numCalculationsAtomic + " calculations");

        return (int) numVisibleIndexedStream;
    }

    private static int getNumVisibleForRowParallel(int rowIndex, List<List<Integer>> forest){
        long startTime = System.currentTimeMillis();
        AtomicInteger numVisibleAtomic = new AtomicInteger();
        AtomicInteger numCalculationsAtomic = new AtomicInteger();
        AtomicInteger indexAtomic = new AtomicInteger();
        List<Integer> row = forest.get(rowIndex);
//        int colIndex = 11;
//        System.out.println("Comparing against h=" + row.get(colIndex));

//        boolean isVisible =
        row.subList(1, row.size() - 1).parallelStream()
                .map(h -> indexAtomic.getAndIncrement())
                .peek(colIndex -> numCalculationsAtomic.incrementAndGet())
                .filter(colIndex -> isIndexVisibleStream(rowIndex, colIndex, forest))
                .forEach(v -> numVisibleAtomic.getAndIncrement());
//                .map(colIndex -> isIndexVisible(rowIndex, colIndex, forest))
//                .filter(h -> h < row.get(colIndex))
//                .forEach(h -> numVisibleAtomic.incrementAndGet());
//                .allMatch(h -> h < row.get(colIndex));

//        IntStream.range(1, row.size()-1)
//                .parallel()
//                .filter(i ->  {
//                    numCalculationsAtomic.incrementAndGet();
//                    return isIndexVisible(rowIndex, i, forest);
//                })
//                .forEach(visibleItem -> numVisibleAtomic.incrementAndGet());

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Parallel Row Calculation Time (ms): " + duration +  " visible: " + numVisibleAtomic +
                " for " + numCalculationsAtomic + " calculations");

        return numVisibleAtomic.get();

    }

    private static boolean isIndexVisible(int rowIndex, int colIndex, List<List<Integer>> forest){
        boolean debug = false;
        List<Integer> currentRow = forest.get(rowIndex);
        List<Integer> currentCol = getColumn(colIndex, forest);
        Integer baselineHeight = forest.get(rowIndex).get(colIndex);
        if (debug) System.out.println("Current Row: " + currentRow);
        if (debug) System.out.println("Baseline height: " + baselineHeight);

        // look left
        if (debug) System.out.println("Left Looking: " + currentRow.subList(0,colIndex));
        List<Boolean> leftBools = currentRow.subList(0, colIndex).stream()
                .map(h -> h < baselineHeight).toList();
        if (debug) System.out.println("Left Bools: " + leftBools);

        if (debug) System.out.println("\n\nPerform allMatch... \n-------------------\n");
        Boolean visibleLeft = currentRow.subList(0, colIndex).stream()
                .allMatch(h -> h < baselineHeight);
        if (debug) System.out.println("Visible Left: " + visibleLeft);
        if (debug) System.out.println();


        // look right
        if (debug) System.out.println("Right Looking: " + currentRow.subList(colIndex+1, currentRow.size()));
        List<Boolean> rightBools = currentRow.subList(colIndex+1, currentRow.size()).stream()
                .map(h -> h < baselineHeight).toList();
        if (debug) System.out.println("Right Bools: " + rightBools);

        if (debug) System.out.println("\n\nPerform allMatch... \n-------------------\n");
        Boolean visibleRight = currentRow.subList(colIndex+1, currentRow.size()).stream()
                .allMatch(h -> h < baselineHeight);

        if (debug) System.out.println("Visible Right: " + visibleRight);
        if (debug) System.out.println();

        return isIndexVisibleStream(rowIndex, colIndex, forest);

        // look up
//        Boolean visibleUp =


        // look down

//        return visibleLeft || visibleRight;

    }

    private static boolean isIndexVisibleStream(int rowIndex, int colIndex, List<List<Integer>> forest){
        List<Integer> row = forest.get(rowIndex);
        List<Integer> column = getColumn(colIndex, forest);
        Integer baselineHeight = row.get(colIndex);
        Integer rowEnd = row.size();
        Integer colEnd = column.size();
        return Stream.of(ViewFrom.values())
//                .parallel()
                .map(dir -> {
                    if(dir == ViewFrom.LEFT) return row.subList(0, colIndex);
                    if(dir == ViewFrom.RIGHT) return row.subList(colIndex+1, rowEnd);
                    if(dir == ViewFrom.TOP) return column.subList(0, rowIndex);
                    else return column.subList(rowIndex+1, colEnd);
                })
                .anyMatch(list -> isVisible(baselineHeight,list));
//        return false;

    }

    private static boolean isVisibleParallelA(){
        return false;
    }

    private static boolean isVisible(Integer height, List<Integer> list){
        return list.stream().allMatch(tree -> tree < height);
    }
}
