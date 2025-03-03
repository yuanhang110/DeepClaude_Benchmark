import java.util.Objects;

class Rational {

    private final int numerator;
    private final int denominator;

    Rational(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        
        // Handle sign - ensure denominator is positive
        boolean needSignFlip = denominator < 0;
        
        // Find GCD using absolute values
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        
        // Reduce to lowest terms
        this.numerator = numerator / gcd;
        this.denominator = Math.abs(denominator) / gcd;
        
        // Apply sign correction if needed
        if (needSignFlip) {
            this.numerator = -this.numerator;
        }
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
            throw new ArithmeticException("Cannot divide by zero.");
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
        }
        
        if (numerator == 0) {
            if (power < 0) {
                throw new ArithmeticException("Cannot raise zero to a negative power");
            }
            return new Rational(0, 1);
        }
        
        if (power > 0) {
            int newNumerator = 1;
            int newDenominator = 1;
            
            for (int i = 0; i < power; i++) {
                newNumerator *= numerator;
                newDenominator *= denominator;
            }
            
            return new Rational(newNumerator, newDenominator);
        } else {
            int m = Math.abs(power);
            int newNumerator = 1;
            int newDenominator = 1;
            
            for (int i = 0; i < m; i++) {
                newNumerator *= denominator;
                newDenominator *= numerator;
            }
            
            return new Rational(newNumerator, newDenominator);
        }
    }

    double exp(double exponent) {
        if (numerator == 0 && exponent < 0) {
            throw new ArithmeticException("Cannot raise zero to a negative power");
        }
        
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
