import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

public class Solution {

    public static int[] solution(int[][] item){
        Fraction one = new Fraction(1, 1);
        Fraction zero = new Fraction(0, 0);
        MatrixProperties p = new MatrixProperties(item);
        Fraction[][] IminusQ = new Fraction[p.qMatrixHead.size()][p.qMatrixHead.size()];


        if (p.absorbingState.size() == 0){
            int[] array = new int[item.length-1];
            return array;
        }

        for (int i = 0 ; i < p.qMatrixHead.size() ; i++){
            for (int j = 0 ; j < p.qMatrixHead.size() ; j++){
                int rowID = p.qMatrixHead.get(i);
                int colID = p.qMatrixHead.get(j);
                FractionRow pRow = p.transitionProbability.get(rowID);
                Fraction probability = new Fraction(pRow.numerators[colID], pRow.commonDenominator);
                IminusQ[i][j] = (i == j)? one.subtract(probability) : zero.subtract(probability);
            }
        }


        InverseMatrix inverseMatrix = new InverseMatrix(IminusQ);
        Fraction[][] IminusQInverse =  inverseMatrix.getInverse();
        Fraction[][] rMatrix = new Fraction[p.qMatrixHead.size()][p.absorbingState.size()];


        for (int i = 0 ; i < p.qMatrixHead.size() ; i++){
            for (int j = 0 ; j < p.absorbingState.size() ; j++){
                int row = p.qMatrixHead.get(i);
                int col = p.absorbingState.get(j);
                FractionRow probabilities = p.transitionProbability.get(row);
                int probability = probabilities.numerators[col];
                Fraction probabilityFraction = new Fraction(probability, probabilities.commonDenominator);
                rMatrix[i][j] = probabilityFraction;
            }
        }


        Fraction[][] abProMatrix = new Fraction[IminusQInverse.length][rMatrix[0].length];

        // multiply R matrix with I-Q matrix
        for (int i = 0 ; i < p.qMatrixHead.size() ; i++){
            for (int j = 0 ; j < rMatrix[0].length ; j++){
                Fraction f = multiplyRowCol(IminusQInverse, i, rMatrix, j, IminusQInverse.length);
                abProMatrix[i][j] = f;
            }
        }



        int commonDenominator = FractionRow.getRowLCM(abProMatrix[0]);
        int[]answer = new int[item.length-1];

        // fill absorbing state into answer array
        for (int i=0 ; i < p.absorbingState.size() ; i++){
            int absorbingState = p.absorbingState.get(i);
            Fraction itsProbability = abProMatrix[0][i];
            int numerator;
            if (itsProbability.denominator == commonDenominator){
                numerator = itsProbability.numerator;
            }else{
                numerator = itsProbability.numerator*((commonDenominator/itsProbability.denominator));
            }
            answer[absorbingState-2] = numerator;
        }

        // last element as common denominator
        answer[answer.length-1] = commonDenominator;

        //System.out.println(Arrays.toString(answer));
        return answer;
    }


    private static Fraction multiplyRowCol(Fraction[][]rowSource, int rowIndex, Fraction[][]colSource, int colIndex, int colHeight){
        Fraction sum = new Fraction(0, 0);
        Fraction[] fRow = rowSource[rowIndex];

        for (int i = 0 ; i < colHeight ; i++){
            Fraction colFraction = colSource[i][colIndex];
            Fraction multiply = colFraction.multiply(fRow[i]);
            sum = sum.add(multiply);
        }

        return sum;
    }

    private static class MatrixProperties {
        LinkedHashSet<Integer> reachableState = new LinkedHashSet<>();
        ArrayList<Integer> absorbingState = new  ArrayList<>();
        ArrayList<Integer> qMatrixHead = new ArrayList<>();
        LinkedHashMap<Integer, FractionRow> transitionProbability = new LinkedHashMap<>();
        final int[] allZeroTemplate;
        int[][] originalMatrix;

        MatrixProperties(int[][] matrix) {
            allZeroTemplate = new int[matrix.length];
            originalMatrix = matrix;
            analyzeMatrix(matrix);
        }

        private void analyzeMatrix(int[][] matrix){

            ArrayDeque<Integer> nextReachableStates = new ArrayDeque<>();
            nextReachableStates.add(0);
            reachableState.add(0);

            while (!nextReachableStates.isEmpty()){

                for (int state : nextReachableStates){

                    int[] stateChangeProbability = matrix[state];
                    int zeroCount = 0, rowSum = 0;

                    FractionRow fracRow = new FractionRow(matrix.length);

                    for (int i = 0 ; i < stateChangeProbability.length ; i++){

                        rowSum += stateChangeProbability[i];
                        fracRow.addNumerator(stateChangeProbability[i]);

                        if (stateChangeProbability[i] == 0){
                            ++zeroCount;
                        }

                        if (!reachableState.contains(i) && stateChangeProbability[i] != 0){
                            nextReachableStates.add(i);
                            reachableState.add(i);
                        }

                    }

                    this.transitionProbability.put(state, fracRow);
                    fracRow.setDenominator(rowSum);

                    if (isAbsorbedState(state, stateChangeProbability, zeroCount)){
                        absorbingState.add(state);
                        fracRow.setDenominator(1);
                        fracRow.numerators[state] = 1;
                    }else if (reachableState.contains(state)){
                        qMatrixHead.add(state);
                    }

                    nextReachableStates.remove();
                }
            }
        }

