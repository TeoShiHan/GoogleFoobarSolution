import java.util.Arrays;
import java.util.Objects;

public class IM {
        double[][] oriMatrix;
        double[][] augMatrix;

        FLCondition con = (c, s) -> Objects.equals(s, "below diagonal") ? c < oriMatrix.length : c >= 0;
        FLInit i = (c, s) -> Objects.equals(s, "below diagonal") ? c+1 : c-1;
        FLCounter counter = (c, s) -> Objects.equals(s, "below diagonal")? c+1 : c-1;


        IM(double[][] oriMatrix){
            this.oriMatrix = oriMatrix;
            this.augMatrix = new double[oriMatrix.length][oriMatrix.length];

            for (int i = 0 ; i < oriMatrix.length ; i++){
                for (int j = 0 ; j < oriMatrix.length ; j++){
                    if (i == j){
                        augMatrix[i][j] = 1;
                    }else {
                        augMatrix[i][j] = 0;
                    }
                }
            }
        }

//    public void getInverseMatrix(double[][]) {
////        int[][] matrixInt = new int[][] {{2,5,7},{5,3,4},{5,-2,-3}};
////        augMatrix = new double[matrixInt.length][matrixInt.length];
////        oriMatrix = new double[matrixInt.length][matrixInt.length];
////
////
////
////        //System.out.println(" \nORIGINAL");
////        for (double[] row : oriMatrix){
////            for (double f : row){
////                //System.out.print(f.toString() + ", ");
////            }
////            //System.out.println("");
////        }
////
////
////        //System.out.println(" \nAUGMENTING MATRIX");
////        for (double[] row : augMatrix){
////            for (double f : row){
////                //System.out.print(f.toString() + ", ");
////            }
////            //System.out.println("");
////        }
//         getInverse();
//
//    }

        public double[][] getInverse() {

            // settle bottom left corner
            for (int col = 0 ; col < oriMatrix.length-1 ; col++){ // exclude last column
                convertPivotTo0(col, "below diagonal");
            }

            // settle top right corner
            for (int col = oriMatrix.length-1 ; col >= 0 ; col--){ // exclude last column
                convertPivotTo0(col, "above diagonal");
            }


//        //System.out.println(" \nORIGINAL");
//        for (double[] row : oriMatrix){
//            for (double f : row){
//                //System.out.print(f.toString() + ", ");
//            }
//            //System.out.println("");
//        }


            for (int row = 0 ; row < oriMatrix.length ; row++){
                double toDivide = oriMatrix[row][row];

                for (int col= 0 ; col < oriMatrix.length ; col++){
                    oriMatrix[row][col] = oriMatrix[row][col]/(toDivide);
                    augMatrix[row][col] = augMatrix[row][col]/(toDivide);
                }

            }


            return augMatrix;
//        //System.out.println(" \nORIGINAL");
//        for (double[] row : oriMatrix){
//            for (double f : row){
//                //System.out.print(f.toString() + ", ");
//            }
//            //System.out.println("");
//        }
//
//
//        //System.out.println(" \nAUGMENTING");
//        for (double[] row : augMatrix){
//            for (double f : row){
//                //System.out.print(f.toString() + ", ");
//            }
//            //System.out.println("");
//        }


//        for (double[] row : oriMatrix){
//            //System.out.println(Arrays.toString(row));
//        }
//
//        //System.out.println("\nAugmenting matrix = ");
//        for (double[] row : augMatrix) {
//            //System.out.println(Arrays.toString(row));
//        }
        }

        public void convertPivotTo0(int diagonalPosition, String direction){
            double diagonal = oriMatrix[diagonalPosition][diagonalPosition];
            if (diagonal == 0){
                Integer nonZeroRowIndex = findNonZeroRowFromDiagonal(direction, diagonalPosition, oriMatrix);
                if (nonZeroRowIndex!=null){
                    swapZeroDiagonalWithNonZeroDiagonal(nonZeroRowIndex, diagonalPosition);
                    formZeroFromDiagonal(direction, diagonalPosition);
                }
            }else {
                formZeroFromDiagonal(direction, diagonalPosition);
            }
        }

