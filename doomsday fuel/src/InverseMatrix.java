//import java.math.BigInteger;
//import java.util.Objects;
//
//class Fraction{
//    BigInteger numerator;
//    BigInteger denominator;
//
//    public Fraction(BigInteger numerator, BigInteger denominator){
//        this.numerator = numerator;
//        this.denominator = denominator;
//    }
//
//    public String toString(){
//        return numerator + "/" + denominator;
//    }
//
//    public Fraction multiply(Fraction fraction){
//        BigInteger newNumerator = fraction.numerator.multiply(numerator);
//        BigInteger newDenominator = fraction.denominator.multiply(denominator);
//        Fraction f = new Fraction(newNumerator, newDenominator);
//        return f;
//    }
//
//    public Fraction subtract(Fraction fraction){
//
//        BigInteger newNumerator =
//                (this.numerator.multiply(fraction.denominator)).subtract((fraction.numerator.multiply(this.denominator)));
//        BigInteger newDenominator = (this.denominator.multiply(fraction.denominator));
//
//        Fraction frac = new Fraction(newNumerator, newDenominator);
//        frac.simplifyFraction();
//
//        return frac;
//    }
//
//    public Fraction add(Fraction fraction){
//        if (this.isZero()){
//            return fraction;
//        }else if (fraction.isZero()){
//            return this;
//        }else {
//            Fraction f = this.subtract(fraction.multiply(new Fraction(BigInteger.ONE.subtract(BigInteger.TWO), BigInteger.ONE)));
//            f.simplifyFraction();
//            return f;
//        }
//    }
//
//    public Fraction divide(Fraction fraction){
//
//        Fraction divideResult = this.multiply(new Fraction(fraction.denominator,fraction.numerator));
//        divideResult.simplifyFraction();
//
//
//        System.out.println("DIVIDE " + this + " WITH " + fraction + " IS " + divideResult);
//        return divideResult;
//
//    }
//
//    public void simplifyFraction(){
//        if (!this.isZero()){
//            BigInteger gcd = GCD(denominator, numerator);
//            numerator = numerator.divide(gcd);
//            denominator = denominator.divide(gcd);
//        }
//        if ((denominator.compareTo(BigInteger.ZERO) < 0) && (numerator.compareTo(BigInteger.ZERO) < 0) ||
//                (denominator.compareTo(BigInteger.ZERO) < 0) && (numerator.compareTo(BigInteger.ZERO) > 0)){
//            numerator = numerator.multiply(BigInteger.valueOf(-1));
//            denominator = denominator.multiply(BigInteger.valueOf(-1));
//        }
//    }
//
//    public static BigInteger GCD(BigInteger numerator, BigInteger denominator){
//        if (denominator.equals(BigInteger.ZERO)){
//            return numerator;
//        }
//
////            System.out.println(numerator.abs() + " mod " + denominator);
//
//        return GCD(denominator.abs(), numerator.abs().mod(denominator.abs()));
//    }
//
//    public static BigInteger LCM(BigInteger num1, BigInteger num2){
//        return (num1.multiply(num2)).divide(GCD(num1, num2)).abs();
//    }
//
//    public boolean isZero(){
//        return this.numerator.equals(BigInteger.ZERO) || this.denominator.equals(BigInteger.ZERO);
//    }
//}
//
//public class InverseMatrix {
//    Fraction[][] oriMatrix;
//    Fraction[][] augMatrix;
//
//    InverseMatrix.FLCondition con = (c, s) -> Objects.equals(s, "below diagonal") ? c < oriMatrix.length : c >= 0;
//    InverseMatrix.FLInit i = (c, s) -> Objects.equals(s, "below diagonal") ? c+1 : c-1;
//    InverseMatrix.FLCounter counter = (c, s) -> Objects.equals(s, "below diagonal")? c+1 : c-1;
//
//
//    InverseMatrix(Fraction[][] oriMatrix){
//        this.oriMatrix = oriMatrix;
//        this.augMatrix = new Fraction[oriMatrix.length][oriMatrix.length];
//
//        for (int i = 0 ; i < oriMatrix.length ; i++){
//            for (int j = 0 ; j < oriMatrix.length ; j++){
//                if (i == j){
//                    augMatrix[i][j] = new Fraction(BigInteger.ONE, BigInteger.ONE);
//                }else {
//                    augMatrix[i][j] = new Fraction(BigInteger.ZERO, BigInteger.ZERO);
//                }
//            }
//        }
//    }
//
////    public void getInverseMatrix(Fraction[][]) {
//////        int[][] matrixInt = new int[][] {{2,5,7},{5,3,4},{5,-2,-3}};
//////        augMatrix = new Fraction[matrixInt.length][matrixInt.length];
//////        oriMatrix = new Fraction[matrixInt.length][matrixInt.length];
//////
//////
//////
//////        //System.out.println(" \nORIGINAL");
//////        for (Fraction[] row : oriMatrix){
//////            for (Fraction f : row){
//////                //System.out.print(f.toString() + ", ");
//////            }
//////            //System.out.println("");
//////        }
//////
//////
//////        //System.out.println(" \nAUGMENTING MATRIX");
//////        for (Fraction[] row : augMatrix){
//////            for (Fraction f : row){
//////                //System.out.print(f.toString() + ", ");
//////            }
//////            //System.out.println("");
//////        }
////         getInverse();
////
////    }
//
//    public Fraction[][] getInverse() {
//
//        // settle bottom left corner
//        for (int col = 0 ; col < oriMatrix.length-1 ; col++){ // exclude last column
//            convertPivotTo0(col, "below diagonal");
//        }
//
//        // settle top right corner
//        for (int col = oriMatrix.length-1 ; col >= 0 ; col--){ // exclude last column
//            convertPivotTo0(col, "above diagonal");
//        }
//
//
////        //System.out.println(" \nORIGINAL");
////        for (Fraction[] row : oriMatrix){
////            for (Fraction f : row){
////                //System.out.print(f.toString() + ", ");
////            }
////            //System.out.println("");
////        }
//
//
//        for (int row = 0 ; row < oriMatrix.length ; row++){
//            Fraction toDivide = oriMatrix[row][row];
//
//            for (int col= 0 ; col < oriMatrix.length ; col++){
//                oriMatrix[row][col] = oriMatrix[row][col].divide(toDivide);
//                augMatrix[row][col] = augMatrix[row][col].divide(toDivide);
//            }
//
//        }
//
//
//        return augMatrix;
////        //System.out.println(" \nORIGINAL");
////        for (Fraction[] row : oriMatrix){
////            for (Fraction f : row){
////                //System.out.print(f.toString() + ", ");
////            }
////            //System.out.println("");
////        }
////
////
////        //System.out.println(" \nAUGMENTING");
////        for (Fraction[] row : augMatrix){
////            for (Fraction f : row){
////                //System.out.print(f.toString() + ", ");
////            }
////            //System.out.println("");
////        }
//
//
////        for (double[] row : oriMatrix){
////            //System.out.println(Arrays.toString(row));
////        }
////
////        //System.out.println("\nAugmenting matrix = ");
////        for (double[] row : augMatrix) {
////            //System.out.println(Arrays.toString(row));
////        }
//    }
//
//    public void convertPivotTo0(int diagonalPosition, String direction){
//        Fraction diagonal = oriMatrix[diagonalPosition][diagonalPosition];
//
//
//        if (diagonal.isZero()){
//            Integer nonZeroRowIndex = findNonZeroRowFromDiagonal(direction, diagonalPosition, oriMatrix);
//
////            //System.out.println("\nNON ZERO ROW INDEX = " + nonZeroRowIndex);
//
//            if (nonZeroRowIndex!=null){
//
//
//
//
//
//                //System.out.println(" \nBEFORE SWAP");
//                for (Fraction[] row : oriMatrix){
//                    for (Fraction f : row){
//                        //System.out.print(f.toString() + ", ");
//                    }
//                    //System.out.println("");
//                }
//                //System.out.println("\n");
//
//
//
//                swapZeroDiagonalWithNonZeroDiagonal(nonZeroRowIndex, diagonalPosition);
//
//
//
//                //System.out.println(" \nAFTER SWAP");
//                for (Fraction[] row : oriMatrix){
//                    for (Fraction f : row){
//                        //System.out.print(f.toString() + ", ");
//                    }
//                    //System.out.println("");
//                }
//                //System.out.println("\n");
//
//
//                formZeroFromDiagonal(direction, diagonalPosition);
//            }
//        }else {
//            formZeroFromDiagonal(direction, diagonalPosition);
//        }
//    }
//
//    public Integer findNonZeroRowFromDiagonal(String direction, int diagonalPosition, Fraction[][] matrix){
//        Integer nonZeroRowIndex = null;
//        for (int k = i.init(diagonalPosition, direction) ;
//             con.loop(k, direction) ;
//             k = counter.progress(k, direction))
//        {
//            if (!matrix[k][diagonalPosition].isZero()) {
//                nonZeroRowIndex = k;
//                break;
//            }
//        }
//        return nonZeroRowIndex;
//    }
//
//    public static Fraction getConstantToFormZero(Fraction number, Fraction agent){
//
//        Fraction constant = number.multiply(new Fraction(BigInteger.ONE.subtract(BigInteger.TWO), BigInteger.ONE)).divide(agent);
//
//
//        //System.out.println("\n TO FORM ZERO FROM " + number + " WITH " + agent);
//        //System.out.println(number + " + " + agent + " * " + constant);
//
//
//        return constant;
//    }
//
//
//    public void formZeroFromDiagonal(String direction, int diagonalCol){
//        for (int k = i.init(diagonalCol, direction);
//             con.loop(k, direction) && !oriMatrix[k][diagonalCol].isZero();
//             k = counter.progress(k, direction)) {
//
//            Fraction additionConstant = getConstantToFormZero(oriMatrix[k][diagonalCol], oriMatrix[diagonalCol][diagonalCol]);
//
//
//            for (int j = 0 ; j < oriMatrix.length ; j++){
//
//
//                //System.out.println(" \n----------------------ROW　OPERATION　ADD----------------------");
//
//
//                //System.out.println(" \nBEFORE ADD ORIGINAL");
//                for (Fraction[] row : oriMatrix){
//                    for (Fraction f : row){
//                        //System.out.print(f.toString() + ", ");
//                    }
//                    //System.out.println("");
//                }
//
//
//                //System.out.println(" \nBEFORE ADD AUGMENTING");
//                for (Fraction[] row : augMatrix){
//                    for (Fraction f : row){
//                        //System.out.print(f.toString() + ", ");
//                    }
//                    //System.out.println("");
//                }
//
//
//
//                Fraction f1 = oriMatrix[diagonalCol][j].multiply(additionConstant);
//                Fraction f2 = augMatrix[diagonalCol][j].multiply(additionConstant);
//
//                //System.out.println("\nADD " + oriMatrix[k][j] + " WITH ( " + oriMatrix[diagonalCol][j] + " * " + additionConstant + " = " + oriMatrix[diagonalCol][j].multiply(additionConstant) + ") result in " + oriMatrix[k][j].add(f1));
//
//
//
//                oriMatrix[k][j] = oriMatrix[k][j].add(f1);
//                augMatrix[k][j] = augMatrix[k][j].add(f2);
//
//
//
//
//
//
//                //System.out.println(" \nORIGINAL");
//                for (Fraction[] row : oriMatrix){
//                    for (Fraction f : row){
//                        //System.out.print(f.toString() + ", ");
//                    }
//                    //System.out.println("");
//                }
//
//
//                //System.out.println(" \nAUGMENTING");
//                for (Fraction[] row : augMatrix){
//                    for (Fraction f : row){
//                        //System.out.print(f.toString() + ", ");
//                    }
//                    //System.out.println("");
//                }
//
//
//            }
//
//
//
//        }
//    }
//
//    public void swapZeroDiagonalWithNonZeroDiagonal(int nonZeroRowIndex, int diagonalRowIndex){
//        Fraction[] temp = oriMatrix[nonZeroRowIndex];
//        oriMatrix[nonZeroRowIndex] = oriMatrix[diagonalRowIndex];
//        oriMatrix[diagonalRowIndex] = temp;
//
//        Fraction[] temp2 = augMatrix[nonZeroRowIndex];
//        augMatrix[nonZeroRowIndex] = augMatrix[diagonalRowIndex];
//        augMatrix[diagonalRowIndex] = temp2;
//
//        //System.out.println(" \n----------------------ROW　OPERATION　SWAP----------------------");
//
//        //System.out.println(" \nORIGINAL");
//        for (Fraction[] row : oriMatrix){
//            for (Fraction f : row){
//                //System.out.print(f.toString() + ", ");
//            }
//            //System.out.println("");
//        }
//
//
//        //System.out.println(" \nAUGMENTING");
//        for (Fraction[] row : augMatrix){
//            for (Fraction f : row){
//                //System.out.print(f.toString() + ", ");
//            }
//            //System.out.println("");
//        }
//
//    }
//
//    private interface FLCondition{ boolean loop(int counter, String direction);}
//
//    private interface FLCounter{ int progress(int counter, String direction); }
//
//    private interface FLInit{ int init(int counter, String direction); }
//
//    public static void main(String[] args) {
//
//        int[][] arr = new int[][]{{1,-1,2}, {4,0,6},{0,1,-1}};
//        Fraction[][] farr = new Fraction[arr.length][arr.length];
//
//
//        System.out.println("B iterate");
//
//        for (int i = 0 ; i < arr.length ; i++ ){
//            System.out.println("1 iterate");
//
//
//
//            for (int j = 0 ; j < arr.length ; j++){
//                farr[i][j] = new Fraction(BigInteger.valueOf(arr[i][j]), BigInteger.ONE);
//                System.out.println("iterate");
//                System.out.println(farr[i][j]);
//            }
//        }
//
//        for (Fraction[] row : farr){
//            for (Fraction f : row){
//                System.out.print(f + "\t");
//            }
//            System.out.print("\n");
//        }
//
//
//        InverseMatrix i = new InverseMatrix(farr);
//        Fraction[][] iM = i.getInverse();
//
//
//        for (Fraction[] row : iM){
//            for (Fraction f : row){
//                System.out.print(f + "\t");
//            }
//            System.out.print("\n");
//        }
//
//    }
//}