#include "phone_number.h"
#include <cctype>

namespace phone_number {

PhoneNumber::PhoneNumber(const std::string& s) {
    std::string cleaned;
    for (char c : s) {
        if (std::isdigit(static_cast<unsigned char>(c))) {
            cleaned += c;
        }
    }
    
    // Check country code and length
    if (cleaned.size() == 11 && cleaned[0] == '1') {
        cleaned = cleaned.substr(1);
    } else if (cleaned.size() != 10) {
        throw std::invalid_argument(cleaned.empty() ? "Empty input" : "Invalid number");
    }
    
    // Validate area code (NXX) and exchange code (NXX)
    if (cleaned[0] < '2' || cleaned[0] > '9') {
        throw std::invalid_argument("Area code cannot start with " + std::string(1, cleaned[0]));
    }
    if (cleaned[3] < '2' || cleaned[3] > '9') {
        throw std::invalid_argument("Exchange code cannot start with " + std::string(1, cleaned[3]));
    }
    
    num_ = cleaned;
}

std::string PhoneNumber::number() const {
    return num_;
}

std::string PhoneNumber::area_code() const {
    return num_.substr(0, 3);
}

}  // namespace phone_number
