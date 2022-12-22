package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridPath implements Comparable<GridPath>{

    private Grid grid;
    private GridLocation startLoc;
    private GridLocation endLoc;
    private ArrayList<GridLocation> breadcrumbs = new ArrayList<>();
    private ArrayList<GridLocation> backTrackPath = new ArrayList<>();
    private ArrayList<GridLocation> clipped = new ArrayList<>();

    private boolean debug = false;

    public GridPath(Grid grid){
        this.grid = grid;
        this.startLoc = grid.start;
        this.endLoc = grid.end;
    }

    public int getSolutionSize(){
        return breadcrumbs.size();
    }

    public ArrayList<GridLocation> getSolution(){
        return breadcrumbs;
    }

    public int findPath(){
        boolean debug = false;
        GridLocation curLoc = startLoc;
        GridLocation nextLoc = null;
        boolean moveExecuted;

        for(int m=0; m<5; m++){
            // Force First move up
//            nextLoc = planMove(curLoc, new ArrayList<>(Arrays.asList(GridDirection.UP)));
            nextLoc = planMove(curLoc, new ArrayList<>(GridLocation.getRandomDirectionList()));
            if (debug) System.out.println("Next Loc: " + nextLoc);
            moveExecuted = executeMove(curLoc, nextLoc);
            if (moveExecuted){
                curLoc = nextLoc;
            } else{
                throw new RuntimeException("Unable to force first move!!!!");
            }

        }

        while(!curLoc.equals(grid.end)){
            if (debug) System.out.println("Before move from " + curLoc + " breadcrumb size: " + breadcrumbs.size() + " last 3 entries: " +
                    getLastThree(breadcrumbs));

            // try moving
//            nextLoc = planMove(curLoc);
//            nextLoc = planMove(curLoc, GridLocation.getRandomDirectionList());
            // get direction preferences

            // Add some randomness to path
            ArrayList<GridDirection> preferredDirections;
            if ((int) (Math.random() * 12) == 3){
                preferredDirections = new ArrayList<>(GridLocation.getRandomDirectionList());
            } else {
                preferredDirections = new ArrayList<>(curLoc.getHybridDirectionList(endLoc, 5));
            }

//            ArrayList<GridDirection> preferredDirections = new ArrayList<>(curLoc.getHybridDirectionList(endLoc, 5));
//            ArrayList<GridDirection> preferredDirections = new ArrayList<>(curLoc.getRandomDirectionList());
            preferredDirections = sortDirsByHeight(curLoc, preferredDirections);
            nextLoc = planMove(curLoc, preferredDirections);
            moveExecuted = executeMove(curLoc, nextLoc);
            if (moveExecuted){
                curLoc = nextLoc;
            }else{
                // no move available, so move backwards
                if (debug) System.out.println("******  NO MOVE AVAILABLE FROM " + curLoc + "   *********");


                if(breadcrumbs.size() > 0) {
                    curLoc = breadcrumbs.remove(breadcrumbs.size() - 1);
                    clipped.add(curLoc);
                    if (debug) System.out.println("Moving Backwards!!  to " + curLoc);
                } else{
                    if (debug) System.out.println("\n\n!!!No Way out from " + curLoc + " !!!!");
                    grid.printVisited();
                    throw new RuntimeException("Backtracked too far" + breadcrumbs);
                }
            }

            if (debug) System.out.println("After move BreadCrumbs Size: " + breadcrumbs.size() + " last entry: " +
                    getLastThree(breadcrumbs));

        }
        // Record the final move
        grid.recordMove(curLoc);

        // Now look for shortcuts by walking the path backward
        GridLocation shortCutLoc;
        // split the breadcrumb trail and look for shortcuts
        int i = 1;
        int shortcutsFound = 0;
        ArrayList<GridLocation> shortestPath = new ArrayList<>();
        int len;
        if (debug) System.out.println("Looking for shortcuts");
        int splitLocation;
        while((splitLocation = breadcrumbs.size() - i) > 2){
//            int splitLocation = breadcrumbs.size() - i;
            ArrayList<GridLocation> frontPath = new ArrayList<>(breadcrumbs.subList(0, splitLocation));
            ArrayList<GridLocation> endPath = new ArrayList<>(breadcrumbs.subList(splitLocation, breadcrumbs.size()));
            curLoc = endPath.get(0);
            if (debug) System.out.println("starting at " + curLoc);

            shortCutLoc = getShortCut(curLoc);
            if (shortCutLoc != null) {
                frontPath = trimAfter(shortCutLoc, frontPath);
                shortcutsFound++;
                shortestPath.clear();
                shortestPath.addAll(frontPath);
                shortestPath.addAll(endPath);
                breadcrumbs = shortestPath;
                if (debug) System.out.println("Shortest path size: " + shortestPath.size() + "  --  " + shortestPath);
            }
            i++;

        }

        if (debug) grid.printVisited();
        if (debug) grid.printGrid();
        if (debug) System.out.println("\n\n");
        if (debug) System.out.println("\n\n");
        if (debug) grid.printPath(breadcrumbs, false);
        // subtract 1 because 1st step doesn't count
        if (debug) System.out.println("\n\nLength of shortestPath: " + (shortestPath.size()-1));

//        System.out.println("\n\nLength of shortestPath Set: " + (new HashSet<>(shortestPath).size()-1));

        // 586 is too high  (Perhaps because it includes the starting square?!)
        return (shortestPath.size() -1);
    }

    public ArrayList<GridDirection> sortDirsByHeight(GridLocation curLoc, ArrayList<GridDirection> preferredDirections){
        ArrayList<GridDirection> sorted = new ArrayList<>();
        // filter the lists for directions where the resulting spot is not lower
        preferredDirections.stream()
                .filter(dir -> grid.exists(curLoc.move(dir)))
                .filter(dir -> grid.isHigher(curLoc, curLoc.move(dir)))
                .forEach(sorted::add);

        preferredDirections.stream()
                .filter(dir -> !sorted.contains(dir))
                .filter(dir -> grid.exists(curLoc.move(dir)))
                .filter(dir -> grid.isNotLower(curLoc, curLoc.move(dir)))
                .forEach(sorted::add);

        //add back the spots where height is lost
        preferredDirections.forEach(dir -> {if (!sorted.contains(dir)) sorted.add(dir);});
        return sorted;

    }

    public <T> List<T> getLastThree(List<T> list){
        if(list.size() > 4){
            return list.subList(list.size()-3, list.size());
        }
        return list;
    }

    private GridLocation planMove(GridLocation curLoc){
        // find preferred direction
        List<GridDirection> preferredDirections = curLoc.getPreferredDirectionList(grid.end);

        return planMove(curLoc, preferredDirections);
    }

    private GridLocation planMove(GridLocation curLoc, List<GridDirection> preferredDirections){
//        System.out.println("Inside plan move");
        while(preferredDirections.size() > 0){
            // take from the top of the list
            GridDirection gridDirection = preferredDirections.remove(0);

            GridLocation newLoc = curLoc.move(gridDirection);
//            System.out.println("Inside while loop");
            // make sure new location exists on the grid and hasn't been visited
            if (grid.exists(newLoc) && !grid.wasVisited(newLoc) && grid.isNavigable(curLoc, newLoc)){
                return newLoc;
            }
        }
        return null;
    }

    private boolean executeMove(GridLocation curLoc, GridLocation nextLoc) {
        if (!hasMove(nextLoc)) return false;

        breadcrumbs.add(curLoc);
        curLoc = nextLoc;
        grid.recordMove(curLoc);
        if (debug) System.out.println("Moving to: " + curLoc);
        return true;
    }

    private boolean hasMove(GridLocation loc){
        return loc != null;
    }

    private GridLocation getShortCut(GridLocation loc){
        if(breadcrumbs.size() == 0) return null;
        // see if current cell is adjacent to a previously visited spot
        // but not the last spot
        List<GridLocation> previousSpots = breadcrumbs.subList(0, breadcrumbs.size()-1);

//        boolean isAdjacent = previousSpots.stream()
//                .anyMatch(spot -> loc.calcDistanceFrom(spot) < 2);


        GridLocation shortLoc = previousSpots.stream()
                .filter(spot -> loc.calcDistanceFrom(spot) < 2)
                .filter(spot -> grid.isNavigable(spot, loc))
                .findFirst()
                .orElse(null);

        if (shortLoc != null){
            if (debug) System.out.println("\nShortcut found!!!!!!!!");
            if (debug) System.out.println("Connect previous spot " + shortLoc + " to current spot " + loc );
            if (debug) System.out.println();
        } else {
            if (debug) System.out.println("No shortcut found for " + loc);
        }


        return shortLoc;
    }

    public ArrayList<GridLocation> trimAfter(GridLocation trimAfter, ArrayList<GridLocation> list){
        if (debug) System.out.println("Before trim list size: " + list.size());
        int index = list.indexOf(trimAfter);
        grid.trimmedList.addAll(list.subList(index + 1, list.size()));
        ArrayList<GridLocation> trimmedList=  new ArrayList<>(list.subList(0, index+1));
        if (debug) System.out.println("After trim list size: " + trimmedList.size());
//        System.out.println(trimmedList);

        return trimmedList;
    }

    public void printSolution(){
        grid.printPath(breadcrumbs, false);
    }


    @Override
    public int compareTo(GridPath o) {
        return Integer.compare(breadcrumbs.size(), o.breadcrumbs.size());
    }

    @Override
    public String toString(){
        return "Solution Size: " + breadcrumbs.size();
    }
}
