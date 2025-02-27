#include "gigasecond.h"

namespace gigasecond {

std::chrono::system_clock::time_point advance(std::chrono::system_clock::time_point start) {
    return start + std::chrono::seconds(1000000000);
}

}  // namespace gigasecond