        public Integer findNonZeroRowFromDiagonal(String direction, int diagonalPosition, double[][] matrix){
            Integer nonZeroRowIndex = null;
            for (int k = i.init(diagonalPosition, direction) ;
                 con.loop(k, direction) ;
                 k = counter.progress(k, direction))
            {
                if (matrix[k][diagonalPosition] !=0) {
                    nonZeroRowIndex = k;
                    break;
                }
            }
            return nonZeroRowIndex;
        }


        public static double getConstantToFormZero(double number, double agent){
            return number*(-1)/(agent);
        }


        public void formZeroFromDiagonal(String direction, int diagonalCol){
            for (int k = i.init(diagonalCol, direction);
                 con.loop(k, direction) && oriMatrix[k][diagonalCol] != 0;
                 k = counter.progress(k, direction)) {

                double additionConstant = getConstantToFormZero(oriMatrix[k][diagonalCol], oriMatrix[diagonalCol][diagonalCol]);


                for (int j = 0 ; j < oriMatrix.length ; j++){


                    //System.out.println(" \n----------------------ROW　OPERATION　ADD----------------------");


                    //System.out.println(" \nBEFORE ADD ORIGINAL");
                    for (double[] row : oriMatrix){
                        for (double f : row){
                            //System.out.print(f.toString() + ", ");
                        }
                        //System.out.println("");
                    }


                    //System.out.println(" \nBEFORE ADD AUGMENTING");
                    for (double[] row : augMatrix){
                        for (double f : row){
                            //System.out.print(f.toString() + ", ");
                        }
                        //System.out.println("");
                    }



                    double f1 = oriMatrix[diagonalCol][j]*(additionConstant);
                    double f2 = augMatrix[diagonalCol][j]*(additionConstant);

                    //System.out.println("\nADD " + oriMatrix[k][j] + " WITH ( " + oriMatrix[diagonalCol][j] + " * " + additionConstant + " = " + oriMatrix[diagonalCol][j]*(additionConstant) + ") result in " + oriMatrix[k][j]+(f1));



                    oriMatrix[k][j] = oriMatrix[k][j]+(f1);
                    augMatrix[k][j] = augMatrix[k][j]+(f2);






                    //System.out.println(" \nORIGINAL");
                    for (double[] row : oriMatrix){
                        for (double f : row){
                            //System.out.print(f.toString() + ", ");
                        }
                        //System.out.println("");
                    }


                    //System.out.println(" \nAUGMENTING");
                    for (double[] row : augMatrix){
                        for (double f : row){
                            //System.out.print(f.toString() + ", ");
                        }
                        //System.out.println("");
                    }


                }



            }
        }

        public void swapZeroDiagonalWithNonZeroDiagonal(int nonZeroRowIndex, int diagonalRowIndex){
            double[] temp = oriMatrix[nonZeroRowIndex];
            oriMatrix[nonZeroRowIndex] = oriMatrix[diagonalRowIndex];
            oriMatrix[diagonalRowIndex] = temp;

            double[] temp2 = augMatrix[nonZeroRowIndex];
            augMatrix[nonZeroRowIndex] = augMatrix[diagonalRowIndex];
            augMatrix[diagonalRowIndex] = temp2;

            //System.out.println(" \n----------------------ROW　OPERATION　SWAP----------------------");

            //System.out.println(" \nORIGINAL");
            for (double[] row : oriMatrix){
                for (double f : row){
                    //System.out.print(f.toString() + ", ");
                }
                //System.out.println("");
            }


            //System.out.println(" \nAUGMENTING");
            for (double[] row : augMatrix){
                for (double f : row){
                    //System.out.print(f.toString() + ", ");
                }
                //System.out.println("");
            }

        }

        private interface FLCondition{ boolean loop(int counter, String direction);}

        private interface FLCounter{ int progress(int counter, String direction); }

        private interface FLInit{ int init(int counter, String direction); }


    public static void main(String[] args) {
        IM tools = new IM(
                new double[][]{
                        {6, 1, 3, 1, 5, 3},
                        {1, 4, 8, 4, 3, 0},
                        {9, 5, 3, 0, 3, 5},
                        {0, 3, 9, 7, 7, 7},
                        {4, 1, 9, 2, 9, 1},
                        {9, 6, 9, 1, 6, 1},
                }
        );

        double[][] inverse = tools.getInverse();


        for (double[] row : inverse){
            System.out.println(Arrays.toString(row));
        }
    }

    }