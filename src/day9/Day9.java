package day9;

import util.ReadFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9 {

    public enum MoveDirection{
        LEFT(-1,0), UP(0,1), DOWN(0,-1), RIGHT(1,0);

        private GridPosition move;
        MoveDirection(int x, int y){
            this.move = new GridPosition(x, y);
        }

        public GridPosition getMove() {
            return this.move;
        }
    }

    public static void doPartA(){
        var inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day9.txt";
        List<String> lines = ReadFile.streamToArray(inputPath);

        List<GridPosition> moves = lines.stream()
                .flatMap(line -> parseMoves(line).stream().map(m -> m))
                .toList();
//        System.out.println(moves);
        int numSegments = 10;
        List<GridPosition> body = new ArrayList<>(IntStream.range(0, numSegments)
                .mapToObj(i -> new GridPosition(0, 0))
                .toList());
//        GridPosition gp1 = new GridPosition(0,0);
//        GridPosition tailPosition = gp1;
//        GridPosition headPosition = gp1;
        int i = 0;
        Set<GridPosition> mySet = new HashSet<>();
        for(GridPosition move: moves){
            body.set(0, body.get(0).moveBy(move));
            i++;
//            GridPosition moveTail = tailPosition.getMove(headPosition);
//            System.out.println("head: " + headPosition + "  ||  tail: " + tailPosition);
//            System.out.println("After move " + i + " tail should move " + moveTail);
//            tailPosition = tailPosition.moveBy(moveTail);
            IntStream.range(1, body.size())
                            .forEach(idx -> body.set(idx, moveSegment(body.get(idx), body.get(idx-1))));
            ;
            mySet.add(body.get(body.size()-1));
//            if(i>100) break;
        }

        System.out.println("Num Tail Positions: " + mySet.size());  //5710 right answer

//        testPartA(lines.get(0));

    }

    public static GridPosition moveSegment(GridPosition currentSegment, GridPosition precedingSegment){
        GridPosition moveSegment = currentSegment.getMove(precedingSegment);
//        System.out.println("preceding: " + precedingSegment + "  ||  current: " + currentSegment);
//        System.out.println("After move " + i + " tail should move " + moveSegment);
        return currentSegment.moveBy(moveSegment);

    }

    private static void testPartA(String line) {
//        GridPosition gp1 = new GridPosition(3,2);
//        GridPosition gp2 = new GridPosition(4,2);
//        GridPosition gp3 = new GridPosition(4,2);
//        System.out.println(gp1.equals(gp2));
//
//        Set<GridPosition> mySet = new HashSet<>();
//        mySet.add(gp1);
//        mySet.add(gp2);
//        mySet.add(gp3);
//        System.out.println(mySet);
//
//        GridPosition gp4 = new GridPosition(1,2);
//        GridPosition gp5 = new GridPosition(1,1);
//        System.out.println(gp1.getMove(gp4));
//        System.out.println(gp1.getMove(gp5));
        GridPosition gp1 = new GridPosition(0,0);
        List<GridPosition> moves = parseMoves(line);
        GridPosition tailPosition = gp1;
        GridPosition headPosition = gp1;
        int i = 0;
        for(GridPosition move: moves){
            headPosition = headPosition.moveBy(move);
            i++;
            GridPosition moveTail = tailPosition.getMove(headPosition);
            System.out.println("After move " + i + " tail should move " + moveTail);
            tailPosition = gp1.moveBy(moveTail);
        }
        System.out.println("head: " + headPosition);
        System.out.println("tail: " + tailPosition);

    }

    public static List<GridPosition> parseMoves (String line){
        String[] lineParts = line.split(" ");
        GridPosition move = switch (lineParts[0]) {
            case "R" -> MoveDirection.RIGHT.getMove();
            case "L" -> MoveDirection.LEFT.getMove();
            case "U" -> MoveDirection.UP.getMove();
            case "D" -> MoveDirection.DOWN.getMove();
            default -> throw new RuntimeException("Not able to parse move");
        };
//        System.out.println("The move --> " + move);

        List<GridPosition> moves = new ArrayList<>();

        int numTimes = 0;
        try{
            numTimes = Integer.parseInt(lineParts[1]);
        } catch (NumberFormatException e){
            System.out.println("Not able to parse numTimes in parseMoves from " + lineParts[1]);
        }
//        System.out.println("Num Times: " + numTimes);

        for(int i=0; i<numTimes; i++){
            moves.add(move);
        }
//        System.out.println("The parsed moves: " + moves);
        return moves;
    }
}
