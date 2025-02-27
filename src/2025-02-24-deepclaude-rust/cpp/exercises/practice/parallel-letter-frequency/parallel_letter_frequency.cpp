#include "parallel_letter_frequency.h"
#include <algorithm>
#include <execution>
#include <cctype>

namespace parallel_letter_frequency {
    std::array<int, 26> frequency(const std::vector<std::string>& texts) {
        return std::transform_reduce(
            std::execution::par_unseq,
            texts.begin(), texts.end(),
            std::array<int, 26>{},
            // Reduction operation: element-wise addition of arrays
            [](const auto& a, const auto& b) {
                std::array<int, 26> result;
                for (size_t i = 0; i < 26; ++i) {
                    result[i] = a[i] + b[i];
                }
                return result;
            },
            // Transform operation: count letters in each text
            [](const std::string& text) {
                std::array<int, 26> freq{};
                for (char c : text) {
                    c = std::tolower(c);
                    if (c >= 'a' && c <= 'z') {
                        ++freq[c - 'a'];
                    }
                }
                return freq;
            }
        );
    }
}
