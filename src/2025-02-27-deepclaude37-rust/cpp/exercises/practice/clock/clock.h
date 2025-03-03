#if !defined(CLOCK_H)
#define CLOCK_H

#include <string>
#include <ostream>

namespace date_independent {

class Clock {
private:
    int total_minutes;
    Clock(int minutes) : total_minutes(((minutes % 1440) + 1440) % 1440) {}

public:
    static Clock at(int hours, int minutes) {
        return Clock(hours * 60 + minutes);
    }

    Clock plus(int minutes) const {
        return Clock(total_minutes + minutes);
    }

    Clock minus(int minutes) const {
        return Clock(total_minutes - minutes);
    }

    std::string to_string() const;

    bool operator==(const Clock& other) const {
        return total_minutes == other.total_minutes;
    }

    bool operator!=(const Clock& other) const {
        return !(*this == other);
    }

    friend std::ostream& operator<<(std::ostream& os, const Clock& clock);
};

using clock = Clock;

}  // namespace date_independent

#endif // CLOCK_H
