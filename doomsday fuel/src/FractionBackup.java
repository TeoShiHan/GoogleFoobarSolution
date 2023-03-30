public final class FractionBackup {
    private final int numerator;
    private final int denominator;

    private FractionBackup(int numerator, int denominator){
        this.numerator = numerator;
        this.denominator = denominator;
    }

    static FractionBackup F(int numerator, int denominator){
        return (new FractionBackup(numerator, denominator)).simplifyFraction();
    }

    public String toString(){
        return numerator + "/" + denominator;
    }

    public FractionBackup multiply(FractionBackup fraction){
        int newNumerator = fraction.numerator * numerator;
        int newDenominator = fraction.denominator * denominator;
        FractionBackup f = new FractionBackup(newNumerator, newDenominator);
        f.simplifyFraction();
        return f;
    }

    public FractionBackup subtract(FractionBackup fraction){
        if (this.isZero()){
            return fraction.multiply(new FractionBackup(-1, 1));
        }else if (fraction.isZero()){
            return F(numerator, denominator);
        }else {
            int LCM = LCM(fraction.denominator, denominator);
            int newNumerator = (numerator * LCM / denominator) - (fraction.numerator * LCM / fraction.denominator);
            return F(newNumerator, LCM);
        }
    }

    public FractionBackup add(FractionBackup fraction){
        if (this.isZero()){
            return F(fraction.numerator, fraction.denominator);
        }else if (fraction.isZero()){
            return F(numerator,denominator);
        }else {
            FractionBackup f = this.subtract(fraction.multiply(new FractionBackup(-1, 1)));
            return f.simplifyFraction();
        }
    }

    public FractionBackup divide(FractionBackup fraction){
        FractionBackup divideResult = this.multiply(F(fraction.denominator,fraction.numerator));
        return divideResult.simplifyFraction();
    }

    public FractionBackup simplifyFraction(){
        if (!this.isZero()){
            int gcd = GCD(denominator, numerator);
            int newNumerator = numerator/ gcd;
            int newDenominator = denominator/gcd;

            if ((newDenominator < 0 && newNumerator < 0) || (newDenominator < 0 && newNumerator > 0)){
                return new FractionBackup(-newNumerator, -newDenominator);
            }
            return new FractionBackup(newNumerator, newDenominator);

        }else {
            return new FractionBackup(0,1);
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


    public static void main(String[] args) {
        System.out.println(F(0,0));
        System.out.println(F(0,0).multiply(F(0,0)));
        System.out.println(F(1,2).add(F(1,2)));
        System.out.println(F(1,3).add(F(1,4)));
        System.out.println(F(1,3).multiply(F(1,4)));
        System.out.println(F(1,3).divide(F(0,4)));
        System.out.println(F(1,3).subtract(F(1,4)));
        System.out.println(F(2,12).subtract(F(6,12)));
        System.out.println(F(2,12).divide(F(6,12)));

    }
}