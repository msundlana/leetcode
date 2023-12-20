package main.io.educative;

import java.util.List;

public class FindMissing {
    /**
     * Runtime Complexity: Linear, O(n)
     *
     * Memory Complexity: Constant, O(1)
     * */
    static int find_missing(List<Integer> input) {
        int n = input.size()+1;
        Integer sumOfNNumbers = (n*(n+1))/2;
        Integer sum = input.stream().reduce(0,Integer::sum);
        return sumOfNNumbers - sum;
    }
}
