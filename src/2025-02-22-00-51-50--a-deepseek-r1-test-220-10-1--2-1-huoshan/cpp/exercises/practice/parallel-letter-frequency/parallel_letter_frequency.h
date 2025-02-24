#if !defined(PARALLEL_LETTER_FREQUENCY_H)
#define PARALLEL_LETTER_FREQUENCY_H

#include <unordered_map>
#include <vector>
#include <string>

namespace parallel_letter_frequency {

std::unordered_map<char, int> frequency(const std::vector<std::string>& texts);

}

#endif

