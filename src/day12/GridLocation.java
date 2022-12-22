package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public record GridLocation(int i, int j) {

    public String toString() {
        return String.format("GridLocation (%d, %d)", i, j);
    }

    public GridLocation calcOffsetFrom(GridLocation other){
        int iOffset = other.i - this.i;
        int jOffset = other.j - this.j;

        return new GridLocation(iOffset, jOffset);
    }

    public int calcDistanceFrom(GridLocation other){
        GridLocation offset = calcOffsetFrom(other);
        return Math.abs(offset.i()) + Math.abs(offset.j());
    }

    public List<GridDirection> getPreferredDirectionList(GridLocation other){
        List<GridDirection> directions = new ArrayList<>();
        GridLocation offset = calcOffsetFrom(other);

        Function<Integer, GridDirection> iDirGetter = (iOffset) -> Math.signum(iOffset) > 0 ? GridDirection.DOWN : GridDirection.UP;
        Function<Integer, GridDirection> jDirGetter = (jOffset) -> Math.signum(jOffset) > 0 ? GridDirection.RIGHT : GridDirection.LEFT;


        // Add first preference to list
        if (Math.abs(offset.i) > Math.abs(offset.j)){
            // Move in the i Direction
            // last preference is negative i direction
            directions.add(iDirGetter.apply(offset.i));
            // Add Second preference ot list
            directions.add(jDirGetter.apply(offset.j));
        } else{
            // MOVE IN THE j direction is preferred
            directions.add(jDirGetter.apply(offset.j));
            // Add Second preference ot list
            directions.add(iDirGetter.apply(offset.i));
        }

        // Add Third and Fourth preference
        directions.add(directions.contains(GridDirection.UP) ? GridDirection.DOWN : GridDirection.UP);
        directions.add(directions.contains(GridDirection.RIGHT) ? GridDirection.LEFT : GridDirection.RIGHT);

        return directions;
    }

    public List<GridDirection> getHybridDirectionList(GridLocation other, int errorThreshold){
        List<GridDirection> directions = new ArrayList<>();
        GridLocation offset = calcOffsetFrom(other);

        // Only if error is greater than threshold value will the preference be set
        // Otherwise pick a random value
        final int threshold = errorThreshold;
        Function<Integer, GridDirection> iDirGetter = (iOffset) -> {
            if (Math.abs(offset.i) > threshold){
                return Math.signum(iOffset) > 0 ? GridDirection.DOWN : GridDirection.UP;
            } else {
                return Math.random() > 0.5 ? GridDirection.DOWN : GridDirection.UP;
            }
        };

        Function<Integer, GridDirection> jDirGetter = (jOffset) -> {
            if (Math.abs(offset.j) > threshold){
                return Math.signum(jOffset) > 0 ? GridDirection.RIGHT : GridDirection.LEFT;
            } else {
                return Math.random() > 0.5 ? GridDirection.RIGHT : GridDirection.LEFT;
            }
        };


        // Add first preference to list
        if (Math.abs(offset.i) > Math.abs(offset.j)){
            // Move in the i Direction
            // last preference is negative i direction
            directions.add(iDirGetter.apply(offset.i));
            // Add Second preference ot list
            directions.add(jDirGetter.apply(offset.j));
        } else{
            // MOVE IN THE j direction is preferred
            directions.add(jDirGetter.apply(offset.j));
            // Add Second preference ot list
            directions.add(iDirGetter.apply(offset.i));
        }

        // Add Third and Fourth preference
        directions.add(directions.contains(GridDirection.UP) ? GridDirection.DOWN : GridDirection.UP);
        directions.add(directions.contains(GridDirection.RIGHT) ? GridDirection.LEFT : GridDirection.RIGHT);

        return directions;
    }

    public static List<GridDirection> getRandomDirectionList(){
        List<GridDirection> randomList = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        IntStream.range(0, GridDirection.values().length)
                .forEach(i -> order.add(i));
        Collections.shuffle(order);

        order.forEach(j -> randomList.add(GridDirection.values()[order.get(j)]));
//        System.out.println(randomList);
        return randomList;
    }

    public GridLocation move(GridDirection moveDirection){
        GridLocation newLocation = null;
        switch(moveDirection){
            case UP -> newLocation = new GridLocation(i-1, j);
            case RIGHT -> newLocation = new GridLocation(i, j+1);
            case DOWN -> newLocation = new GridLocation(i+1, j);
            case LEFT -> newLocation = new GridLocation(i, j-1);
            default -> System.out.println("Direction not recognized" + moveDirection);
        }
        // verify new location is valid
        return newLocation;
    }

}
