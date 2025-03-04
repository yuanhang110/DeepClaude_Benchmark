import java.util.Objects;

class Rational {
    private final int numerator;
    private final int denominator;

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private Rational reduce() {
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        int reducedNum = numerator / gcd;
        int reducedDen = denominator / gcd;
        
        // Ensure denominator is positive
        if (reducedDen < 0) {
            reducedNum *= -1;
            reducedDen *= -1;
        }
        
        return new Rational(reducedNum, reducedDen);
    }

    Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }
        
        // Create reduced rational first
        Rational reduced = new Rational(numerator, denominator, false).reduce();
        this.numerator = reduced.numerator;
        this.denominator = reduced.denominator;
    }
    
    // Private constructor for internal use only
    private Rational(int numerator, int denominator, boolean dummy) {
        this.numerator = numerator;
        this.denominator = denominator;
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
        int newNum = this.numerator * other.numerator;
        int newDen = this.denominator * other.denominator;
        return new Rational(newNum, newDen);
    }

    Rational divide(Rational other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        int newNum = this.numerator * other.denominator;
        int newDen = this.denominator * other.numerator;
        return new Rational(newNum, newDen);
    }

    Rational abs() {
        return new Rational(Math.abs(this.numerator), Math.abs(this.denominator));
    }

    Rational pow(int power) {
        if (power >= 0) {
            return new Rational(
                (int)Math.pow(this.numerator, power),
                (int)Math.pow(this.denominator, power)
            );
        } else {
            int m = Math.abs(power);
            return new Rational(
                (int)Math.pow(this.denominator, m),
                (int)Math.pow(this.numerator, m)
            );
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
