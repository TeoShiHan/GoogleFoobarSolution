import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Solution03Backup {
    static Fraction one = new Fraction(BigInteger.ONE, BigInteger.ONE);
    static Fraction zero = new Fraction(BigInteger.ZERO, BigInteger.ONE);
    static ArrayList<Integer> terminal = new ArrayList<>();
    static ArrayList<Integer> nonTerminal = new ArrayList<>();

    public static int[] solution(int[][] item) {
        int[] terminalTemplate = new int[item.length];
        Fraction[][] probabilities = new Fraction[item.length][item.length];


        // aggregate terminal and non-terminal + form fraction matrix
        for (int i = 0 ; i < item.length ; i++){

            int rowSum = 0;
            for (int j = 0 ; j < item.length ; j++) {
                rowSum += item[i][j];
            }



            // aggregate terminal and non terminal
            if (rowSum == 0){
                item[i][i] = 1; terminal.add(i) ; rowSum = 1;
            }else {
                nonTerminal.add(i);
            }


            // create fractions
            for (int j = 0 ; j < item.length ; j++){
                Fraction fraction = new Fraction(Int(item[i][j]), Int(rowSum));
                probabilities[i][j] = fraction;
            }
        }


        {
            System.out.println("TERMINAL IS = " + terminal);
            System.out.println("NON TERMINAL IS = " + nonTerminal);
            System.out.println("FRACTION MATRIX IS = \n");

            for (Fraction[] row : probabilities){
                for (Fraction f : row){
                    System.out.print(f + ",\t");
                }
                System.out.println("");
            }



        }


        Fraction[][] IminusQmatrix = new Fraction[nonTerminal.size()][nonTerminal.size()];

        for (int i = 0 ; i < nonTerminal.size() ; i++){
            for (int j = 0 ; j < nonTerminal.size() ; j++){
                if (i == j){
                    IminusQmatrix[i][j] =
                            one.subtract(probabilities[nonTerminal.get(i)][nonTerminal.get(j)]);
                }else {
                    IminusQmatrix[i][j] =
                            zero.subtract(probabilities[nonTerminal.get(i)][nonTerminal.get(j)]);
                }
            }
        }


        {
            System.out.println("\nI - Q MATRIX IS");

            for (Fraction[] row : IminusQmatrix) {
                for (Fraction f : row) {
                    System.out.print(f + ",\t");
                }
                System.out.println("");
            }
        }

        InverseMatrix im = new InverseMatrix(IminusQmatrix);
        Fraction[][] Nmatrix = im.getInverseMatrix();


        {
            System.out.println("\nINVERSE MATRIX IS");

            for (Fraction[] row : Nmatrix) {
                for (Fraction f : row) {
                    System.out.print(f + ",\t");
                }
                System.out.println("");
            }
        }


        // get R matrix
        Fraction[][] Rmatrix = new Fraction[nonTerminal.size()][terminal.size()];

        for (int i = 0 ; i < nonTerminal.size() ; i++){
            int nonTer = nonTerminal.get(i);
            for (int j = 0 ; j < terminal.size() ; j++){
                int ter = terminal.get(j);
                Rmatrix[i][j] = probabilities[nonTer][ter];
            }
        }

        System.out.println("\nRmatrix = ");
        for (Fraction[] r : Rmatrix){
            System.out.println(Arrays.toString(r));
        }

        Fraction[][] NRmatrix = new Fraction[Nmatrix.length][Rmatrix[0].length];


        for (int i = 0 ; i < Nmatrix.length ; i++){
            for (int j = 0 ; j < Rmatrix[0].length ; j++){
                NRmatrix[i][j] = multiplyRowCol(Nmatrix, i, Rmatrix, j, nonTerminal.size());
            }
        }

        System.out.println("\nNRmatrix = ");
        for (Fraction[] r : NRmatrix){
            System.out.println(Arrays.toString(r));
        }

        int commonDenominator = getRowLCM(NRmatrix[0]);
        System.out.println("COMMON DENOMINATOR = " +commonDenominator);

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

        System.out.println("ANS = " + Arrays.toString(ansMatrix));


        return ansMatrix;
    }

    public static BigInteger Int(int number){
        return BigInteger.valueOf(number);
    }

    public static final class Fraction {
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
            if (this.isZero()){
                return fraction.multiply(new Fraction(BigInteger.valueOf(-1), BigInteger.ONE));
            }else if (fraction.isZero()){
                return new Fraction(this.numerator, this.denominator);
            }else {
                BigInteger LCM = LCM(fraction.denominator, denominator);
                BigInteger newNumerator =
                        (numerator.multiply(LCM).divide(denominator))
                                .subtract(fraction.numerator.multiply(LCM).divide(fraction.denominator));
                return new Fraction(newNumerator, LCM);
            }
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
        Fraction[][] oriMatrix;
        Fraction[][] augMatrix;
        int BELOW_DIAGONAL_ALL_ZERO = -999;
        final Fraction ONE =new Fraction(BigInteger.ONE, BigInteger.ONE);
        final Fraction ZERO =new Fraction(BigInteger.ZERO, BigInteger.ONE);
        final Fraction N_ONE =new Fraction(BigInteger.valueOf(-1), BigInteger.ONE);

        InverseMatrix(Fraction[][] doubleM){
            this.oriMatrix = doubleM;
            initAugMatrix();
            makeLeftBottomPivot();
            makeRightTopPivot();
        }


//    public static void main(String[] args) {
//
//        int[][] arr = new int[][]{
//                {8 ,2 ,9 ,6 ,5 ,9 ,7 ,4 ,9 ,5},
//                {7 ,7 ,6 ,6 ,8 ,0 ,4 ,7 ,8 ,3},
//                {3 ,5 ,1 ,6 ,5 ,3 ,0 ,9 ,6 ,5},
//                {7 ,6 ,6 ,4 ,6 ,7 ,9 ,3 ,3 ,2},
//                {4 ,7 ,9 ,7 ,2 ,7 ,8 ,8 ,3 ,5},
//                {9 ,6 ,8 ,4 ,8 ,4 ,7 ,0 ,4 ,0},
//                {3 ,9 ,4 ,5 ,9 ,0 ,5 ,0 ,6 ,8},
//                {1 ,9 ,5 ,4 ,1 ,0 ,0 ,1 ,5 ,7},
//                {7 ,3 ,0 ,3 ,1 ,6 ,6 ,4 ,4 ,7},
//                {6 ,0 ,4 ,7 ,0 ,5 ,4 ,8 ,9 ,9},
//        };
//
//
//        Fraction[][] fMatrix = new Fraction[arr.length][arr.length];
//
//
//        for (int i = 0 ; i < arr.length ; i++){
//            for (int j = 0 ; j < arr.length ; j++){
//                fMatrix[i][j] = new Fraction(BigInteger.valueOf(arr[i][j]), BigInteger.ONE);
//            }
//        }
//
//        IMnewF imnew = new IMnewF(fMatrix);
//
//        Fraction[][] inverse = imnew.getInverseMatrix();
//
//        for (Fraction[] f : inverse){
//            for (Fraction i : f){
//                System.out.print(i + "\t");
//            }
//            System.out.println("");
//        }
//    }

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

    public static void main(String[] args) {
        Solution03Backup.solution(new int[][]{
                {0, 1, 0, 0, 0, 1},
                {4, 0, 0, 3, 2, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        });
    }

    private static Fraction multiplyRowCol(
            Fraction[][]rowSource, int rowIndex, Fraction[][]colSource, int colIndex, int colHeight)
    {
        Fraction sum = zero;
        Fraction[] fRow = rowSource[rowIndex];

        for (int i = 0 ; i < colHeight ; i++){
            Fraction colFraction = colSource[i][colIndex];
            Fraction multiply = colFraction.multiply(fRow[i]);
            sum = sum.add(multiply);
        }

        return sum;
    }

    public static int getRowLCM(Fraction[] row){
            if (row.length > 1){
                BigInteger rowLCM = Fraction.LCM(row[0].denominator, row[1].denominator);
                for (int i = 2 ; i < row.length-1 ; i++){
                    rowLCM = Fraction.LCM(rowLCM, row[i].denominator);
                }
                System.out.println(rowLCM);
                return rowLCM.intValue();
            }else if (row.length == 1){
                return row[0].denominator.intValue();
            }else {
                return -1;
            }
        }

}
