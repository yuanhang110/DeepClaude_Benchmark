#if !defined(CLOCK_H)
#define CLOCK_H

#include <string>
#include <stdexcept>

namespace date_independent {

class clock {
private:
    int hours;
    int minutes;

    void normalize() {
        if (minutes >= 60 || minutes < 0) {
            hours += minutes / 60;
            minutes = minutes % 60;
            if (minutes < 0) {
                minutes += 60;
                hours--;
            }
        }
        if (hours >= 24 || hours < 0) {
            hours = hours % 24;
            if (hours < 0) {
                hours += 24;
            }
        }
    }

public:
    clock(int h, int m) : hours(h), minutes(m) {
        normalize();
    }

    static clock at(int h, int m) {
        return clock(h, m);
    }

    clock plus(int m) const {
        return clock(hours, minutes + m);
    }

    clock minus(int m) const {
        return clock(hours, minutes - m);
    }

    bool operator==(const clock& other) const {
        return hours == other.hours && minutes == other.minutes;
    }

    bool operator!=(const clock& other) const {
        return !(*this == other);
    }

    operator std::string() const {
        char buffer[6];
        sprintf(buffer, "%02d:%02d", hours, minutes);
        return std::string(buffer);
    }
};

}  // namespace date_independent

#endif // CLOCK_H
