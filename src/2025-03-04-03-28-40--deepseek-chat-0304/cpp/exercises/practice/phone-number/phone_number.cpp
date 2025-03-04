#include "phone_number.h"
#include <algorithm>
#include <cctype>

namespace phone_number {

phone_number::phone_number(const std::string& input) {
    clean_number(input);
}

std::string phone_number::number() const {
    return digits;
}

std::string phone_number::area_code() const {
    return digits.substr(0, 3);
}

phone_number::operator std::string() const {
    return "(" + area_code() + ") " + digits.substr(3, 3) + "-" + digits.substr(6);
}

void phone_number::clean_number(const std::string& input) {
    // Remove all non-digit characters
    digits.reserve(input.size());
    for (char ch : input) {
        if (std::isdigit(ch)) {
            digits.push_back(ch);
        }
    }

    // Remove country code if present
    if (digits.length() == 11 && digits[0] == '1') {
        digits.erase(0, 1);
    }

    // Validate the number
    validate_number();
}

void phone_number::validate_number() const {
    // Check for invalid characters
    if (digits.empty()) {
        throw std::domain_error("Invalid phone number");
    }

    // Check length
    if (digits.length() == 11 && digits[0] != '1') {
        throw std::domain_error("11 digits must start with 1");
    }
    if (digits.length() < 10) {
        throw std::domain_error("Number too short");
    }
    if (digits.length() > 11) {
        throw std::domain_error("Number too long");
    }

    // If 11 digits, remove country code for further validation
    std::string num = digits;
    if (num.length() == 11) {
        num.erase(0, 1);
    }

    // Area code must start with 2-9
    if (num[0] < '2') {
        throw std::domain_error("Area code cannot start with 0 or 1");
    }

    // Exchange code must start with 2-9
    if (num[3] < '2') {
        throw std::domain_error("Exchange code cannot start with 0 or 1");
    }
}

}  // namespace phone_number
