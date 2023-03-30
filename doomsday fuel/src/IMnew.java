import java.util.Arrays;

public class IMnew {
    double[][] oriMatrix;
    double[][] augMatrix;
    int BELOW_DIAGONAL_ALL_ZERO = -999;

    IMnew(double[][] doubleM){
        this.oriMatrix = doubleM;
        initAugMatrix();




        {   System.out.println("\n----------INIT----------");

            System.out.println("\nORI MATRIX");
            for (double[] row : oriMatrix){
                System.out.println(Arrays.toString(row));
            }

            System.out.println("\nAUGMENTING MATRIX");
            for (double[] row : augMatrix){
                System.out.println(Arrays.toString(row));
            }
        }


        makeLeftBottomPivot();
        makeRightTopPivot();


        {   System.out.println("\n----------FINAL----------");

            System.out.println("\nORI MATRIX");
            for (double[] row : oriMatrix){
                System.out.println(Arrays.toString(row));
            }

            System.out.println("\nAUGMENTING MATRIX");
            for (double[] row : augMatrix){
                System.out.println(Arrays.toString(row));
            }
        }


    }


    public static void main(String[] args) {

        double[][] arr = new double[][]{
                {8 ,2 ,9 ,6 ,5 ,9 ,7 ,4 ,9 ,5},
                {7 ,7 ,6 ,6 ,8 ,0 ,4 ,7 ,8 ,3},
                {3 ,5 ,1 ,6 ,5 ,3 ,0 ,9 ,6 ,5},
                {7 ,6 ,6 ,4 ,6 ,7 ,9 ,3 ,3 ,2},
                {4 ,7 ,9 ,7 ,2 ,7 ,8 ,8 ,3 ,5},
                {9 ,6 ,8 ,4 ,8 ,4 ,7 ,0 ,4 ,0},
                {3 ,9 ,4 ,5 ,9 ,0 ,5 ,0 ,6 ,8},
                {1 ,9 ,5 ,4 ,1 ,0 ,0 ,1 ,5 ,7},
                {7 ,3 ,0 ,3 ,1 ,6 ,6 ,4 ,4 ,7},
                {6 ,0 ,4 ,7 ,0 ,5 ,4 ,8 ,9 ,9},
        };

        IMnew imnew = new IMnew(arr);


    }



    private void initAugMatrix(){
        double[][] temp = new double[oriMatrix.length][oriMatrix.length];
        for (int i = 0 ; i < oriMatrix.length ; i++){
            for (int j = 0 ; j < oriMatrix.length ; j++){
                temp[i][j] = (i == j)? 1 : 0;
            }
        }
        this.augMatrix = temp;
    }


    private void makeLeftBottomPivot(){
        for (int column = 0 ; column < oriMatrix.length ; column++){

            System.out.println("TRY SETTLE COLUMN +==" + column);

            double diagonal = oriMatrix[column][column];

            if (diagonal == 0){
                int nonZero = findNonZeroRowBelowDiagonal(column);
                if (nonZero != BELOW_DIAGONAL_ALL_ZERO){
                    swapRow(nonZero, column);
                    --column;

                }else {
                    // nothing can do
                }
            }else {

                System.out.println(">>>>>>> MAKING 1 FOR COL" + column);

                makeDiagonalTo1(column);

                System.out.println(">>>>>>> AFTER MNAKING 1 1 FOR COL" + column);

                System.out.println("\nORI MATRIX");
                for (double[] rows : oriMatrix){
                    for (double d : rows){
                        System.out.print(String.format("%.2f", d) + "\t");
                    }
                    System.out.println("");
                }


                for (int row = column+1 ; row < oriMatrix.length ; row++ ){
                    double constantToForm0 = -oriMatrix[row][column];
                    if (oriMatrix[row][column] != 0){
                        {
                            System.out.println("\nSETTLE THE [" + column + "," + column + "]");
                            System.out.println("\nOPERATION = " + " + " + constantToForm0 + " with ");
                            System.out.println("\nAUGMENTING MATRIX");

                            for (double[] rows : augMatrix){
                                for (double d : rows){
                                    System.out.print(String.format("%.2f", d) + "\t");
                                }
                                System.out.println("");
                            }

                            System.out.println("\nORI MATRIX");
                            for (double[] rows : oriMatrix){
                                for (double d : rows){
                                    System.out.print(String.format("%.2f", d) + "\t");
                                }
                                System.out.println("");
                            }
                        }
                        oriMatrix[row] = applyRowValueToOtherRow(oriMatrix[row], oriMatrix[column], constantToForm0);
                        augMatrix[row] = applyRowValueToOtherRow(augMatrix[row], augMatrix[column], constantToForm0);
                    }
                }
            }
        }
        {
            for (double[] rows : augMatrix){
                for (double d : rows){
                    System.out.print(String.format("%.0f", d) + "\t");
                }
                System.out.println("");
            }

            System.out.println("\nORI MATRIX");
            for (double[] rows : oriMatrix){
                for (double d : rows){
                    System.out.print(String.format("%.0f", d) + "\t");
                }
                System.out.println("");
            }
        }
    }



    private void makeRightTopPivot(){
        for (int column = oriMatrix.length-1 ; column >= 0 ; column--){
            for (int row = column-1 ; row >= 0 ; row--){
                if (oriMatrix[row][column] != 0){
                    double constantToForm0 = -oriMatrix[row][column];
                    oriMatrix[row] = applyRowValueToOtherRow(oriMatrix[row], oriMatrix[column], constantToForm0);
                    augMatrix[row] = applyRowValueToOtherRow(augMatrix[row], augMatrix[column], constantToForm0);
                }
            }
        }
    }


    private int findNonZeroRowBelowDiagonal(int column){
        for (int row = column+1 ; row < oriMatrix.length ; row++){
            if (oriMatrix[row][column] != 0){
               return row;
            }
        }
        return BELOW_DIAGONAL_ALL_ZERO;
    }

    private void swapRow(int row1_index, int row2_index){
        double[] temp = oriMatrix[row1_index];
        oriMatrix[row1_index] = oriMatrix[row2_index];
        oriMatrix[row2_index] = temp;
        double[] tempAug = augMatrix[row1_index];
        augMatrix[row1_index] = augMatrix[row2_index];
        augMatrix[row2_index] = tempAug;
    }

    private void makeDiagonalTo1(int diagonalPosition){
        double constantToForm1 = 1 / oriMatrix[diagonalPosition][diagonalPosition];
        for (int col = 0 ; col < oriMatrix.length ; col++ ){
            oriMatrix[diagonalPosition][col] *= constantToForm1;
            augMatrix[diagonalPosition][col] *= constantToForm1;
        }
    }

    private double[] applyRowValueToOtherRow(double[]beApplied, double[]applier, double constant){


        // System.out.println("APPLY " + Arrays.toString(applier) + " * " + constant + " TO " + Arrays.toString(beApplied));

        for (int i = 0 ; i < beApplied.length ; i++){
//            {
//                System.out.println( beApplied[i] + " + " + constant + " * " + applier[i] + " = " + beApplied[i]+constant*applier[i] );
//
//            }
            beApplied[i] = beApplied[i]+(constant*applier[i]);
        }
        return beApplied;
    }
}
