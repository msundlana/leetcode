package main;

import java.util.ArrayList;
import java.util.List;

public class PairSums {
    int numberOfWays(int[] arr, int k) {
        // Write your code here
        List pairs = new ArrayList();
        for(int i=0; i<arr.length; i++){

            for(int j=i; j<arr.length; j++){
                if(i!=j && arr[i]+arr[j]==k){
                    List pair = new ArrayList();
                    pair.add(arr[i]);
                    pair.add(arr[j]);
                    pairs.add(pair);
                }
            }

        }
        System.out.println(pairs);
        return pairs.size();

    }

    public static void main(String [] args){
        int[] arr = {1, 5, 3, 3, 3};
        int k = 6;
        PairSums pairSums = new PairSums();
        pairSums.numberOfWays(arr,k);
    }

}
