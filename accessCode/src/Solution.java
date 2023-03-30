//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.ListIterator;
//
//public class Solution {
//    public static int solution(int[] l) {
//        int[] descSorted = getDescSortedArray(l);
//        System.out.println("Sorted = " + Arrays.toString(descSorted));
//        Graph graph = new Graph(new ArrayList<>());
//        Node root = new Node(0);
//        graph.addNode(root);
//
//        int processID = 0;
//
//        for (int element : descSorted){
//            ++processID;
//            ListIterator<ArrayList<Node>> layersItr = graph.getLayers().listIterator();
//            System.out.println("-------------------------------------ELEMENT = " + element + "-----------------------------------");
//
//            while (layersItr.hasNext()){
//                ArrayList<Node> layer = layersItr.next();
//                for (Node node : layer){
//                    System.out.println("TEST THE NODE " + node.getValue());
//                    System.out.println("TEST RESULT = " + node.canHaveChild(graph, element, processID));
//
//                    graph.printGraph();
//
//                    if (node.canHaveChild(graph, element, processID)){
//                        System.out.println("Node layer =" + (node.getLayer()+1));
//                        Node tempNode = new Node(node, element, (node.getLayer()+1), processID);
//                        graph.addNodeWhenIterating(tempNode, layersItr);
//                    }
//                }
//            }
//        }
//        ArrayList<Node> layer3 = graph.getNodesAtLayer(3);
//        for (Node node : layer3) {
//            System.out.print("\n[");
//            System.out.print(node.getValue());
//            System.out.print(",");
//            Node parent = node;
//            for (int i = 2 ; i >0 ; i-- ){
//                System.out.print(parent.getValue() + ",");
//                parent = parent.getParent();
//            }
//            System.out.print("]");
//
//        }
//        graph.printGraph();
//        return graph.getNodesQtyAtLayer(3);
//    }
//
//    public static int[] getDescSortedArray(int[] array){
//        Arrays.sort(array);
//        int length = array.length;
//        int[] descSorted = new int[length];
//        for (int element : array){ descSorted[--length] = element; }
//        return descSorted;
//    }
//
//    public static void main(String[] args) {
//        System.out.println(Solution.solution(new int[]{4, 21, 7, 14, 56, 8, 56, 4, 42}));
//    }
//}
//
//class Graph{
//    private ArrayList<ArrayList<Node>> layers;
//
//    // Constructor
//    Graph(ArrayList<ArrayList<Node>> layersOfNodes) {
//        this.layers = layersOfNodes;
//    }
//
//    // getter
//    public ArrayList<ArrayList<Node>> getLayers() { return layers;}
//
//    // methods
//    public void addNodeWhenIterating(Node node, ListIterator<ArrayList<Node>> itr){
//        System.out.println("node parent = " + node.getParent().getValue());
//        System.out.println("node curr = " + node.getValue());
//
//        int layer = node.getLayer();
//        int size = layers.size();
//        if (layer >= size){
//            System.out.println("====================Created new level");
//            itr.add(new ArrayList<>());
//        }
//
//        System.out.println("Added " + node.getValue() + " for parent" + node.getParent().getValue() + " at layer" + node.getLayer());
//        layers.get(layer).add(node);
//    }
//
//    public void addNode(Node node){
//        int layer = node.getLayer();
//        int size = layers.size();
//        if (layer >= size){ layers.add(new ArrayList<>()); }
//        layers.get(layer).add(node);
//    }
//
//    public void addLayerWhenIterating(ListIterator<ArrayList<Node>> itr){
//        itr.add(new ArrayList<>());
//    }
//
//    public ArrayList<Node> getNodesAtLayer(int layer) {
//        System.out.println("Layer err = " + layer);
////        System.out.println("Layer is " + layer);
//        return layers.get(layer);
//    }
//    public int lastLayer(){ return layers.size()-1;}
//    public int getNodesQtyAtLayer(int layer){ return getNodesAtLayer(layer).size(); }
//    public void printGraph(){
//        int i = 0;
//        for (ArrayList<Node> row : layers){
//            System.out.print("\nLevel " + i++ + "= [");
//            for (Node node : row){
//                int parent = 0;
//                try {
//                    parent = node.getParent().getValue();
//                }catch (NullPointerException e){
//                    parent = -1;
//                }
//                System.out.print(node.getValue()+ "(P" + parent  + "), ");
//            }
//        }
//        System.out.println("]");
//    }
//}
//
//class Node{
//    private static int max = 0;
//    private Node parent; // Link to parent (optional)
//    private final int id = max++;
//    private final int value;
//    private final int layer;
//    private final int processID;
//
//
//    // constructors
//    public Node(int value) {    // root node
//        this.value = value;
//        this.layer = 0;
//        this.processID = 0;
//    }
//    public Node(Node parent, int value, int layer, int processID) {    // child node
//        this.parent = parent;
//        this.value = value;
//        this.layer = layer;
//        this.processID = processID;
//    }
//
//
//    // methods
//    public boolean canHaveChild(Graph graph, int newValue, int newProcessID){
//        boolean smallerOrEqual = (newValue <= value);
//        System.out.println("smallerOrEqual= " + smallerOrEqual );
//
//        boolean noRemainder = (value % newValue == 0);
//        System.out.println("noRemainder= " + noRemainder );
//
//        boolean isUniqueChild = !childrenExistValue(graph, newValue);
//        System.out.println("isUniqueChild= " + isUniqueChild );
//
//        boolean nodeIsNotProcessed = !nodeIsProcessed(newProcessID);
//        System.out.println("Node = " + value + " processID = " + this.processID + " new process = " + newProcessID);
//
//        if (layer == 0){ return  isUniqueChild; }
////        System.out.println("\nScreening==============================================");
////        System.out.println("smallerOrEqual = " + smallerOrEqual);
////        System.out.println("noRemainder =" + noRemainder);
////        System.out.println("isUniqueChild =" + isUniqueChild);
////        System.out.println("childNotAddedLastRound =" + childNotAddedLastRound);
//        return smallerOrEqual && noRemainder && isUniqueChild && nodeIsNotProcessed;
//    }
//    public int getLayer(){ return layer; }
//    public int getValue(){ return value; }
//    public Node getParent(){ return parent; }
//    public int getId() { return id; }
//    public boolean nodeIsProcessed(int newProcessID){ return processID == newProcessID; }
//    public ArrayList<Node> getChildren(Graph graph){
//        ArrayList<Node> children = new ArrayList<>();
//        if (layer == graph.lastLayer()){ // is last layer
//            return children;
//        } else {
//            System.out.println("layer+1 = " + (layer+1));
//            ArrayList<Node> nextLayerNode = graph.getNodesAtLayer(layer+1);
//            for (Node node : nextLayerNode){
//                if (node.getParent().getId() == id){ children.add(node); }
//            }
//        }
//        System.out.println("Children detected===============================================");
//        for (Node child : children){
//            System.out.println(child.getValue());
//        }
//        System.out.println();
//        return  children;
//    }
//    public boolean childrenExistValue(Graph graph, int newValue){
//        ArrayList<Node> children = getChildren(graph);
//        for (Node child: children){
////            System.out.println("Sibling value is" + child.getValue());
//            if (child.getValue() == newValue){
////                System.out.println("Similar value detected");
//                return true;
//            }
//        }
//        return false;
//    }
//
//}