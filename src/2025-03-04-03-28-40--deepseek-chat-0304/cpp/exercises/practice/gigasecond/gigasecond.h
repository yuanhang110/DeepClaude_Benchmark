#if !defined(GIGASECOND_H)
#define GIGASECOND_H

#include <chrono>
#include <ctime>

namespace gigasecond {

std::tm advance(const std::tm& start_time);

}  // namespace gigasecond

#endif // GIGASECOND_H
