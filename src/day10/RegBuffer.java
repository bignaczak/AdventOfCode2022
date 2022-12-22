package day10;

public class RegBuffer {
    private int value = 1;

    public RegBuffer() {

    }

    public int add(int adder){
        this.value += adder;
        return this.value;
    }

    public int getAndFlush(){
        int regValue = this.value;
        this.value = 0;
        return regValue;
    }
}
