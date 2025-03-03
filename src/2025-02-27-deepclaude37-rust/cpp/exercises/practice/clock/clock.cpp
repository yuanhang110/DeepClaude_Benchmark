#include "clock.h"
#include <iomanip>
#include <sstream>

namespace date_independent {

std::string Clock::to_string() const {
    std::ostringstream oss;
    oss << std::setfill('0') << std::setw(2) << (total_minutes / 60)
        << ":" 
        << std::setfill('0') << std::setw(2) << (total_minutes % 60);
    return oss.str();
}

std::ostream& operator<<(std::ostream& os, const Clock& clock) {
    return os << clock.to_string();
}

}  // namespace date_independent
