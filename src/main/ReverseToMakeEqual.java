package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReverseToMakeEqual {

    private void reverseList(List list){
        Collections.reverse(list);
    }

    private boolean isArraysContainsSameItems(List<Integer> listA, List<Integer>listB){
        listA = new ArrayList<>(listA);
        listB = new ArrayList<>(listB);
        Collections.sort(listA);
        Collections.sort(listB);

       return listA.equals(listB);
    }

    private List<Integer> convertArrayToList(int[] array){
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

    /**
     * 1. Check if the array A size is equal to array B size
     * 2. Check if they contains the same objects or numbers
     * 3. If above statements are true you can reverse the subarrays from B any number of times.
     *
     * */

    boolean areTheyEqual(int[] array_a, int[] array_b) {
        // Write your code here

        List listA = convertArrayToList(array_a);
        List listB = convertArrayToList(array_b);
        if(listA.size()!=listB.size() || !isArraysContainsSameItems(listA,listB)){
            return false;
        }
        return true;
//        for(int i=1; i<listB.size(); i++){
//            reverseList(listB.subList(0,i));
//
//        }
//        return false;
    }

    public static void main(String [] args){
        ReverseToMakeEqual reverseToMakeEqual = new ReverseToMakeEqual();
        int[] array_a = {1, 2, 3, 4};
        int[] array_b = {1, 4, 3, 2};
        System.out.println(reverseToMakeEqual.areTheyEqual(array_a,array_b));
    }
}
