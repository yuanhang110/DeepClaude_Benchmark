#include "parallel_letter_frequency.h"
#include <mutex>

static std::mutex mutex;

namespace parallel_letter_frequency {

std::map<char, int> frequency(const std::vector<std::string>& texts) {
    std::map<char, int> frequency_map;
    
    // Parallel processing of each text
    std::for_each(std::execution::par, texts.begin(), texts.end(),
        [&](const std::string& text) {
            std::map<char, int> local_map;
            
            // Count letters in current text
            for (char ch : text) {
                if (std::isalpha(ch)) {
                    local_map[std::tolower(ch)]++;
                }
            }
            
            // Merge local counts into global map
            std::lock_guard<std::mutex> lock(mutex);
            for (const auto& [letter, count] : local_map) {
                frequency_map[letter] += count;
            }
        });
    
    return frequency_map;
}

}
