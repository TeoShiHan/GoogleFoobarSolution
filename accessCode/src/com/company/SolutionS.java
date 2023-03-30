package com.company;

public class SolutionS {
    public static int solution(int[] l) {
        int noOfCombinations = 0;
        int[] noOfDoubles = new int[l.length];

        // Count lucky doubles for each item in the array, except the first and last items
        for( int i = 1; i < l.length-1; ++i)
        {
            for( int j = 0; j < i; ++j)
            {
                if( l[i] % l[j] == 0)
                    ++noOfDoubles[i];
            }
        }

        // Count lucky triples
        for( int i = 2; i < l.length; i++)
        {
            for( int j = 1; j < i; ++j)
            {
                if( l[i] % l[j] == 0)
                    noOfCombinations += noOfDoubles[j];
            }
        }
        return noOfCombinations;
    }

    public static void main(String[] args) {
        System.out.print(SolutionS.solution(new int[]{6, 5, 4, 3, 2, 1}));
    }
}
