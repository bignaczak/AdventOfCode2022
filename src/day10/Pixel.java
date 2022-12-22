package day10;

public class Pixel {
    public enum Content{
        BLANK, FILLED;
    }
    public String content;
    public int x;
    public int y;

    public Pixel(){}
    public Pixel(Content content){
        if(content == Content.BLANK){
            this.content = ".";
        } else {
            this.content = "#";
        }
    }

}
