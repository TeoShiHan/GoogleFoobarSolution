package com.company;

public class SolutionCompare {
    public static void main(String[] args) {

        while (true){
            int[] arr = new int[10];

            for (int i = 0; i < arr.length; i++) {
                int number = (int) ((Math.random() * (1000 - -1000)) + -1000);
                arr[i] = number; // storing random integers in an array
            }

            System.out.println("Array is:");
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i] + " ");
            }

            String correctString = SolutionTrue.solution(arr);
            String incorrectString =  Solution.solution(arr);
            System.out.println("\nCorrect   is " + correctString);
            System.out.println("Incorrect is " + incorrectString);

            if (!correctString.equals(incorrectString)){

                System.out.println("Unmatched detected==============================");
                System.out.println("Unmatched detected=============================");
                System.out.println("Unmatched detected==============================");
                System.out.println("Unmatched detected=============================");

                System.out.println("\n" + "Corr = " + correctString);
                System.out.println("Incorr = " + incorrectString);
            }

        }
    }
}
