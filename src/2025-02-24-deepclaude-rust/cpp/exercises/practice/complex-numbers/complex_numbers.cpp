#include "complex_numbers.h"

namespace complex_numbers {

Complex::Complex(double real, double imag) : real_(real), imag_(imag) {}

Complex Complex::operator+(const Complex& rhs) const {
    return Complex(real_ + rhs.real_, imag_ + rhs.imag_);
}

Complex Complex::operator-(const Complex& rhs) const {
    return Complex(real_ - rhs.real_, imag_ - rhs.imag_);
}

Complex Complex::operator*(const Complex& rhs) const {
    return Complex(real_ * rhs.real_ - imag_ * rhs.imag_,
                  imag_ * rhs.real_ + real_ * rhs.imag_);
}

Complex Complex::operator/(const Complex& rhs) const {
    double denominator = rhs.real_ * rhs.real_ + rhs.imag_ * rhs.imag_;
    return Complex((real_ * rhs.real_ + imag_ * rhs.imag_) / denominator,
                  (imag_ * rhs.real_ - real_ * rhs.imag_) / denominator);
}

Complex Complex::conj() const {
    return Complex(real_, -imag_);
}

double Complex::abs() const {
    return std::sqrt(real_ * real_ + imag_ * imag_);
}

Complex Complex::exp() const {
    double exp_real = std::exp(real_);
    return Complex(exp_real * std::cos(imag_),
                  exp_real * std::sin(imag_));
}

Complex operator+(double lhs, const Complex& rhs) {
    return Complex(lhs) + rhs;
}

Complex operator-(double lhs, const Complex& rhs) {
    return Complex(lhs) - rhs;
}

Complex operator*(double lhs, const Complex& rhs) {
    return Complex(lhs) * rhs;
}

Complex operator/(double lhs, const Complex& rhs) {
    return Complex(lhs) / rhs;
}

}  // namespace complex_numbers
