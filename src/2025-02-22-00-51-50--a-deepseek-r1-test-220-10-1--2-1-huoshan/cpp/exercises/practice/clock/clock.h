#if !defined(CLOCK_H)
#define CLOCK_H

#include <string>

namespace date_independent {

class clock {
    int totalMinutes;
    
public:
    clock(int hours, int minutes) {
        totalMinutes = (hours * 60 + minutes) % 1440;
        if (totalMinutes < 0) totalMinutes += 1440;
    }

    static clock at(int hours, int minutes) {
        return clock(hours, minutes);
    }

    clock plus(int minutes) const {
        return clock(0, totalMinutes + minutes);
    }

    clock minus(int minutes) const {
        return clock(0, totalMinutes - minutes);
    }

    bool operator==(const Clock& other) const {
        return totalMinutes == other.totalMinutes;
    }

    bool operator!=(const Clock& other) const {
        return !(*this == other);
    }

    std::string toString() const {
        char buffer[6];
        sprintf(buffer, "%02d:%02d", totalMinutes / 60, totalMinutes % 60);
        return buffer;
    }
};

}  // namespace date_independent

#endif // CLOCK_H
