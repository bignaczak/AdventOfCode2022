package day10;

import util.ReadFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day10 {
    private static AtomicInteger cycle = new AtomicInteger();
    private static RegBuffer regBuffer = new RegBuffer();
    public static void doPartA(){
        var inputPath = "/Users/brianignaczak/code/advent2022/src/static/input-day10.txt";
        List<String> lines = ReadFile.streamToArray(inputPath);
        int i = 0;
        for(var line: lines){
            Command command = parseLine(line);
            System.out.println(command);
            if (++i>10) break;
        }
        List<Command> commands = lines.stream()
                .map(line -> parseLine(line))
                .toList();
        cycle.incrementAndGet();
        Map<Integer, Integer> register = new HashMap<>();

//        register.put(1,4);
//        register.put(2,1);
//        register.put(4,4);

//        System.out.println(register.keySet().stream().max(Integer::compare).orElseGet(() -> 0));



        commands.stream()
                .forEach(c -> processCommand(c, register));

//        System.out.println(register);
        List<Integer> targetCycles = new ArrayList<>();
        targetCycles.add(20);
        targetCycles.add(60);
        targetCycles.add(100);
        targetCycles.add(140);
        targetCycles.add(180);
        targetCycles.add(220);

        int sumSignals =
                register.keySet().stream()
                .filter(key -> targetCycles.contains(key.intValue()))
                .map(key -> {
                    System.out.println("Key: " + key + " Value: " + register.get(key) + " Signal: " + register.get(key) * key);
                    return register.get(key) * key;
                })
                .reduce(Integer::sum)
                .orElse(0);


        System.out.println(register);
        System.out.println("Sum Signals = " + sumSignals);
        //104 too low
        //12800 too low

        // part B

        CRT crt = new CRT();
        // 1) create new sprite with default center point 1
        Sprite sprite = new Sprite();
        int j=0;
        for(Integer regKey: register.keySet()){
            // Set the sprite position based on the register
            sprite.setPosition(register.get(regKey));
            // current position lags register index by 1
            int currentPosition = regKey - 1;

            // 2) draw pixel based on whether sprite is on current position
            boolean spriteOnPosition = sprite.getRange().contains(currentPosition % crt.width);
            System.out.println("Register Key: " + regKey + " Value: " + register.get(regKey) + "  " + sprite);
            Pixel.Content content = spriteOnPosition ? Pixel.Content.FILLED : Pixel.Content.BLANK;
            crt.addPixel(new Pixel(content));
//            if(++j>10); break;

        }

        crt.renderDisplay();

        //PGPHBEAB


    }

    private static void processCommand(Command command, Map<Integer, Integer> register){
        // 1) get the previous register value
        // 2) derive new register value by adding previous register value and purge buffer
        // 3) write the new register value for the number of prescribed steps
        // 4) add the adder to the buffer


        // 1) get the previous register value
        int maxKey = register.keySet().stream().max(Integer::compare).orElseGet(() -> 0);
        int previousValue = maxKey == 0 ? 0 : register.get(maxKey);
        // 2) derive new register value by adding previous register value and purge buffer
        int newValue = previousValue + regBuffer.getAndFlush();
        // 3) write the new register value for the number of prescribed steps
        IntStream.range(0, command.numCycles())
                .forEach(i -> register.put(cycle.getAndIncrement(), newValue));
        // 4) add the adder to the buffer
        regBuffer.add(command.registryAdder());


    }

    private static Command parseLine(String line){
        String[] commandParts = line.split(" ");
        int adder = 0;
        int numCycles = switch(commandParts[0]){
            case "noop" -> 1;
            case "addx" -> {adder = Integer.parseInt(commandParts[1]);
                yield 2;
            }
            default -> throw new RuntimeException("Unable to parse command " + line);
        };
        return new Command(numCycles, adder);


    }
}
