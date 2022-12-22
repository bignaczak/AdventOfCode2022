package day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sprite {
    public int x = 1;

    public Sprite() {

    }

    public void setPosition(int pos){
        this.x = pos;
    }

    public List<Integer> getRange() {
        return Arrays.asList(x - 1, x, x + 1);
    }

    public String toString(){
        return String.format("Sprite position %d covering range %s", x, this.getRange().toString());
    }

}
