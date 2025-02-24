#include "all_your_base.h"
#include <algorithm>

namespace all_your_base {

std::vector<unsigned int> convert(const std::vector<unsigned int>& digits,
                                unsigned int input_base,
                                unsigned int output_base) {
    // Validate bases
    if (input_base < 2 || output_base < 2) {
        return {};
    }
    
    // Validate input digits
    if (digits.empty()) {
        return {};
    }
    
    // Check for leading zeros or invalid digits
    if ((digits.size() > 1 && digits[0] == 0) || 
        std::any_of(digits.begin(), digits.end(), [input_base](unsigned int d) { return d >= input_base; })) {
        return {};
    }
    
    // Calculate numeric value
    unsigned long long number = 0;
    for (auto d : digits) {
        number = number * input_base + d;
    }
    
    // Handle zero case
    if (number == 0) {
        return {0};
    }
    
    // Convert to output base
    std::vector<unsigned int> output;
    while (number > 0) {
        output.push_back(number % output_base);
        number /= output_base;
    }
    std::reverse(output.begin(), output.end());
    
    return output;
}

}  // namespace all_your_base
