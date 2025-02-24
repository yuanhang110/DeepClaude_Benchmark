#include "all_your_base.h"
#include <algorithm>

namespace all_your_base {
    std::vector<unsigned int> convert(unsigned int input_base,
                                    const std::vector<unsigned int>& input_digits,
                                    unsigned int output_base) {
        // Validate bases
        if (input_base < 2 || output_base < 2) {
            return {};
        }

        // Handle empty input
        if (input_digits.empty()) {
            return {};
        }

        // Validate digits and convert to decimal
        unsigned long long decimal = 0;
        bool all_zeros = true;

        for (unsigned int digit : input_digits) {
            // Check if digit is valid for input base
            if (digit >= input_base) {
                return {};
            }
            if (digit != 0) {
                all_zeros = false;
            }
            decimal = decimal * input_base + digit;
        }

        // Handle special case of all zeros
        if (all_zeros) {
            return {0};
        }

        // Convert decimal to output base
        std::vector<unsigned int> result;
        while (decimal > 0) {
            result.push_back(decimal % output_base);
            decimal /= output_base;
        }

        // Reverse the result to get correct order
        std::reverse(result.begin(), result.end());

        return result;
    }
}  // namespace all_your_base
