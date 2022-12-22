package day3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElfGroup{
    private List<String> sacks = new ArrayList<>();
    private String badge;
    private int badgeValue;

    public ElfGroup(String ...items){
        this.sacks = Arrays.asList(items);
        this.setBadgeWithStream();
        this.setBadgeValue();
    }

    public String getBadge() {
        return badge;
    }

    public int getBadgeValue() {
        return badgeValue;
    }

    private void setBadge(){
        String sharedItem = getSharedItems(sacks.get(0), sacks.get(1));
        sharedItem = getSharedItems(sharedItem, sacks.get(2));
        if (sharedItem.length() > 1) throw new RuntimeException("Badge is too long!  -- " + sharedItem);
        this.badge = sharedItem;
    }

    private void setBadgeWithStream(){
        this.badge = Arrays.stream(sacks.get(0).split(""))
                .filter(sacks.get(1)::contains)
                .filter(sacks.get(2)::contains)
                .collect(Collectors.joining());
    }

    private void setBadgeValue(){
        this.badgeValue = Character.getNumericValue(this.badge.charAt(0)) - 9;
        if (Character.isUpperCase(this.badge.charAt(0))) badgeValue += 26;
    }

    private String getSharedItems(String sack1, String sack2){
        StringBuilder stringBuilder = new StringBuilder();
        // loop through each letter of sack1
//        Stream.of(sack1)
//                .map((element) -> sack2.contains(element) ? element : null)
//                .forEach(System.out::println);

        for (int i=0; i<sack1.length(); i++){
            String currentLetter = sack1.substring(i, i+1);
            if (sack2.contains(currentLetter)){
                // Only add unique letter
                if(!stringBuilder.toString().contains(currentLetter)) stringBuilder.append(currentLetter);
            }
        }
        //
        return stringBuilder.toString();
    }


    public String toString(){
        return sacks.toString() + " with shared badge: " + this.badge + " and value=" + this.badgeValue;
    }

}