import java.util.Objects;

class Rational {
    private final int numerator;
    private final int denominator;

    Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }
        
        int gcd = gcd(numerator, denominator);
        int reducedNum = numerator / gcd;
        int reducedDen = denominator / gcd;
        
        // Ensure denominator is positive
        if (reducedDen < 0) {
            reducedNum = -reducedNum;
            reducedDen = -reducedDen;
        }
        
        this.numerator = reducedNum;
        this.denominator = reducedDen;
    }

    private static int gcd(int a, int b) {
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
        int newNum = this.numerator * other.denominator + other.numerator * this.denominator;
        int newDen = this.denominator * other.denominator;
        return new Rational(newNum, newDen);
    }

    Rational subtract(Rational other) {
        int newNum = this.numerator * other.denominator - other.numerator * this.denominator;
        int newDen = this.denominator * other.denominator;
        return new Rational(newNum, newDen);
    }

    Rational multiply(Rational other) {
        return new Rational(
            this.numerator * other.numerator,
            this.denominator * other.denominator
        );
    }

    Rational divide(Rational other) {
        return new Rational(
            this.numerator * other.denominator,
            this.denominator * other.numerator
        );
    }

    Rational abs() {
        return new Rational(Math.abs(numerator), denominator);
    }

    Rational pow(int power) {
        if (power >= 0) {
            return new Rational(
                (int) Math.pow(numerator, power),
                (int) Math.pow(denominator, power)
            );
        }
        int m = -power;
        return new Rational(
            (int) Math.pow(denominator, m),
            (int) Math.pow(numerator, m)
        );
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
