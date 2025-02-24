#if !defined(CLOCK_H)
#define CLOCK_H

#include <string>

namespace date_independent {
class clock {
public:
    static Clock at(int hours, int minutes);
    Clock plus(int minutes) const;
    Clock minus(int minutes) const;
    bool operator==(const Clock& other) const;
    bool operator!=(const Clock& other) const;
    operator std::string() const;

private:
    explicit Clock(int total_minutes);
    int total_minutes_;
};

}  // namespace date_independent

#endif // CLOCK_H
