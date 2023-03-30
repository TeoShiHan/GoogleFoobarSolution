public final class Fraction {
    private final int denominator;
    private final int numerator;

    public Fraction(int numerator, int denominator) {

        if (numerator == 0 || denominator == 0){
            this.numerator = 0;
            this.denominator = 1;
        }else {
            int gcd = GCD(denominator, numerator);
            int newNumerator = numerator/ gcd;
            int newDenominator = denominator/gcd;

            if ( (newDenominator < 0 && newNumerator < 0) || (newDenominator < 0 && newNumerator > 0)){
                newDenominator = -newDenominator;
                newNumerator = -newNumerator;
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
            int LCM = LCM(fraction.denominator, denominator);
            int newNumerator = (numerator * LCM / denominator) + (fraction.numerator * LCM / fraction.denominator);
            return new Fraction(newNumerator, LCM);
        }
    }

    public Fraction subtract(Fraction fraction){
        if (this.isZero()){
            return fraction.multiply(new Fraction(-1, 1));
        }else if (fraction.isZero()){
            return new Fraction(this.numerator, this.denominator);
        }else {
            int LCM = LCM(fraction.denominator, denominator);
            int newNumerator = (numerator * LCM / denominator) - (fraction.numerator * LCM / fraction.denominator);
            return new Fraction(newNumerator, LCM);
        }
    }

    public Fraction multiply(Fraction fraction){
        if (this.isZero() || fraction.isZero()){
            return new Fraction(0,1);
        }else {
            int LCM = LCM(fraction.denominator, denominator);
            int newNumerator = (numerator * LCM / denominator) - (fraction.numerator * LCM / fraction.denominator);
            return new Fraction(newNumerator, LCM);
        }
    }

    public Fraction divide(Fraction fraction){
        return this.multiply(new Fraction(fraction.denominator, fraction.numerator));
    }

    public boolean isZero(){
        return denominator == 0 || numerator ==0;
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
}
