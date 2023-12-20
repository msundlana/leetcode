package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Definition for singly-linked list.*/
   class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {

        List<Integer> list = new ArrayList<Integer>();
        for(int i=0; i<lists.length; i++){
            ListNode currentNode = lists[i];
            while(currentNode!=null){
                list.add(currentNode.val);
                currentNode = currentNode.next;
            }
        }

        Collections.sort(list, Collections.reverseOrder());
        return sortListNode(list);

    }


    private ListNode sortListNode(List<Integer> list){
        ListNode sortedList = null;
        for(Integer val: list){
            sortedList = sortedList==null?new ListNode(val): new ListNode(val,sortedList );
        }
        return sortedList;
    }
}