        private boolean _1AtRightPlace(int stateNo, int[] stateArr){
            return stateArr[stateNo] == 1;
        }

        private boolean allElementIsZero(int[] stateArr){
            return Arrays.equals(stateArr, allZeroTemplate);
        }

        private boolean isAbsorbedState(int stateNo, int[] stateArr, int zeroCount) {
            return (zeroCount == originalMatrix[0].length && _1AtRightPlace(stateNo, stateArr)) ||
                    allElementIsZero(stateArr);
        }

        private String print2dArr(int[][] arr){
            StringBuilder arrStr = new StringBuilder("{");
            for (int[] row : arr){
                arrStr.append(", ").append(Arrays.toString(row));
            }
            return arrStr + "}";
        }

        private String printFracRowMap(){
            StringBuilder str = new StringBuilder();
            Set<Integer> keySet = transitionProbability.keySet();
            for (int e : keySet){
                FractionRow r = transitionProbability.get(e);
                str.append("\n").append("{").append(e).append(" : ").append(r.toString()).append("}");
            }
            return str.toString();
        }

        public String toString(){
            return
                    "reachable state = " + reachableState.toString() + "\n" +
                            "absorbed state = "  + absorbingState.toString() + "\n" +
                            "original matrix = " + print2dArr(originalMatrix) + "\n" +
                            "row Map = " + printFracRowMap() + "\n" +
                            "Q head = " + qMatrixHead;
        }
    }

    private static class FractionRow{
        int commonDenominator;
        int[] numerators;
        int offset;

        FractionRow(int length){
            this.offset = 0;
            this.numerators = new int[length];
        }

        public void addNumerator(int numerator){
            this.numerators[offset++] = numerator;
        }

        public void setDenominator(int denominator){
            this.commonDenominator = denominator;
        }

        private static int getRowLCM(Fraction[] row){
            if (row.length > 1){
                int rowLCM = Fraction.LCM(row[0].denominator, row[1].denominator);
                for (int i = 2 ; i < row.length-1 ; i++){
                    rowLCM = Fraction.LCM(rowLCM, row[i].denominator);
                }
                //System.out.println(rowLCM);
                return rowLCM;
            }else if (row.length == 1){
                return row[0].denominator;
            }else {
                return -1;
            }
        }

        public String toString(){
            StringBuilder str = new StringBuilder();
            for (int f : numerators){
                Fraction fr = new Fraction(f, commonDenominator);
                fr.simplifyFraction();
                str.append(fr).append(", ");
            }
            return str.toString();
        }
    }

    static class Fraction{
        int numerator;
        int denominator;

        public Fraction(int numerator,int denominator){
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public String toString(){
            return numerator + "/" + denominator;
        }

        public Fraction multiply(Fraction fraction){
            int newNumerator = fraction.numerator * numerator;
            int newDenominator = fraction.denominator * denominator;
            Fraction f = new Fraction(newNumerator, newDenominator);
            f.simplifyFraction();
            return f;
        }

        public Fraction subtract(Fraction fraction){
            if (this.isZero()){
                return fraction.multiply(new Fraction(-1, 1));
            }else if (fraction.isZero()){
                return this;
            }else {
                int LCM = LCM(fraction.denominator, denominator);
                int newNumerator = (numerator * LCM / denominator) - (fraction.numerator * LCM / fraction.denominator);
                Fraction frac = new Fraction(newNumerator, LCM);
                frac.simplifyFraction();
                return frac;
            }
        }

        public Fraction add(Fraction fraction){
            if (this.isZero()){
                return fraction;
            }else if (fraction.isZero()){
                return this;
            }else {
                Fraction f = this.subtract(fraction.multiply(new Fraction(-1, 1)));
                f.simplifyFraction();
                return f;
            }
        }

        public Fraction divide(Fraction fraction){
            Fraction divideResult = this.multiply(new Fraction(fraction.denominator,fraction.numerator));
            divideResult.simplifyFraction();
            return divideResult;

        }

        public void simplifyFraction(){
            if (!this.isZero()){
                int gcd = GCD(denominator, numerator);
                numerator = numerator/ gcd;
                denominator = denominator/gcd;
            }
            if ((denominator < 0) && (numerator < 0) || (denominator < 0) && (numerator> 0)){
                numerator = numerator *-1;
                denominator = denominator*-1;
            }
        }

        public static int GCD(int numerator, int denominator){
            if (denominator == 0){
                return numerator;
            }
            return GCD(denominator, numerator%denominator);
        }

        public static int LCM(int num1, int num2){
            return (num1 * num2 / GCD(num1, num2));
        }

        public boolean isZero(){
            return this.numerator == 0 || this.denominator == 0;
        }
    }

