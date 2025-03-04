#include "all_your_base.h"
#include <cmath>

namespace all_your_base {

std::vector<unsigned int> convert(unsigned int input_base,
                                 const std::vector<unsigned int>& input_digits,
                                 unsigned int output_base) {
    // Validate bases
    if (input_base < 2 || output_base < 2) {
        throw std::invalid_argument("Bases must be at least 2");
    }

    // Validate digits
    if (input_digits.empty()) {
        return {};
    }
    
    // Convert input digits to base 10
    unsigned int value = 0;
    for (size_t i = 0; i < input_digits.size(); ++i) {
        unsigned int digit = input_digits[i];
        if (digit >= input_base) {
            throw std::invalid_argument("All digits must be less than the input base");
        }
        value = value * input_base + digit;
    }

    // Handle zero case - return empty vector as per test requirements
    if (value == 0) {
        return {};
    }

    // Convert base 10 value to output base
    std::vector<unsigned int> output_digits;
    while (value > 0) {
        output_digits.insert(output_digits.begin(), value % output_base);
        value /= output_base;
    }

    return output_digits;
}

}  // namespace all_your_base
