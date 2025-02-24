#include "phone_number.h"

namespace phone_number {

phone_number::phone_number(const std::string& input) {
    std::string cleaned;
    for (char c : input) {
        if (c >= '0' && c <= '9') {
            cleaned += c;
        }
    }
    
    // Validate country code and length
    if (cleaned.length() == 11 && cleaned[0] == '1') {
        cleaned = cleaned.substr(1);
    } else if (cleaned.length() != 10) {
        if (cleaned.length() > 11) {
            throw std::invalid_argument("more than 11 digits");
        } else if (cleaned.length() == 11) {
            throw std::invalid_argument("11 digits must start with 1");
        } else {
            throw std::invalid_argument("incorrect number of digits");
        }
    }
    
    // Validate area code (NXX) and exchange code (NXX)
    if (cleaned[0] < '2' || cleaned[0] > '9') {
        throw std::invalid_argument("area code cannot start with " + std::string(1, cleaned[0]));
    }
    if (cleaned[3] < '2' || cleaned[3] > '9') {
        throw std::invalid_argument("exchange code cannot start with " + std::string(1, cleaned[3]));
    }
    
    number_ = cleaned;
}

}  // namespace phone_number
