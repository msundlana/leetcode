package main.io.educative;

class LinkedListNode{
    int data;
    LinkedListNode next;

    public LinkedListNode() {
    }

    public LinkedListNode(int data) {
        this.data = data;
    }

    public LinkedListNode(int data, LinkedListNode next) {
        this.data = data;
        this.next = next;
    }
}

public class MergeSortList {
    public static LinkedListNode merge_sorted(
            LinkedListNode head1,
            LinkedListNode head2) {
        if(head1==null){
            return head2;
        }else if(head2==null){
            return head1;
        }
        if(head1.data<head2.data){
            head1.next = merge_sorted(head1.next,head2);
            return head1;
        }else {
            head2.next = merge_sorted(head1,head2.next);
            return head2;
        }
    }
}
