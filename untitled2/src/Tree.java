

import java.util.ArrayList;
import java.util.HashMap;

public class Tree {



    public static int[] solution(int h, int[] q){

        BinaryTree tree = new BinaryTree(h);
        int[] ans = new int[q.length];

        for (int i = 0 ; i < q.length ; i++){
            ans[i] = tree.getParentValFromChildVal(q[i]);
        }

        return ans;
    }

    public static void main(String[] args) {
        solution(5, new int[]{19, 14, 28});
    }
}


class BinaryTree{
    ArrayList<ArrayList<Node>> levelNodes = new ArrayList();
    Node root = new Node(null);
    int height;
    int count = 0;
    HashMap<Integer, Node> valNodeMap = new HashMap<>();

    BinaryTree(int height) {
        this.height = height;
        initLevels();
        levelNodes.get(0).add(root);
        constructEmptyTree();
        postOrderTraversal(root);
    }

    private void initLevels(){
        for (int i = 0 ; i < height ; i++){
            levelNodes.add(new ArrayList<>());
        }
    }

    private void constructEmptyTree(){
        for (int lvl = 0 ; lvl < height-1 ; lvl++){
            ArrayList<Node> nodes = levelNodes.get(lvl);
            for (Node node : nodes){
                Node left = new Node(node);
                Node right = new Node(node);
                levelNodes.get(lvl+1).add(left);
                levelNodes.get(lvl+1).add(right);
                node.left = left;
                node.right = right;
            }
        }
    }

    static class Node{
        int val;
        Node left;
        Node right;
        Node parent;

        public Node(Node parent) {
            this.parent = parent;
        }
    }

    public void trackSize(){
        for (ArrayList<Node> l : levelNodes){
            System.out.println(l.size() + ",");
            for (Node node : l){
                System.out.print(node.val + ", ");
            }
            System.out.println("\n");
        }
    }

    public void postOrderTraversal(Node node){

        if (node == null){
            return;
        }

        postOrderTraversal(node.left);
        postOrderTraversal(node.right);

        node.val = ++count;
        valNodeMap.put(count, node);
    }

    public int getParentValFromChildVal(int val){
        Node node = valNodeMap.get(val);
        if (node == root){
            return -1;
        }else {
            return node.parent.val;
        }
    }
}