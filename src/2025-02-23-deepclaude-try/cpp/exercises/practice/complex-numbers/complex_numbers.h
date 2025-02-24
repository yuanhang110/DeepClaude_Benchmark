#if !defined(COMPLEX_NUMBERS_H)
#define COMPLEX_NUMBERS_H

namespace complex_numbers {

class Complex {
private:
    double real_;
    double imag_;

public:
    Complex(double real = 0.0, double imag = 0.0);
    
    // Basic arithmetic operations
    Complex operator+(const Complex& other) const;
    Complex operator-(const Complex& other) const;
    Complex operator*(const Complex& other) const;
    Complex operator/(const Complex& other) const;
    
    // Other operations
    Complex conj() const;
    double abs() const;
    Complex exp() const;
    
    // Getters
    double real() const { return real_; }
    double imag() const { return imag_; }

    // Friend operators for double-Complex operations
    friend Complex operator+(double lhs, const Complex& rhs);
    friend Complex operator-(double lhs, const Complex& rhs);
    friend Complex operator*(double lhs, const Complex& rhs);
    friend Complex operator/(double lhs, const Complex& rhs);
};

}  // namespace complex_numbers

#endif  // COMPLEX_NUMBERS_H
