package main.io.algoexpert;


import java.util.*;

public class FindApartment {
    /**
     * Sample input
     *
     * Blocks = [
     * {
     *  "gym": false,
     *  "school":true,
     *  "store": false
     * },
     * {
     *  "gym": true,
     *  "school":false,
     *  "store": false
     * },
     * {
     *  "gym": true,
     *  "school":true,
     *  "store": false
     * },
     * {
     *  "gym": false,
     *  "school":true,
     *  "store": false
     * },
     * {
     *  "gym": false,
     *  "school":true,
     *  "store": true
     * }
     * ]
     *
     * Reqs = ["gym","school","store"]
     *
     * Sample output
     *
     * Block 4 minimal distance equals 1
     *
     * Assuming findApartment(List<Map<String,Boolean>> blocks, List<String> reqs) or
     * findApartment(List<List<Amenity>> blocks, List<String> reqs)
     * 1. iterate through each block and check maximal distance to each reqs
     * 2. Then get minimal maximal distance block
     * 	contraints [ 1>=left and right<=block.length]
     *
     * 	return 0 if not found
     *
     * 	Time complexity is O(n)*O(m)*O(log(n)) = O(n*m*log(n))
     * 	Space complexity is O(n) constant
     * */

    public static int findApartment(List<Map<String,Boolean>> blocks, List<String> reqs){
        int minimalBlock = 0;
        int minimalDistance = Integer.MAX_VALUE;

        for(int i=0; i<blocks.size(); i++){
            int maxDistance = 0;
            for (String req: reqs) {
                for (int j=0; j<blocks.size(); j++) {
                    /**
                     * If Not Map
                     * And Object Amenity String name and boolean exist
                     * is stored in a list for each block then
                     * List<List<Amenity>> blocks
                     * blocks.get(index).get(req) will be
                     * blocks.get(index).filter(x->x.name==req).get().exist
                     */
                    if((i-j>=0 && blocks.get(i-j).get(req))
                    || (i+j<blocks.size() && blocks.get(i+j).get(req))){
                        maxDistance = Math.max(maxDistance,j);
                        break;
                    }
                }
            }
            if(minimalDistance>maxDistance){
                minimalDistance = maxDistance;
                minimalBlock = i+1;
            }
        }
        return minimalBlock;
    }

    public static void main(String [] args){
        List<Map<String,Boolean>> blocks = new ArrayList<>();
        Map<String,Boolean> block1 = new HashMap<>();
        block1.put("gym",false);
        block1.put("school",true);
        block1.put("store",false);
        blocks.add(block1);
        Map<String,Boolean> block2 = new HashMap<>();
        block2.put("gym",true);
        block2.put("school",false);
        block2.put("store",false);
        blocks.add(block2);
        Map<String,Boolean> block3 = new HashMap<>();
        block3.put("gym",true);
        block3.put("school",true);
        block3.put("store",false);
        blocks.add(block3);
        Map<String,Boolean> block4 = new HashMap<>();
        block4.put("gym",false);
        block4.put("school",true);
        block4.put("store",false);
        blocks.add(block4);
        Map<String,Boolean> block5 = new HashMap<>();
        block5.put("gym",false);
        block5.put("school",true);
        block5.put("store",true);
        blocks.add(block5);

        System.out.println("Expected output block 4");
        List<String> reqs = Arrays.asList("gym","school","store");

        System.out.println("Your output block " + findApartment(blocks,reqs));

    }
}
