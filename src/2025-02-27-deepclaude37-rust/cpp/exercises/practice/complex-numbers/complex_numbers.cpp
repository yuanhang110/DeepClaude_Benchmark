#include "complex_numbers.h"
#include <cmath>

namespace complex_numbers {

Complex::Complex(double real, double imag) : real_(real), imag_(imag) {}

Complex Complex::conj() const {
    return Complex(real_, -imag_);
}

double Complex::abs() const {
    return std::sqrt(real_ * real_ + imag_ * imag_);
}

Complex operator+(const Complex& lhs, const Complex& rhs) {
    return Complex(lhs.real() + rhs.real(), lhs.imag() + rhs.imag());
}

Complex operator-(const Complex& lhs, const Complex& rhs) {
    return Complex(lhs.real() - rhs.real(), lhs.imag() - rhs.imag());
}

Complex operator*(const Complex& lhs, const Complex& rhs) {
    double a = lhs.real();
    double b = lhs.imag();
    double c = rhs.real();
    double d = rhs.imag();
    return Complex(a * c - b * d, b * c + a * d);
}

Complex operator/(const Complex& lhs, const Complex& rhs) {
    double a = lhs.real();
    double b = lhs.imag();
    double c = rhs.real();
    double d = rhs.imag();
    double denominator = c * c + d * d;
    return Complex((a * c + b * d) / denominator, (b * c - a * d) / denominator);
}

Complex Complex::exp() const {
    double a = real_;
    double b = imag_;
    double ea = std::exp(a);
    return Complex(ea * std::cos(b), ea * std::sin(b));
}

}  // namespace complex_numbers
