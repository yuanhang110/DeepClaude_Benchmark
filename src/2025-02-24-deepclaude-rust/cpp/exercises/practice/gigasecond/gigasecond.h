#if !defined(GIGASECOND_H)
#define GIGASECOND_H

#include <chrono>

namespace gigasecond {
    std::chrono::system_clock::time_point advance(std::chrono::system_clock::time_point start);
}  // namespace gigasecond

#endif // GIGASECOND_H
