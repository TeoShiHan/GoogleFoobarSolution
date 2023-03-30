package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static int[] solution(int number) {
        ArrayList<Integer> solarPanelSetup = new ArrayList<Integer>();
        State state = new State(number);

        while (state.haveSolarPanelLeft()){
            solarPanelSetup.add(state.getNextUsage());
            state.setUpSolarPanel();
        }
        int[] arr =  solarPanelSetup.stream().mapToInt(i -> i).toArray();
        return arr;
    }

    private static class State{
        int unusedSolarPanel;

        State(int unusedSolarPanel){
            this.unusedSolarPanel = unusedSolarPanel;
        }

        public int getNextUsage(){
            int num = (int) Math.pow(unusedSolarPanel, 0.5);
            return (int) Math.pow(num, 2);
        }

        public boolean haveSolarPanelLeft(){
            return unusedSolarPanel != 0;
        }

        public void setUpSolarPanel(){
            unusedSolarPanel -= getNextUsage();
        }
    }

    public static void main(String[] args) {
        System.out.println(solution(15324));
    }
}


