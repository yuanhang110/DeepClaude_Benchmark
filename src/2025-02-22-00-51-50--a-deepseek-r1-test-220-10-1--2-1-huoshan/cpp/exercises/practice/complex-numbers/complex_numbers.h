#if !defined(COMPLEX_NUMBERS_H)
#define COMPLEX_NUMBERS_H

namespace complex_numbers {

class Complex {
public:
    Complex(double real, double imaginary = 0.0);  // Add default value for imaginary
    double real() const;
    double imag() const;  // Rename to match test's .imag()
    
    // Convert free functions to member functions
    double abs() const;
    Complex conj() const;
    Complex exp() const;

private:
    double real_;
    double imaginary_;
};

// Add double-compatible operator overloads
Complex operator+(const Complex& lhs, double rhs);
Complex operator+(double lhs, const Complex& rhs);
Complex operator-(const Complex& lhs, double rhs); 
Complex operator-(double lhs, const Complex& rhs);
Complex operator*(const Complex& lhs, double rhs);
Complex operator*(double lhs, const Complex& rhs);
Complex operator/(const Complex& lhs, double rhs);
Complex operator/(double lhs, const Complex& rhs);

// Original complex-complex operators
Complex operator+(const Complex& lhs, const Complex& rhs);
Complex operator-(const Complex& lhs, const Complex& rhs);
Complex operator*(const Complex& lhs, const Complex& rhs);
Complex operator/(const Complex& lhs, const Complex& rhs);

}  // namespace complex_numbers

#endif  // COMPLEX_NUMBERS_H
