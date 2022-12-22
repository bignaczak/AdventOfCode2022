package day14;

import util.ReadFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day14 {

    public static void doPartA(){
        String path = "/Users/brianignaczak/code/advent2022/src/static/input-day14.txt";
        List<String> lines = ReadFile.streamToArray(path);

        String line = lines.get(0);
        System.out.println(line);
        String[] pairs = line.split("->");
        List<Point> points = Arrays.stream(pairs)
                        .map(p -> {
                            String[] coord = p.split(",");
                            return new Point(Integer.parseInt(coord[0].trim()), Integer.parseInt(coord[1].trim()));
                        }).toList();

        System.out.println(points);
//        Stream.of(line)
//
//                .forEach(c -> System.out.println(c));


    }
}
