#include "all_your_base.h"
#include <vector>
#include <algorithm>

namespace all_your_base {

std::vector<unsigned int> convert(unsigned int input_base, const std::vector<unsigned int>& digits, unsigned int output_base) {
    if (input_base < 2 || output_base < 2) {
        return {};
    }

    if (digits.empty()) {
        return {};
    }

    for (auto d : digits) {
        if (d >= input_base) {
            return {};
        }
    }

    if (digits.size() > 1 && digits[0] == 0) {
        return {};
    }

    unsigned int number = 0;
    for (auto d : digits) {
        number = number * input_base + d;
    }

    if (number == 0) {
        return {0};
    }

    std::vector<unsigned int> result;
    while (number > 0) {
        unsigned int rem = number % output_base;
        result.push_back(rem);
        number = number / output_base;
    }

    std::reverse(result.begin(), result.end());

    return result;
}

}  // namespace all_your_base
