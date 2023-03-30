import java.math.BigInteger;
import java.util.LinkedHashSet;

public class Solution {
    private static final Fraction ONE =new Fraction(BigInteger.ONE, BigInteger.ONE);
    private static final Fraction ZERO =new Fraction(BigInteger.ZERO, BigInteger.ONE);
    private static final Fraction N_ONE =new Fraction(BigInteger.valueOf(-1), BigInteger.ONE);

    public static int[] solution(int[][] item) {
        if (s0isTerminating(item)){ return s0TerminateMatrix(item); }
        Aggregation agg = getTerminalNonTerminalAndProbabilityMatrix(item);
        Fraction[][] probabilities = agg.probabiliryMatrix;
        Fraction[][] IminusQmatrix = getIminusQmatrix(agg.nonTerminal, probabilities);
        Fraction[][] Nmatrix = (new InverseMatrix(IminusQmatrix)).getInverseMatrix();
        Fraction[][] Rmatrix = getRmatrix(agg.terminal, agg.nonTerminal, probabilities);
        Fraction[][] NRmatrix = getNRmatrix(Nmatrix, Rmatrix, agg.nonTerminal);
        return getAnsMatrix(NRmatrix);
    }


    public static BigInteger Int(int number){
        return BigInteger.valueOf(number);
    }
    public static boolean s0isTerminating(int[][] item){
        return getRowSum(item, 0) == 0;
    }


    // aggregations
    private static Aggregation getTerminalNonTerminalAndProbabilityMatrix(int[][] item){
        LinkedHashSet<Integer> terminal = new LinkedHashSet<>();
        LinkedHashSet<Integer> nonTerminal = new LinkedHashSet<>();
        Fraction[][] probabilities = new Fraction[item.length][item.length];

        // aggregate terminal and non-terminal + form fraction matrix
        for (int row = 0 ; row < item.length ; row++){

            int rowSum = getRowSum(item, row);

            if (rowSum == 0){ // is terminal
                item[row][row] = 1 ; terminal.add(row) ; rowSum = 1;
            }else { // not terminal
                nonTerminal.add(row);
            }

            Fraction[] rowFraction = getRowFraction(item, row, rowSum);
            probabilities[row] = rowFraction;
        }

        return new Aggregation(
                terminal.stream().mapToInt(i->i).toArray(),
                nonTerminal.stream().mapToInt(i->i).toArray(),
                probabilities, terminal, nonTerminal);
    }
    private static int getRowSum(int[][] item, int row){
        int rowSum = 0;
        for (int j = 0 ; j < item.length ; j++) {
            rowSum += item[row][j];
        }
        return rowSum;
    }
    public static Fraction[] getRowFraction(int[][]item, int rowNum, int rowSum){
        Fraction[] fractionRow = new Fraction[item.length];
        for (int column = 0 ; column < item.length ; column++){
            Fraction fraction = new Fraction(Int(item[rowNum][column]), Int(rowSum));
            fractionRow[column] = fraction;
        }
        return fractionRow;
    }


    // Matrix operations
    private static Fraction multiplyRowCol(Fraction[][]rowSource, int rowIndex, Fraction[][]colSource, int colIndex, int colHeight) {
        Fraction sum = ZERO;
        Fraction[] fRow = rowSource[rowIndex];

        for (int i = 0 ; i < colHeight ; i++){
            Fraction colFraction = colSource[i][colIndex];
            Fraction multiply = colFraction.multiply(fRow[i]);
            sum = sum.add(multiply);
        }

        return sum;
    }


    // absorbing probability procedure
    private static Fraction[][] getIminusQmatrix(int[] nonTerminalStates, Fraction[][] probabilities){
        Fraction[][] IminusQmatrix = new Fraction[nonTerminalStates.length][nonTerminalStates.length];
        for (int i = 0 ; i < nonTerminalStates.length ; i++){
            for (int j = 0 ; j < nonTerminalStates.length ; j++){

                int rowStateID = nonTerminalStates[i];
                int colStateID = nonTerminalStates[j];

                if (i == j){
                    IminusQmatrix[i][j] = ONE.subtract(probabilities[rowStateID][colStateID]);
                }else {
                    IminusQmatrix[i][j] = ZERO.subtract(probabilities[rowStateID][colStateID]);
                }

            }
        }
        return IminusQmatrix;
    }
    private static Fraction[][] getRmatrix(int[] terminals, int[] nonTerminals, Fraction[][] probabilities){
        Fraction[][] Rmatrix = new Fraction[nonTerminals.length][terminals.length];
        for (int i = 0 ; i < nonTerminals.length ; i++){
            int nonTer = nonTerminals[i];
            for (int j = 0 ; j < terminals.length ; j++){
                int ter = terminals[j];
                Rmatrix[i][j] = probabilities[nonTer][ter];
            }
        }
        return Rmatrix;
    }
    private static Fraction[][] getNRmatrix(Fraction[][]Nmatrix, Fraction[][]Rmatrix, int[]nonTerminal){
        Fraction[][] NRmatrix = new Fraction[Nmatrix.length][Rmatrix[0].length];
        for (int i = 0 ; i < Nmatrix.length ; i++){
            for (int j = 0 ; j < Rmatrix[0].length ; j++){
                NRmatrix[i][j] = multiplyRowCol(Nmatrix, i, Rmatrix, j, nonTerminal.length);
            }
        }
        return NRmatrix;
    }


