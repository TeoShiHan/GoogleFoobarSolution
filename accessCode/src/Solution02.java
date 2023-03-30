//import java.util.*;
//
//public class Solution02 {
//    public static int solution(int[] l) {
//        HashSet<String>luckyTriples = new HashSet<>();
//        Preprocessing data = new Preprocessing(l);
//        data.preprocessing();
//        Relation re = new Relation(data.hashedSet, data.sortedPairs);
//        for (pair pair : data.sortedPairs){
//            re.addValueToMultipleSets(pair);
//        }
//        re.printMap();
//        return 0;
//    }
//
//    public static void main(String[] args) {
//        Solution02.solution(new int[]{ 1, 1, 1});
//    }
//}
//
//class Relation{
//    pair[] sortedPairs;
//    Map<pair, HashSet<pair>> hashedSets;
//
//
//    public Relation(HashMap<pair, HashSet<pair>> hashedSet, pair[] sortedPairs) {
//        this.hashedSets = hashedSet;
//        this.sortedPairs = sortedPairs;
//    }
//
//
//    public void addValueToMultipleSets(pair pair){
//        Iterator<Map.Entry<pair, HashSet<pair>>> itr = hashedSets.entrySet().iterator();
//        while (itr.hasNext()){
//            Map.Entry<pair, HashSet<pair>> nextHash = itr.next();
//
//            boolean refIsNotSame = nextHash.getKey() != pair;
//            boolean isDivisible = (nextHash.getKey().value % pair.value == 0);
//
//            if (isDivisible && refIsNotSame){   //is divisible && not me
//                nextHash.getValue().add(pair);
//                nextHash.getKey().chainedQty++;
//            }
//        }
//    }
//
//
//    public void printMap(){
//        Iterator<Map.Entry<pair, HashSet<pair>>> itr = hashedSets.entrySet().iterator();
//        while (itr.hasNext()){
//            Map.Entry<pair, HashSet<pair>> entry = itr.next();
//            System.out.print(entry.getKey() + "\tval= " + entry.getKey().value + "\tchain= " + entry.getKey().chainedQty + " =[");
//            for (pair pair : entry.getValue()){
//                System.out.print(pair + "\tval= " + pair.value + "\tchain= " + pair.chainedQty +  " ,");
//            }
//            System.out.println(" ]");
//        }
//    }
//}
//
//class Pair{
//    int id;
//    int value;
//    int chainedQty;
//    Pair(int id, int value){
//        this.id = id;
//        this.value = value;
//        this.chainedQty = 0;
//    }
//}
//
//class Preprocessing{
//    pair[] sortedPairs;
//    HashMap<pair, HashSet<pair>> hashedSet = new HashMap<pair, HashSet<pair>>();
//    int[] data;
//
//    public Preprocessing(int[] data){ this.data = data; }
//
//    public void preprocessing(){
//        int length = data.length;
//        int id = 0;
//        Arrays.sort(data);
//        pair[] pairs = new pair[length];
//        int backwardIndex = length-1;
//
//        for (int i = 0 ; i < length ; i++) {
//            pair tempPair = new pair(id++, data[i]);
//            pairs[backwardIndex--] = tempPair;      // create new pair
////            if (isNotSameAsPrevious(i)) {
//            HashSet<pair> tempSet = new HashSet<>();    // create empty set
//            hashedSet.put(tempPair, tempSet);            // create a hash map with unique value as key
////            }
//        }
//        this.sortedPairs = pairs;
//    }
//
////    private boolean isNotSameAsPrevious(int i){
////        return !(i > 0 && data[i] == data[i - 1]);
////    }
//}