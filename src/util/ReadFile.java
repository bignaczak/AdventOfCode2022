package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadFile {

    public static List<String> toArray(String absPath){
        List<String> outArray = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(absPath));
            String line;
            while ((line = reader.readLine()) != null){
                outArray.add(line);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outArray;
    }

    public static List<String> streamToArray(String absPath){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(absPath));
            return reader.lines()
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
