#include "all_your_base.h"
#include <algorithm>
#include <stdexcept>

namespace all_your_base {

std::vector<unsigned int> convert(unsigned int input_base, const std::vector<unsigned int>& digits, unsigned int output_base) {
    if (input_base < 2 || output_base < 2) {
        throw std::invalid_argument("Invalid base");
    }

    for (auto d : digits) {
        if (d >= input_base) {
            throw std::invalid_argument("Invalid digit");
        }
    }

    if (digits.empty()) {
        throw std::invalid_argument("Empty digits");
    }

    unsigned long long sum = 0;
    for (auto d : digits) {
        sum = sum * input_base + d;
    }

    if (sum == 0) {
        return {0};
    }

    std::vector<unsigned int> output;
    while (sum > 0) {
        output.push_back(sum % output_base);
        sum /= output_base;
    }
    std::reverse(output.begin(), output.end());

    return output;
}

}  // namespace all_your_base
