#if !defined(COMPLEX_NUMBERS_H)
#define COMPLEX_NUMBERS_H

#include <cmath>

namespace complex_numbers {

class Complex {
public:
    Complex(double real = 0.0, double imag = 0.0);
    
    Complex operator+(const Complex& rhs) const;
    Complex operator-(const Complex& rhs) const;
    Complex operator*(const Complex& rhs) const;
    Complex operator/(const Complex& rhs) const;
    
    friend Complex operator+(double lhs, const Complex& rhs);
    friend Complex operator-(double lhs, const Complex& rhs);
    friend Complex operator*(double lhs, const Complex& rhs);
    friend Complex operator/(double lhs, const Complex& rhs);
    
    Complex conj() const;
    double abs() const;
    Complex exp() const;
    
    double real() const { return real_; }
    double imag() const { return imag_; }
    
private:
    double real_;
    double imag_;
};

}  // namespace complex_numbers

#endif  // COMPLEX_NUMBERS_H
