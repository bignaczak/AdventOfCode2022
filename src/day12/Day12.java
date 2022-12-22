package day12;

import util.ReadFile;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Day12 {

    private static Grid grid;
    private static ArrayList<GridPath> myPaths = new ArrayList<>();

    public static List<String> lines = new ArrayList<>();


    public static void doPartA_V2(){
        var fullPath = "/Users/brianignaczak/code/advent2022/src/static/input-day12.txt";
        lines = ReadFile.streamToArray(fullPath);

        Grid grid = new Grid(lines);
        Graph graph = new Graph(grid);

        Vertex startVertex = graph.getVertexAt(grid.start);
        startVertex.minDistance = 0.0;

        int i = 0;
        Vertex curVertex;
        while ((curVertex = graph.getShortestUnvisitedVertex()) != null){
//            Vertex curVertex = graph.getShortestUnvisitedVertex();
//            System.out.println("Visiting: " + curVertex);
            for (Vertex neighbor : graph.getUnvisitedNeighborsFor(curVertex)) {
                Double distToNeighbor = curVertex.minDistance + 1;
                if(distToNeighbor < neighbor.minDistance){
                    neighbor.minDistance = distToNeighbor;
                    neighbor.previousLocation = curVertex.gridLocation;
                }
            }
            curVertex.isVisited = true;
            i++;
            if (i % 1000 == 0) System.out.println("Looked at " + i + " nodes...");
        }

        System.out.println("Completed all nodes, inspected : " + i);
        System.out.println("End Vertex: " + graph.getVertexAt(grid.end));

        // visit each of t

    }

    public static void doPartB() {
        grid = new Grid(lines);
        Graph graph = new Graph(grid);

        HashMap<Vertex, Double> distFromLowPoints = new HashMap<>();
        long startTime = System.currentTimeMillis();
        List<Vertex> lowVertices = graph.getAllLowestElevationVertices();
        System.out.println(lowVertices);

//        calculateShortestPathFrom(new GridLocation(21,0), new Graph(grid));

//        int i = 0;
//        for(Vertex v: lowVertices){
//            Double dist = calculateShortestPathFrom(v.gridLocation, new Graph(grid));
//            distFromLowPoints.put(v,dist);
//            i++;
//            System.out.println("Completed " + i + " of " + lowVertices.size());
//            if(i > 50) break;
//        }

        AtomicInteger atomicInteger = new AtomicInteger();
        lowVertices.stream()
                .parallel()
                .forEach(v -> {
                    Double dist = calculateShortestPathFrom(v.gridLocation, new Graph(grid));
                    distFromLowPoints.put(v,dist);
                    System.out.println("Completed " + atomicInteger.incrementAndGet() + " of " + lowVertices.size());
                });
//
        Vertex v = distFromLowPoints.keySet().stream()
                .min(Comparator.comparingDouble(distFromLowPoints::get))
                .orElse(null);

        System.out.println("Shortest start point " + v + " at dist: " + distFromLowPoints.get(v));
//                .forEach(k -> System.out.println("Dist From " + k + " is: " + distFromLowPoints.get(k)));
//
//        long duration = System.currentTimeMillis() - startTime;
//        System.out.println("Calculation took " + duration + " ms");
    }

    public static Double calculateShortestPathFrom(GridLocation loc, Graph graph){
        Vertex startVertex = graph.getVertexAt(loc);
        startVertex.minDistance = 0.0;

        int i = 0;
        Vertex curVertex;
        while ((curVertex = graph.getShortestUnvisitedVertex()) != null){
//            Vertex curVertex = graph.getShortestUnvisitedVertex();
//            System.out.println("Visiting: " + curVertex);
            for (Vertex neighbor : graph.getUnvisitedNeighborsFor(curVertex)) {
                Double distToNeighbor = curVertex.minDistance + 1;
                if(distToNeighbor < neighbor.minDistance){
                    neighbor.minDistance = distToNeighbor;
                    neighbor.previousLocation = curVertex.gridLocation;
                }
            }
            curVertex.isVisited = true;
            i++;
//            if (i % 1000 == 0) System.out.println("Looked at " + i + " nodes...");
        }

//        System.out.println("Completed all nodes, inspected : " + i);
//        System.out.println("Start Vertex: " + startVertex + "|| Distance: " + graph.getVertexAt(grid.end).minDistance);

        return graph.getVertexAt(grid.end).minDistance;

    }


        public static void doPartA() {
        var fullPath = "/Users/brianignaczak/code/advent2022/src/static/input-day12.txt";
        var lines = ReadFile.streamToArray(fullPath);

//        var line = lines.get(0);
//        var row = line.split("");
//        var rowHeights = Arrays.stream(row)
//                .map(s -> Character.getNumericValue(s.charAt(0)) - 9)
//                .toArray();
//        System.out.println(Arrays.toString(rowHeights));
//        System.out.println(rowHeights[0]);

        int[][] gridArray = lines.stream()
                .map(l -> Arrays.stream(l.split(""))
                        .peek(s -> {if (s.equals("S")) System.out.println("Found Start");})
                        .map(s -> Character.getNumericValue((s.charAt(0)))-9)
                        .mapToInt(i -> i)
                        .toArray()
                )
                .toArray(int[][]::new);

        GridLocation startLoc = null;
        GridLocation endLoc = null;
        for (int i = 0; i < lines.size(); i++){
            var curRow = lines.get(i).split("");
            for(int j = 0; j < curRow.length; j++){
                if(curRow[j].equals("S")){
                    startLoc = new GridLocation(i,j);
                    System.out.println("Start Location: " + startLoc);
                    gridArray[i][j] = 1;
                } else if (curRow[j].equals("E")){
                    endLoc = new GridLocation(i,j);
                    gridArray[i][j] = 26;
                    System.out.println("End Location " + endLoc);
                }
                if (startLoc != null && endLoc != null) break;
            }
        }

//        Arrays.stream(grid)
//                .forEach(r -> System.out.println(Arrays.toString(r)));
        grid = new Grid(lines);
//        grid.setStart(startLoc);
        System.out.println("iRange: " + grid.iRange);
        System.out.println("jRange: " + grid.jRange);
        System.out.println("Start Loc: " + grid.start);
        int numThreads = 20;
        int numSolutions = 10000;

//        ArrayList<GridPath> myPaths = new ArrayList<>();
//        solveSingleThreaded(numSolutions, myPaths);
        solveMultiThreaded(numSolutions, numThreads,myPaths);
//        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
//        for (int p = 0; p<numSolutions; p++){
//            GridLocation finalStartLoc = startLoc;
//            GridLocation finalEndLoc = endLoc;
////            GridPath gridPath = new GridPath(new Grid(gridArray, finalStartLoc, finalEndLoc));
////            int numSteps = gridPath.findPath();
////            myPaths.add(gridPath);
//            executorService.execute(() -> {
//                    GridPath gridPath = new GridPath(new Grid(gridArray, finalStartLoc, finalEndLoc));
//                    int numSteps = gridPath.findPath();
//                    myPaths.add(gridPath);
//                }
//            );
//
//            new Thread(runnable).start();

//        }
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(120, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("After shutdown executorService paths.size(): " + myPaths.size());
        Collections.sort(myPaths);
        myPaths.subList(0, 15).forEach(l -> System.out.println("Steps: " + l));

        int nPaths = myPaths.size() < 15 ? myPaths.size() : 15;
        for(int i=0; i<nPaths; i++){
            System.out.println("Num Steps: " + myPaths.get(i).getSolutionSize());
            System.out.println("------------------------------------------");
            myPaths.get(i).printSolution();
        }

        // comparing the top paths
        int nTopPaths = 10;
        // create a set of the shortest path
        ArrayList<GridLocation> shortestPath = myPaths.get(0).getSolution();
        HashSet<GridLocation> allLocs = new HashSet<>(shortestPath);
        // find the last unique point among shortest paths
        ArrayList<GridLocation> sharedPoints = new ArrayList<>(shortestPath);
        ArrayList<GridLocation> curPath2 = new ArrayList<>();
        ArrayList<GridLocation> uniquePoints = new ArrayList<>();
        for(int i=1; i<nTopPaths; i++){
            // Find locs not in the shortest set
            curPath2 = myPaths.get(i).getSolution();
            uniquePoints = new ArrayList<>(myPaths.get(i).getSolution());
            uniquePoints.removeAll(shortestPath);
            allLocs.addAll(curPath2);
//            curPath2.removeAll(shortestPath);
            curPath2.stream()
                            .forEach(loc -> {if(!sharedPoints.contains(loc)) sharedPoints.remove(loc);});

            System.out.println(curPath2.size() + " Unique locations for " + i + ": " + curPath2);

            if (i==1) break;
        }
        System.out.println("Shared: " + sharedPoints.size());
        grid.printPath(sharedPoints,false);
        GridLocation lastShared = sharedPoints.get(sharedPoints.size() -1);
        System.out.println("Last Unique point: " + uniquePoints.get(uniquePoints.size() -1 ));
        System.out.println("Last shared point: " + lastShared);

        System.out.println("Distance from point to end: Shortest--> " +
                sharedPoints.subList(shortestPath.indexOf(lastShared), shortestPath.size()).size() +
                "curPath --> " + curPath2.subList(curPath2.indexOf(lastShared), curPath2.size()).size());

        System.out.println("Distance to point: Shortest--> " +
                sharedPoints.subList(0, shortestPath.indexOf(lastShared)).size() +
                "curPath --> " + curPath2.subList(0, curPath2.indexOf(lastShared)).size());
        System.out.println();

        createFrankenPath(myPaths);
        // 586 is too high  (Perhaps because it includes the starting square?!)
        // 585 too high
        // 575 too high
        // 524 not right
        // 522 not right

        //502 is wrong
        // 504 w/ 10000 solutions and randomization  wrong
        // 501 is wrong


    }

    public static void solveSingleThreaded(int numSolutions, ArrayList<GridPath> myPaths) {
        long startTime = System.currentTimeMillis();

        for (int p = 0; p < numSolutions; p++) {
            GridLocation finalStartLoc = grid.start;
            GridLocation finalEndLoc = grid.end;
            GridPath gridPath = new GridPath(new Grid(lines));
            int numSteps = gridPath.findPath();
            myPaths.add(gridPath);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Calc Time " +  duration + " ms with 1 thread and " + numSolutions + " solutions.");

    }

    public static void solveMultiThreaded(int numSolutions, int numThreads, ArrayList<GridPath> myPaths){
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int p = 0; p < numSolutions; p++) {
            GridLocation finalStartLoc = grid.start;
            GridLocation finalEndLoc = grid.end;
            executorService.execute(() -> {
                        GridPath gridPath = new GridPath(new Grid(lines));
                        int numSteps = gridPath.findPath();
//                        myPaths.add(gridPath);
                        addPath(gridPath);
                    }
            );
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Calc Time " +  duration + " ms with " + numThreads + " threads and " + numSolutions + " solutions.");

    }

    public static synchronized void addPath(GridPath path){
        myPaths.add(path);
    }

    public static <T> List<T> getLastThree(List<T> list){
        if(list.size() > 4){
            return list.subList(list.size()-3, list.size());
        }
        return list;
    }

    private static GridLocation planMove(GridLocation curLoc){
        // find preferred direction
        List<GridDirection> preferredDirections = curLoc.getPreferredDirectionList(grid.end);

        return planMove(curLoc, preferredDirections);
    }

    private static GridLocation planMove(GridLocation curLoc, List<GridDirection> preferredDirections){

        while(preferredDirections.size() > 0){
            // take from the top of the list
            GridDirection gridDirection = preferredDirections.remove(0);

            GridLocation newLoc = curLoc.move(gridDirection);

            // make sure new location exists on the grid and hasn't been visited
            if (grid.exists(newLoc) && !grid.wasVisited(newLoc) && grid.isNavigable(curLoc, newLoc)){
                return newLoc;
            }
        }
        return null;
    }

    private static boolean executeMove(GridLocation curLoc, GridLocation nextLoc, List<GridLocation> breadcrumbs) {
        if (!hasMove(nextLoc)) return false;

        breadcrumbs.add(curLoc);
        curLoc = nextLoc;
        grid.recordMove(curLoc);
        System.out.println("Moving to: " + curLoc);
        return true;
    }

    private static boolean hasMove(GridLocation loc){
        return loc != null;
    }

    private static GridLocation getShortCut(GridLocation loc, List<GridLocation> breadcrumbs){
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
            System.out.println("\nShortcut found!!!!!!!!");
            System.out.println("Connect previous spot " + shortLoc + " to current spot " + loc );
            System.out.println();
        } else {
            System.out.println("No shortcut found for " + loc);
        }


        return shortLoc;
    }

    public static ArrayList<GridLocation> trimAfter(GridLocation trimAfter, ArrayList<GridLocation> list){
        System.out.println("Before trim list size: " + list.size());
        int index = list.indexOf(trimAfter);
        grid.trimmedList.addAll(list.subList(index + 1, list.size()));
        ArrayList<GridLocation> trimmedList=  new ArrayList<>(list.subList(0, index+1));
        System.out.println("After trim list size: " + trimmedList.size());
//        System.out.println(trimmedList);

        return trimmedList;
    }

    public static void createFrankenPath(ArrayList<GridPath> myPaths){
        System.out.println("Entering frankenpath");
        // Receives list of paths sorted with shortest first

        // start at the end of shortest path, work backwards to see if any shorter paths exist to that place
        ArrayList<GridLocation> frankenPath = new ArrayList<>(myPaths.get(0).getSolution());

        // reverse the order of the path
        Collections.reverse(frankenPath);
        boolean keepLooping = true;
        int i = 0;
        while(keepLooping){
            // starting with last point, look for any shorter routes to achieve the same position
            GridLocation curLoc = frankenPath.get(i);
            int curDistance = frankenPath.size() - i;

            // loop through all paths and find shorter route
            GridPath shorterPath = myPaths.stream()
                    .filter(path -> path.getSolution().contains(curLoc))
                    .filter(path -> path.getSolution().indexOf(curLoc) + 1 < curDistance)
                    .reduce((shortest, path) -> path.getSolutionSize() < shortest.getSolutionSize() ? path : shortest)
                    .orElse(null);

            if (shorterPath != null) {
                if (shorterPath.getSolutionSize() < curDistance)
                    System.out.println("Shorter path was found to " + curLoc);
                    frankenPath = new ArrayList<>(frankenPath.subList(0, i+1));
                    ArrayList<GridLocation> newPath = new ArrayList<>(shorterPath.getSolution().subList(0, shorterPath.getSolution().indexOf(curLoc)));
                    Collections.reverse(newPath);
                    frankenPath.addAll(newPath);
            } else{
//                throw new RuntimeException("The shorter path stream failed!!!!");
            }

            keepLooping = ++i < frankenPath.size();
        }
        Collections.reverse(frankenPath);
        System.out.println("Frankenpath length: " + frankenPath.size());

    }
}
