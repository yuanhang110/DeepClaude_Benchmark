#include "clock.h"
#include <sstream>
#include <iomanip>

namespace date_independent {

clock::clock(int total_minutes) : total_minutes_((total_minutes % 1440 + 1440) % 1440) {}

clock clock::at(int hours, int minutes) {
    return Clock(hours * 60 + minutes);
}

clock clock::plus(int minutes) const {
    return Clock(total_minutes_ + minutes);
}

clock clock::minus(int minutes) const {
    return Clock(total_minutes_ - minutes);
}

bool clock::operator==(const clock& other) const {
    return total_minutes_ == other.total_minutes_;
}

bool clock::operator!=(const clock& other) const {
    return !(*this == other);
}

clock::operator std::string() const {
    int hours = total_minutes_ / 60;
    int minutes = total_minutes_ % 60;
    std::ostringstream oss;
    oss << std::setfill('0') 
        << std::setw(2) << hours << ":" 
        << std::setw(2) << minutes;
    return oss.str();
}

}  // namespace date_independent
