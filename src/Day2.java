import org.javatuples.Triplet;

import java.io.*;
import java.util.*;

public record Day2() {

    private static int totalScore = 0;
    private static List<Match> matches= new ArrayList<>();
    public enum Play{
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        private int value;
        Play(int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }
    }

    public enum Outcome{
        WIN("Z", 6),
        DRAW("Y", 3),
        LOSE("X", 0);

        private String code;
        private int points;
        Outcome(String code, int points){
            this.code = code;
            this.points = points;
        }

        public static Outcome getFromCode(String code){
            for(Outcome o: Outcome.values()){
                if (o.code.equalsIgnoreCase(code)) return o;
            }
            return null;
        }

        public int getPoints(){
            return this.points;
        }
    }

    public enum StrategyInterpretation{
        A,
        B
    }

    public static Play decodePlay(String p){
        switch (p.toUpperCase()){
            case "A", "X" -> {return Play.ROCK;}
            case "B", "Y" -> {return Play.PAPER;}
            case "C", "Z" -> {return Play.SCISSORS;}
            default -> {throw new RuntimeException();}
        }
    }

    public static Match getMatch(String theirCode, String myCode){
        return new Match(decodePlay(theirCode), decodePlay(myCode));
    }

    public static void readAllMatches(StrategyInterpretation strategyInterpretation){
        String path = "/Users/brianignaczak/code/advent2022/src/static/input-day2.txt";
        matches.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null){
                String[] codedPlays = line.split(" ");
//                System.out.printf("Theirs: %s and Mine: %s\n", codedPlays[0].trim(), codedPlays[1].trim());
                Match match;
                if (strategyInterpretation == StrategyInterpretation.A){
                    match = getMatch(codedPlays[0].trim(), codedPlays[1].trim());
                } else {
                    Outcome outcome = Outcome.getFromCode(codedPlays[1].trim());
                    Play theirPlay = decodePlay(codedPlays[0].trim());
                    Play myPlay = determinePlay(theirPlay, outcome);
                    match = new Match(theirPlay, myPlay);
//                    System.out.printf("Their play %s: %s || outcome %s: %s || my play %s\n",
//                            codedPlays[0].trim(), theirPlay, codedPlays[1], outcome, myPlay);
                }
                matches.add(match);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Total matches: " + matches.size());
    }

    public static void calculateTotalScore(){
        totalScore = 0;
        for (Match m: matches){
//            String outcome = "LOSS";
//            if (m.determineWinner() == m.myPlay){
//                outcome = "WIN";
//            } else if (m.getMatchSet().size() == 1 ){
//                outcome = "DRAW";
//            }
            totalScore += m.getMyScore();
//            System.out.println(m + " Outcome: " + outcome + " Match Points: " + m.getMyScore() + " || Total Score: " + totalScore);
        }
        System.out.printf("Total score %d\n", totalScore);

    }

    public static Play determinePlay(Play theirPlay, Outcome outcome){

        if (outcome == Outcome.DRAW){
            return theirPlay;
        } else {
            switch (theirPlay){
                case Play p when p==Play.PAPER-> {return (outcome == Outcome.WIN) ? Play.SCISSORS : Play.ROCK;}
                case Play p when p==Play.SCISSORS -> {return (outcome == Outcome.WIN) ? Play.ROCK : Play.PAPER;}
                case Play p when p==Play.ROCK -> {return (outcome == Outcome.WIN) ? Play.PAPER : Play.SCISSORS;}
                default  -> {throw new RuntimeException("Invalid Set provided to determine Winner!!!");}
            }
        }
    }

    public static Set<Triplet<Play, Play, Outcome>> getMatchSet(){
//        Triplet<Play, Play, Outcome> triplet = new Triplet<Play, Play, Outcome>(Play.SCISSORS, Play.ROCK, Outcome.WIN);
//        Triplet<Play, Play, Outcome> triplet2 = new Triplet<Play, Play, Outcome>(Play.SCISSORS, Play.PAPER, Outcome.LOSE);
//        System.out.println("Triplets equal? " + (triplet2.equals(triplet)));
//        Set<Triplet<Play,Play,Outcome>> mySet = new HashSet<Triplet<Play,Play,Outcome>>(Arrays.asList(triplet2, triplet));

        Set<Triplet<Play,Play,Outcome>> mySet = new HashSet<Triplet<Play,Play,Outcome>>();
        for (Match m: matches){
            mySet.add(new Triplet<Play,Play,Outcome>(m.theirPlay, m.myPlay, m.getOutcome()));
        }
        return mySet;
    }



    public record Match(Play theirPlay, Play myPlay){

        public Set<Play> getMatchSet(){
            return new HashSet<Play>(Arrays.asList(theirPlay, myPlay));
        }

//        public Play determineWinner(){
//            Set<Play> playSet = getMatchSet();
//            Play winningPlay = null;
//            if (playSet.containsAll(Arrays.asList(Play.ROCK, Play.PAPER))){
//                winningPlay = Play.PAPER;
//            } else if (playSet.containsAll(Arrays.asList(Play.ROCK, Play.SCISSORS))){
//                winningPlay = Play.ROCK;
//            } else if (playSet.containsAll(Arrays.asList(Play.PAPER, Play.SCISSORS))){
//                winningPlay = Play.SCISSORS;
//            }
//            return winningPlay;
//        }

        public Play determineWinner(){
            Set<Play> playSet = getMatchSet();
            Play winningPlay = null;
            switch (playSet){
                case Set<Day2.Play> p when p.size() == 1 -> {return null;}
                case Set<Day2.Play> p when p.containsAll(Arrays.asList(Play.ROCK, Play.SCISSORS)) -> {return Play.ROCK;}
                case Set<Day2.Play> p when p.containsAll(Arrays.asList(Play.ROCK, Play.PAPER)) -> {return Play.PAPER;}
                case Set<Day2.Play> p when p.containsAll(Arrays.asList(Play.SCISSORS, Play.PAPER)) -> {return Play.SCISSORS;}
                default  -> {throw new RuntimeException("Invalid Set provided to determine Winner!!!");}
            }
        }

        public Outcome getOutcome(){
            if(getMatchSet().size() == 1) return Outcome.DRAW;
            if(myPlay == determineWinner()){
                return Outcome.WIN;
            } else{
                return Outcome.LOSE;
            }
        }

        public int getMyScore(){

            int pointsForOutcome = this.getOutcome().getPoints();
            int pointsForPlay = this.myPlay.getValue();

            return pointsForOutcome + pointsForPlay;

        }

    }


}