    private static class InverseMatrix {
        Fraction[][] oriMatrix;
        Fraction[][] augMatrix;

        FLCondition con = (c, s) -> Objects.equals(s, "below diagonal") ? c < oriMatrix.length : c >= 0;
        FLInit i = (c, s) -> Objects.equals(s, "below diagonal") ? c+1 : c-1;
        FLCounter counter = (c, s) -> Objects.equals(s, "below diagonal")? c+1 : c-1;


        InverseMatrix(Fraction[][] oriMatrix){
            this.oriMatrix = oriMatrix;
            this.augMatrix = new Fraction[oriMatrix.length][oriMatrix.length];

            for (int i = 0 ; i < oriMatrix.length ; i++){
                for (int j = 0 ; j < oriMatrix.length ; j++){
                    if (i == j){
                        augMatrix[i][j] = new Fraction(1, 1);
                    }else {
                        augMatrix[i][j] = new Fraction(0, 0);
                    }
                }
            }
        }


        public Fraction[][] getInverse() {

            // settle bottom left corner
            for (int col = 0 ; col < oriMatrix.length-1 ; col++){ // exclude last column
                convertPivotTo0(col, "below diagonal");
            }

            // settle top right corner
            for (int col = oriMatrix.length-1 ; col >= 0 ; col--){ // exclude last column
                convertPivotTo0(col, "above diagonal");
            }


            for (int row = 0 ; row < oriMatrix.length ; row++){
                Fraction toDivide = oriMatrix[row][row];

                for (int col= 0 ; col < oriMatrix.length ; col++){
                    oriMatrix[row][col] = oriMatrix[row][col].divide(toDivide);
                    augMatrix[row][col] = augMatrix[row][col].divide(toDivide);
                }

            }

            return augMatrix;

        }

        public void convertPivotTo0(int diagonalPosition, String direction){
            Fraction diagonal = oriMatrix[diagonalPosition][diagonalPosition];
            if (diagonal.isZero()){
                Integer nonZeroRowIndex = findNonZeroRowFromDiagonal(direction, diagonalPosition, oriMatrix);
                if (nonZeroRowIndex!=null){
                    swapZeroDiagonalWithNonZeroDiagonal(nonZeroRowIndex, diagonalPosition);
                    formZeroFromDiagonal(direction, diagonalPosition);
                }
            }else {
                formZeroFromDiagonal(direction, diagonalPosition);
            }
        }

        public Integer findNonZeroRowFromDiagonal(String direction, int diagonalPosition, Fraction[][] matrix){
            Integer nonZeroRowIndex = null;
            for (int k = i.init(diagonalPosition, direction) ;
                 con.loop(k, direction) ;
                 k = counter.progress(k, direction))
            {
                if (!matrix[k][diagonalPosition].isZero()) {
                    nonZeroRowIndex = k;
                    break;
                }
            }
            return nonZeroRowIndex;
        }

        public static Fraction getConstantToFormZero(Fraction number, Fraction agent){
            return number.multiply(new Fraction(-1, 1)).divide(agent);
        }


        public void formZeroFromDiagonal(String direction, int diagonalCol){
            for (int k = i.init(diagonalCol, direction);
                 con.loop(k, direction) && !oriMatrix[k][diagonalCol].isZero();
                 k = counter.progress(k, direction)) {

                Fraction additionConstant = getConstantToFormZero(oriMatrix[k][diagonalCol], oriMatrix[diagonalCol][diagonalCol]);


                for (int j = 0 ; j < oriMatrix.length ; j++){

                    Fraction f1 = oriMatrix[diagonalCol][j].multiply(additionConstant);
                    Fraction f2 = augMatrix[diagonalCol][j].multiply(additionConstant);


                    oriMatrix[k][j] = oriMatrix[k][j].add(f1);
                    augMatrix[k][j] = augMatrix[k][j].add(f2);


                }



            }
        }

        public void swapZeroDiagonalWithNonZeroDiagonal(int nonZeroRowIndex, int diagonalRowIndex){
            Fraction[] temp = oriMatrix[nonZeroRowIndex];
            oriMatrix[nonZeroRowIndex] = oriMatrix[diagonalRowIndex];
            oriMatrix[diagonalRowIndex] = temp;

            Fraction[] temp2 = augMatrix[nonZeroRowIndex];
            augMatrix[nonZeroRowIndex] = augMatrix[diagonalRowIndex];
            augMatrix[diagonalRowIndex] = temp2;
        }

        private interface FLCondition{ boolean loop(int counter, String direction);}

        private interface FLCounter{ int progress(int counter, String direction); }

        private interface FLInit{ int init(int counter, String direction); }

    }
}



