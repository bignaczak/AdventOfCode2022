package day12;

import java.util.ArrayList;
import java.util.Arrays;

public class Vertex implements Comparable<Vertex>{
    public GridLocation gridLocation;

    public boolean isVisited = false;
    public Double minDistance = Double.POSITIVE_INFINITY;
    public GridLocation previousLocation = null;

    public Vertex(GridLocation gridLocation){
        this.gridLocation = gridLocation;
    }


    public String toString(){
        return "Vertex at " + this.gridLocation + " minDistance: " + minDistance + " isVisited: " + isVisited;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(this.minDistance, o.minDistance);
    }
}
