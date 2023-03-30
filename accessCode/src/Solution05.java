import java.util.ArrayList;

public class Solution05 {
    public static int solution(int[] l) {
        int tripleChainQty = 0;
        ArrayList<Val> values = new ArrayList<>();

        for (int integer : l){
            Val newVal = new Val(integer);
            values.add(newVal);

            for (Val value : values){
                if (newVal != value && newVal.val % value.val == 0) {
                    ++newVal.refQty;
                    // if this object refBy "Other objects" and this object ref new one => new triple chains
                    // how many new triple chains? => qty of refBy Objects
                    tripleChainQty += value.refQty;
                }
            }
        }
        return tripleChainQty;
    }
}

class Val{
    int val;
    int refQty =0;
    Val(int val){
        this.val = val;
    }
}