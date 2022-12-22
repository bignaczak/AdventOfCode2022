package day5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrateStack {

    public static HashMap<Integer, CrateStack> stackMap = new HashMap<>();
    private int id;
    public java.util.Stack<String> stringStack = new java.util.Stack<>();

    public CrateStack(int id){
        this.id = id;
        stackMap.put(id, this);
    }

    public void push(String crate){
        this.stringStack.push(crate);
    }

    public String pop(){
        return this.stringStack.pop();
    }


    public void moveCrateTo(int id){
        CrateStack destinationCrateStack = stackMap.get(id);
        destinationCrateStack.push(this.stringStack.pop());
    }

    /**
     * Move multiple creates
     */
    public void moveMultipleCratesTo(CrateMove crateMove){
        CrateStack destinationCrateStack = stackMap.get(crateMove.toCrate());
        int startIndex = this.stringStack.size() - (crateMove.numMoves());
        destinationCrateStack.stringStack.addAll(this.stringStack.subList(startIndex, this.stringStack.size()));
        for (int i=0; i<crateMove.numMoves(); i++){
            this.stringStack.pop();
        }
    }

    public static CrateStack get(int id){
        return stackMap.get(id);
    }

    public String toString(){
        return String.format("Crate id: %d contains %s", this.id, this.stringStack);
    }
}