    // preparing answers
    private static int[] getAnsMatrix(Fraction[][] NRmatrix){
        int commonDenominator = getRowLCM(NRmatrix[0]);
        int[] ansMatrix = new int[NRmatrix[0].length+1];
        for (int i = 0 ; i < ansMatrix.length-1 ; i++){
            BigInteger numerator = NRmatrix[0][i].numerator;
            BigInteger denominator = NRmatrix[0][i].denominator;
            int newNumerator = numerator.intValue();
            if (!denominator.equals(Int(commonDenominator))){
                BigInteger constant = BigInteger.valueOf(commonDenominator).divide(denominator);
                newNumerator = numerator.multiply(constant).intValue();
            }
            ansMatrix[i] = newNumerator;
        }
        ansMatrix[ansMatrix.length-1] = commonDenominator;
        return ansMatrix;
    }
    public static int getRowLCM(Fraction[] row){
        if (row.length > 1){
            BigInteger rowLCM = Fraction.LCM(row[0].denominator, row[1].denominator);
            for (int i = 2 ; i < row.length-1 ; i++){
                rowLCM = Fraction.LCM(rowLCM, row[i].denominator);
            }
            return rowLCM.intValue();
        }else if (row.length == 1){
            return row[0].denominator.intValue();
        }else {
            return -1;
        }
    }
    public static int[] s0TerminateMatrix(int[][] item){
        int[] ans = new int[item.length+1];
        ans[0] = 1; ans[ans.length-1] = 1;
        return ans;
    }


    // private classes
    private static class Aggregation{
        LinkedHashSet terminalSet;
        LinkedHashSet nonTerminalSet;
        int[] terminal;
        int[] nonTerminal;
        Fraction[][] probabiliryMatrix;


        public Aggregation(
                int[] terminal, int[] nonTerminal, Fraction[][] probabiliryMatrix,
                LinkedHashSet terminalSet, LinkedHashSet nonTerminalSet)
        {
            this.terminal = terminal;
            this.nonTerminal = nonTerminal;
            this.probabiliryMatrix = probabiliryMatrix;
            this.terminalSet = terminalSet;
            this.nonTerminalSet = nonTerminalSet;
        }
    }
    public static final class Fraction { // make it immutable
        private final BigInteger denominator;
        private final BigInteger numerator;

        public Fraction(BigInteger numerator, BigInteger denominator) {

            if (numerator.equals(BigInteger.ZERO)  || denominator.equals(BigInteger.ZERO)){
                this.numerator = BigInteger.ZERO;
                this.denominator = BigInteger.ONE;
            }else {
                BigInteger gcd = GCD(denominator, numerator);
                BigInteger newNumerator = numerator.divide(gcd);
                BigInteger newDenominator = denominator.divide(gcd);

                if (requireSymbolSwitching(newNumerator, newDenominator)) {
                    newDenominator = newDenominator.multiply(BigInteger.valueOf(-1));
                    newNumerator = newNumerator.multiply(BigInteger.valueOf(-1));
                }

                this.numerator = newNumerator;
                this.denominator = newDenominator;
            }
        }

        public Fraction add(Fraction fraction){
            if (fraction.isZero()){
                return new Fraction(this.numerator, this.denominator);
            }else if (this.isZero()){
                return new Fraction(fraction.numerator, fraction.denominator);
            }else {
                BigInteger LCM = LCM(fraction.denominator, denominator);
                BigInteger newNumerator = (numerator.multiply(LCM).divide(denominator))
                        .add(fraction.numerator.multiply(LCM).divide(fraction.denominator));
                return new Fraction(newNumerator, LCM);
            }
        }

        public Fraction subtract(Fraction fraction){
            return this.add(fraction.multiply(N_ONE));
        }

        public Fraction multiply(Fraction fraction){
            if (this.isZero() || fraction.isZero()){
                return new Fraction(BigInteger.ZERO,BigInteger.ONE);
            }else {
                BigInteger newNumerator = this.numerator.multiply(fraction.numerator);
                BigInteger newDenominator = this.denominator.multiply(fraction.denominator);
                return new Fraction(newNumerator, newDenominator);
            }
        }

