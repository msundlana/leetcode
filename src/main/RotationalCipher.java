package main;

import java.util.Arrays;
import java.util.List;

public class RotationalCipher {
    /**
     * Loop through all the characters
     *  Check if character is Letter
     *      Get the index of the replacement Letter from the Alphabets
     *      Return same case Letter
     *  Else Check if character is Digit
     *      Get the replacement digit
     *  Else
     *      leave as it is
     *
     * return rotated string
     * */

    private String findReplacementLetter(String letter, int rotationFactor){
        String alphebates = "abcdefghijklmnopqrstuvwxyz";
        List<String> alphebateList = Arrays.asList(alphebates.split(""));


        int indexOfLetter = alphebateList.indexOf(letter.toLowerCase()) + 1;

        int indexOfReplacementLetter = rotationFactor%26 + indexOfLetter;
        if(indexOfReplacementLetter > 26){
            indexOfReplacementLetter -= 26;
        }
        if(Character.isUpperCase(letter.charAt(0))){
            return alphebateList.get(indexOfReplacementLetter-1).toUpperCase();
        }
        return alphebateList.get(indexOfReplacementLetter-1);
    }

    private String findReplacementInteger(int number, int rotationFactor){
        int replacementDigit= rotationFactor%10 + number;
        if(replacementDigit >=10){
            replacementDigit -= 10;
        }
        return replacementDigit+"";
    }



    private boolean isNumeric(String strNum) {
        try {
            Integer.parseInt(strNum);
        }catch (NumberFormatException e){
            return false;
        }

        return true;

    }

    private boolean isAlpba(String str){
        String alphebates = "abcdefghijklmnopqrstuvwxyz";
        List<String> alphebateList = Arrays.asList(alphebates.split(""));
        return alphebateList.contains(str.toLowerCase());
    }



    public static void main(String [] args){
        RotationalCipher rotationalCipher = new RotationalCipher();
        String input = "abcdZXYzxy-999.@";
        int rotationFactor = 200;
        String [] inputArray = input.split("");
        String replacementString = "";
        for (String character: inputArray) {
            if(rotationalCipher.isNumeric(character)){
                replacementString +=rotationalCipher.findReplacementInteger(Integer.parseInt(character), rotationFactor);
            }else if(rotationalCipher.isAlpba(character)) {
                replacementString +=rotationalCipher.findReplacementLetter(character, rotationFactor);
            }else {
                replacementString +=character;
            }
        }

        System.out.println(replacementString);

    }
}
