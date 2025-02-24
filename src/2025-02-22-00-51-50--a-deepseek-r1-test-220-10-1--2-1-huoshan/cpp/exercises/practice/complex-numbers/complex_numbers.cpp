#include "complex_numbers.h"
#include <cmath>

namespace complex_numbers {

Complex::Complex(double real, double imaginary) : real_(real), imaginary_(imaginary) {}

double Complex::real() const {
    return real_;
}

double Complex::imag() const {
    return imaginary_;
}

// Member function implementations
double Complex::abs() const {
    return std::sqrt(real_ * real_ + imaginary_ * imaginary_);
}

Complex Complex::conj() const {
    return Complex(real_, -imaginary_);
}

Complex Complex::exp() const {
    double e_a = std::exp(real_);
    return Complex(e_a * std::cos(imaginary_), e_a * std::sin(imaginary_));
}

Complex operator+(const Complex& lhs, const Complex& rhs) {
    return Complex(lhs.real() + rhs.real(), lhs.imaginary() + rhs.imaginary());
}

Complex operator-(const Complex& lhs, const Complex& rhs) {
    return Complex(lhs.real() - rhs.real(), lhs.imaginary() - rhs.imaginary());
}

Complex operator*(const Complex& lhs, const Complex& rhs) {
    double a = lhs.real();
    double b = lhs.imaginary();
    double c = rhs.real();
    double d = rhs.imaginary();
    return Complex(a * c - b * d, b * c + a * d);
}

Complex operator/(const Complex& lhs, const Complex& rhs) {
    double a = lhs.real();
    double b = lhs.imaginary();
    double c = rhs.real();
    double d = rhs.imaginary();
    double denominator = c * c + d * d;
    return Complex((a * c + b * d) / denominator, (b * c - a * d) / denominator);
}

// Implement double-compatible operators
Complex operator+(const Complex& lhs, double rhs) { return lhs + Complex(rhs); }
Complex operator+(double lhs, const Complex& rhs) { return Complex(lhs) + rhs; }
Complex operator-(const Complex& lhs, double rhs) { return lhs - Complex(rhs); }
Complex operator-(double lhs, const Complex& rhs) { return Complex(lhs) - rhs; }
Complex operator*(const Complex& lhs, double rhs) { return lhs * Complex(rhs); }
Complex operator*(double lhs, const Complex& rhs) { return Complex(lhs) * rhs; }
Complex operator/(const Complex& lhs, double rhs) { return lhs / Complex(rhs); }
Complex operator/(double lhs, const Complex& rhs) { return Complex(lhs) / rhs; }

}  // namespace complex_numbers
