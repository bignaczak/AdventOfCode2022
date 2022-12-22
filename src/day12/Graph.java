package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class Graph {

    public Vertex[][] graph;
    private Grid grid;

    public Graph(Grid grid){
        this.grid = grid;
        this.graph = new Vertex[grid.iRange.getMaximum()+1][grid.jRange.getMaximum()+1];
        IntStream.range(0,grid.iRange.getMaximum() + 1)
                .forEach(i -> IntStream.range(0, grid.jRange.getMaximum() + 1)
                        .forEach(j -> graph[i][j] = new Vertex(new GridLocation(i,j))));
    }

    public ArrayList<Vertex> getUnvisitedNeighborsFor(Vertex vertex) {
        // Look in each direction to identify edges and neighbors
        return new ArrayList<>(Arrays.stream(GridDirection.values())
                .filter(dir -> grid.exists(vertex.gridLocation.move(dir)))
                .map(dir -> vertex.gridLocation.move(dir))
                .filter(loc -> grid.isNavigable(vertex.gridLocation, loc))
                .map(loc -> this.getVertexAt(loc))
                .filter(v -> ! v.isVisited)
                .toList());
    }

    public Vertex getShortestUnvisitedVertex(){
        return IntStream.range(grid.iRange.getMinimum(), grid.iRange.getMaximum() + 1)
                .mapToObj(i -> Arrays.stream(graph[i], grid.jRange.getMinimum(), grid.jRange.getMaximum() + 1)
                        .toList())
                .flatMap(Collection::stream)
                .filter(v -> !v.isVisited)
                .min(Vertex::compareTo)
                .orElse(null);


    }

    public List<Vertex> getAllLowestElevationVertices(){
        return Arrays.stream(graph)
                .flatMap(Arrays::stream)
                .filter(v -> grid.getHeight(v.gridLocation) == 1)
                .toList();
    }

    public Vertex getVertexAt(GridLocation gridLocation){
        if (!grid.exists(gridLocation)) return null;

        return graph[gridLocation.i()][gridLocation.j()];
    }

}
