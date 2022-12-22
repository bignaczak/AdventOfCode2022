package day12;

import org.apache.commons.lang3.Range;

import java.util.*;

public class Grid {
    public int[][] grid;
    public String [][] visited;
    public String [][] shortest;

    public Set<GridLocation> visitedList = new HashSet<>();

    public Set<GridLocation> trimmedList = new HashSet<>();

    public GridLocation start = null;
    public GridLocation end = null;

    public Range<Integer> iRange;
    public Range<Integer> jRange;

    public Grid(List<String> lines){
        this.grid = createGrid(lines);
        this.iRange = Range.between(0, this.grid.length -1);
        this.jRange = Range.between(0, this.grid[0].length -1);

        visited = new String[grid.length][grid[0].length];
        Arrays.stream(visited)
                .forEach(row -> Arrays.fill(row, "."));
        shortest = new String[grid.length][grid[0].length];

        System.out.println("iRange: " + this.iRange);
        System.out.println("jRange: " + this.jRange);
        System.out.println("Start Loc: " + this.start);

    }

    public int[][] createGrid(List<String> lines){
        int[][] gridArray = lines.stream()
                .map(l -> Arrays.stream(l.split(""))
                        .peek(s -> {if (s.equals("S")) System.out.println("Found Start");})
                        .map(s -> Character.getNumericValue((s.charAt(0)))-9)
                        .mapToInt(i -> i)
                        .toArray()
                )
                .toArray(int[][]::new);

        for (int i = 0; i < lines.size(); i++){
            var curRow = lines.get(i).split("");
            for(int j = 0; j < curRow.length; j++){
                if(curRow[j].equals("S")){
                    this.start = new GridLocation(i,j);
                    System.out.println("Start Location: " + start);
                    gridArray[i][j] = 1;
                } else if (curRow[j].equals("E")){
                    this.end = new GridLocation(i,j);
                    gridArray[i][j] = 26;
                    System.out.println("End Location " + end);
                }
                if (start != null && end != null) break;
            }
        }

//        Grid grid = new Grid(gridArray, startLoc, endLoc);
////        grid.setStart(startLoc);
//        System.out.println("iRange: " + grid.iRange);
//        System.out.println("jRange: " + grid.jRange);
//        System.out.println("Start Loc: " + grid.start);
        return gridArray;
    }




    public void setStart(GridLocation start){
        if(this.start != null) throw new RuntimeException("Tried to set start point more than once!!");

        this.start = start;
        this.markVisited(start);
    }

    public void setEnd(GridLocation end){
        if(this.end != null) throw new RuntimeException("Tried to set end point more than once!!");

        this.end = end;
    }

    public boolean exists(GridLocation loc){
        return iRange.contains(loc.i()) && jRange.contains(loc.j());
    }

    public boolean wasVisited(GridLocation loc){
        return visitedList.contains(loc);
    }

    public boolean isNavigable(GridLocation curLoc, GridLocation newLoc){
//        if (getHeight(newLoc) - getHeight(curLoc) >= 2){
//            System.out.println("Unable to navigate from " + curLoc + " to " + newLoc);
//        }
        // make sure the height is not more than 1 higher
        return getHeight(newLoc) - getHeight(curLoc) < 2;
    }

    public boolean isNotLower(GridLocation curLoc, GridLocation newLoc){
        return getHeight(newLoc) >= getHeight(curLoc);
    }

    public boolean isHigher(GridLocation curLoc, GridLocation newLoc){
        return getHeight(newLoc) > getHeight(curLoc);
    }

    public int getHeight(GridLocation loc){
        return grid[loc.i()][loc.j()];
    }


    private void markVisited(GridLocation gridLocation){
        visited[gridLocation.i()][gridLocation.j()] = "*";
        visitedList.add(gridLocation);

    }

    public void printVisited(){
        Arrays.stream(visited)
                .forEach(row -> System.out.println(String.join("", row)));
//                .forEach(row -> System.out.println(Arrays.toString(row)));
    }

    public void printPath(List<GridLocation> path, boolean includeTrimmed){
        // create blank grid
        String[][] myMap = new String[grid.length][grid[0].length];
        Arrays.stream(myMap).forEach(row -> Arrays.fill(row, "."));

        // mark the path
        path.forEach(loc -> myMap[loc.i()][loc.j()] = "*");

        // mark the locs removed by shortcuts
        if(includeTrimmed) this.trimmedList.forEach(loc -> myMap[loc.i()][loc.j()] = "x");

        // print the map
        Arrays.stream(myMap).forEach(row -> System.out.println(String.join("", row)));
    }

    public void printGrid(){
        var newList = Arrays.stream(grid)
                .map(row -> Arrays.stream(row)
                        .mapToObj(i -> String.format("%02d", i))
//                        .map(str -> String.format("%02s", str))
                        .toList()
                )
                .map(row -> String.join(" ", row))
                .toList();

        newList.forEach(System.out::println);
//        System.out.println(newList);
    }

    public void recordMove(GridLocation loc){
        visitedList.add(loc);
        markVisited(loc);

    }




}
