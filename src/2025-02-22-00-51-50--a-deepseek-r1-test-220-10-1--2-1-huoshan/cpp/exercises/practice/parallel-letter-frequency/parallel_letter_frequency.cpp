#include "parallel_letter_frequency.h"
#include <algorithm>
#include <execution>
#include <numeric>
#include <cctype>

namespace parallel_letter_frequency {

std::unordered_map<char, int> frequency(const std::vector<std::string>& texts) {
    return std::transform_reduce(
        std::execution::par_unseq,
        texts.begin(), texts.end(),
        std::unordered_map<char, int>(),
        // Reduction: Merge two maps by summing their counts
        [](std::unordered_map<char, int> a, const std::unordered_map<char, int>& b) {
            for (const auto& [key, value] : b) {
                a[key] += value;
            }
            return a;
        },
        // Transformation: Convert each text to a frequency map
        [](const std::string& text) {
            std::unordered_map<char, int> freq;
            for (char c : text) {
                if (std::isalpha(c)) {
                    freq[static_cast<char>(std::tolower(c))]++;
                }
            }
            return freq;
        }
    );
}

}
