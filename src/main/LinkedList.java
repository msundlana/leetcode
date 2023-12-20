package main;

class LinkedList {

    static Node head;

    static class Node {

        int data;
        Node next;

        Node(int d)
        {
            data = d;
            next = null;
        }
    }

    /* Function to reverse the linked list */
    Node reverse(Node head)
    {
        Node current = head;
        Node prev = null;
        Node next = null;
        while(current !=null){
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }

     
    Node mergeNodes(Node n1, Node n2){
        if(n1==null){
            return n2;
        }else if(n2==null){
           return n1;
        }

        if(n1.data > n2.data){
            n1.next = mergeNodes(n1.next,n2);
            return n1;
        }else {
            n2.next = mergeNodes(n2.next,n1);
            return n2;
        }
    }

    // prints content of double linked list
    void printList(Node node)
    {
        while (node != null) {
            System.out.print(node.data + " ");
            node = node.next;
        }
    }

    // Driver Code
    public static void main(String[] args)
    {
        LinkedList list = new LinkedList();
        list.head = new Node(85);
        list.head.next = new Node(15);
        list.head.next.next = new Node(4);
        list.head.next.next.next = new Node(20);

        System.out.println("Given Linked list");
        list.printList(head);
        head = list.reverse(head);
        System.out.println("");
        System.out.println("Reversed linked list ");
        list.printList(head);
    }
}
