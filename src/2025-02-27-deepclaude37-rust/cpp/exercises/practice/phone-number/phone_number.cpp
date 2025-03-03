#include "phone_number.h"
#include <cctype>
#include <stdexcept>

namespace phone_number {

phone_number::phone_number(const std::string& s) {
    std::string digits;
    for (char c : s) {
        if (std::isdigit(c)) {
            digits.push_back(c);
        }
    }

    if (digits.length() == 11 && digits[0] == '1') {
        digits = digits.substr(1);
    }

    if (digits.length() != 10) {
        throw std::domain_error("Invalid phone number");
    }

    if (digits[0] < '2' || digits[0] > '9') {
        throw std::domain_error("Area code must start with 2-9");
    }

    if (digits[3] < '2' || digits[3] > '9') {
        throw std::domain_error("Exchange code must start with 2-9");
    }

    num = digits;
}

}  // namespace phone_number
