package main.io.educative;

import java.util.LinkedList;
import java.util.Queue;

public class IsBinarySearchTree {
    public static boolean is_bst(
            BinaryTreeNode root) {

        Queue<BinaryTreeNode> queue = is_bst(
                root, new LinkedList<>());
        BinaryTreeNode prev = null;
        while(!queue.isEmpty()){
            BinaryTreeNode node = queue.remove();
            if(prev!=null && prev.data>node.data){
                return false;
            }
            prev = node;

        }

        return true;
    }

    public static  Queue<BinaryTreeNode> is_bst(
            BinaryTreeNode root, Queue<BinaryTreeNode> queue) {

        if(root!=null){
            is_bst(root.left,queue);
            queue.add(root);
            is_bst(root.right,queue);
        }
        return queue;
    }
}
