import java.util.Objects;

class Rational {
    private final int numerator;
    private final int denominator;

    Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }
        int gcd = computeGCD(numerator, denominator);
        int reducedNum = numerator / gcd;
        int reducedDen = denominator / gcd;
        if (reducedDen < 0) {
            reducedNum *= -1;
            reducedDen *= -1;
        }
        this.numerator = reducedNum;
        this.denominator = reducedDen;
    }

    private int computeGCD(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    int getNumerator() {
        return numerator;
    }

    int getDenominator() {
        return denominator;
    }

    Rational add(Rational other) {
        int newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Rational(newNumerator, newDenominator);
    }

    Rational subtract(Rational other) {
        int newNumerator = this.numerator * other.denominator - other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Rational(newNumerator, newDenominator);
    }

    Rational multiply(Rational other) {
        int newNumerator = this.numerator * other.numerator;
        int newDenominator = this.denominator * other.denominator;
        return new Rational(newNumerator, newDenominator);
    }

    Rational divide(Rational other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        int newNumerator = this.numerator * other.denominator;
        int newDenominator = this.denominator * other.numerator;
        return new Rational(newNumerator, newDenominator);
    }

    Rational abs() {
        return new Rational(Math.abs(numerator), denominator);
    }

    Rational pow(int power) {
        if (power == 0) {
            return new Rational(1, 1);
        } else if (power > 0) {
            int newNum = (int) Math.pow(numerator, power);
            int newDen = (int) Math.pow(denominator, power);
            return new Rational(newNum, newDen);
        } else {
            int m = -power;
            int newNum = (int) Math.pow(denominator, m);
            int newDen = (int) Math.pow(numerator, m);
            return new Rational(newNum, newDen);
        }
    }

    double exp(double exponent) {
        return Math.pow(numerator, exponent) / Math.pow(denominator, exponent);
    }

    @Override
    public String toString() {
        return String.format("%d/%d", this.getNumerator(), this.getDenominator());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rational other) {
            return this.getNumerator() == other.getNumerator()
                    && this.getDenominator() == other.getDenominator();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getNumerator(), this.getDenominator());
    }
}
