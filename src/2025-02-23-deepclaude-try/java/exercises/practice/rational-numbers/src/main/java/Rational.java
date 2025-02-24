import java.util.Objects;

class Rational {
    private final int numerator;
    private final int denominator;
    
    Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        
        // Ensure denominator is positive
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        
        // Reduce the fraction
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }
    
    private static int gcd(int a, int b) {
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
        return new Rational(
            numerator * other.denominator + other.numerator * denominator,
            denominator * other.denominator
        );
    }

    Rational subtract(Rational other) {
        return new Rational(
            numerator * other.denominator - other.numerator * denominator,
            denominator * other.denominator
        );
    }

    Rational multiply(Rational other) {
        return new Rational(
            numerator * other.numerator,
            denominator * other.denominator
        );
    }

    Rational divide(Rational other) {
        return new Rational(
            numerator * other.denominator,
            denominator * other.numerator
        );
    }

    Rational abs() {
        return new Rational(Math.abs(numerator), denominator);
    }

    private int powInt(int base, int exponent) {
        if (exponent == 0) return 1;
        if (base == 0) {
            if (exponent < 0) {
                throw new IllegalArgumentException("Cannot raise zero to a negative power.");
            }
            return 0;
        }
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }

    Rational pow(int power) {
        if (power >= 0) {
            return new Rational(
                powInt(numerator, power),
                powInt(denominator, power)
            );
        } else {
            int absPower = -power;
            return new Rational(
                powInt(denominator, absPower),
                powInt(numerator, absPower)
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
