package main.io.educative;

import java.util.Enumeration;
import java.util.Hashtable;


public class ArbitraryPointer {

    class LinkedListNode {
        int data;
        LinkedListNode next;
        LinkedListNode arbitrary_pointer;

        public LinkedListNode() {
        }

        public LinkedListNode(int data) {
            this.data = data;
        }

        public LinkedListNode(int data, LinkedListNode next, LinkedListNode arbitrary_pointer) {
            this.data = data;
            this.next = next;
            this.arbitrary_pointer = arbitrary_pointer;
        }
    }

    public  LinkedListNode deep_copy_arbitrary_pointer(
            LinkedListNode head) {
        Hashtable clone = new Hashtable();

        LinkedListNode current  = head;
        LinkedListNode prev = null;

        while(current!=null){
            LinkedListNode newNode = new LinkedListNode(current.data);
            if(prev!=null){
                prev.next = newNode;
            }
            newNode.arbitrary_pointer = current.arbitrary_pointer;
            clone.put(current,newNode);
            prev = newNode;
            current = current.next;
        }

        Enumeration keys =  clone.keys();
        while(keys.hasMoreElements()) {
            current = (LinkedListNode)clone.get(keys.nextElement());
            if(current.arbitrary_pointer!=null){
                current.arbitrary_pointer = (LinkedListNode)clone.get(current.arbitrary_pointer);
            }
        }


        return (LinkedListNode)clone.get(head);
    }

}
