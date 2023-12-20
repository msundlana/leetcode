package main.amazon;




        import java.util.*;
        import java.util.stream.*;




class Result {

    /*
     * Complete the 'minimalHeaviestSetA' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts INTEGER_ARRAY arr as parameter.
     */

    public static List<Integer> minimalHeaviestSetA(List<Integer> arr) {
        // Write your code here
        List<Integer> A = null;


        List<Integer> sortedArr = arr.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());

        int size = sortedArr.size();
        boolean intersects = false;
        System.out.println("Sorted");
        System.out.println(sortedArr);


        for(int i=0; i<size/2; i++){
            List<Integer> subsetA = new ArrayList<>();
            List<Integer> subsetB = new ArrayList<>();
            if(i==0){
                subsetA.add(sortedArr.get(i));
            }else{
                subsetA.addAll(sortedArr.subList(0, i+1));
            }
            if(i+1<size){
                subsetB.addAll(sortedArr.subList(i+1, size));
            }

            int sumA = subsetA.stream().reduce(Integer::sum).get();
            int sumB = subsetB.stream().reduce(Integer::sum).get();
            System.out.println("Subset A");
            System.out.println(subsetA);
            System.out.println("Subset  B");
            System.out.println(subsetB);


          for (Integer integer : subsetA) {
                if(subsetB.contains(integer)){
                    intersects = true;
                }
          }

            if(subsetA.size()<subsetB.size() && sumA>sumB && !intersects){
                if(A==null){
                    A = subsetA;
                }else{
                    int maxA = A.stream().reduce(Integer::sum).get();
                    if(maxA<sumA){
                        A = subsetA;
                    }
                }


            }
            intersects = false;
        }


        return A;
    }

}

public class Solution {
    public static void main(String[] args) {

        List<Integer> arr = new ArrayList<>();

        arr.add(6);
        arr.add(5);
        arr.add(3);
        arr.add(2);
        arr.add(4);
        arr.add(1);
        arr.add(2);

        System.out.println("Input");
        System.out.println(arr);
        List<Integer> result = Result.minimalHeaviestSetA(arr);

        System.out.println("Output");
       System.out.println(result);
    }
}

