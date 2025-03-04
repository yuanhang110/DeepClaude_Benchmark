#if !defined(CLOCK_H)
#define CLOCK_H

#include <string>

namespace date_independent {
    class clock {
        int total_minutes;
        clock(int total_minutes);
    public:
        static clock at(int hours, int minutes);
        clock plus(int minutes) const;
        clock minus(int minutes) const;
        std::string to_string() const;
        operator std::string() const;
        bool operator==(const clock& other) const;
        bool operator!=(const clock& other) const;
    };
}  // namespace date_independent

#endif // CLOCK_H
