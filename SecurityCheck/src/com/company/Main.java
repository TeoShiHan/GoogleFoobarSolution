package com.company;

public class Main {


    public static int solution(int start, int length) {
        State state = new State(1, length+1, 1, length, start);
        int result = 0;

        while (!state.isEnd()){
            for (int i = 1 ; i < state.skipAt ; i++){
                result = result ^ state.xorNum++;
            }
            state.resetCursor();
            ++state.row;
            --state.skipAt;
            state.updateXORnum();
        }

        return result;

    }

    private static class State{
        int cursorPosition;
        int skipAt;
        int row;
        final int rowCount;
        int xorNum;
        int lazyXORNum;

        public State(int cursorPosition, int skipAt, int row, int rowCount, int xorNum) {
            this.cursorPosition = cursorPosition;
            this.skipAt = skipAt;
            this.row = row;
            this.rowCount = rowCount;
            this.xorNum = xorNum;
            this.lazyXORNum = xorNum;
        }

        public void resetCursor(){
            cursorPosition = 0;
        }

        public boolean isEnd(){
            return row == rowCount+1;
        }

        public void updateLazyXORnum(){
            lazyXORNum += rowCount;
        }

        public void updateXORnum(){
            updateLazyXORnum();
            xorNum = lazyXORNum;
        }
    }
}
