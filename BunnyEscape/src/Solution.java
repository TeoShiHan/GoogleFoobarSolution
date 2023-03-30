import java.util.ArrayList;

public class Solution {

    public static int solution(int[][] map) {
        int level = 1;
        boolean notReachDest = true;
        Node rootNode = new Node(new VisitedNodesDetails(), new Map(map), "0000");
        ArrayList<Node> currentLevelNode = rootNode.nextVisitableChildren();
        while (notReachDest){
            ++level;
            ArrayList<Node> nextLevelNode = new ArrayList<>();
            for(Node node : currentLevelNode){
                nextLevelNode.addAll(node.nextVisitableChildren());
                if (node.isDestination()){ notReachDest = false;}
            }
            currentLevelNode = nextLevelNode;
        }
        return level;
    }
}


class Node{
    private static VisitedNodesDetails visitedNodeDetails;
    private static Map map;
    private final String id, reachBy;
    private final int val;
    private Node parentNode;
    private final boolean parentRemovedWall;


    // Constructors
    public Node(VisitedNodesDetails visitedNodeDetails, Map map, String id){  // for root nodes
        Node.visitedNodeDetails = visitedNodeDetails;
        Node.map = map;
        this.id = id;
        this.val = getValueFromID(id);
        this.parentRemovedWall = false;
        this.reachBy = "WITHOUT REMOVED WALL";
        visitedNodeDetails.add(new String[]{id,"WITHOUT REMOVED WALL"});
    }
    public Node(String id, Node parentNode){   // for child nodes
        this.id = id;
        this.parentNode = parentNode;
        this.parentRemovedWall = parentNode.isWall() || parentNode.parentRemovedWall;  // inherit from parent node
        this.val = getValueFromID(id);
        this.reachBy = parentRemovedWall? "REMOVED WALL":"WITHOUT REMOVED WALL";
    }


    // methods
    public String[] generateAdjacentIDs(){
        int row = convertIDtoRowAndCol(id)[0];
        int col = convertIDtoRowAndCol(id)[1];
        return new String[]{
                toSig2(row) + toSig2(col-1), // left
                toSig2(row-1) + toSig2(col), // up
                toSig2(row) + toSig2(col+1), // right
                toSig2(row+1) + toSig2(col)  // down
        };
    }
    public ArrayList<String> getIdsToVisitNext(){
        ArrayList<String> validIDs = new ArrayList<>();
        String[] ids = generateAdjacentIDs();
        if (isNotDeadEnd()) { // ignore dead end adjacent
            for (String id : ids) {
                boolean isPrevious = parentNode != null && parentNode.id.equals(id);

                // acceptable when visit is "removed wall" and current node is "not removed wall"
                // only this happens it possible to update visit with "not removed wall"

                boolean detailsAcceptable = visitedNodeDetails.idAcceptable(id, reachBy);
                boolean idValid = !(isOutOfBound(id) || isPrevious);
                if (idValid && detailsAcceptable) { validIDs.add(id); }
            }
        }
        return validIDs;
    }
    public ArrayList<Node> nextVisitableChildren(){
        ArrayList<Node> childNodes = new ArrayList<>();
        ArrayList<String> ids = getIdsToVisitNext();
        for(String id: ids){
            Node tempNode = new Node(id, this);
            childNodes.add(tempNode);
        }
        markNodesWithVisitDetails(childNodes);
        return childNodes;
    }
    public boolean isDestination(){
        int destRow = map.getHeight() - 1;
        int destCol = map.getWidth() - 1;
        return this.id.equals(toSig2(destRow) + toSig2(destCol));
    }
    public boolean isNotDeadEnd(){
        boolean thisNodeIsWall = isWall();
        return !(thisNodeIsWall && parentRemovedWall);
    }
    public boolean isWall(){ return val == 1; }


    // static methods
    public static String toSig2(int number){ return String.format("%02d", number); }
    public static int toInt(String numString){ return Integer.parseInt(numString); }
    public static int[] convertIDtoRowAndCol(String id){
        int row = toInt(id.substring(0,2));
        int col = toInt(id.substring(2));
        return new int[] {row,col};
    }
    public static boolean isOutOfBound(String id){
        int[] rowAndCol = convertIDtoRowAndCol(id);
        int rowIndex = rowAndCol[0];
        int colIndex = rowAndCol[1];
        boolean containNegatives = id.contains("-");
        boolean exceedMapWidth = colIndex >= map.getWidth();
        boolean exceedMapHeight = rowIndex >= map.getHeight();
        return containNegatives || exceedMapHeight || exceedMapWidth;
    }
    public static int getValueFromID(String id){
        int[] rowAndCol = convertIDtoRowAndCol(id);
        int row = rowAndCol[0];
        int col = rowAndCol[1];
        return map.getMap()[row][col];
    }
    public static void markNodesWithVisitDetails(ArrayList<Node> nodes){
        for (Node node : nodes){
            if (visitedNodeDetails.contains(node.id)){
                visitedNodeDetails.update(node.id, node.reachBy);
            } else {
                visitedNodeDetails.add(new String[]{node.id, node.reachBy});
            }
        }
    }
}


class Map{
    private final int[][] map;
    public int getHeight(){ return map.length;}
    public int getWidth(){ return map[0].length;}
    public int[][] getMap(){ return map; }
    public Map(int[][] map){this.map = map;}
}


class VisitedNodesDetails{
    ArrayList<String[]> visitedNodeDetails  = new ArrayList<>();
    public void add(String[] record){ visitedNodeDetails.add(record); }
    public void update(String id, String details){
        for (String[] record: visitedNodeDetails){
            if (record[0].equals(id)){
                record[1] = details;
            }
        }
    }
    public boolean contains(String id){
        for (String[] record: visitedNodeDetails){
            if (record[0].equals(id)){return true;}
        }
        return false;
    }
    public String getVisitDetails(String id){
        for (String[] node: visitedNodeDetails){
            if (node[0].equals(id)){ return node[1]; }
        }
        return "No record";
    }
    public boolean idAcceptable(String id, String details){
        boolean acceptable = false;
        if (contains(id)){
            if (getVisitDetails(id).equals("REMOVED WALL")){
                // have possibility to update from removed wall to no removed wall
                if (details.equals("WITHOUT REMOVED WALL")){ acceptable =  true;}
            }
        } else { acceptable = true; }
        return acceptable;
    }
}