package main;

import java.util.ArrayList;
import java.util.List;

public class CountSubarrays {

    /**
     * Loop through Array
     *  Check if integers before and after are less than integer at index
     *      if greater return subarray of integer at index only
     *      else return all the contiguous subarray starting from or ending at.
     *
     * */

    int[] countSubarrays(int[] arr) {
        int[] output = new int[arr.length];
        for (int i=0; i<arr.length; i++){
            List list = subArray(i,arr);
            System.out.println(list);
            output[i] = list.size();
        }
        return output;
    }

    List subArray(int index ,int[] arr){
        List list = new ArrayList();
        for (int i=0; i<arr.length; i++) {
            List innerList= new ArrayList();
            List innerList2= new ArrayList();

            for (int j=0; j<=i; j++){
                if(j+index<arr.length && arr[j+index] <= arr[index] ){
                    innerList.add(arr[j+index]);
                }

                if(index-j>=0 && arr[index-j]<= arr[index]){
                    innerList2.add(arr[index-j]);
                }
                if((index-j>=0 && arr[index-j]>arr[index]) || (j+index<arr.length && arr[j+index] >arr[index])){
                    break;
                }
            }

            if(!list.contains(innerList)){
                list.add(innerList);
            }
            if(!list.contains(innerList2)){
                list.add(innerList2);
            }


        }
        return list;
    }

    void printIntegerArray(int[] arr) {
        int len = arr.length;
        System.out.print("[");
        for(int i = 0; i < len; i++) {
            if (i != 0) {
                System.out.print(", ");
            }
            System.out.print(arr[i]);
        }
        System.out.print("]");
    }

    public static void main(String [] args){
        CountSubarrays countSubarrays = new CountSubarrays();
        int[] arr = {2, 4, 7, 1, 5, 3};
        countSubarrays.printIntegerArray(countSubarrays.countSubarrays(arr));
    }


}
