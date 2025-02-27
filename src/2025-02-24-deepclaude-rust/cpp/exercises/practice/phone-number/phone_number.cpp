#include "phone_number.h"

namespace phone_number {

phone_number::phone_number(const std::string& input) {
    std::string cleaned = std::regex_replace(input, std::regex(R"([^\d])"), "");
    
    if (cleaned.length() == 11) {
        if (cleaned[0] != '1') {
            throw std::invalid_argument("Country code must be 1");
        }
        cleaned = cleaned.substr(1);
    }
    
    if (cleaned.length() != 10) {
        throw std::invalid_argument("Invalid number of digits");
    }
    
    if (cleaned[0] < '2' || cleaned[0] > '9') {
        throw std::invalid_argument("Area code cannot start with 0 or 1");
    }
    
    if (cleaned[3] < '2' || cleaned[3] > '9') {
        throw std::invalid_argument("Exchange code cannot start with 0 or 1");
    }
    
    number_ = cleaned;
}

std::string phone_number::number() const {
    return number_;
}

std::string phone_number::area_code() const {
    return number_.substr(0, 3);
}

}  // namespace phone_number
