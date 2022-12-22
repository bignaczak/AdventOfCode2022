package day10;

import java.util.ArrayList;
import java.util.List;

public class CRT {
    int width = 40;
    int height = 6;

    List<Pixel> pixels = new ArrayList<>(height * width);
    List<List<String>> display = new ArrayList<>(height);

    public void addPixel(Pixel pixel){
        pixels.add(pixel);

    }

    public void renderDisplay(){
        System.out.println("Number of pixels: " + pixels.size());
        for(int row=0; row< height; row++){
            int subStart = row * width;
            int subEnd = subStart + width;
            StringBuilder sb = new StringBuilder();
            List<String> line = pixels.subList(subStart, subEnd).stream()
                    .peek(p -> sb.append(p.content))
                    .map(p -> p.content)
                    .toList();
            display.add(line);
            System.out.println(sb);
        }
    }



}
