package main.io.educative;

import java.util.Set;

public class SegmentationString {
    // public static boolean canSegmentString(String s, Set<String> dictionary) {

    //   for(String word: dictionary){
    //     if(s.contains(word)){
    //       s = s.substring(word.length());
    //     }
    //     if(s.length()==0){
    //       return true;
    //     }
    //   }

    //   if(s.length()!=0){
    //     return false;
    //   }
    //   //TODO: Write - Your - Code
    //   return true;
    // }

    public static boolean canSegmentString(String s, Set<String> dictionary) {

        for(int i=0; i<s.length() ;i++){
            for(int j=i; j<s.length(); j++){
                if(dictionary.contains(s)){
                    return true;
                }
                if(dictionary.contains(s.substring(i,j))){
                    s = s.substring(s.substring(i,j).length());
                }
            }
        }

        if(s.length()!=0){
            return false;
        }

        return true;
    }

}
