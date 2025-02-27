import java.util.Objects;

class Rational {

    private final int numerator;
    private final int denominator;

    Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
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
        return new Rational(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    Rational divide(Rational other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        return new Rational(this.numerator * other.denominator, this.denominator * other.numerator);
    }

    Rational abs() {
        return new Rational(Math.abs(numerator), denominator);
    }

    private int intPow(int base, int exponent) {
        if (exponent == 0) return 1;
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }

    Rational pow(int power) {
        if (power < 0) {
            if (numerator == 0) {
                throw new ArithmeticException("Cannot raise zero to a negative power.");
            }
            int m = -power;
            return new Rational(intPow(denominator, m), intPow(numerator, m));
        } else {
            return new Rational(intPow(numerator, power), intPow(denominator, power));
        }
    }

    double exp(double exponent) {
        return Math.pow(numerator, exponent) / Math.pow(denominator, exponent);
    }

    public static double exp(double base, Rational exponent) {
        int a = exponent.getNumerator();
        int b = exponent.getDenominator();
        double powered = Math.pow(base, a);
        return Math.pow(powered, 1.0 / b);
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
