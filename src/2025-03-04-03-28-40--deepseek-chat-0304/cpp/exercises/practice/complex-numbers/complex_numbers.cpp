#include "complex_numbers.h"

namespace complex_numbers {

Complex::Complex(double real, double imaginary) 
    : real_(real), imaginary_(imaginary) {}

double Complex::real() const {
    return real_;
}

double Complex::imag() const {
    return imaginary_;
}

Complex Complex::conj() const {
    return Complex(real_, -imaginary_);
}

double Complex::abs() const {
    return std::sqrt(real_ * real_ + imaginary_ * imaginary_);
}

Complex Complex::operator+(const Complex& other) const {
    return Complex(real_ + other.real_, imaginary_ + other.imaginary_);
}

Complex Complex::operator-(const Complex& other) const {
    return Complex(real_ - other.real_, imaginary_ - other.imaginary_);
}

Complex Complex::operator*(const Complex& other) const {
    return Complex(
        real_ * other.real_ - imaginary_ * other.imaginary_,
        imaginary_ * other.real_ + real_ * other.imaginary_
    );
}

Complex Complex::operator/(const Complex& other) const {
    double denominator = other.real_ * other.real_ + other.imaginary_ * other.imaginary_;
    return Complex(
        (real_ * other.real_ + imaginary_ * other.imaginary_) / denominator,
        (imaginary_ * other.real_ - real_ * other.imaginary_) / denominator
    );
}

Complex Complex::exp() const {
    double exp_real = std::exp(real_);
    return Complex(
        exp_real * std::cos(imaginary_),
        exp_real * std::sin(imaginary_)
    );
}

// Non-member operator implementations
Complex operator+(double lhs, const Complex& rhs) {
    return Complex(lhs + rhs.real(), rhs.imag());
}

Complex operator-(double lhs, const Complex& rhs) {
    return Complex(lhs - rhs.real(), -rhs.imag());
}

Complex operator*(double lhs, const Complex& rhs) {
    return Complex(lhs * rhs.real(), lhs * rhs.imag());
}

Complex operator/(double lhs, const Complex& rhs) {
    double denominator = rhs.real() * rhs.real() + rhs.imag() * rhs.imag();
    return Complex(
        (lhs * rhs.real()) / denominator,
        (-lhs * rhs.imag()) / denominator
    );
}

}  // namespace complex_numbers
