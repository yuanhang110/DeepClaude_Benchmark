#include "gigasecond.h"

namespace gigasecond {

std::tm advance(const std::tm& start_time) {
    // Convert tm to time_t
    std::time_t time = std::mktime(const_cast<std::tm*>(&start_time));
    
    // Add one gigasecond (1,000,000,000 seconds)
    constexpr std::time_t gigasecond = 1000000000;
    time += gigasecond;
    
    // Convert back to tm structure
    std::tm* result = std::gmtime(&time);
    
    return *result;
}

}  // namespace gigasecond
