#include "parallel_letter_frequency.h"
#include <execution>
#include <algorithm>
#include <cctype>

namespace parallel_letter_frequency {
    std::unordered_map<char, int> frequency(const std::vector<std::string>& texts) {
        return std::transform_reduce(
            std::execution::par_unseq,
            texts.begin(), texts.end(),
            std::unordered_map<char, int>{},
            [](auto&& a, auto&& b) {
                for (const auto& [key, value] : b) {
                    a[key] += value;
                }
                return a;
            },
            [](const std::string& text) {
                std::unordered_map<char, int> counts;
                for (char c : text) {
                    if (std::isalpha(c)) {
                        counts[std::tolower(c)]++;
                    }
                }
                return counts;
            }
        );
    }
}
