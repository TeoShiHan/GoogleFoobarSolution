//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Collections;
//
//public class Solution03 {
//    public static int solution(int[] l) {
//        ArrayList<Integer> descSorted = sortDescAndTruncateRedundancy(l, 3);
//        HashMap<Integer,Chain> chainTable = new HashMap<Integer,Chain>() ;
//        HashSet<String> luckyTriplets = new HashSet<>();
//
//        for (int val : descSorted){
//            boolean added = false;
//            for (int key : chainTable.keySet()){
//                if (key % val == 0){    // is divisible
//                    Chain chain = chainTable.get(key);
//                    chain.add(val);
//                    if (chain.strGenerateAble()){
//                        String luckyTriple = chain.generateString();
//                        luckyTriplets.add(luckyTriple);
//                    }
//                    added = true;
//                }
//            }
//            if (!added){
//                ArrayList<Integer> chain = new ArrayList<>();
//                chain.add(val);
//                Chain newChain = new Chain(chain);
//                chainTable.put(val, newChain);
//            }
//        }
//        printSet(luckyTriplets);
//        return luckyTriplets.size();
//    }
//    public static ArrayList<Integer> sortDescAndTruncateRedundancy(int[] array, int maxRepeat){
//        Arrays.sort(array);
//        int length = array.length;
//        int repeat = 1;
//        ArrayList<Integer>truncatedAndSorted = new ArrayList<>();
//        int[] descSorted = new int[length];
//        for (int i = 0 ; i < length ; i++){
//            repeat = i>0 && array[i] == array[i-1]? repeat+1 : 1;
//            if (repeat <= maxRepeat){
//                truncatedAndSorted.add(array[i]);
//            }
//        }
//        truncatedAndSorted.sort(Collections.reverseOrder());
//        System.out.print(truncatedAndSorted);
//
//        return truncatedAndSorted;
//    }
//
//    public static void printSet(HashSet<String> luckyTriplets){
//        for (String triplet : luckyTriplets){
//            System.out.println(triplet);
//        }
//    }
//
//    public static void main(String[] args) {
//        System.out.print(Solution03.solution(new int[]{4, 21, 7, 14, 56, 8, 56, 4, 42}));
//    }
//
//
//}
//
//class Chain{
//    int [] triplesTracker = new int[]{0,1,2};
//    ArrayList<Integer>chain;
//
//    public Chain(ArrayList<Integer>chain){
//        this.chain = chain;
//    }
//
//    public void add(int val){
//        chain.add(val);
//    }
//
//    public String generateString(){
//        int a = chain.get(triplesTracker[0]++);
//        int b = chain.get(triplesTracker[1]++);
//        int c = chain.get(triplesTracker[2]++);
//        int[]luckyTriples = new int[]{a,b,c};
//        return Arrays.toString(luckyTriples);
//    }
//
//    boolean strGenerateAble(){
//        return chain.size() >= 3;
//    };
//}
