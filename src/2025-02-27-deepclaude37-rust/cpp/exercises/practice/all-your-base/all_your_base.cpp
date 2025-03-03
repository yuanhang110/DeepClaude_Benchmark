#include "all_your_base.h"
#include <vector>
#include <algorithm>
#include <stdexcept>

namespace all_your_base {

std::vector<int> convert(int input_base, const std::vector<int>& digits, int output_base) {
    // Validate input
    if (input_base < 2) {
        throw std::invalid_argument("input base must be >= 2");
    }
    
    if (output_base < 2) {
        throw std::invalid_argument("output base must be >= 2");
    }

    if (digits.empty()) {
        throw std::invalid_argument("digits may not be empty");
    }

    // Check for invalid digits and leading zeros
    for (int d : digits) {
        if (d < 0 || d >= input_base) {
            throw std::invalid_argument("all digits must satisfy 0 <= d < input base");
        }
    }

    // Calculate the value in decimal (base 10)
    long long value = 0;
    for (int d : digits) {
        value = value * input_base + d;
    }

    // Convert to output base
    std::vector<int> result;

    if (value == 0) {
        result.push_back(0);
    } else {
        while (value > 0) {
            int remainder = value % output_base;
            result.push_back(remainder);
            value /= output_base;
        }
        std::reverse(result.begin(), result.end());
    }

    return result;
}

}  // namespace all_your_base
