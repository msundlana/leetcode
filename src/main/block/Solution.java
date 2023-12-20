package main.block;

// Main class should be named 'Solution'
/**
 Suppose a collection of people want to exchange presents.

 - They each write their name on a piece of paper, and toss all the papers into a box.
 - Shake up the box, and everyone pulls out a name. The name that you draw is who you’re giving your gift to. You are their Secret Santa.
 - If you pull out your own name, pull out another one and put the first one back — you can't give a gift to yourself.

 When we're done, we expect that:

 - Everyone is receiving one gift
 - Everyone is giving one gift
 - No one knows where their gift is coming from, this needs to be random.
 */

import java.util.*;
import static java.util.Arrays.asList;

public class Solution {

    /**

     */

    public List<List<String>> assignSantas(List<String> players) {
        List<List<String>> output = new ArrayList<>();
        List<Integer> generatedIndexs = new ArrayList<>();
        Random random = new Random();

        for(int i=0; i<players.size(); i++){
            if(generatedIndexs.size()==players.size()-1 && !generatedIndexs.contains(i)){
                String player =  output.get(0).get(1);
                output.get(0).add(1, players.get(i));
                List<String> pairing = Arrays.asList(players.get(i),player);
                output.add(pairing);
                continue;
            }
            int index = random.nextInt(players.size());
            while(index==i || generatedIndexs.contains(index) ){
                index = random.nextInt(players.size());
            }
            generatedIndexs.add(index);
            List<String> pairing = Arrays.asList(players.get(i),players.get(index));
            output.add(pairing);

        }
        return output;
    }

    public static void main(String... args) {
      /*
      Fred -> Wilma
      Wilma -> Barney
      Barney -> Fred


      */
        List<String> players = asList("Fred", "Wilma", "Barney", "Bam Bam");
        List<List<String>> santas = null;
        for(int i=0; i<50; i++){
            santas = new Solution().assignSantas(players);
            System.out.println(santas);
        }

    }
}