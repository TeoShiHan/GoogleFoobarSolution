package com.company;
import java.math.BigInteger;
public class SolutionTrue {
    public static String solution(int[] xs) {
        BigInteger result = new BigInteger("1");
        int xsLen = xs.length, pos = 0,ng=0;
        int[] negatives = new int[xsLen];
        if (xsLen == 1){
            return Integer.toString(xs[0]);
        }

        for (int n = 0;n < xsLen;n++){
            int val = xs[n];
            if (val == 0){
                continue;
            }
            if (val > 0){
                result = result.multiply(new BigInteger(Integer.toString(val)));
                ng++;
            } else {
                negatives[pos] = val;
                pos++;
            }
        }
        if(ng==0)
        {
            int l=0;
            return Integer.toString(l);
        }
        if ((pos % 2) == 0){

            for (int i = 0;i < pos;i++){
                result = result.multiply(new BigInteger(Integer.toString(negatives[i])));
            }
        } else {

            int min = -1000; int mPos = -1;
            for (int i = 0;i < pos;i++){
                if(negatives[i] > min){
                    min = negatives[i];
                    mPos = i;
                }
            }
            for (int j = 0;j < pos;j++){
                if(j == mPos){
                    continue;
                }
                result = result.multiply(new BigInteger(Integer.toString(negatives[j])));
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(Solution.solution(new int[]{-4,0}));
    }
}



