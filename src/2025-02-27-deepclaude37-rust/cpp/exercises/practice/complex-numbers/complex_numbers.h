#if !defined(COMPLEX_NUMBERS_H)
#define COMPLEX_NUMBERS_H

namespace complex_numbers {

class Complex {
private:
    double real_;
    double imag_;
public:
    Complex(double real = 0.0, double imag = 0.0);
    double real() const { return real_; }
    double imag() const { return imag_; }
    Complex conj() const;
    double abs() const;
    Complex exp() const;
};

Complex operator+(const Complex& lhs, const Complex& rhs);
Complex operator-(const Complex& lhs, const Complex& rhs);
Complex operator*(const Complex& lhs, const Complex& rhs);
Complex operator/(const Complex& lhs, const Complex& rhs);

}  // namespace complex_numbers

#endif  // COMPLEX_NUMBERS_H