        public Fraction divide(Fraction fraction){
            return this.multiply(new Fraction(fraction.denominator, fraction.numerator));
        }

        public boolean isZero(){
            return denominator.equals(BigInteger.ZERO) || numerator.equals(BigInteger.ZERO);
        }

        public boolean requireSymbolSwitching(BigInteger numerator, BigInteger denominator){
            return (denominator.compareTo(BigInteger.ZERO) < 0 && numerator.compareTo(BigInteger.ZERO) < 0) ||
                    (denominator.compareTo(BigInteger.ZERO) < 0 && numerator.compareTo(BigInteger.ZERO) > 0);
        }

        public static BigInteger GCD(BigInteger numerator, BigInteger denominator){
            if (denominator.equals(BigInteger.ZERO)){
                return numerator;
            }
            return GCD(denominator, (numerator.abs()).mod(denominator.abs()));
        }

        public static BigInteger LCM(BigInteger num1, BigInteger num2){
            return (num1.multiply(num2).divide(GCD(num1, num2)));
        }

        public String toString(){
            return numerator + "/" + denominator;
        }
    }
    private static class InverseMatrix {
        private final Fraction[][] oriMatrix;
        private Fraction[][] augMatrix;
        private final int BELOW_DIAGONAL_ALL_ZERO = -999;


        InverseMatrix(Fraction[][] doubleM){
            this.oriMatrix = doubleM;
            initAugMatrix();
            makeLeftBottomPivot();
            makeRightTopPivot();
        }

        private void initAugMatrix(){
            Fraction[][] temp = new Fraction[oriMatrix.length][oriMatrix.length];
            for (int i = 0 ; i < oriMatrix.length ; i++){
                for (int j = 0 ; j < oriMatrix.length ; j++){
                    temp[i][j] = (i == j)? ONE : ZERO;
                }
            }
            this.augMatrix = temp;
        }

        private void makeLeftBottomPivot(){
            for (int column = 0 ; column < oriMatrix.length ; column++){
                Fraction diagonal = oriMatrix[column][column];
                if (diagonal.isZero()){
                    int nonZero = findNonZeroRowBelowDiagonal(column);
                    if (nonZero != BELOW_DIAGONAL_ALL_ZERO){
                        swapRow(nonZero, column);
                        column--;
                    }
                }else {
                    makeRowDiagonalBecome1(column);
                    for (int row = column+1 ; row < oriMatrix.length ; row++ ){
                        makeZeroFromDiagonal(row, column);
                    }
                }
            }
        }

        private void makeRightTopPivot(){
            for (int column = oriMatrix.length-1 ; column >= 0 ; column--){
                for (int row = column-1 ; row >= 0 ; row--){
                    makeZeroFromDiagonal(row, column);
                }
            }
        }

        private void makeZeroFromDiagonal(int row, int column){
            if (!oriMatrix[row][column].isZero()){
                Fraction constantToForm0 = oriMatrix[row][column].multiply(N_ONE);
                oriMatrix[row] = applyRowValueToOtherRow(oriMatrix[row], oriMatrix[column], constantToForm0);
                augMatrix[row] = applyRowValueToOtherRow(augMatrix[row], augMatrix[column], constantToForm0);
            }
        }

        private int findNonZeroRowBelowDiagonal(int column){
            for (int row = column+1 ; row < oriMatrix.length ; row++){
                if (!oriMatrix[row][column].isZero()){
                    return row;
                }
            }
            return BELOW_DIAGONAL_ALL_ZERO;
        }

        private void swapRow(int row1_index, int row2_index){
            Fraction[] temp = oriMatrix[row1_index];
            oriMatrix[row1_index] = oriMatrix[row2_index];
            oriMatrix[row2_index] = temp;
            Fraction[] tempAug = augMatrix[row1_index];
            augMatrix[row1_index] = augMatrix[row2_index];
            augMatrix[row2_index] = tempAug;
        }

        private void makeRowDiagonalBecome1(int column){
            Fraction constantToForm1 = ONE.divide(oriMatrix[column][column]);

            for (int col = 0 ; col < oriMatrix.length ; col++ ){
                oriMatrix[column][col] = oriMatrix[column][col].multiply(constantToForm1);
                augMatrix[column][col] = augMatrix[column][col].multiply(constantToForm1);
            }
        }

        private Fraction[] applyRowValueToOtherRow(Fraction[]beApplied, Fraction[]applier, Fraction constant){
            for (int i = 0 ; i < beApplied.length ; i++){
                beApplied[i] = beApplied[i].add(constant.multiply(applier[i]));
            }
            return beApplied;
        }

        public Fraction[][] getInverseMatrix(){
            return augMatrix;
        }

    }
}
