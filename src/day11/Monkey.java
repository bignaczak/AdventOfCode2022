package day11;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Monkey {
    public int id;
    public int numInspections = 0;
    public ArrayList<Integer> items = new ArrayList<>();
    private Function<Integer, Integer> worryOperation = null;
    private Function<Integer, Integer> testAndGetRecipient = null;

    public static List<Monkey> monkeys = new ArrayList<>();

    public Monkey(){}
    public Monkey(int id, ArrayList<Integer> items, Function<Integer, Integer> worryOp, Function<Integer,Integer> test){
        this.id = id;
        this.items = items;
        this.worryOperation = worryOp;
        this.testAndGetRecipient = test;
        monkeys.add(this);
    }

    public int passFirstItem(){
        if(items.size() == 0) return -1;
        // get item worry num
        int item = items.get(0);
        // apply worry operation
        var afterWorry = this.worryOperation.apply(item);
        // floor divide by 3
        var afterChill = (int) (afterWorry / 3);
        // set the new value of the item
        items.set(0, afterChill);
        // find recipient
        int recipient = testAndGetRecipient.apply(afterChill);
        System.out.printf("Monkey %d item started as %d increased to %d, chilled to %d, and passed to %d\n",
                this.id, item, afterWorry, afterChill, recipient);
        return recipient;
    }

    public int processItem(int index){
        numInspections++;
        var item = items.get(index);
        // apply worry operation
        var afterWorry = this.worryOperation.apply(item);
        // floor divide by 3
        var afterChill = (int) (afterWorry / 3);
        // find recipient
        int recipient = testAndGetRecipient.apply(afterChill);
        // set the new value of the item
        items.set(index, afterChill);
        System.out.printf("Monkey %d item started as %d increased to %d, chilled to %d, and passed to %d\n",
                this.id, item, afterWorry, afterChill, recipient);
//        System.out.println("After update item: " + this);
        return recipient;
    }

    public void playRound(){
        // add list length to inspection count
//        this.addItemCount();
        //stream through to determine recipient and distribute
        var recipients = IntStream.range(0, items.size())
                .mapToObj(idx -> processItem(idx))
                .toList();

        System.out.println("Recipients: " + recipients);

        for(int id: recipients){
//            var m = monkeys.get(id);
//            var item = this.items.remove(0);
//            System.out.println("About to pass " + item + " to " + m);
//            m.items.add(item);
            monkeys.get(id).items.add(this.items.remove(0));
//            System.out.println("After pass: " + m + "\n" + this);
        }
    }

    public void addItemCount(){
        this.numInspections += items.size();
    }

    public String toString(){
        return "Monkey " + id + " Total Inspections: " + this.numInspections + " has " + items;
    }
}
