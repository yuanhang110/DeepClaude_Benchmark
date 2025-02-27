#if !defined(PARALLEL_LETTER_FREQUENCY_H)
#define PARALLEL_LETTER_FREQUENCY_H

#include <array>
#include <string>
#include <vector>

namespace parallel_letter_frequency {
    std::array<int, 26> frequency(const std::vector<std::string>& texts);
}

#endif

