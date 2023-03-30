import java.math.BigInteger;


//public class IMnewF {
//    Fraction[][] oriMatrix;
//    Fraction[][] augMatrix;
//    int BELOW_DIAGONAL_ALL_ZERO = -999;
//    final Fraction ONE =new Fraction(BigInteger.ONE, BigInteger.ONE);
//    final Fraction ZERO =new Fraction(BigInteger.ZERO, BigInteger.ONE);
//    final Fraction N_ONE =new Fraction(BigInteger.valueOf(-1), BigInteger.ONE);
//
//    IMnewF(Fraction[][] doubleM){
//        this.oriMatrix = doubleM;
//        initAugMatrix();
//        makeLeftBottomPivot();
//        makeRightTopPivot();
//    }
//
//
////    public static void main(String[] args) {
////
////        int[][] arr = new int[][]{
////                {8 ,2 ,9 ,6 ,5 ,9 ,7 ,4 ,9 ,5},
////                {7 ,7 ,6 ,6 ,8 ,0 ,4 ,7 ,8 ,3},
////                {3 ,5 ,1 ,6 ,5 ,3 ,0 ,9 ,6 ,5},
////                {7 ,6 ,6 ,4 ,6 ,7 ,9 ,3 ,3 ,2},
////                {4 ,7 ,9 ,7 ,2 ,7 ,8 ,8 ,3 ,5},
////                {9 ,6 ,8 ,4 ,8 ,4 ,7 ,0 ,4 ,0},
////                {3 ,9 ,4 ,5 ,9 ,0 ,5 ,0 ,6 ,8},
////                {1 ,9 ,5 ,4 ,1 ,0 ,0 ,1 ,5 ,7},
////                {7 ,3 ,0 ,3 ,1 ,6 ,6 ,4 ,4 ,7},
////                {6 ,0 ,4 ,7 ,0 ,5 ,4 ,8 ,9 ,9},
////        };
////
////
////        Fraction[][] fMatrix = new Fraction[arr.length][arr.length];
////
////
////        for (int i = 0 ; i < arr.length ; i++){
////            for (int j = 0 ; j < arr.length ; j++){
////                fMatrix[i][j] = new Fraction(BigInteger.valueOf(arr[i][j]), BigInteger.ONE);
////            }
////        }
////
////        IMnewF imnew = new IMnewF(fMatrix);
////
////        Fraction[][] inverse = imnew.getInverseMatrix();
////
////        for (Fraction[] f : inverse){
////            for (Fraction i : f){
////                System.out.print(i + "\t");
////            }
////            System.out.println("");
////        }
////    }
//
//    private void initAugMatrix(){
//        Fraction[][] temp = new Fraction[oriMatrix.length][oriMatrix.length];
//        for (int i = 0 ; i < oriMatrix.length ; i++){
//            for (int j = 0 ; j < oriMatrix.length ; j++){
//                temp[i][j] = (i == j)? new ONE : ZERO;
//            }
//        }
//        this.augMatrix = temp;
//    }
//
//    private void makeLeftBottomPivot(){
//        for (int column = 0 ; column < oriMatrix.length ; column++){
//            Fraction diagonal = oriMatrix[column][column];
//            if (diagonal.isZero()){
//                int nonZero = findNonZeroRowBelowDiagonal(column);
//                if (nonZero != BELOW_DIAGONAL_ALL_ZERO){
//                    swapRow(nonZero, column);
//                    column--;
//                }
//            }else {
//                makeRowDiagonalBecome1(column);
//                for (int row = column+1 ; row < oriMatrix.length ; row++ ){
//                    makeZeroFromDiagonal(row, column);
//                }
//            }
//        }
//    }
//
//    private void makeRightTopPivot(){
//        for (int column = oriMatrix.length-1 ; column >= 0 ; column--){
//            for (int row = column-1 ; row >= 0 ; row--){
//                makeZeroFromDiagonal(row, column);
//            }
//        }
//    }
//
//    private void makeZeroFromDiagonal(int row, int column){
//        if (!oriMatrix[row][column].isZero()){
//            Fraction constantToForm0 = oriMatrix[row][column].multiply(N_ONE);
//            oriMatrix[row] = applyRowValueToOtherRow(oriMatrix[row], oriMatrix[column], constantToForm0);
//            augMatrix[row] = applyRowValueToOtherRow(augMatrix[row], augMatrix[column], constantToForm0);
//        }
//    }
//
//    private int findNonZeroRowBelowDiagonal(int column){
//        for (int row = column+1 ; row < oriMatrix.length ; row++){
//            if (!oriMatrix[row][column].isZero()){
//               return row;
//            }
//        }
//        return BELOW_DIAGONAL_ALL_ZERO;
//    }
//
//    private void swapRow(int row1_index, int row2_index){
//        Fraction[] temp = oriMatrix[row1_index];
//        oriMatrix[row1_index] = oriMatrix[row2_index];
//        oriMatrix[row2_index] = temp;
//        Fraction[] tempAug = augMatrix[row1_index];
//        augMatrix[row1_index] = augMatrix[row2_index];
//        augMatrix[row2_index] = tempAug;
//    }
//
//    private void makeRowDiagonalBecome1(int column){
//        Fraction constantToForm1 = ONE.divide(oriMatrix[column][column]);
//
//        for (int col = 0 ; col < oriMatrix.length ; col++ ){
//            oriMatrix[column][col] = oriMatrix[column][col].multiply(constantToForm1);
//            augMatrix[column][col] = augMatrix[column][col].multiply(constantToForm1);
//        }
//    }
//
//    private Fraction[] applyRowValueToOtherRow(Fraction[]beApplied, Fraction[]applier, Fraction constant){
//        for (int i = 0 ; i < beApplied.length ; i++){
//            beApplied[i] = beApplied[i].add(constant.multiply(applier[i]));
//        }
//        return beApplied;
//    }
//
//    public Fraction[][] getInverseMatrix(){
//        return augMatrix;
//    }
//
//}
