package main.io.educative;

import java.util.LinkedList;
import java.util.Queue;

class BinaryTreeNode {
    int data;
    BinaryTreeNode left;
    BinaryTreeNode right;
}

public class LevelOrderTraversal {
    public static String level_order_traversal(
            BinaryTreeNode root) {
        //TODO: Write - Your - Code
        if(root==null){
            return "";
        }

        Queue<BinaryTreeNode> nodes = new LinkedList();
        nodes.add(root);
        String s = "";

        while(!nodes.isEmpty()){
            //returns and removes the element at the front the container
            BinaryTreeNode node = nodes.remove();
            s += node.data + ",";
            if(node.left!=null){
                nodes.add(node.left);
            }
            if(node.right!=null){
                nodes.add(node.right);
            }


        }

        return s;
    }
}
