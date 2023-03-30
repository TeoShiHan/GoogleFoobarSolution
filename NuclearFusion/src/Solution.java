import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Solution {
    public static int solution(String x) {
        BigInteger num = new BigInteger(x);
        HashMap<BigInteger, Odd> oddHashMap = new HashMap<>();
        HashMap<BigInteger, Even>evenHashMap = new HashMap<>();
        int shortestPath = 0;

        if (isEven(num)){
            Even newEven = new Even(0, num);
            evenHashMap.put(newEven.value, newEven);    // map value with object
        } else {
            Odd newOdd = new Odd(0, num);
            oddHashMap.put(newOdd.value, newOdd);
        }

        boolean processIncomplete = true;

        while (processIncomplete){

            for (Map.Entry<BigInteger, Even> evenMap: evenHashMap.entrySet()){ // will skip if no even
                Even even = evenMap.getValue();
                Odd newOdd = even.createOdd();

                boolean oddNumberExistInMap = oddHashMap.containsKey(newOdd.value);

                if (oddNumberExistInMap){
                    int existingOddStep = oddHashMap.get(newOdd.value).step;
                    boolean newOddNumberIsShorter = newOdd.step < existingOddStep;

                    if (newOddNumberIsShorter){ // replace old odd with new odd with shorter steps
                        oddHashMap.put(newOdd.value, newOdd);
                    }
                    // else don't add because same value but step longer

                } else { // introduce new odd to map
                    oddHashMap.put(newOdd.value, newOdd);
                }
            }
            evenHashMap.clear();

            for (Map.Entry<BigInteger, Odd> oddMap: oddHashMap.entrySet()){ // will skip if no odd
                Odd odd = oddMap.getValue();

                if (odd.processComplete()){
                    processIncomplete = false;
                    shortestPath = oddHashMap.get(BigInteger.ONE).step;
                }

                ArrayList<Even> newEvens = odd.createEvens();
                for (Even even : newEvens){
                    boolean evenAlreadyExistInMap = evenHashMap.containsKey(even.value);

                    if (evenAlreadyExistInMap){
                        boolean newEvenIsShorter = even.step < evenHashMap.get(even.value).step;

                        if (newEvenIsShorter){  // replace old even with new even required shorter path
                            evenHashMap.put(even.value, even);
                        }

                    }else { // introduce new even into map
                        evenHashMap.put(even.value, even);
                    }
                }
            }
            oddHashMap.clear();
        }
        return shortestPath;
    }

    public static boolean  isEven(BigInteger number){
        return number.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }

}

class Odd{
    int step;
    BigInteger value;
    Odd(int step, BigInteger value){
        this.step = step;
        this.value = value;
    }

    public boolean processComplete(){
        return value.equals(BigInteger.valueOf(1));
    }

    public ArrayList<Even>createEvens(){
        ArrayList<Even> newEvens = new ArrayList<>();
        newEvens.add(new Even(step+1, value.add(BigInteger.ONE)));
        newEvens.add(new Even(step+1, value.subtract(BigInteger.ONE)));
        return newEvens;
    }
}

class Even{
    int step;
    BigInteger value;
    Even(int step, BigInteger value){
        this.step = step;
        this.value = value;
    }

    public Odd createOdd(){
        BigInteger newValue = value;
        while (isEven(newValue)){
            ++step;
            newValue = newValue.divide(BigInteger.TWO);
        }
        return new Odd(step, newValue);
    }

    public boolean isEven(BigInteger val){
        return val.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }
}