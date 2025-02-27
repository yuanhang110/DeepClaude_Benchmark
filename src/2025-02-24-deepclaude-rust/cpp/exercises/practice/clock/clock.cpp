#include "clock.h"
#include <cstdio>

namespace date_independent {

clock clock::at(int hours, int minutes) {
    return clock(hours, minutes);
}

clock::clock(int hours, int minutes) {
    int total = hours * 60 + minutes;
    total = (total % 1440 + 1440) % 1440;  // Normalize to positive 24-hour range
    hours_ = total / 60;
    minutes_ = total % 60;
}

clock clock::from_total_minutes(int total) {
    total = (total % 1440 + 1440) % 1440;  // Normalize to positive 24-hour range
    return clock(total / 60, total % 60);
}

clock clock::plus(int minutes) const {
    int total = hours_ * 60 + minutes_ + minutes;
    return from_total_minutes(total);
}

clock clock::minus(int minutes) const {
    int total = hours_ * 60 + minutes_ - minutes;
    return from_total_minutes(total);
}

bool clock::operator==(const clock& other) const {
    return hours_ == other.hours_ && minutes_ == other.minutes_;
}

std::string Clock::to_string() const {
    char buffer[6];
    std::sprintf(buffer, "%02d:%02d", hours_, minutes_);
    return std::string(buffer);
}

}  // namespace date_independent
