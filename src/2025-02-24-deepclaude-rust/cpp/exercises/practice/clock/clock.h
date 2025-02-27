#if !defined(CLOCK_H)
#define CLOCK_H

#include <string>

namespace date_independent {

class clock {
public:
    static clock at(int hours, int minutes = 0);
    clock plus(int minutes) const;
    clock minus(int minutes) const;
    bool operator==(const clock& other) const;
    std::string to_string() const;

private:
    clock(int hours, int minutes);
    static clock from_total_minutes(int total);
    
    int hours_;
    int minutes_;
};

}  // namespace date_independent

#endif // CLOCK_H
