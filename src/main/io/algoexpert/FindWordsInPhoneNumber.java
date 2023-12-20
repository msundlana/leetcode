package main.io.algoexpert;

import java.util.*;

public class FindWordsInPhoneNumber {

    public static Map<String,Integer> initDictionary(){
        Map<String,Integer> letterToNumberMap = new HashMap<>();
        letterToNumberMap.put("a",2);
        letterToNumberMap.put("b",2);
        letterToNumberMap.put("c",2);
        letterToNumberMap.put("d",3);
        letterToNumberMap.put("e",3);
        letterToNumberMap.put("f",3);
        letterToNumberMap.put("g",4);
        letterToNumberMap.put("h",4);
        letterToNumberMap.put("i",4);
        letterToNumberMap.put("j",5);
        letterToNumberMap.put("k",5);
        letterToNumberMap.put("l",5);
        letterToNumberMap.put("m",6);
        letterToNumberMap.put("n",6);
        letterToNumberMap.put("o",6);
        letterToNumberMap.put("p",7);
        letterToNumberMap.put("q",7);
        letterToNumberMap.put("r",7);
        letterToNumberMap.put("s",7);
        letterToNumberMap.put("t",8);
        letterToNumberMap.put("u",8);
        letterToNumberMap.put("v",8);
        letterToNumberMap.put("w",9);
        letterToNumberMap.put("x",9);
        letterToNumberMap.put("y",9);
        letterToNumberMap.put("z",9);
        return letterToNumberMap;
    }

    /**
     * Initialize a dictionary of letters(key) to numbers(value)
     * The iterate through each word in the list and covert to number word using the dictionary
     * In the same iteration check if phone number contains word
     * Then add to the output list
     * And sort the output list
     * Time complexity of O(n)*O(m) = O(n*m)
     * for n is size of words lists and m is the size of each word in the list
     * Space complexity is linear O(n)
     * */
    public static List<String> findWords(String phoneNumber, List<String> words ){
        List<String> output = new ArrayList<>();
        Map<String,Integer> letterToNumberMap = initDictionary();

        for (String word: words) {
            String numberWord = "";
            for (String letter:word.split("")) {
                numberWord+=letterToNumberMap.get(letter);
            }
            if(phoneNumber.contains(numberWord)){
                output.add(word);
            }
        }
        Collections.sort(output);
        return output;
    }

    public static void main(String [] args){

        List<String> words = Arrays.asList("foo","bar","baz","foobar","emo","cap","car","cat");
        String phoneNumber = "3662277";
        System.out.println(findWords(phoneNumber,words));
    }
}
