package main;


public class ReverseOperations {
    class Node {
        int data;
        Node next;
        Node(int x) {
            data = x;
            next = null;
        }
    }

    // Add any helper functions you may need here


    Node reverse(Node head) {
        // Write your code here
        Node output = null;
        Node previous = null;
        Node next = null;
        Node temp = null;
        while (head!=null){
            if(head.data%2==0){
                if(previous==null || previous.data%2!=0) {
                    temp = new Node(head.data);
                }else{
                    /**Reverse Node
                     * ------------
                     * 1. Have a placeholder to hold the previous head node
                     * 2. Create a new Node and make it the head
                     * 3. Assign the previous head node to next node
                     *
                     * */
                    Node hold = temp;
                    temp = new Node(head.data);
                    temp.next = hold;
                }


                if((head.next!=null && head.next.data%2!=0) || head.next==null){
                    /**Add to the end of the list
                     * --------------------------
                     * 1.Assign the current head node as last node
                     * 2.Transverse till the last node
                     * 3.Insert the new node there
                     *
                     * */
                    if(output==null){
                        output = temp;
                    }else {
                        Node last = output;
                        while (last.next != null) {
                            last = last.next;
                        }

                        // Insert the new_node at last node
                        last.next = temp;
                    }
                }
            }else {
                if(output==null){
                    output = new Node(head.data);
                }else {
                    Node last = output;
                    while (last.next != null) {
                        last = last.next;
                    }

                    // Insert the new_node at last node
                    last.next = new Node(head.data);
                }
                //current.next = new Node(head.data);
            }

            previous = head;
            head = head.next;

        }

        return output;
    }

    Node createLinkedList(int[] arr) {
        Node head = null;
        Node tempHead = head;
        for (int v : arr) {
            if (head == null) {
                head = new Node(v);
                tempHead = head;
            } else {
                head.next = new Node(v);
                head = head.next;
            }
        }
        return tempHead;
    }

    void printLinkedList(Node head) {
        System.out.print("[");
        while (head != null) {
            System.out.print(head.data);
            head = head.next;
            if (head != null)
                System.out.print(" ");
        }
        System.out.print("]");
    }

    public static void main(String [] args){
        ReverseOperations reverseOperations = new ReverseOperations();
        int[] arr = {2, 8, 4,9, 12, 16};
        Node head = reverseOperations.createLinkedList(arr);
        Node output = reverseOperations.reverse(head);
        reverseOperations.printLinkedList(output);
    }
}
