package main.io.educative;

import java.util.HashSet;
import java.util.Set;

public class FindSum {
    /**
     * Runtime Complexity: Linear, O(n)
     *
     * Memory Complexity: linear, O(n)
     * */
    static boolean findSumOfTwo(int[] A, int val) {
        Set previousVisited = new HashSet();
        for(int num:A){
            if(previousVisited.contains(val-num)){
                return true;
            }
            previousVisited.add(num);
        }

        return false;
    }
}
