#include "clock.h"

    clock::clock(int total) : total_minutes(total % 1440) {
        if (total_minutes < 0) {
            total_minutes += 1440;
        }
    }

    clock clock::at(int hours, int minutes) {
        return clock(hours * 60 + minutes);
    }

    clock clock::plus(int minutes) const {
        return clock(total_minutes + minutes);
    }

    clock clock::minus(int minutes) const {
        return clock(total_minutes - minutes);
    }

    std::string clock::to_string() const {
        char buffer[6];
        snprintf(buffer, sizeof(buffer), "%02d:%02d", total_minutes / 60, total_minutes % 60);
        return buffer;
    }

    bool clock::operator==(const clock& other) const {
        return total_minutes == other.total_minutes;
    }

    bool clock::operator!=(const clock& other) const {
        return total_minutes != other.total_minutes;
    }
    clock::operator std::string() const {
        return to_string();
    }
}  // namespace date_independent
