import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Arrays;

public class Solution04 {
    public static int solution(int[] l) {
        Node root = new Node();
        Node.graph.add(root);

        for (int val : l){
            boolean noChildAdded = true;
            Value value = new Value(val);
            ListIterator<Node> nodeItr = Node.graph.listIterator();

            while (nodeItr.hasNext()){
                Node node = nodeItr.next();

                boolean added = node.addChildAndGrandchild(value, nodeItr);

                if (added){
                    noChildAdded = false;
                }

            }

            if (noChildAdded){
                Node newNode = new Node(root, new ArrayList<>(), new ArrayList<>(), value);
                nodeItr.add(newNode);
                root.childNodes.add(newNode);
            }
        }

        return Node.uniqueGrandChildren.size();
    }

    public static void printGraph(int iterate){
        System.out.println("Iterate number: "+ ++iterate  +" item in set is =");
        for (Node nod : Node.graph){
            try {
                int id = nod.value.id;
                int value = nod.value.value;
                System.out.println(id + ":" + value);
            } catch (NullPointerException e){
                System.out.println("null" + ":" + "null");
            }
        }
    }

    public static void printTargetElement(Value val){
        System.out.println("Target element = " + val.id + ":" + val.value);
    }

    public static void main(String[] args) {
        System.out.println(Solution04.solution(new int[]{1, 1, 2, 2, 2}));
    }

    public static void printLogic_compareProcessID(int id1, int id2){
        System.out.println("id1 = " + id1);
        System.out.println("id2 = " + id2);
        System.out.println("node.processID != processID  =  " + (id1 != id2));
    }

    public static void printNodeAtCertainLevel(int level){
        System.out.println("Node lv=" + level);
        ArrayList<Node> nodes = Node.levelOrganized.get(level);
        for (Node node : nodes){
            System.out.println(node.value.id  + ":" + node.value.value);
            System.out.println("grandson = ");
            for (Node nod : node.grandchildren){
                System.out.print(nod.value.id + ":" + nod.value.value + " ");
            }
        }

    }
}



class Node{
    static ArrayList<Node> graph = new ArrayList<>();
    static HashMap<Integer, ArrayList<Node>> levelOrganized = new HashMap<Integer, ArrayList<Node>>();
    static HashSet<String> uniqueGrandChildren = new HashSet<>();


    Node parent;
    ArrayList<Node> childNodes;
    ArrayList<Node> grandchildren;
    int level;
    boolean isRoot;
    Value value;


    public Node(Node parent, ArrayList<Node> childNodes, ArrayList<Node> grandchildren, Value value) {
        this.parent = parent;
        this.childNodes = childNodes;
        this.grandchildren = grandchildren;
        this.value = value;
        this.level = parent.level+1;
        this.isRoot = false;

        if (!levelOrganized.containsKey(this.level)){
            ArrayList<Node> nodes = new ArrayList<>();
            nodes.add(this);
            levelOrganized.put(this.level, nodes);
        } else {
            levelOrganized.get(this.level).add(this);
        }
    }


    public Node(){  // root
        this.isRoot = true;
        this.childNodes = new ArrayList<>();
        this.level = 0;
        this.value = new Value(0);

        if (!levelOrganized.containsKey(this.level)){
            ArrayList<Node> nodes = new ArrayList<>();
            nodes.add(this);
            levelOrganized.put(this.level, nodes);
        } else {
            levelOrganized.get(this.level).add(this);
        }
    };


    public boolean addChildAndGrandchild(Value newValue, ListIterator<Node> itr){
        boolean added = false;

        if (isRoot){
            if (Node.graph.size() == 0){
                Node newNode = new Node(this, new ArrayList<>(), new ArrayList<>(), newValue);
                itr.add(newNode);
                childNodes.add(newNode);
                added = true;
            }
        } else {
            if (newValue.value % value.value == 0){ // added as child
                Node newNode = new Node(this, new ArrayList<>(), new ArrayList<>(), newValue);
                itr.add(newNode);
                childNodes.add(newNode);
                added = true;
//                System.out.println("Added " + newValue.id + ":" + newValue.value + " for parent" + value.id + ":" + value.value);
                if (isGrandChild(newNode)){

                    int a = newNode.parent.parent.value.id;
                    int b = newNode.parent.value.id;
                    int c = newNode.value.id;

                    int[] lucky = new int[] {a, b, c};

                    uniqueGrandChildren.add(Arrays.toString(lucky));

                    Node grandparent = newNode.parent.parent;
                    grandparent.addGrandChild(newNode);
                }
            }
        }
        return added;
    }


    public void addGrandChild(Node grandChild){
        grandchildren.add(grandChild);
    }


    public static boolean isGrandChild(Node node){
        return node.level >= 3;
    }
}

class Value{
    static int idCount = 0;
    int value;
    int id;

    public Value(int value){
        this.id = idCount++;
        this.value = value;
    }
}