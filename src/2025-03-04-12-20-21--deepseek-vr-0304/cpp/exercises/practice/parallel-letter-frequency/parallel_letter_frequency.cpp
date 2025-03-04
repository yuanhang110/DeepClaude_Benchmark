#include "parallel_letter_frequency.h"
#include <algorithm>
#include <array>
#include <cctype>
#include <execution>

namespace parallel_letter_frequency {

std::map<char, int> frequency(const std::vector<std::string>& texts) {
    // Array to hold counts for each letter a-z
    using LetterCounts = std::array<int, 26>;
    
    // Transform each text into its letter counts and reduce by summing them
    LetterCounts total_counts = std::transform_reduce(
        std::execution::par_unseq,
        texts.begin(),
        texts.end(),
        LetterCounts{}, // Initial value
        [](const LetterCounts& a, const LetterCounts& b) { // Reduce
            LetterCounts result;
            for (int i = 0; i < 26; ++i) {
                result[i] = a[i] + b[i];
            }
            return result;
        },
        [](const std::string& text) { // Transform
            LetterCounts counts{};
            for (char c : text) {
                if (std::isalpha(static_cast<unsigned char>(c))) {
                    char lower = std::tolower(static_cast<unsigned char>(c));
                    counts[lower - 'a']++;
                }
            }
            return counts;
        }
    );
    
    // Convert the array to a map
    std::map<char, int> result;
    for (int i = 0; i < 26; ++i) {
        if (total_counts[i] > 0) {
            result[static_cast<char>('a' + i)] = total_counts[i];
        }
    }
    return result;
}

}
